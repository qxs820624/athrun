#! /bin/sh

#@author mijun@douban.com
#@author taichan@taobao.com

XCODE_PATH=`xcode-select -print-path`
TRACETEMPLATE="$XCODE_PATH/Platforms/iPhoneOS.platform/Developer/Library/Instruments/PlugIns/AutomationInstrument.bundle/Contents/Resources/Automation.tracetemplate"
APP_LOCATION=$1
INSTRUMENT_ROOT=$2

#APP_LOCATION="/Users/komejun/Library/Application Support/iPhone Simulator/5.0/Applications/1622F505-8C07-47E0-B0F0-3A125A88B329/Recipes.app/"


#APP_LOCATION="/Users/athrun/Desktop/TaoTest/build/Debug-iphonesimulator/TaoTest.app"

echo $XCODE_PATH
echo instruments -t $TRACETEMPLATE "$APP_LOCATION" -e UIASCRIPT  "$INSTRUMENT_ROOT"/CSRunner.js -e UIARESULTSPATH  "$INSTRUMENT_ROOT"/log/


instruments \
-t $TRACETEMPLATE \
"$APP_LOCATION" \
-e UIASCRIPT  "$INSTRUMENT_ROOT"/CSRunner.js \
-e UIARESULTSPATH  "$INSTRUMENT_ROOT"/log/



