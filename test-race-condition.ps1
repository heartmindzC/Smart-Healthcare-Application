# Race Condition Test Script with detailed output
$body1 = '{"doctorId":"doc-racetest","doctorName":"Dr. RaceTest","patientId":"patient-101","patientName":"Nguyen Van Test1","hospitalId":"hospital-001","hospitalName":"City Hospital","departmentId":"dept-001","departmentName":"Cardiology","appointmentDateTime":"2026-04-20T10:00:00","reason":"Race test 1"}'

$body2 = '{"doctorId":"doc-racetest","doctorName":"Dr. RaceTest","patientId":"patient-102","patientName":"Tran Thi Test2","hospitalId":"hospital-001","hospitalName":"City Hospital","departmentId":"dept-001","departmentName":"Cardiology","appointmentDateTime":"2026-04-20T10:00:00","reason":"Race test 2"}'

$url = "http://localhost:8086/appointments/"

Write-Host "=== RACE CONDITION TEST ===" -ForegroundColor Cyan
Write-Host "Sending BOTH requests simultaneously..." -ForegroundColor Yellow

# Start both requests in parallel jobs
$job1 = Start-Job -ScriptBlock {
    param($url, $body)
    try {
        $response = Invoke-WebRequest -Uri $url -Method POST -ContentType 'application/json' -Body $body -TimeoutSec 30 -ErrorAction Stop
        return @{Status = "SUCCESS"; Body = $response.Content; Time = Get-Date }
    } catch {
        return @{Status = "ERROR"; Body = $_.Exception.Message; Time = Get-Date }
    }
} -ArgumentList $url, $body1

$job2 = Start-Job -ScriptBlock {
    param($url, $body)
    Start-Sleep -Milliseconds 50
    try {
        $response = Invoke-WebRequest -Uri $url -Method POST -ContentType 'application/json' -Body $body -TimeoutSec 30 -ErrorAction Stop
        return @{Status = "SUCCESS"; Body = $response.Content; Time = Get-Date }
    } catch {
        return @{Status = "ERROR"; Body = $_.Exception.Message; Time = Get-Date }
    }
} -ArgumentList $url, $body2

Write-Host "Waiting for results..." -ForegroundColor Yellow

# Wait for both jobs
$result1 = Receive-Job -Job $job1 -Wait
$result2 = Receive-Job -Job $job2 -Wait

Write-Host ""
Write-Host "=== RESULT 1 (Patient 1) ===" -ForegroundColor Green
Write-Host "Time: $($result1.Time)"
if ($result1.Status -eq "SUCCESS") {
    $result1.Body | ConvertFrom-Json | ConvertTo-Json -Depth 3
} else {
    Write-Host "Error: $($result1.Body)" -ForegroundColor Red
}

Write-Host ""
Write-Host "=== RESULT 2 (Patient 2) ===" -ForegroundColor Green
Write-Host "Time: $($result2.Time)"
if ($result2.Status -eq "SUCCESS") {
    $result2.Body | ConvertFrom-Json | ConvertTo-Json -Depth 3
} else {
    Write-Host "Error: $($result2.Body)" -ForegroundColor Red
}

# Clean up
Remove-Job -Job $job1, $job2 -Force
