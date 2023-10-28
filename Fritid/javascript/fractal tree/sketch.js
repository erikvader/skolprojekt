var sliAng, sliShrink, sliStartLen, sliAngShrink, sliLimit, sliBranches;
var plzDraw = true;

var ang = 3.14/4;
var angShrink = 1;
var shrink = 0.67
var limit = 10;
var startLen = 100;
var branches = 2;

function setup(){
	createCanvas(1080, 680);
	pixelDensity(1);
	frameRate(30);
	//noLoop();
	
	select("#defaultCanvas0").parent(select("#canvasHolder"));
	
	var sh = select("#controls");
	
	sh.child(createSpan("angle: "));
	sliAng = createSlider(0, PI, ang, 0.01);
	sliAng.input(sliAngChanged);
	sh.child(sliAng);
	
	sh.child(createSpan("shrink: "));
	sliShrink = createSlider(0, 1, shrink, 0.01);
	sliShrink.input(sliShrinkChanged);
	sh.child(sliShrink);
	
	sh.child(createSpan("startLen: "));
	sliStartLen = createSlider(1, 500, startLen, 1);
	sliStartLen.input(sliStartLenChanged);
	sh.child(sliStartLen);
	
	sh.child(createSpan("angShrink: "));
	sliAngShrink = createSlider(0, 2, angShrink, 0.01);
	sliAngShrink.input(sliAngShrinkChanged);
	sh.child(sliAngShrink);
	
	sh.child(createSpan("depth: "));
	sliLimit = createSlider(0, 25, limit, 1);
	sliLimit.input(sliLimitChanged);
	sh.child(sliLimit);
	
	sh.child(createSpan("branches: "));
	sliBranches = createSlider(1, 7, branches, 1);
	sliBranches.input(sliBranchesChanged);
	sh.child(sliBranches);
}

function sliAngChanged(){
	ang = this.value();
	plzDraw = true;
}

function sliShrinkChanged(){
	shrink = this.value();
	plzDraw = true;
}

function sliStartLenChanged(){
	startLen = this.value();
	plzDraw = true;
}

function sliAngShrinkChanged(){
	angShrink = this.value();
	plzDraw = true;
}

function sliLimitChanged(){
	limit = this.value();
	plzDraw = true;
}

function sliBranchesChanged(){
	branches = this.value();
	plzDraw = true;
}

function pinne(len, a, count){
	if(count >= limit) return;
	push();
	
	line(0, 0, 0, -len);
	translate(0, -len);
	
	push();
	var eachSide = parseInt(branches/2);
	if(branches % 2 == 0){
		rotate(-a/2);
	}
	for(var i = 0; i < eachSide; i++){
		rotate(a);
		pinne(len*shrink, a*angShrink, count+1);
	}
	pop();
	push();
	if(branches % 2 == 0){
		rotate(a/2);
	}
	for(var i = 0; i < eachSide; i++){
		rotate(-a);
		pinne(len*shrink, a*angShrink, count+1);
	}
	pop();
	
	if(branches % 2 == 1){
		pinne(len*shrink, a*angShrink, count+1);
	}
	
	pop();
}

function draw(){
	if(!plzDraw) return;
	background(0);
	
	stroke(255);
	translate(width/2, height);
	
	pinne(startLen*shrink, ang*angShrink, 0);
	
	plzDraw = false;
}