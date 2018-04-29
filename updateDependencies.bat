cd app\src\main\java\com\asofterspace

rd /s /q toolbox

md toolbox
cd toolbox

md web

cd ..

copy "..\..\..\..\..\..\..\Toolbox-Java\src\com\asofterspace\toolbox\*.java" "toolbox"
copy "..\..\..\..\..\..\..\Toolbox-Java\src\com\asofterspace\toolbox\web\*.*" "toolbox\web"

pause
