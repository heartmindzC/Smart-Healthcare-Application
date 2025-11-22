# Script khởi động Hospital Service
Write-Host "=== Hospital Service Startup Script ===" -ForegroundColor Cyan

# Kiểm tra MySQL
Write-Host "`n[1/4] Kiểm tra MySQL..." -ForegroundColor Yellow
$mysqlProcess = Get-Process | Where-Object {$_.ProcessName -like "*mysql*"}
if ($mysqlProcess) {
    Write-Host "✓ MySQL đang chạy" -ForegroundColor Green
} else {
    Write-Host "✗ MySQL không chạy. Vui lòng khởi động MySQL trước!" -ForegroundColor Red
    Write-Host "  Có thể chạy: net start MySQL80 (hoặc tên service MySQL của bạn)" -ForegroundColor Yellow
}

# Kiểm tra cổng 3311
Write-Host "`n[2/4] Kiểm tra cổng MySQL 3311..." -ForegroundColor Yellow
$port3311 = netstat -an | Select-String "3311"
if ($port3311) {
    Write-Host "✓ Cổng 3311 đang lắng nghe" -ForegroundColor Green
} else {
    Write-Host "⚠ Cổng 3311 không được tìm thấy. Kiểm tra cổng 3306..." -ForegroundColor Yellow
    $port3306 = netstat -an | Select-String "3306"
    if ($port3306) {
        Write-Host "✓ Cổng 3306 đang lắng nghe" -ForegroundColor Green
        Write-Host "  Lưu ý: Có thể cần thay đổi cổng trong application.properties từ 3311 sang 3306" -ForegroundColor Yellow
    } else {
        Write-Host "✗ Không tìm thấy MySQL trên cổng 3306 hoặc 3311" -ForegroundColor Red
    }
}

# Hướng dẫn tạo database
Write-Host "`n[3/4] Kiểm tra database..." -ForegroundColor Yellow
Write-Host "  Để tạo database, chạy lệnh:" -ForegroundColor Cyan
Write-Host "  mysql -u admin -p123456 -h localhost -P 3311 < init-database.sql" -ForegroundColor White
Write-Host "  Hoặc nếu dùng cổng 3306:" -ForegroundColor Cyan
Write-Host "  mysql -u root -p -h localhost -P 3306 < init-database.sql" -ForegroundColor White

# Khởi động service
Write-Host "`n[4/4] Khởi động Hospital Service..." -ForegroundColor Yellow
Write-Host "  Đang chạy: mvn spring-boot:run" -ForegroundColor Cyan
Write-Host "  Service sẽ chạy trên: http://localhost:8084" -ForegroundColor Green
Write-Host "`n  Nhấn Ctrl+C để dừng service`n" -ForegroundColor Yellow

# Chạy service
.\mvnw.cmd spring-boot:run

