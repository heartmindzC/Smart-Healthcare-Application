@echo off
REM Script to run API integration tests using Python

echo ==========================================
echo Running Hospital Service API Tests
echo ==========================================

REM Navigate to project root
cd /d "%~dp0\.."

REM Check if Python is available
python --version >nul 2>&1
if errorlevel 1 (
    echo Error: Python is not installed
    pause
    exit /b 1
)

REM Check if requests library is installed
python -c "import requests" >nul 2>&1
if errorlevel 1 (
    echo Installing required dependencies...
    pip install -r test\requirements.txt
)

REM Run tests
echo.
python test\test_api.py

REM Check if result.json was created
if exist "result.json" (
    echo.
    echo Full results available in: %CD%\result.json
) else (
    echo Warning: result.json was not created. Check test output for errors.
    pause
    exit /b 1
)

pause
