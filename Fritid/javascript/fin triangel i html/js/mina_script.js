$(document).ready(function(){
	
	var ctx = $("canvas")[0].getContext("2d");
	
	//starttriangel
	var top = {x: 250, y: 30};
	var right = {x: 475, y:419.711};
	var left = {x: 25, y: 419.711};
	
	ctx.fillStyle = "#000000";
	ctx.beginPath();
	ctx.moveTo(top.x, top.y);
	ctx.lineTo(right.x, right.y);
	ctx.lineTo(left.x, left.y);
	ctx.fill();
	
	function doHole(top, left, right, times){
		if(times == 0) return;
		
		ctx.beginPath();
		ctx.fillStyle = "#FFFFFF";
		
		//upp och ner
		var m = left.x + (right.x - left.x) / 2;	
		var bottomPoint = {x: m, y: left.y};
		
		//left o right
		var q = (top.x-left.x)/2;
		var w = (top.y-left.y)/2;
	
		var leftPoint = {x: top.x-q, y: top.y-w};
		var rightPoint = {x: top.x+q, y: top.y-w};
	
		//skär ut
		if(times == 1){
			ctx.moveTo(bottomPoint.x, bottomPoint.y);
			ctx.lineTo(leftPoint.x, leftPoint.y);
			ctx.lineTo(rightPoint.x, rightPoint.y);
			ctx.fill();
		}
		
		//recurs
		doHole(leftPoint, left, bottomPoint, times-1);
		doHole(rightPoint, bottomPoint, right, times-1);
		doHole(top, leftPoint, rightPoint, times-1);
	
	}
	
	var depth = 1;
	var interval;
	
	$("button").click(function(){
		doHole(top, left, right, depth);
		depth++;
	});
	
});