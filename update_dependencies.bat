IF NOT EXIST ..\Toolbox-Java\ (
	echo "It looks like you did not yet get the Toolbox-Java project - please do so (and put it as a folder next to the CDM Script Editor folder.)"
	EXIT
)

cd app\src\main\java\com\asofterspace

rd /s /q toolbox

md toolbox
cd toolbox

md coders
md io
md utils
md web

cd ..\..\..\..\..\..\..

copy "..\Toolbox-Java\src\com\asofterspace\toolbox\*.java" "app\src\main\java\com\asofterspace\toolbox"
copy "..\Toolbox-Java\src\com\asofterspace\toolbox\coders\*.*" "app\src\main\java\com\asofterspace\toolbox\coders"
copy "..\Toolbox-Java\src\com\asofterspace\toolbox\io\*.*" "app\src\main\java\com\asofterspace\toolbox\io"
copy "..\Toolbox-Java\src\com\asofterspace\toolbox\utils\*.*" "app\src\main\java\com\asofterspace\toolbox\utils"
copy "..\Toolbox-Java\src\com\asofterspace\toolbox\web\*.*" "app\src\main\java\com\asofterspace\toolbox\web"

pause
