@echo off
@setlocal enableextensions
@cd /d "%~dp0"

echo Task Manager Application
echo =====================
echo 1. Console Application
echo 2. GUI Application
echo =====================
set /p choice="Please enter your choice (1 or 2): "

if "%choice%"=="1" (
    echo Starting Console Application...
    java -jar taskmanager-app/target/taskmanager-app-1.0-SNAPSHOT.jar --console
) else if "%choice%"=="2" (
    echo Starting GUI Application...
    java -jar taskmanager-app/target/taskmanager-app-1.0-SNAPSHOT.jar --gui
) else (
    echo Invalid choice! Please enter 1 or 2.
    pause
    exit /b 1
)

echo Operation completed!
pause