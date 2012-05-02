#!/bin/bash

echo "The app path is : $1"
echo "The script path is : $2"
touch sort.temp
function init_dir(){   

    for file in ` ls $1 ` ;do
        if (test -d $1"/"$file );then
            init_dir $1"/"$file
        else        
            echo $1"/"$file >> sort.temp
        fi
    done
}

if test -f $2;then
    echo "The script $2 is  running!!"
    oneCmd="instruments -t /Developer/Platforms/iPhoneOS.platform/Developer/Library/Instruments/PlugIns/AutomationInstrument.bundle/Contents/Resources/Automation.tracetemplate $1 -e UIASCRIPT $2 -e UIARESULTSPATH /Athrun/log/"
    $oneCmd
    echo "$2 run end!"

else
    #call  the init_dir function.
    init_dir $2

    for file in `sort -g sort.temp`;do
        
        echo "The script $file is  running!"
        cmd="instruments -t /Developer/Platforms/iPhoneOS.platform/Developer/Library/Instruments/PlugIns/AutomationInstrument.bundle/Contents/Resources/Automation.tracetemplate $1 -e UIASCRIPT $file -e UIARESULTSPATH /Athrun/log/"

        $cmd
        echo "$file run end!"
    done
fi

rm sort.temp
delCmd="rm -rf /Athrun/*.trace"
$delCmd

echo "all script run end!"
