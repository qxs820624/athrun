adb forward tcp:5678 tcp:5678
adb push D:/source/¿ªÔ´/taocode/TMTS/android/no-use/gsnap/libs/armeabi/gsnap /data/local/
adb shell chmod 777 /data/local/gsnap
adb shell /data/local/gsnap /sdcard/test/1.jpg /dev/graphics/fb0 50 2
