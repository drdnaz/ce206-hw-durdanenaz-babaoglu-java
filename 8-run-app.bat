@echo off
@setlocal enableextensions
@cd /d "%~dp0"

echo Running Application
java -jar taskmanager-app/target/taskmanager-app-1.0-SNAPSHOT.jar

echo Operation Completed!
pause