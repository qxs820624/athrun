<%@ page language="java" contentType="text/html; charset=GB18030"
	pageEncoding="GB18030"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=GB18030">
<title>Athrun PC-Mobile Demo</title>
<link rel="stylesheet" type="text/css" href="/athrun/css/athrun.css">
</head>



<!-- jQuery�� -->
<script type="text/javascript"
	src="http://ajax.googleapis.com/ajax/libs/jquery/1.7.2/jquery.min.js"></script>



<body>

<input id="rotateRate" type="button" value="��ת" />
			 <br /> 
	<span style="display: none">!!! ����֧�� HTML 5 ������� !!!</span>
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
		<br /> <input type="radio" value="1" name="resize">����</input> <input
			type="radio" value="2" name="resize" checked="checked">����</input> <input
			type="radio" value="3" name="resize">����</input> <br /> <span>ѹ����</span><input
			id="qualityRate" type="text" value="50" /> <br /> 
			
			<span id="eventSent" style="display: none">eventSent Msg</span> 
			<br /> 
			<span id="eventResult" style="display: none">eventResult Msg</span>

	</div>

	<dir>
		<span> ʹ��˵����</span>
		<br />
		<span> 1. �����ɫStart��ť</span>
		<br />
		<span> 2. ����Ļ����������</span>
		<br />
		<span> 3. ���ɫStop��ť����F5ˢ�£����¿�ʼ</span>
	</dir>


	<script>
		var serialNumber =	"<%=request.getParameter("serialNumber")%>";

		var width, height;

		var rotateRate = 0;

		var imgtag = document.getElementById('imgtag');
		var canvas = document.getElementById('myCanvas');
		var context = canvas.getContext('2d');
		imgtag.onload = function() {
			width = this.width;
			height = this.height;
			adjustCanvasSize(canvas, context);
			context.drawImage(imgtag, 0, 0, width, height);

			context.fillStyle = '#f00';
			context.font = 'bold 42px sans-serif';
			context.textBaseline = 'top';
			context.fillText('Stoped', width / 2 - 80, height / 2);
		}
		imgtag.src = "/athrun/JpgGen.jpg?ts=0&serialNumber=" + serialNumber;

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
						redraw(); // �ػ����
						$('#start').trigger('click');

					}; //����ͼ����ȫ���뵽�ڴ�֮����á�

					img.onerror = function() {
						alert("error!");
					}; //����ͼ������ʧ�ܺ���ã�ͼ������ʧ�ܲ������onload�¼���
					timestamp = (new Date()).valueOf();
					img.src = "/athrun/JpgGen.jpg?ts=" + timestamp
							+ "&serialNumber=" + serialNumber; //��ʼ����ͼƬ,����ͼƬ���첽���̡�

				});

		var timer;

		$('#stop').click(function() {
			window.location.reload(true);
		});

		$('#rotateRate').click(function() {
			rotateRate++;
			if (rotateRate >= 4) {
				rotateRate = 0;
			}
			//var canvas = document.getElementById('myCanvas');
			//var context = canvas.getContext('2d');
			adjustCanvasSize(canvas, context);
		});

		var clickX = new Array();
		var clickY = new Array();
		var clickDrag = new Array();
		var drawCount = 0;
		var paint;
		var actionNumber = 0;

		function adjustCanvasSize(canvas, context) {
			canvas.width = (rotateRate % 2) ? height : width;
			canvas.height = (rotateRate % 2) ? width : height;

			context.translate(
					(rotateRate == 1 || rotateRate == 2) ? canvas.width : 0,
					(rotateRate == 2 || rotateRate == 3) ? canvas.height : 0);
			context.rotate(Math.PI * rotateRate / 2);
		}

		function addActionNumber() {
			if (actionNumber > 9) {
				actionNumber = 0;
			} else {
				actionNumber++;
			}
		}

		function addClick(x, y, dragging) {
			clickX.push(x);
			clickY.push(y);
			clickDrag.push(dragging);
		}

		function loadWhileStart() {
			$('input:radio[name=resize]').change(function() {
				txt = $('input:radio:checked[name=resize]').val();
				$.post("/athrun/imageSize?serialNumber=" + serialNumber, {
					rate : txt
				});
			});
			$('#qualityRate').focusout(function(e) {
				txt = $('#qualityRate').val();
				$.post("/athrun/imageQuality?serialNumber=" + serialNumber, {
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
						addActionNumber();
						addClick(e.pageX - this.offsetLeft, e.pageY
								- this.offsetTop);
						sendEvent(e.pageX - this.offsetLeft, e.pageY
								- this.offsetTop, "0");
						redraw();
					});

			$('#myCanvas').mousemove(
					function(e) {
						if (paint) {//�ǲ��ǰ��������  
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
				if (clickDrag[i] && i) {//�����϶�����i!=0ʱ������һ���㿪ʼ���ߡ�  
					var p = adjustDrawPoint(clickX[i - 1], clickY[i - 1]);
					context.moveTo(p[0],p[1]);
				} else {
					var p = adjustDrawPoint(clickX[i] - 1, clickY[i]);
					context.moveTo(p[0],p[1]);
				}
				var pt = adjustDrawPoint(clickX[i], clickY[i]);
				context.lineTo(pt[0],pt[1]);
				context.closePath();
				context.stroke();

			}
		}

		function adjustDrawPoint(x, y) {
			if (rotateRate == 0) {
				return [ x, y ];
			} else {
				if (rotateRate == 1) {
					return [ y, height - x ];
				} else {
					if (rotateRate == 2) {
						return [ width - x, height - y ];
					} else {//rotateRate==3
						return [ width - y, x ];
					}
				}
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
			url = url + "&an=" + actionNumber;

			$('#eventSent').text(url);

			$.get(url, function(data) {
				$('#eventResult').text("ͬ�����");
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
				$('#eventResult').text("ͬ�����");
			});
		}
	</script>


</body>
</html>