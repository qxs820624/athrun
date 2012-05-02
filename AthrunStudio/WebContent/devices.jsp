<%@page contentType="text/html" pageEncoding="UTF-8"%>
<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN"
"http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<style type="text/css">
body {
	font-family: Arial, Sans-Serif;
	font-size: 1em;
}

#search-input {
	width: 250px;
	height: 29px;
	margin: 0;
	float: left;
	-webkit-border-radius: 1px;
	-moz-border-radius: 1px;
	border-radius: 1px;
	border: 1px solid #D9D9D9;
	border-top: 1px solid silver;
	font-size: 13px;
	height: 25px;
	padding: 1px 8px;
}

.search-button {
	background-color: #4D90FE;
	background-image: -webkit-linear-gradient(top, #4D90FE, #4787ED);
	background-image: -moz-linear-gradient(top, #4D90FE, #4787ED);
	background-image: -ms-linear-gradient(top, #4D90FE, #4787ED);
	background-image: -o-linear-gradient(top, #4D90FE, #4787ED);
	background-image: linear-gradient(top, #4D90FE, #4787ED);
	border: 1px solid #3079ED;
	color: white;
	letter-spacing: 3px;
	-webkit-border-radius: 2px;
	-moz-border-radius: 2px;
	border-radius: 2px;
	cursor: default;
	font-size: 11px;
	font-weight: bold;
	text-align: center;
	margin-right: 16px;
	height: 27px;
	line-height: 27px;
	min-width: 54px;
	outline: 0;
	padding: 0 8px;
	position: relative;
	display: -moz-inline-box;
	display: inline-block;
}

img {
	border: transparent 1px solid;
	padding: 2px;
}

img:hover {
	border: #D7D3E0 1px solid;
}

#list {
	width: 850px;
	margin: 0 auto;
	padding: 0;
}

#list li {
	list-style: none;
	width: 360px;
	float: left;
	margin: 0 50px 0 0;
	padding: 25px 0;
	border-top: 1px solid #d7d3e0;
}

#list .left {
	width: 200px;
	margin-right: 20px;
}

#list .right,#list .right img {
	width: 130px;
}
/*#list h3 { display:block; font-size:1.5em; margin:5px; color:#fb5615; }*/
#list li div {
	display: inline-block;
	margin: 5px 0px;
}

#list td {
	padding: 4px 12px 4px 0;
	font-size: 0.75em;
}

a {
	color: #0092c7;
	text-decoration: none;
}

a:hover {
	text-decoration: underline;
}

#list.uncompact li {
	width: 880px;
}

#commands {
	margin: 0 38px 0 0;
	padding-top: 12px;
	height: 29px;
	float: right;
	display: inline-block;
	font-size: 0.75em;
	width: 150px;
}

#commands a,#commands span {
	float: left;
}
</style>

<title>Athrun Device List</title>
</head>
<body>	
	<ul id="list">
		<h3>
			<input id="search-input" type="text" title="" style="height: 29px;">
			<div role="button" class="search-button" tabindex="0" title="搜索"
				style="font-size: 13px;">搜索</div>

			<p id="commands">
				<span>改变布局：</span> <a id="verticalswitch" href="#"><img
					alt="上下式布局" title="上下式布局"
					src="img/adaptable-view/switch_sm_compact.png" /></a> <a
					id="horizontalswitch" href="#"><img alt="左右式布局" title="左右式布局"
					src="img/adaptable-view/switch_sm_full_gray.png" /></a>
			</p>
		</h3>
		<span id="resp"> </span>
</body>
<script type="text/javascript"
	src="http://ajax.googleapis.com/ajax/libs/jquery/1/jquery.min.js"></script>
<script type="text/javascript">
	$('#horizontalswitch').click(
			function() {
				$('#horizontalswitch > img').attr("src",
						"img/adaptable-view/switch_sm_full.png")
				$('#verticalswitch > img').attr("src",
						"img/adaptable-view/switch_sm_compact_gray.png")
				$('#list').addClass('uncompact');
			})
	$('#verticalswitch').click(
			function() {
				$('#horizontalswitch > img').attr("src",
						"img/adaptable-view/switch_sm_full_gray.png")
				$('#verticalswitch > img').attr("src",
						"img/adaptable-view/switch_sm_compact.png")
				$('#list').removeClass('uncompact');
			})
</script>
<script type="text/javascript">
	$('.search-button').click(function() {
		$.ajax({
			url : "device/HelloWorld.action",
			method : "post",
			data : "userName=" + $("#search-input").val(),
			// 一系列其他你需要的参数（详见文档）
			success : function(dataFromServer) {
				// 处理服务端返回的数据dataFromServer
				$("#resp").html(dataFromServer);
			}
		});
	});
</script>
<script type="text/javascript">
	function search() {
		$('.search-button').click();
	}
	window.onload = search();
</script>
</html>
