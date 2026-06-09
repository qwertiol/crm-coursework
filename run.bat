@echo off
chcp 65001 >nul
set JAVA_HOME=C:\Program Files\Eclipse Adoptium\jdk-21.0.11.10-hotspot
set PATH=%JAVA_HOME%\bin;%PATH%

echo Starting CRM Application...
echo Using Java from: %JAVA_HOME%
echo.

java -jar crm-coursework-1.0.0-SNAPSHOT.jar

echo.
echo Application stopped. Press any key to exit.
pause >nul
