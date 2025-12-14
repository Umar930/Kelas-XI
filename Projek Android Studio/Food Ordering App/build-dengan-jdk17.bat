@echo off
echo Menyiapkan JDK 17 untuk Food Ordering App...

:: Pastikan JAVA_HOME ditetapkan ke JDK 17
set JAVA_HOME=C:\Program Files\Java\jdk-17
echo JAVA_HOME diatur ke: %JAVA_HOME%

:: Tambahkan bin JDK ke PATH
set PATH=%JAVA_HOME%\bin;%PATH%

:: Tampilkan informasi versi Java
echo Menggunakan Java:
java -version

:: Bersihkan build sebelumnya
echo Membersihkan build sebelumnya...
call ./gradlew clean

:: Buat build baru
echo Membuat build baru dengan JDK 17.0.12...
call ./gradlew assembleDebug

echo.
echo Jika tidak ada error, build berhasil.
echo APK Debug tersedia di app\build\outputs\apk\debug\app-debug.apk
echo.

pause