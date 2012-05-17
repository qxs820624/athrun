<%@ page language="java" contentType="text/html; charset=GB18030"
	pageEncoding="GB18030"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=GB18030">
<title>Athrun PC-Mobile Demo</title>
<link rel="stylesheet" type="text/css"
	href="/athrun/css/athrun.css">
</head>



<!-- kelude组件样式库 -->
<link type="text/css" rel="stylesheet"
	href="http://assets.kelude.taobao.net/doc/widget/css/kelude-all.css">

<!-- jQuery库 -->
<script type="text/javascript"
	src="http://assets.kelude.taobao.net/doc/widget/js/jquery.js"></script>



<body>

	<span>!!! 请用支持 HTML 5 的浏览器 !!!</span>
	<br />
	<img id="start" src="/athrun/img/start.png" title="start" />
	<img id="stop" src="/athrun/img/stop.png" title="stop" />
	<div>
		<img id="imgtag" style="display: none;"></img>
	</div>
	<br />

	<canvas id="myCanvas"></canvas>

	<div class="controls stoped">
		<button id="home">home</button>
		<button id="menu">menu</button>
		<button id="back">back</button>
		<button id="search">search</button>
		<br />
		<button id="VOLUME_UP">VOLUME_UP</button>
		<button id="VOLUME_DOWN">VOLUME_DOWN</button>
		<br /> <input type="radio" value="1" name="resize">高清</input> <input
			type="radio" value="2" name="resize" checked="checked">清晰</input> <input
			type="radio" value="3" name="resize">流畅</input> <br /> <span>压缩率</span><input
			id="qualityRate" type="text" value="50" /> <br /> <span
			id="eventSent">eventSent Msg</span> <br /> <span id="eventResult">eventResult
			Msg</span>

	</div>

	<dir>
		<span> 使用说明：</span>
		<br />
		<span> 1. 点击蓝色Start按钮</span>
		<br />
		<span> 2. 在屏幕里用鼠标操作</span>
		<br />
		<span> 3. 点红色Stop按钮或者F5刷新，重新开始</span>
	</dir>


	<script>
		var serialNumber =	"<%=request.getParameter("serialNumber")%>" ;

		var width, height;

		var imgtag = document.getElementById('imgtag');
		var canvas = document.getElementById('myCanvas');
		var context = canvas.getContext('2d');
		imgtag.onload = function() {
			width = this.width;
			height = this.height;
			canvas.width = width;
			canvas.height = height;
			context.drawImage(imgtag, 0, 0, width, height);

			context.fillStyle = '#f00';
			context.font = 'bold 42px sans-serif';
			context.textBaseline = 'top';
			context.fillText('Stoped', width / 2 - 80, height / 2);
		}
		imgtag.src = "/athrun/JpgGen.jpg?ts=0&serialNumber="
				+ serialNumber;

		var timestamp = 0;

		var firsttime = true;

		$('#start').mousedown(function(e) {
			if (firsttime == true) {
				loadWhileStart();
				firsttime = false;
			}
		});

		$('#start').live(
				'click',
				function() {

					var img = new Image();
					img.onload = function() {

						context.drawImage(img, 0, 0, width, height);
						redraw(); // 重绘鼠标
						$('#start').trigger('click');

					}; //它在图像完全载入到内存之后调用。

					img.onerror = function() {
						alert("error!");
					}; //它在图像载入失败后调用，图像载入失败不会调用onload事件。
					timestamp = (new Date()).valueOf();
					img.src = "/athrun/JpgGen.jpg?ts=" + timestamp
							+ "&serialNumber=" + serialNumber; //开始加载图片,加载图片是异步过程。

				});

		var timer;

		$('#stop').click(function() {
			window.location.reload(true);
		});

		var clickX = new Array();
		var clickY = new Array();
		var clickDrag = new Array();
		var drawCount = 0;
		var paint;

		function addClick(x, y, dragging) {
			clickX.push(x);
			clickY.push(y);
			clickDrag.push(dragging);
		}

		function loadWhileStart() {
			$('input:radio[name=resize]').change(
					function() {
						txt = $('input:radio:checked[name=resize]').val();
						$.post("/athrun/imageSize?serialNumber="
								+ serialNumber, {
							rate : txt
						});
					});
			$('#qualityRate').focusout(
					function(e) {
						txt = $('#qualityRate').val();
						$.post("/athrun/imageQuality?serialNumber="
								+ serialNumber, {
							rate : txt
						});
					});
			$('.controls').removeClass("stoped");
			$('.controls').addClass("started");

			$('#myCanvas').mousedown(
					function(e) {
						var mouseX = e.pageX - this.offsetLeft;
						var mouseY = e.pageY - this.offsetTop;

						paint = true;
						addClick(e.pageX - this.offsetLeft, e.pageY
								- this.offsetTop);
						sendEvent(e.pageX - this.offsetLeft, e.pageY
								- this.offsetTop, "0");
						redraw();
					});

			$('#myCanvas').mousemove(
					function(e) {
						if (paint) {//是不是按下了鼠标  
							addClick(e.pageX - this.offsetLeft, e.pageY
									- this.offsetTop, true);
							sendEvent(e.pageX - this.offsetLeft, e.pageY
									- this.offsetTop, "2");
							redraw();
						}
					});

			$('#myCanvas').mouseup(
					function(e) {
						paint = false;
						clickX.length = 0; // clear
						clickY.length = 0; // clear
						clickDrag.length = 0; // clear
						sendEvent(e.pageX - this.offsetLeft, e.pageY
								- this.offsetTop, "1");
					});

			$('#home').mousedown(function(e) {
				// 0 down, 1 up
				keyEvent(0, KEYCODE_HOME);
			});

			$('#home').mouseup(function(e) {
				// 0 down, 1 up
				keyEvent(1, KEYCODE_HOME);
			});

			$('#back').mousedown(function(e) {
				// 0 down, 1 up
				keyEvent(0, KEYCODE_BACK);
			});

			$('#back').mouseup(function(e) {
				// 0 down, 1 up
				keyEvent(1, KEYCODE_BACK);
			});

			$('#menu').mousedown(function(e) {
				// 0 down, 1 up
				keyEvent(0, KEYCODE_MENU);
			});

			$('#menu').mouseup(function(e) {
				// 0 down, 1 up
				keyEvent(1, KEYCODE_MENU);
			});

			$('#search').mousedown(function(e) {
				// 0 down, 1 up
				keyEvent(0, KEYCODE_SEARCH);
			});

			$('#search').mouseup(function(e) {
				// 0 down, 1 up
				keyEvent(1, KEYCODE_SEARCH);
			});

			$('#VOLUME_UP').mousedown(function(e) {
				// 0 down, 1 up
				keyEvent(0, KEYCODE_VOLUME_UP);
			});

			$('#VOLUME_UP').mouseup(function(e) {
				// 0 down, 1 up
				keyEvent(1, KEYCODE_VOLUME_UP);
			});

			$('#VOLUME_DOWN').mousedown(function(e) {
				// 0 down, 1 up
				keyEvent(0, KEYCODE_VOLUME_DOWN);
			});

			$('#VOLUME_DOWN').mouseup(function(e) {
				// 0 down, 1 up
				keyEvent(1, KEYCODE_VOLUME_DOWN);
			});
		}

		function redraw() {
			// canvas.width = canvas.width; // Clears the canvas  

			context.strokeStyle = "#df4b26";
			context.lineJoin = "round";
			context.lineWidth = 5;

			for ( var i = 0; i < clickX.length; i++) {
				context.beginPath();
				if (clickDrag[i] && i) {//当是拖动而且i!=0时，从上一个点开始画线。  
					context.moveTo(clickX[i - 1], clickY[i - 1]);
				} else {
					context.moveTo(clickX[i] - 1, clickY[i]);
				}
				context.lineTo(clickX[i], clickY[i]);
				context.closePath();
				context.stroke();

			}
		}

		var resize = 2;

		function sendEvent(x, y, action) {
			var url = "Event.htm";
			url = url + "?type=" + "pointer";
			url = url + "&x=" + x * resize;
			url = url + "&y=" + y * resize;
			url = url + "&action=" + action;
			url = url + "&serialNumber=" + serialNumber;

			$('#eventSent').text(url);

			$.get(url, function(data) {
				$('#eventResult').text("同步完成");
			});
		}

		var KEYCODE_BACK = 4;
		var KEYCODE_HOME = 3;
		var KEYCODE_MENU = 82;
		var KEYCODE_SEARCH = 84;
		var KEYCODE_POWER = 26;
		var KEYCODE_VOLUME_UP = 24;
		var KEYCODE_VOLUME_DOWN = 25;
		var KEYCODE_CAMERA = 27;

		function keyEvent(action, keycode) {
			var url = "Event.htm";
			url = url + "?type=" + "key";
			url = url + "&keyCode=" + keycode;
			url = url + "&serialNumber=" + serialNumber;

			$.get(url + "&action=" + action, function(data) {
				$('#eventResult').text("同步完成");
			});
		}
	</script>

	<!-- kelude组件脚本库，可置底加载 -->
	<script type="text/javascript"
		src="http://assets.kelude.taobao.net/doc/widget/js/kelude.js"></script>


</body>
</html>