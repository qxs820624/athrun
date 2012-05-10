
1.说明
  因为没法在instrument javascript脚本里建立服务端，所以只能以Mobile作为客户端，测试用例作为socket的服务端，客户端循环请求服务端(消费用例步骤)，让用例不停往下运行。直至出现发送结束符或者抛出异常。

2.相关文件(Athrun目录下)
  客户端主要有三个文件，RunScript.sh 用于启动 CSRunner.js ,该脚本文件在于Junit 测试用例进行通信运行。CSSocket.sh 作为C/S 双方的通信介质，供 CSRunner.js 调用以请求服务端的用例步骤。
