@echo off

:: Enable necessary extensions
@setlocal enableextensions

echo Get the current directory
set "currentDir=%CD%"

echo Change the current working directory to the script directory
@cd /d "%~dp0"

echo Delete the "docs" folder and its contents
rd /S /Q "taskmanager-app\target\site\coveragereport"
rd /S /Q "taskmanager-app\target\site\doxygen"

echo Delete and Create the "release" folder and its contents
rd /S /Q "release"
mkdir release

echo Change directory to taskmanager-app
cd taskmanager-app

echo Perform Maven clean, test, and packaging
call mvn clean test package

echo Return to the previous directory
cd ..

echo Create Required Folders coverxygen/coveragereport/doxygen
cd taskmanager-app
mkdir target
cd target
mkdir site
cd site
mkdir coverxygen
mkdir coveragereport
mkdir doxygen
cd ..
cd ..
cd ..

echo Generate Doxygen HTML and XML Documentation
call doxygen Doxyfile

echo Change directory to taskmanager-app
cd taskmanager-app

echo Generate ReportGenerator HTML Report
call reportgenerator "-reports:target\site\jacoco\jacoco.xml" "-sourcedirs:src\main\java" "-targetdir:target\site\coveragereport" -reporttypes:Html

echo Generate ReportGenerator Badges
call reportgenerator "-reports:target\site\jacoco\jacoco.xml" "-sourcedirs:src\main\java" "-targetdir:target\site\coveragereport" -reporttypes:Badges

echo Display information about the binary file
echo Our Binary is a Single Jar With Dependencies. You Do Not Need to Compress It.

echo Return to the previous directory
cd ..

echo Run Coverxygen
call python -m coverxygen --xml-dir ./taskmanager-app/target/site/doxygen/xml --src-dir ./ --format lcov --output ./taskmanager-app/target/site/coverxygen/lcov.info --prefix %currentDir%/taskmanager-app/

echo Run lcov genhtml
call perl C:\ProgramData\chocolatey\lib\lcov\tools\bin\genhtml --legend --title "Documentation Coverage Report" ./taskmanager-app/target/site/coverxygen/lcov.info -o taskmanager-app/target/site/coverxygen

echo Copy badge files to the "assets" directory
call copy "taskmanager-app\target\site\coveragereport\badge_combined.svg" "assets\badge_combined.svg"
call copy "taskmanager-app\target\site\coveragereport\badge_combined.svg" "assets\badge_combined.svg"
call copy "taskmanager-app\target\site\coveragereport\badge_branchcoverage.svg" "assets\badge_branchcoverage.svg"
call copy "taskmanager-app\target\site\coveragereport\badge_linecoverage.svg" "assets\badge_linecoverage.svg"
call copy "taskmanager-app\target\site\coveragereport\badge_methodcoverage.svg" "assets\badge_methodcoverage.svg"

call copy "assets\rteu_logo.jpg" "taskmanager-app\src\site\resources\images\rteu_logo.jpg"

echo Copy the "assets" folder and its contents to "maven site images" recursively
call robocopy assets "taskmanager-app\src\site\resources\assets" /E

echo Copy the "README.md" file to "taskmanager-app\src\site\markdown\readme.md"
call copy README.md "taskmanager-app\src\site\markdown\readme.md"

cd taskmanager-app
echo Perform Maven site generation
call mvn site
cd ..

echo Package Output Jar Files
tar -czvf release\application-binary.tar.gz -C taskmanager-app\target '*.jar'

echo Package Jacoco Test Coverage Report (Optional)
call tar -czvf release\test-jacoco-report.tar.gz -C taskmanager-app\target\site\jacoco .

echo Package ReportGenerator Test Coverage Report
call tar -czvf release\test-coverage-report.tar.gz -C taskmanager-app\target\site\coveragereport .

echo Package Code Documentation
call tar -czvf release\application-documentation.tar.gz -C taskmanager-app\target\site\doxygen .

echo Package Documentation Coverage
call tar -czvf release\doc-coverage-report.tar.gz -C taskmanager-app\target\site\coverxygen .

echo Package Product Site
call tar -czvf release\application-site.tar.gz -C taskmanager-app\target\site .

echo Kaynak Coverxygen klasörünün varlığını kontrol ediyorum...
if not exist "coverxygen\" (
  echo UYARI: coverxygen klasörü bulunamadı, kopyalama işlemi atlanıyor
  goto SkipCopy
)

echo Hedef klasörü oluşturuyorum...
if not exist "taskmanager-app\target\site\coverxygen\" (
  mkdir "taskmanager-app\target\site\coverxygen" 2>nul
)

echo Coverxygen dosyalarını kopyalıyorum...
xcopy /E /I /Y "coverxygen\*" "taskmanager-app\target\site\coverxygen\" 
if errorlevel 1 (
  echo HATA: Coverxygen klasörü kopyalama sırasında sorun oluştu!
) else (
  echo Coverxygen klasörü başarıyla kopyalandı.
)

echo Diğer coverxygen dosyalarını kopyalıyorum...
set "DOSYA_SAYACI=0"
if exist "coverage_report.py" (
  copy "coverage_report.py" "taskmanager-app\target\site\coverxygen\"
  set /a "DOSYA_SAYACI+=1"
)
if exist "coverage.lcov" (
  copy "coverage.lcov" "taskmanager-app\target\site\coverxygen\"
  set /a "DOSYA_SAYACI+=1"
)
if exist "coverage_report_lcov.py" (
  copy "coverage_report_lcov.py" "taskmanager-app\target\site\coverxygen\"
  set /a "DOSYA_SAYACI+=1"
)
if exist "coverxygen.json" (
  copy "coverxygen.json" "taskmanager-app\target\site\coverxygen\"
  set /a "DOSYA_SAYACI+=1"
)
echo Toplam %DOSYA_SAYACI% coverxygen dosyası kopyalandı.

echo Kopyalama sonrası durumu kontrol ediyorum...
dir "taskmanager-app\target\site\coverxygen\" 

:SkipCopy
echo ....................
echo Operation Completed!
echo ....................
pause
