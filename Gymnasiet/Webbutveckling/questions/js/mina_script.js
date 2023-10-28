$(document).ready(function(){
	
	//spawna allt
	for(var i = 0; i < 10; i++){
		var div = $("<div class=\"group\"></div>")
		var title = $("<p class=\"titel\">grupp "+(i+1)+"</p>")
		var plus = $("<button class=\"plus\">+</button>");
		var minus = $("<button class=\"minus\">-</button>");
		var value = $("<p class=\"value\">0</p>");
		div.append(title);
		div.append(minus);
		div.append(value);
		div.append(plus);
		$("#groups").append(div);
	}
	
	//fixa points
	$(".plus").click(function(){
		change(this, 1);
	});
	
	$(".minus").click(function(){
		change(this, -1);
	});
	
	function change(a, v){
		var parent = $(a).parent();
		//console.log(parent);
		var value = parent.children(".value");
		//console.log(value[0]);
		var old = value.html();
		value.html(parseInt(old)+v);
	}
	
	//fixa questions
	var questions = [
		"hej1",
		"hej2"
	];
	
	var answers = [
		"japp1",
		"japp2"
	];
	
	var showAnswer = true;
	var cur = 0;
	
	$("#question").html(questions[0]);
	
	$("#next").click(function(){
		if(showAnswer){
			showAnswer = false;
			$("#question").html(answers[cur]);
		}else{
			cur++;
			console.log(cur);
			if(cur >= questions.length){
				$("#question").html("slut på frågor sir!");
			}else{
				$("#question").html(questions[cur]);
				showAnswer = true;
			}
		}
	});
	
});
















