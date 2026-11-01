@echo off
setlocal
cd /d "%~dp0"
where javac >nul 2>nul
if errorlevel 1 (
  echo Java compiler not found. Install JDK 21 or newer and add its bin folder to PATH.
  pause
  exit /b 1
)
if not exist bin mkdir bin
javac -encoding UTF-8 -d bin @sources.txt
if errorlevel 1 (
  echo Compilation failed. Review the errors above.
  pause
  exit /b 1
)
java -cp bin ssps.Main
if errorlevel 1 pause
endlocal
