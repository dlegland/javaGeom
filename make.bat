@echo off

set SRC=.\src
set BIN=.\bin
set TESTSRC=.\tests\src
set TESTBIN=.\tests\bin

if %1\==\ goto main


if %1 == main goto main
if %1 == tests goto tests
if %1 == all goto all

if %1 == geom goto geom

if %1 == doc goto doc
if %1 == clean goto clean
if %1 == jar goto jar


:main
goto fin


:geom
if exist %BIN%\math\geom2d\*.class del %BIN%\math\geom2d\*.class
javac -d %BIN% %SRC%\math\geom2d\*.java
goto fin

:tests
if exist %TESTBIN%\*.class del %TESTBIN%\*.class
javac -classpath %BIN% -d %TESTBIN% %TESTSRC%\*.java
goto fin


:clean
if exist %SRC%\math\geom2d\*.BAK del %SRC%\math\geom2d\*.BAK
goto fin

:doc
javadoc -d doc -private %SRC%\math\geom2d\*.java
goto fin

REM build jar file-------------------------------------------

:jar
echo make jar file
if exist *.jar del *.jar
jar -cvf math.geom2d.jar %BIN%\math\geom2d\*


:all
make geom
goto fin


:fin

