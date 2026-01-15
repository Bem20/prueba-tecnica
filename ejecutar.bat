@echo off
echo ==========================================
echo   Ejecutando aplicacion
echo ==========================================
echo.

if not exist "target\gestion-usuarios-1.0.0-jar-with-dependencies.jar" (
    echo ERROR: No se encontro el JAR. Ejecute compilar.bat primero.
    echo.
    exit /b 1
)

java -jar "target\gestion-usuarios-1.0.0-jar-with-dependencies.jar"