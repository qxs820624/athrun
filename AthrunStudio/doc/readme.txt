1. 把手机插好adb devices验证已经连接上
2. 开一个cmd窗口，cd到本级目录的gsnap下，运行run.bat
3. 再开一个cmd窗口，cd到本级目录的event下，运行run.bat
4. eclipse中用tomcat6，服务器运行AthrunServer工程
5. 访问http://localhost:8080/AthrunServer


说明：
1. gsnap或event的日志可以从窗口里看到，如果他们出了异常或者断开了，请重新运行相应的run.bat
2. 出现上述情况后，要重新运行tomcat服务器。