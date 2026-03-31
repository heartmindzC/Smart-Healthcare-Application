$baseUrl = "http://localhost:8086/appointments"
$doctorServiceUrl = "http://localhost:8082/time-slots"

function Create-Appointment {
    param($doctorId, $patientId, $dateTime, $description)
    Write-Host "`n[$description] POST /appointments"
    Write-Host "  Doctor: $doctorId, Patient: $patientId, DateTime: $dateTime"
    
    $body = @{
        doctorId = $doctorId
        patientId = $patientId
        appointmentDateTime = $dateTime
        hospitalId = "hospital-1"
        departmentId = "dept-1"
        reason = "Test: $description"
    } | ConvertTo-Json -Compress
    
    try {
        $response = Invoke-RestMethod -Uri $baseUrl -Method Post -Headers @{ "Content-Type" = "application/json" } -Body $body -ErrorAction Stop
        Write-Host "  Result: status=$($response.result.status), timeSlotId=$($response.result.timeSlotId)"
        return $response.result
    } catch {
        Write-Host "  Error: $($_.Exception.Message)" -ForegroundColor Red
        return $null
    }
}

function Get-TimeSlot {
    param($doctorId, $dateTime)
    $date = $dateTime.Substring(0, 10)
    $url = "$doctorServiceUrl/doctor/$doctorId/by-date?date=$date"
    
    try {
        $response = Invoke-RestMethod -Uri $url -Method Get -ErrorAction Stop
        $slots = $response.result
        $targetTime = $dateTime.Substring(11, 5)
        
        foreach ($slot in $slots) {
            if ($slot.startTime -eq $targetTime) {
                return $slot
            }
        }
        return $null
    } catch {
        Write-Host "  Error getting time slot: $($_.Exception.Message)" -ForegroundColor Red
        return $null
    }
}

function Show-TimeSlotStatus {
    param($doctorId, $dateTime, $label)
    $slot = Get-TimeSlot -doctorId $doctorId -dateTime $dateTime
    if ($slot) {
        Write-Host "  [$label] TimeSlot: isAvailable=$($slot.isAvailable), id=$($slot.timeSlotId)"
    } else {
        Write-Host "  [$label] TimeSlot: NOT FOUND" -ForegroundColor Yellow
    }
}

Write-Host "========================================" -ForegroundColor Cyan
Write-Host "APPOINTMENT BOOKING TEST SUITE" -ForegroundColor Cyan
Write-Host "========================================" -ForegroundColor Cyan

# Case 1: Happy Path - Đặt thành công
Write-Host "`n--- CASE 1: Happy Path ---" -ForegroundColor Green
$app1 = Create-Appointment "doc-1" "pat-1" "2026-03-30T09:00:00" "Happy Path"
Start-Sleep -Milliseconds 500
Show-TimeSlotStatus -doctorId "doc-1" -dateTime "2026-03-30T09:00:00" -label "After Case 1"

# Case 2: Race Condition - 3 request cùng slot
Write-Host "`n--- CASE 2: Race Condition (3 concurrent) ---" -ForegroundColor Yellow
Write-Host "Testing with jobs..."
$jobs = @()
for ($i = 1; $i -le 3; $i++) {
    $jobs += Start-Job -ScriptBlock {
        param($base, $doc, $pat, $dt)
        $body = @{
            doctorId = $doc
            patientId = $pat
            appointmentDateTime = $dt
            hospitalId = "hospital-1"
            departmentId = "dept-1"
            reason = "Race test"
        } | ConvertTo-Json -Compress
        
        try {
            $resp = Invoke-RestMethod -Uri $base -Method Post -Headers @{ "Content-Type" = "application/json" } -Body $body -TimeoutSec 30
            return @{ status = $resp.result.status; patient = $pat; timeSlotId = $resp.result.timeSlotId }
        } catch {
            return @{ error = $_.Exception.Message; patient = $pat }
        }
    } -ArgumentList $baseUrl, "doc-race", "pat-race-$i", "2026-03-30T14:00:00"
}
$results = $jobs | Wait-Job | Receive-Job
$jobs | Remove-Job

$confirmed = $results | Where-Object { $_.status -eq "CONFIRMED" }
$cancelled = $results | Where-Object { $_.status -eq "CANCELLED" }
$errors = $results | Where-Object { $_.error }

Write-Host "Results:"
Write-Host "  CONFIRMED: $($confirmed.Count) - $($confirmed.patient -join ', ')"
Write-Host "  CANCELLED: $($cancelled.Count) - $($cancelled.patient -join ', ')"
if ($errors) { Write-Host "  Errors: $($errors.Count)" -ForegroundColor Red }

Start-Sleep -Milliseconds 500
Show-TimeSlotStatus -doctorId "doc-race" -dateTime "2026-03-30T14:00:00" -label "After Case 2"

