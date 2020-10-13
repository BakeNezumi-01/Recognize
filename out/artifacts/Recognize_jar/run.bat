@echo off
set ADDRESS=
set /P ADDRESS=Enter the address of pictures: %=%
cd /d %~dp0
java -jar Recognize.jar %ADDRESS%
pause
