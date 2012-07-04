adb push InjectAgent.jar /data/local/tmp/InjectAgent.jar
adb forward tcp:1324 tcp:1324
adb shell "export CLASSPATH=/data/local/tmp/InjectAgent.jar; exec app_process /system/bin net.srcz.android.screencast.client.Main 1324"