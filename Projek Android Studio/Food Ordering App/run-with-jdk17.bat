@echo off
echo Menjalankan Gradle dengan JDK 17 jika tersedia...

set JAVA_HOME_BACKUP=%JAVA_HOME%

IF EXIST "C:\Program Files\Java\jdk-17" (
    set JAVA_HOME=C:\Program Files\Java\jdk-17
    echo Menggunakan JDK 17 dari: %JAVA_HOME%
) ELSE IF EXIST "C:\Program Files\Eclipse Adoptium\jdk-17.0.6.10-hotspot" (
    set JAVA_HOME=C:\Program Files\Eclipse Adoptium\jdk-17.0.6.10-hotspot
    echo Menggunakan Eclipse Adoptium JDK 17 dari: %JAVA_HOME%
) ELSE (
    echo JDK 17 tidak ditemukan. Silakan install JDK 17 dari:
    echo https://adoptium.net/temurin/releases/?version=17
    echo atau
    echo https://www.oracle.com/java/technologies/javase/jdk17-archive-downloads.html
    goto end
)

call gradlew.bat --info %*

:end
set JAVA_HOME=%JAVA_HOME_BACKUP%
echo JAVA_HOME dikembalikan ke: %JAVA_HOME%