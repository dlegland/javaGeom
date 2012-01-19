@REM Build the file javaGeom.jar
@REM
@REM Requires the file 'jar.exe' to be in the path
@REM For adding it with windows (Windows Vista)
@REM - Start->Parameters->Configuration Panel->System->
@REM 	Advanced System Parameters->Environement variables
@REM - Clic on Variable 'Path' in 'System Variable' panel
@REM - Add the path to jar.exe, typically:
@REM 	"C:\Program Files\jdk1.6.0_03\bin\"
@REM 	(Adapt version number according to your system)
@REM

@REM modified for incorporating external library gpcj (polygon clipping)

@echo off

@REM remove old copy of jar folder if it exists
FOR /F "tokens=*" %%i IN ('dir /a:d /b jar') DO rmdir /S /Q %%i

@REM make a copy of the binary directory into the jar directory
robocopy bin jar *.* /s /njs /njh /ndl

@REM remove '.svn' directories, created by Eclipse from subversion repository
FOR /F "tokens=*" %%i IN ('dir /a:d /s /b jar\*.svn*') DO rmdir /S /Q %%i

@REM change directory and uncompress external library
cd jar
jar xf ../lib/gpcj-2.2.0.jar
rmdir /S /Q META-INF
cd ..

@REM remove old archive if it exists
@REM FOR /F "tokens=*" %%i IN ('dir /b javaGeom.jar') DO del /f /q %%i
if exist javaGeom.jar del /f /q javaGeom.jar

@REM build the new archive
jar cf javaGeom.jar -C jar\ .

@REM remove temporary files created during process
:: FOR /F "tokens=*" %%i IN ('dir /a:d /b jar') DO rmdir /S /Q %%i
if exist jar rmdir /S /Q jar
