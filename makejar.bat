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

@REM remove '.svn' directories, created by Eclipse from subversion repository
FOR /F "tokens=*" %%i IN ('dir /a:d /s /b bin\*.svn*') DO rmdir /S /Q %%i

@REM remove old archive if it exists
FOR /F "tokens=*" %%i IN ('dir /b javaGeom.jar') DO del /f /q %%i

@REM build the new archive
jar cvf javaGeom.jar -C bin\ .