# Case 3: Đặt lại slot đã có
Write-Host "`n--- CASE 3: Re-book same slot ---" -ForegroundColor Green
$app3a = Create-Appointment "doc-1" "pat-3" "2026-03-30T09:00:00" "Re-book existing"
Show-TimeSlotStatus -doctorId "doc-1" -dateTime "2026-03-30T09:00:00" -label "After Case 3"

# Case 4: Khác doctor - không ảnh hưởng
Write-Host "`n--- CASE 4: Different Doctor (independent) ---" -ForegroundColor Green
$app4a = Create-Appointment "doc-4" "pat-4a" "2026-03-30T10:00:00" "Doc 4"
$app4b = Create-Appointment "doc-5" "pat-4b" "2026-03-30T10:00:00" "Doc 5"
Write-Host "  Expected: Both CONFIRMED (different doctors)"
Show-TimeSlotStatus -doctorId "doc-4" -dateTime "2026-03-30T10:00:00" -label "Doc 4"
Show-TimeSlotStatus -doctorId "doc-5" -dateTime "2026-03-30T10:00:00" -label "Doc 5"

# Case 5: Khác giờ - không ảnh hưởng
Write-Host "`n--- CASE 5: Different Time (independent) ---" -ForegroundColor Green
$app5a = Create-Appointment "doc-6" "pat-5a" "2026-03-30T11:00:00" "11:00"
$app5b = Create-Appointment "doc-6" "pat-5b" "2026-03-30T12:00:00" "12:00"
Write-Host "  Expected: Both CONFIRMED (different times)"
Show-TimeSlotStatus -doctorId "doc-6" -dateTime "2026-03-30T11:00:00" -label "11:00"
Show-TimeSlotStatus -doctorId "doc-6" -dateTime "2026-03-30T12:00:00" -label "12:00"

# Case 6: Khác ngày - không ảnh hưởng
Write-Host "`n--- CASE 6: Different Date (independent) ---" -ForegroundColor Green
$app6a = Create-Appointment "doc-7" "pat-6a" "2026-03-31T09:00:00" "March 31"
$app6b = Create-Appointment "doc-7" "pat-6b" "2026-04-01T09:00:00" "April 1"
Write-Host "  Expected: Both CONFIRMED (different dates)"

# Case 7: Race condition nhiều request testing 
Write-Host "`n--- CASE 7: Heavy Race (5 concurrent) ---" -ForegroundColor Yellow
$jobs2 = @()
for ($i = 1; $i -le 5; $i++) {
    $jobs2 += Start-Job -ScriptBlock {
        param($base, $doc, $pat, $dt)
        $body = @{
            doctorId = $doc
            patientId = $pat
            appointmentDateTime = $dt
            hospitalId = "hospital-1"
            departmentId = "dept-1"
            reason = "Heavy race test"
        } | ConvertTo-Json -Compress
        
        try {
            $resp = Invoke-RestMethod -Uri $base -Method Post -Headers @{ "Content-Type" = "application/json" } -Body $body -TimeoutSec 30
            return @{ status = $resp.result.status; patient = $pat }
        } catch {
            return @{ error = $true; patient = $pat }
        }
    } -ArgumentList $baseUrl, "doc-heavy", "pat-heavy-$i", "2026-03-30T15:00:00"
}
$results2 = $jobs2 | Wait-Job | Receive-Job
$jobs2 | Remove-Job

$confirmed2 = $results2 | Where-Object { $_.status -eq "CONFIRMED" }
$cancelled2 = $results2 | Where-Object { $_.status -eq "CANCELLED" }
$errors2 = $results2 | Where-Object { $_.error }

Write-Host "Results:"
Write-Host "  CONFIRMED: $($confirmed2.Count) - $($confirmed2.patient -join ', ')"
Write-Host "  CANCELLED: $($cancelled2.Count) - $($cancelled2.patient -join ', ')"
Write-Host "  Errors: $($errors2.Count)"
Write-Host "  Expected: 1 CONFIRMED, 4 CANCELLED"

Start-Sleep -Milliseconds 500
Show-TimeSlotStatus -doctorId "doc-heavy" -dateTime "2026-03-30T15:00:00" -label "After Case 7"

# Summary
Write-Host "`n========================================" -ForegroundColor Cyan
Write-Host "TEST SUMMARY" -ForegroundColor Cyan
Write-Host "========================================" -ForegroundColor Cyan

# Get all appointments for verification
Write-Host "`nAppointments by status:"
$all = Invoke-RestMethod -Uri "$baseUrl" -Method Get -ErrorAction SilentlyContinue
if ($all.result) {
    $all.result | Group-Object status | ForEach-Object {
        Write-Host "  $($_.Name): $($_.Count)"
    }
}

Write-Host "`nTest completed!"
