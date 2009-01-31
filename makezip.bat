@REM Build the file Euclide.jar
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

@echo off


@REM remove old copy of source folder if it exists
FOR /F "tokens=*" %%i IN ('dir /a:d /b src2') DO rmdir /S /Q %%i

@REM make a copy of the source directory
robocopy src src2 *.* /s

@REM remove '.svn' directories, created by Eclipse from subversion repository
FOR /F "tokens=*" %%i IN ('dir /a:d /s /b src2\*.svn*') DO rmdir /S /Q %%i

@REM remove old archive if it exists
FOR /F "tokens=*" %%i IN ('dir /b javaGeom.zip') DO del /f /q %%i


