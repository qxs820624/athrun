<%@page contentType="text/html" pageEncoding="UTF-8"%>
<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN"
"http://www.w3.org/TR/html4/loose.dtd">
<%@taglib uri="/struts-tags" prefix="s"%>
<html>

<!-- jQuery库 -->
<script type="text/javascript"
	src="http://assets.kelude.taobao.net/doc/widget/js/jquery.js"></script>

<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<title>Hello World</title>
</head>
<body>
	<s:form action="HelloWorld" namespace="/device" id="formMain">
		<s:textfield name="userName" label="User Name" id="usrname" value="123" />
		<input type="button" id="go" value="go" />
	</s:form>
	<div id="rep"></div>
</body>
<script type="text/javascript">
	$('#go').click(function() {

		$.ajax({
			url : $("#formMain").attr("action"),
			method : "post",
			data : "userName=" + $("#usrname").val(),
			// 一系列其他你需要的参数（详见文档）
			success : function(dataFromServer) {
				// 处理服务端返回的数据dataFromServer
				$("#rep").html(dataFromServer);
			}
		});

	});
</script>
</html>
