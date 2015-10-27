@echo off
java -version:1.7 -version > nul 2>&1
if %ERRORLEVEL% == 0 goto found
echo Please update your Java to version 1.7
pause

:found
for %%i in (AdyenReportMerger*.jar) do set "jarProg=%%~i"
 java -jar %jarProg%
:end