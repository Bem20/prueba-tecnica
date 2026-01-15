@echo off
title Compilar - Gestion de Usuarios
echo ==========================================
echo   Compilando proyecto con Maven
echo ==========================================
echo.

mvn -U clean package -DskipTests

if %ERRORLEVEL% NEQ 0 (
    echo.
    echo ERROR: Fallo en la compilacion
    echo.
    pause
    exit /b 1
)

echo.
echo ==========================================
echo   Compilacion exitosa
echo ==========================================
echo   JAR generado en:
echo   target\gestion-usuarios-1.0.0-jar-with-dependencies.jar
echo ==========================================
echo.
pause
