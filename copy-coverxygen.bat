@echo off
setlocal enableextensions enabledelayedexpansion

echo Kaynak Coverxygen klasörünün varlığını kontrol ediyorum...
if not exist "coverxygen\" (
  echo HATA: coverxygen klasörü bulunamadı! İşlem iptal ediliyor.
  goto Error
)

echo Hedef klasörün varlığını kontrol ediyorum...
if not exist "taskmanager-app\target\" (
  echo HATA: target klasörü bulunamadı! Önce build işlemini çalıştırın.
  goto Error
)

echo Hedef site klasörü oluşturuluyor...
if not exist "taskmanager-app\target\site\" (
  mkdir "taskmanager-app\target\site" 
)

echo Coverxygen klasörü oluşturuluyor...
if not exist "taskmanager-app\target\site\coverxygen\" (
  mkdir "taskmanager-app\target\site\coverxygen"
)

echo Dosyalar kopyalanıyor...
echo 1- Coverxygen klasörü kopyalanıyor...
xcopy /E /I /Y "coverxygen\*" "taskmanager-app\target\site\coverxygen\"
if errorlevel 1 (
  echo Kopyalama sırasında hata oluştu.
  goto Error
)

echo 2- Coverage dosyaları kopyalanıyor...
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

echo Kopyalama işlemi tamamlandı! Toplam %DOSYA_SAYACI% dosya kopyalandı.
echo Bu batch dosyasını her build işleminden sonra çalıştırmanız gerekebilir.
goto End

:Error
echo İşlem başarısız oldu! Lütfen hataları kontrol edin.
exit /b 1

:End
echo İşlem başarıyla tamamlandı.
pause 