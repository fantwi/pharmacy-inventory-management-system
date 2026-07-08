@echo off
setlocal
if not exist out mkdir out
javac -d out src\com\pharmacy\Main.java src\com\pharmacy\model\*.java src\com\pharmacy\service\*.java src\com\pharmacy\exception\*.java src\com\pharmacy\ui\*.java
if errorlevel 1 (
  echo Build failed.
  exit /b 1
)
java -cp out com.pharmacy.Main