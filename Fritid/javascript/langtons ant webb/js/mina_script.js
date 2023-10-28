$(document).ready(function(){
	
	//drawing
	var can = $("#canvas canvas")[0];
	var ctx = can.getContext("2d");
	
	var scale = 1;
	var xTrans = 0; //- betyder hela grejen är förskjuten år vänster
	var yTrans = 0;
	var tileSize = 30;
	var cWidth, cHeight;
	
	//allt scaleat
	var sxTrans, syTrans, stileSize; //dessa används för att rita, ändra på de ovanför för att göra saker
	
	var board = {}; //column, rad
	var antx = 1;
	var anty = 1;
	var antDir = 0; //0=upp, 1 = vänster
	
	var yScrollSpeed = 30;
	var xScrollSpeed = 30;
	var scaleScrollSpeed = 0.1;
	
	var allColors = ["#FFFFFF", "#222222", "#FF0000", "#00FF00", "#0000FF", "#FF60D7", "#FFBF00"];
	var arrows = ["&#x21B6;", "&#x2191;", "&#x21B7;"] //left, fram, höger
	
	var colors = ["#FFFFFF", "#222222"];
	var turns = [0, 2]; //0 = sväng vänster, 1 = rakt fram, 2 = sväng höger
	var background = colors[0];
	
	var speed = 500;
	var speedScrollSpeed = 50;
	
	var antDraw = 0; //0 = allt, 1=lite, 2 = inget
	//var stepsPerTick = 1;
	var bucketColor = 0;
	var bucketSize = 1;
	
	var mouseClicked = false;
	
	//main draw. Ritar om allt
	function draw(){
		//ctx.clearRect(0, 0, cWidth, cHeight);
		ctx.fillStyle = background;
		ctx.fillRect(0, 0, cWidth, cHeight);
		
		applyScale();
		drawColors();
		drawGrid();
		drawAnt();
	}
	
	//fixar alla scale-variabler för ritning
	function applyScale(){
		sxTrans = Math.round(xTrans * scale);
		syTrans = Math.round(yTrans * scale);
		stileSize = Math.round(tileSize * scale);
		console.log(sxTrans+" "+syTrans+" "+stileSize);
	}
	
	//ritar alla rutor till rätt färg
	function drawColors(){
		var x = parseInt(-sxTrans / stileSize);
		var y = parseInt(-syTrans / stileSize);
	
		var xStart = sxTrans % stileSize;
		for(var i = xStart; i < cWidth; i+=stileSize){
			if(x in board){
				var col = board[x];
				var ty = y;
				var yStart = syTrans % stileSize;
				for(var j = yStart; j < cHeight; j+=stileSize){
					if(ty in col){
						ctx.beginPath();
						ctx.fillStyle = colors[col[ty]];
						ctx.rect(i, j, stileSize, stileSize);
						ctx.fill();
					}
					ty++;
				}	
			}
			x++;
		}
	}
	
	//ritar en grid
	function drawGrid(){
		//ctx.save();
		//ctx.translate(0.5, 0.5);
		
		ctx.strokeStyle = "#000000";
		
		var xStart = ((sxTrans % stileSize) + stileSize) % stileSize;
		for(var x = xStart; x < cWidth; x+=stileSize){
			ctx.beginPath();
			ctx.moveTo(x, 0);
			ctx.lineTo(x, cHeight);
			ctx.stroke();
		}
		
		var yStart = ((syTrans % stileSize) + stileSize) % stileSize;
		for(var y = yStart; y < cHeight; y+=stileSize){
			ctx.beginPath();
			ctx.moveTo(0, y);
			ctx.lineTo(cWidth, y);
			ctx.stroke();
		}
		
		//ctx.restore();
	}
	
	//draw ant
	function drawAnt(){
		if(antDraw == 0){
			ctx.save();
			
			ctx.fillStyle = "#000000";
			ctx.strokeStyle = "#000000";
			ctx.scale(scale, scale);
			ctx.translate(xTrans+antx*tileSize+tileSize/2, yTrans+anty*tileSize+tileSize/2);
			ctx.rotate(-Math.PI/2 * antDir);
			
			//kropp
			ctx.beginPath();
			ctx.moveTo(0, -7);
			ctx.lineTo(0, 5);
			ctx.stroke();
			
			//huvud
			ctx.beginPath();
			ctx.arc(0, -7, 3, 0, 2*Math.PI);
			ctx.fill();
			
			//saker som sticker ut ifrån huvudet
			ctx.beginPath();
			ctx.moveTo(0, -7);
			ctx.lineTo(-5, -13);
			ctx.stroke();
			
			ctx.beginPath();
			ctx.moveTo(0, -7);
			ctx.lineTo(5, -13);
			ctx.stroke();
			
			//ben
			for(var i = 0; i < 3; i++){
				for(var j = 0; j < 2; j++){
					ctx.beginPath();
					ctx.moveTo(0, -2+i*3.5);
					ctx.lineTo(j == 0 ? -7 : 7, -4+i*5.5)
					ctx.stroke();
				}
			}
			
			//bakdel
			ctx.scale(1, 1.4);
			ctx.beginPath();
			ctx.arc(0, 6.5, 3, 0, 2*Math.PI);
			ctx.fill();
			
			ctx.restore();
		}else if(antDraw == 1){
			ctx.beginPath();
			ctx.fillStyle = "#FF0000";
			ctx.rect(sxTrans+antx*stileSize, syTrans+anty*stileSize, stileSize, stileSize);
			ctx.fill();
		}
	}
	
	function drawNecessary(oldx, oldy){
		//där myran var
		drawNessSquare(colors[getColor(oldx, oldy)], oldx, oldy);
		
		//där myran är
		drawNessSquare(colors[getColor(antx, anty)], antx, anty);
		
		drawAnt();
		//drawGrid();
	}
	
	function drawNessSquare(color, x, y){
		var cx = sxTrans + x*stileSize;
		var cy = syTrans + y*stileSize;
		
		ctx.beginPath();
		ctx.fillStyle = color;
		ctx.rect(cx, cy, stileSize, stileSize);
		ctx.fill();
		
		
		ctx.strokeSize = "#000000";
		/*ctx.beginPath();
		ctx.moveTo(cx, cy+stileSize);
		ctx.lineTo(cx, cy);
		ctx.lineTo(cx+stileSize, cy);*/
		ctx.stroke();
		
	}
	
	//html color setter
	$("#colorAdd").click(function(){
		addColor();
	});
	$(".color").click(colorClick);
	$(".direction").click(dirClick);
	$(".remove").click(removeColor);
	$(".color img").click(bucketClick);
	
	function colorClick(){
		var ele = $(this);
		
		var cur = parseInt(ele.attr("colorindex"));
		cur = (cur+1) % allColors.length;
		ele.attr("colorindex", cur);
		ele.css("background", allColors[cur]);
		
		var index = ele.index(".color");
		colors[index] = allColors[cur];
		
		//is the first one
		if(index == 0){
			background = allColors[cur];
		}
		
		draw();
	}
	
	function dirClick(event){
		var ele = $(this);
		event.stopPropagation();
		
		var cur = parseInt(ele.attr("turn"));
		cur = (cur+1) % arrows.length;
		ele.attr("turn", cur);
		ele.html(arrows[cur]);
		
		var index = ele.parent().index(".color");
		turns[index] = cur;
	}
	
	function removeColor(event){
		var ele = $(this);
		event.stopPropagation();
		
		var index = ele.parent().index(".color");
		colors.splice(index, 1);
		turns.splice(index, 1);
		ele.parent().remove();
	}
	
	function bucketClick(event){
		event.stopPropagation();
		bucketColor = $(this).parent().index(".color");
		console.log(bucketColor);
	}
	
	function addColor(){
		var yttre = $("<div></div>");
		yttre.addClass("color");
		yttre.attr("colorindex", "0");
		yttre.css("background", allColors[0]);
		yttre.click(colorClick);
		
		var inre = $("<div></div>");
		inre.addClass("direction");
		inre.attr("turn", "0");
		inre.html(arrows[0]);
		inre.click(dirClick);
		yttre.append(inre);
		
		var rem = $("<div class=\"remove\">X</div>");
		rem.click(removeColor);
		yttre.append(rem);
		
		rem = $("<img src=\"img/bucket.ico\" />");
		rem.click(bucketClick);
		yttre.append(rem);
		
		$("#colorAdd").before(yttre);
		
		colors.push(allColors[0]);
		turns.push(0);
	}
	
	//controls
	$("#antDraw").change(function(){
		antDraw = $(this)[0].selectedIndex;
		draw();
	});
	
	/*
	$("#stepTick").change(function(){
		stepsPerTick = $(this).val()
	});
	*/
	
	$("#pTop").click(function(){
		yTrans += yScrollSpeed;
		draw();
	});
	
	$("#pBottom").click(function(){
		yTrans -= yScrollSpeed;
		draw();
	});
	
	$("#pLeft").click(function(){
		xTrans += xScrollSpeed;
		draw();
	});
	
	$("#pRight").click(function(){
		xTrans -= xScrollSpeed;
		draw();
	});
	
	$(window).keydown(function(event){
		if(event.which == 38){
			$("#pTop").click();
		}else if(event.which == 40){
			$("#pBottom").click();
		}else if(event.which == 37){
			$("#pLeft").click();
		}else if(event.which == 39){
			$("#pRight").click();
		}
	});
	
	//stileSize osv. är inte ändrade än
	function scaleCenter(){
		var oldx = cWidth / stileSize;
		var oldy = cHeight / stileSize;
		var afterx = cWidth / (tileSize*scale);
		var aftery = cHeight / (tileSize*scale);
		xTrans -= ((oldx-afterx)/2)*tileSize;
		yTrans -= ((oldy-aftery)/2)*tileSize;
	}
	
	$("#zoomIn").click(function(){
		scale += scaleScrollSpeed;
		scaleCenter();
		draw();
		console.log("scale: "+scale);
	});
	
	$("#zoomOut").click(function(){
		if(scale > scaleScrollSpeed)
			scale -= scaleScrollSpeed;
		if(scale <= scaleScrollSpeed)
			scale = scaleScrollSpeed;
		
		scaleCenter();
		draw();
		
		console.log("scale: "+scale);
	});
	
	$("#slower").click(function(){
		speed += speedScrollSpeed;
		stop();
		start();
	});
	
	$("#faster").click(function(){
		if(speed > speedScrollSpeed)
			speed -= speedScrollSpeed;
		if(speed <= speedScrollSpeed)
			speed = speedScrollSpeed;
		
		stop();
		start();
	});
	
	$("#reset").click(function(){
		stop();
		reset();
		draw();
	});
	
	$("#stop").click(function(){
		stop();
	});
	
	$("#start").click(function(){
		start();
	});
	
	$("#hyper").click(function(){
		speed = 0;
		stop();
		start();
	});
	
	//main loop
	var loop;
	
	function loopFunc(){
		var oldx = antx;
		var oldy = anty;
		
		//sväng
		var color = getColor(antx, anty);
		var t = turns[color];
		if(t == 0){
			antDir = (((antDir+1) % 4) + 4) % 4;
		}else if(t == 2){
			antDir = (((antDir-1) % 4) + 4) % 4;
		}
		
		//skifta marken
		color = (color+1) % colors.length;
		setColor(color, antx, anty);
		
		//gå framåt
		goForward();
		
		drawNecessary(oldx, oldy);
	}
	
	function goForward(){
		if(antDir == 0){
			anty--;
		}else if(antDir == 1){
			antx--;
		}else if(antDir == 2){
			anty++;
		}else if(antDir == 3){
			antx++;
		}
	}
	
	function getColor(x, y){
		if(x in board){
			var col = board[x];
			if(y in col){
				return col[y];
			}
		}
		return 0;
	}
	
	function setColor(color, x, y){
		if(!(x in board)){
			board[x] = {};
		}
		board[x][y] = color;
	}
	
	function stop(){
		clearInterval(loop);
	}
	
	function start(){
		loop = setInterval(loopFunc, speed);
		console.log("loopar nu med tiden: "+speed+" millisekunder");
	}
	
	//bucket
	function initBucket(){
		//size
		var grej = $("#bucketSize");
		for(var i = 1; i <= 10; i++){
			grej.append("<option>"+i+"</option>");
		}
		
		//color
		/*
		grej = $("#bucketColor");
		for(var i = 0; i < allColors.length; i++){
			grej.append("<option style=\"color: "+allColors[i]+";\">"+allColors[i]+"</option>");
		}
		*/
	}
	
	function bucket(mx, my){
		var x = parseInt(Math.floor((-sxTrans+mx)/stileSize));
		var y = parseInt(Math.floor((-syTrans+my)/stileSize));
		
		for(var i = 0; i < bucketSize; i++){
			for(var j = 0; j < bucketSize; j++){
				setColor(bucketColor, x-parseInt(bucketSize/2)+i, y-parseInt(bucketSize/2)+j);
			}
		}
		
		draw();
	}
	
	$("#bucketSize").change(function(){
		bucketSize = parseInt($(this).val());
	});
	
	$("#canvas canvas").mousedown(function(event){
		mouseClicked = true;
		bucket(event.pageX, event.pageY);
	});
	
	$("#canvas canvas").mouseup(function(event){
		mouseClicked = false;
	});
	
	$("#canvas canvas").mouseleave(function(event){
		mouseClicked = false;
	});
	
	$("#canvas canvas").mousemove(function(event){
		if(mouseClicked){
			bucket(event.pageX, event.pageY);
		}
	});
	
	//resize
	function fixSize(){
		var c = $("#canvas canvas");
		var d = $("#canvas");
		c.attr("width", d.width());
		c.attr("height", d.height());
		
		cWidth = d.width();
		cHeight = d.height();
		
		ctx.translate(0.5, 0.5); //så att linerna inte på antialiasade
	}
	
	$(window).resize(function(){
		fixSize();
		draw();
	});
	
	function initAnt(){
		var w = parseInt(cWidth / stileSize);
		var h = parseInt(cHeight / stileSize);
		
		antx = parseInt(w/2);
		anty = parseInt(h/2);
		
		antDir = 0;
	}
	
	function reset(){
		xTrans = 0;
		yTrans = 0;
		scale = 1;
		board = {};
		speed = 500;
		applyScale();
		initAnt();
	}
	
	//första gången
	fixSize();
	applyScale();
	initBucket();
	reset();
	draw();
	
});