/**
 * 用例名称: C/S模式运行，启动脚本
 * 作   者: ziyu
 * 日   期: 2012-05-08
 * 备   注: Athrun C/S模式运行，启动脚本
 *
 */ 

#import "./LibJS/athrunImports.js"

target = UIATarget.localTarget();
host = target.host();	

UIALogger.logStart("The case is running.");

try {
	
	var step = 1;
	var isEnd = false;
	sendToServer ="Get the next step.";
	while(!isEnd){
		
		var result = host.performTaskWithPathArgumentsTimeout("/Athrun/TcpSocket.sh",[sendToServer],60);
		var stdout = result.stdout.split("##");
		var type = stdout[0];
		var script =stdout[1];
		
		//UIALogger.logMessage("result.stdout:" + result.stdout);
		
		switch(type)
		{
			case "stringType":
			case "booleanType":
			case "voidType":
				sendToServer = eval(script);
				if(sendToServer ==null)
					sendToServer = "";
				break;
			case "JSONArray":
				var elementArray = new Array();
				var elements = eval(script);
				for(var i=0; i<elements.length;i++)
				{
					var element = {};
					element.label = elements[i].label();
					element.name = elements[i].name();
					element.value = elements[i].value();
					element.rect  = elements[i].rect();
					element.guid = script +"[" + i + "]";
					elementArray.push(element);
				}
				sendToServer = elementArray.toJSONString();
				break;
			case "JSONObject":
				var element = eval(script);
				var e = {};
				e.label = element.label();
				e.name = element.name();
				e.value = element.value();
				e.rect  = element.rect();
				e.guid = script;
				sendToServer = e.toJSONString();
				break;
			default:
				sendToServer ="null";
				isEnd =true;
		}
	}
	
	UIALogger.logPass("The case was passed.");

} catch (e) {
		
	sendToServer = "Error #" + e;
	
	//if has exception ,send the exception to client.
	host.performTaskWithPathArgumentsTimeout("/Athrun/TcpSocket.sh",[sendToServer],60);
	//end Exit
	host.performTaskWithPathArgumentsTimeout("/Athrun/TcpSocket.sh",[sendToServer],60);
	UIALogger.logError(sendToServer);
	UIALogger.logFail("The case was failed.");	
}

