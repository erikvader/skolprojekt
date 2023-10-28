var vertices = [];
var started = false;
var drawLines = false;
var curDist = 0;
var startBtn;
var displayDist;
var speed = 1;
var inputSpeed;

function setup(){
	createCanvas(640, 480);
	pixelDensity(1);
	frameRate(60);
	colorMode(RGB);

	select("#defaultCanvas0").parent(select("#canvasHolder"));

	startBtn = select("#startBtn");
	startBtn.mouseClicked(butClick);

	displayDist = select("#distanceDivSpan");
	var si = select("#speedInput");
	si.changed(inputSpeedChanged);
	si.value(speed);

	noLoop();
}

function inputSpeedChanged(){
	var num = parseInt(this.value());
	if(num && num > 0)
		speed = num;
}

function butClick(){
	if(!started){
		if(vertices.length == 0) return;
		shuffle(vertices, true);
		started = true;
		drawLines = true;
		startBtn.html("stop");
		//startDist
		curDist = calcDist(vertices);
		loop();
	}else{
		started = false;
		startBtn.html("start");
		noLoop();
	}
}

function mousePressed(){
	if(mouseX > 0 && mouseX < width && mouseY > 0 && mouseY < height){
		vertices.push(createVector(mouseX, mouseY));
		drawLines = false;
		redraw();
	}
}

function fastDist(p1, p2){
	return pow(p1.x-p2.x, 2) + pow(p1.y-p2.y, 2);
}

function calcDist(arr){
	var dist = 0;
	for(var i = 0; i < arr.length; i++){
		var next = getNext(i, arr.length);
		dist += fastDist(arr[i], arr[next]);
	}
	return dist;
}

function do2opt(){
	var i = parseInt(random(vertices.length));
	var j;
	do{
		j = parseInt(random(vertices.length));
	}while(j == i);

	var newRoute = [];
	newRoute = newRoute.concat(vertices.slice(0, min(i, j)));
	newRoute = newRoute.concat(vertices.slice(min(i, j), max(i, j)+1).reverse());
	newRoute = newRoute.concat(vertices.slice(max(i, j)+1));
	var newDist = calcDist(newRoute);

	//rita vilka man testade att flytta på
	//tror att den ritar rätt ;)
	stroke(255, 0, 0);
	strokeWeight(3);
	drawLine(getPrev(min(i, j), vertices.length), max(i, j));
	drawLine(min(i, j), getNext(max(i, j), vertices.length));

	if(newDist < curDist){
		curDist = newDist;
		vertices = newRoute;
	}
}

function swap(i, j, arr){
	var temp = arr[i];
	arr[i] = arr[j];
	arr[j] = temp;
}

function getNext(i, l){
	return (i+1) % l
}

function getPrev(i, l){
	return (i-1 + l) % l
}

function drawLine(i, j){
	line(vertices[i].x, vertices[i].y, vertices[j].x, vertices[j].y);
}

function drawPoint(i){
	point(vertices[i].x, vertices[i].y);
}

function draw(){
   background(0);
   console.log("loop");

	//leta
	if(started){
		for(var i = 0; i < speed; i++)
			do2opt();
		displayDist.html(curDist);
	}

	//draw vertices
	stroke(255);
	for(var i = 0; i < vertices.length; i++){
		strokeWeight(10);
		drawPoint(i);
		if(drawLines){
			strokeWeight(3);
			drawLine(i, getNext(i, vertices.length));
		}
	}

}
