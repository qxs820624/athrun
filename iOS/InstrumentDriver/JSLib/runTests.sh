#! /bin/sh

#@author mijun@douban.com

XCODE_PATH=`xcode-select -print-path`
TRACETEMPLATE="$XCODE_PATH/Platforms/iPhoneOS.platform/Developer/Library/Instruments/PlugIns/AutomationInstrument.bundle/Contents/Resources/Automation.tracetemplate"
APP_LOCATION=$1

#APP_LOCATION="/Users/komejun/Library/Application Support/iPhone Simulator/5.0/Applications/1622F505-8C07-47E0-B0F0-3A125A88B329/Recipes.app/"


#APP_LOCATION="/Users/athrun/Desktop/TaoTest/build/Debug-iphonesimulator/TaoTest.app"

echo $XCODE_PATH



instruments \
-t $TRACETEMPLATE \
"$APP_LOCATION" \
-e UIASCRIPT  ./JSLib/CSRunner.js \
-e UIARESULTSPATH  /Athrun/log/

