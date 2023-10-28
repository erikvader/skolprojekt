$(document).ready(function(){
	
	//exakt samma sak som scrolltop förutom att denna utgår ifrån pixlar från botten
	$.fn.scrollBottom = function(scroll){
		if(typeof scroll === 'number'){
			this.scrollTop(this[0].scrollHeight - this.height() - scroll);
			return this[0].scrollHeight - this.height() - scroll;
		}else{
			return this[0].scrollHeight - this.height() - this.scrollTop();
		}
	}
	
	//************tider*********
	var maxPerRow = 2; //antal kolumner som tiderna ska ligga i
	var tiderLength = 0; //hur många tider det finns
	var timeFontSize = "6px"; //font-size som alla tid-divar ska ha (väldigt onödig egentligen att ha den global)
	
	//penalty: 0=inget, 1=+2, 2=DNF 

	//fixar alla time-divar till att vara rätt bredd för att kunna vara på ett antal kolumner
	function setMaxPerRow(mpr){
		maxPerRow = mpr;
		$(".historyItem").css("width", ((100/mpr)-0)+"%");
	}
	
	//lägger till en ny time-div med rätt grejer (tid, penalty och blandning)
	function addTime(time, penalty, bland){
		var d = $("<div class=\"time historyItem\" penalty=\""+penalty+"\" time=\""+time+"\" bland=\""+bland+"\"></div>");
		d.click(timeClick);
		updateDivText(d);
		
		//fixa fontSize
		//d.css("font-size", timeFontSize);
		d.attr("style", $("#historySizing").attr("style"));
		
		$("#history").append(d);
		
		tiderLength++;
		
		//åker längst ner
		$("#history").scrollBottom(0);

	}
	
	//uppdaterar texten på en time-div beroende på vilka egenskaper den har (blandning, tid och penalty)
	function updateDivText(div){
		var pena = parseInt(div.attr("penalty"));
		var t = parseInt(div.attr("time"));
		
		var grej = "";
		if(pena == 1){
			grej = "(+2)";
		}else if(pena == 2){
			grej = "(DNF)";
		}
		
		div.html(timeToString(t)+grej);
	}
	
	var curTimeShowing = -1; //index på tiden som visas i 'timeShow', används vid borttagning av tider
	
	//funktion att köra när man klickar på en tid. Denna tar fram och visar tiden i 'timeShow'-diven. 
	function timeClick(){
		var ele = $(this);
		curTimeShowing = getIndex(ele);
		
		$("#timeShowTime").html(ele.html());
		$("#timeShowBland").html(ele.attr("bland"));
		
		//fixa penalty
		var temp = parseInt(ele.attr("penalty"));
		erikCheck($("#timeShowContent .erikRadio").get(temp));
		
		showTimeShow(true);
	}
	
	//fixar snitt o grejer när man ändrar penalty på en tid i 'timeShow'-diven.
	$("#timeShowContent .erikRadio").change(function(){
		var div = getTime(curTimeShowing);
		div.attr("penalty", $(this).index("#timeShowContent .erikRadio"));
		updateDivText(div);
		$("#timeShowTime").html(div.html());
		
		updateAverages();
	});
	
	//tar bort en tid
	function deleteTime(index){
		var td = getTime(index);
		td.remove();
		
		tiderLength--;
	}
	
	//kolalr vilket index en time-div är på.
	//time är time-diven
	function getIndex(time){
		var ind = time.index(".time");
		return ind;
	}
	
	//returnerar en time-div som ligger på ett specifikt index
	function getTime(i){
		var ele = $(".time").get(i);
		return $(ele);
	}
	
	//*****IngA FLER TIDER**********
	
	//******average************
	
	//uppdaterar alla snitt och visar allt på linjediagrammet
	function updateAverages(){
		//spara för snabb åtkomst
		var times = [];
		$(".time").each(function(i){
			var t = $(this).attr("time");
			var pen = parseInt($(this).attr("penalty"));
			
			if(pen == 0){
				times.push(parseInt(t));
			}else if(pen == 1){
				times.push(parseInt(t)+2000);
			}else if(pen == 2){
				times.push(-1);
			}
		});
		
		//padda chart
		var chartPoints = chart.datasets[0].points.length; //0=tid, 1=3/5, 2=10/12
		while(chartPoints != times.length){
			if(chartPoints > times.length){
				chart.removeData();
			}else{
				chart.addData([0, 0, 0], "");
			}
			chartPoints = chart.datasets[0].points.length;
		}
		
		//resetta allt
		for(var i = 0; i < times.length; i++){
			setChartPoint(1, i, -2);
			setChartPoint(2, i, -2);
		}
		
		var def = -2;
		
		//amount of times
		$("#avgAntal").children().last().html(times.length);
		
		if(times.length == 0){ //finns inga tider
			setAvg("Best", def);
			setAvg("Worst", def);
			setAvg("35", def);
			setAvg("Best35", def);
			setAvg("1012", def);
			setAvg("Best1012", def);
			setAvg("Snitt", def);
			setAvg("Mean", def);
			setAvg("50", def);
			setAvg("100", def);
		}else{ //sök genom alla tider och hitta alla average
			var avg35 = -2; //best
			var avg12 = -2; //best
			var temp;
			for(var i = 0; i < times.length; i++){
				//lägga till i chart
				setChartPoint(0, i, times[i]);
				
				if(i + 5 <= times.length){
					temp = calcAverage(i, i+5, times);
					if(avg35 == -2 || compare(temp, avg35) == -1){
						avg35 = temp;
					}
					setChartPoint(1, i+4, temp);
				}
				
				if(i + 12 <= times.length){
					temp = calcAverage(i, i+12, times);
					if(avg12 == -2 || compare(temp, avg12) == -1){
						avg12 = temp;
					}
					setChartPoint(2, i+11, temp);
				}
			}
			
			//chart
			chart.update();
			
			setAvg("Best35", avg35);
			setAvg("Best1012", avg12);
			
			//senaste
			if(times.length >= 5){
				avg35 = calcAverage(times.length-5, times.length, times);
			}else{
				avg35 = -2;
			}
			setAvg("35", avg35);
			
			if(times.length >= 12){
				avg12 = calcAverage(times.length-12, times.length, times);
			}else{
				avg12 = -2;
			}
			setAvg("1012", avg12);
			
			//totala
			setAvg("Snitt", calcAverage(0, times.length, times));
			setAvg("Mean", calcAverageMean(0, times.length, times));
			
			//best/worst
			setAvg("Best", times[findBest(0, times.length, times)]);
			setAvg("Worst", times[findWorst(0, times.length, times)]);
			
			//50
			var grejor;
			if(times.length >= 50){
				grejor = calcAverageMean(times.length-50, times.length, times);
			}else{
				grejor = -2;
			}
			setAvg("50", grejor);
			
			//100
			if(times.length >= 100){
				grejor = calcAverageMean(times.length-100, times.length, times);
			}else{
				grejor = -2;
			}
			setAvg("100", grejor);
		}
		
		//snabbknappar
		$("#snabbBest").children().last().html($("#avgBest").children().last().html());
		$("#snabb35").children().last().html($("#avg35").children().last().html());
		$("#snabb1012").children().last().html($("#avg1012").children().last().html());
		
	}
	
	//sätter värdet på en punkt i diagrammet
	function setChartPoint(dataset, point, value){
		chart.datasets[dataset].points[point].erik = formatText(value);
		if(value < 0) value = 0;
		chart.datasets[dataset].points[point].value = parseInt(value);
	}
	
	//en funktion som gör livet lättare genom att sätta en viss text på rätt avg-div.
	function setAvg(id, text){
		text = formatText(text);
		$("#avg"+id).children().last().html(text);
	}
	
	//formaterar en tid i millisekunder till en string-representation. 
	function formatText(text){
		if(text == -1){
			text = "DNF";
		}else if(text == -2){
			text = "--:--,---";
		}else{
			text = timeToString(text);
		}
		return text;
	}
	
	//jämför två olika tider i storlek
	//-1 = left är mindre, 0 = de är lika, 1 = right är mindre
	function compare(left, right){
		if(left == right){
			return 0;
		}
		if(left == -1){
			return 1;
		}
		if(right == -1){
			return -1;
		}
		if(left < right){
			return -1;
		}else{
			return 1;
		}
	}
	
	//hittar den bästa tiden inom ett intervall. times är en array med tider i millisekunder.
	//right inte inclusive
	function findBest(left, right, times){
		var best = left;
		for(var i = left+1; i < right; i++){
			if(compare(times[best], times[i]) == 1){
				best = i;
			}
		}
		return best;
	}
	
	//samma sak som funktionen ovan förutoma tt denna letar efter den sämsta tiden. 
	function findWorst(left, right, times){
		var worst = left;
		for(var i = left+1; i < right; i++){
			if(compare(times[worst], times[i]) == -1){
				worst = i;
			}
		}
		return worst;
	}
	
	//räknar ut ett snitt på rätt sätt. Denna funktion tar bort den sämsta och bästa tiden.
	function calcAverage(left, right, times){
		if(right-left < 3){
			return -2;
		}
		
		var best = times[findBest(left, right, times)];
		var worst = times[findWorst(left, right, times)];
		var total = 0;
		var DNF = 0;
		for(var i = left; i < right; i++){
			var t = times[i];
			if(t != -1){
				total += t;
			}else{
				DNF++;
			}
		}
		
		if(DNF > 1){
			return -1;
		}
		
		if(best != -1){
			total -= best;
		}
		
		if(worst != -1){
			total -= worst;
		}
		
		total /= (right-left-2);
		return parseInt(total);
	}
	
	//räknar ut snitt utan att ta bort sämsta och bästa tiden. 
	//inte inclusive right
	function calcAverageMean(left, right, times){
		var total = 0;
		var DNF = 0;
		for(var i = left; i < right; i++){
			var t = times[i];
			if(t != -1){
				total += t;
			}else{
				DNF++;
			}
		}
		
		if(DNF == (right-left)){ //allAreDNF
			return -1;
		}else{
			total /= (right-left-DNF);
			return parseInt(total);
		}
	}
	
	//*****inget mer average**********
	
	//*******blandning****
	var blandDrag = [["L", "R"], ["U", "D"], ["F", "B"]];
	var blandPyra = ["R", "L", "U", "B", "r", "l", "u", "b"];
	var blandMega = ["R", "D", "U"];
	var blandSkewb = ["R", "L", "B", "F"];
	var last = -1;
	
	function generateBlandning(){
		if(curCube >= 1 && curCube <= 7){
			generateBlandningNXN(size, curLength);
		}else if(curCube == 8){
			generateBlandningPyra();
		}else if(curCube == 9){
			generateBlandningMega();
		}else if(curCube == 10){
			generateBlandningSkewb();
		}else if(curCube == 11){
			generateBlandningSQ1();
		}else if(curCube == 12){
			generateBlandningClock();
		}else{
			$("#blandning").html("");
		}
		blandningSize(); //fixar storleken
	}
	
	function generateBlandningSQ1(){
		var bland = "";
		//ul, ur, dr, dl
		var sq = [[2, 1, 2, 1], [2, 1, 2, 1], [1, 2, 1, 2], [1, 2, 1, 2]];
		
		for(var i = 0; i < curLength; i++){
			//hitta alla uppe
			var dragU = [0, 6];
			dragU = dragU.concat(sqFindValid(sq[0], sq[1], true));
			dragU = dragU.concat(sqFindValid(flip(sq[0]), flip(sq[1]), false));
			
			//hitta alla nere
			var dragD = [0, 6];
			dragD = dragD.concat(sqFindValid(sq[2], sq[3], true));
			dragD = dragD.concat(sqFindValid(flip(sq[2]), flip(sq[3]), false));
			
			//ta ett random drag
			var u = 0, d = 0;
			while(u == 0 && d == 0){
				u = dragU[parseInt(Math.random()*dragU.length)];
				d = dragD[parseInt(Math.random()*dragD.length)];
			}
			
			//vrida
			sqTurn(u, sq[0], sq[1]);
			sqTurn(d, sq[2], sq[3]);
			
			//vrida 180
			var temp = sq[2];
			sq[2] = sq[1];
			sq[1] = temp;
			
			bland += "("+u+","+d+") / ";
			
		}
		
		$("#blandning").html(bland);
	}
	
	function sqTurn(u, a, b){
		var flip = u < 0 ? false : true;
		u = Math.abs(u);
		
		if(flip){
			a.reverse();
			b.reverse();
		}
		
		sqTurnHelper(u, a, b);
		sqTurnHelper(u, b, a);
		
		if(flip){
			a.reverse();
			b.reverse();
		}
	}
	
	function sqTurnHelper(u, a, b){
		var sum = 0;
		for(var i = 0; i < a.length; i++){
			sum += a[i];
			if(sum == u){
				for(var j = 0; j <= i; j++){
					b.push(a.shift());
				}		
			}
		}
	}
	
	function flip(a){
		var aa = [];
		for(var i = a.length-1; i >= 0; i--){
			aa.push(a[i]);
		}
		return aa;
	}
	
	function sqFindValid(a, b, neg){
		var res = []; 
		
		var sum = 0;
		for(var i = 0; i < a.length-1; i++){ //testa att snurra
			sum += a[i];
			var sum2 = 0;
			for(var j = 0; j < b.length; j++){
				sum2 += b[j];
				if(sum2 > sum){
					break;
				}else if(sum2 == sum){
					res.push(sum * (neg ? -1 : 1));
					break;
				}
			}
		}
		
		return res;
	}
	
	function generateBlandningClock(){
		var bland = "";
		
		for(var i = 0; i < curLength; i++){
			var a = "(";
			a += clockPins()+",";
			a += clockTurns("u")+",";
			a += clockTurns("d")+") ";
			bland += a;
		}
		
		bland += "("+clockPins()+")";
		
		$("#blandning").html(bland);
	}
	
	function clockTurns(uellerd){
		var a = "";
		var u = parseInt(Math.random()*7);
		a += uellerd+""+u;
		if(u != 0){
			if(parseInt(Math.random()*2) == 0){
				a += "'";
			}
		}
		return a;
	}
	
	function clockPins(){
		var slut = "";
		for(var i = 0; i < 4; i++){
			var grunka = parseInt(Math.random()*2);
			if(grunka == 0){
				slut += "d";
			}else{
				slut += "U";
			}
		}
		return slut;
	}
	
	function generateBlandningSkewb(){
		var bland = "";
		var last = -1;
		for(var i = 0; i < curLength; i++){
			do{
				rand = parseInt(Math.random()*4);
			}while(rand == last && last != -1);
			last = rand;
			
			var prime = parseInt(Math.random()*2);
		
			bland += blandSkewb[rand] + (prime == 0 ? "' " : " ");
		}
					
		
		$("#blandning").html(bland);
	}
	
	function generateBlandningMega(){
		var bland = "";
		var last = -1;
		for(var i = 0; i < curLength; i++){
			if((i+1) % 11 == 0){				
				var p = parseInt(Math.random()*2);
				bland += blandMega[2] + (p == 0 ? "' " : " ");
			}else{
				do{ //kommer ju ändå alltid att bli varannan
					rand = parseInt(Math.random()*2);
				}while(rand == last && last != -1);
				last = rand;
				
				var prime = parseInt(Math.random()*2);
			
				bland += blandMega[rand] + (prime == 0 ? "++ " : "-- ");
			}
					
		}
		
		$("#blandning").html(bland);
	}
	
	function generateBlandningPyra(){
		var bland = "";
		//tips
		var rand;
		for(var i = 4; i < 8; i++){
			rand = parseInt(Math.random()*3);
			if(rand == 1){
				bland += blandPyra[i]+" ";
			}else if(rand == 2){
				bland += blandPyra[i]+"' ";
			}
		}
		
		//resten
		var last = -1;
		for(var i = 0; i < curLength; i++){
			do{
				rand = parseInt(Math.random()*4);
			}while(rand == last && last != -1);
			last = rand;
			
			var prime = parseInt(Math.random()*2);
			
			bland += blandPyra[rand] + (prime == 0 ? "' " : " ");
		}
		
		$("#blandning").html(bland);
	}
	
	//size 0 = 3x3, 1 = 4x4 etc
	function generateBlandningNXN(size, length){
		if(size < 0) size = 0;
		var bland = "";
		last = -1;
		for(var i = 0; i < length; i++){
			//hitta 0, 1, 2
			var rand;
			do{
				rand = parseInt(Math.random()*3);
			}while(rand == last && last != -1);
			last = rand;
			
			//hitta en av dem
			var rand1 = parseInt(Math.random()*2);
			//prime, dubbel eller int
			var rand2 = parseInt(Math.random()*3);
			
			//lägg till
			var toAdd = blandDrag[rand][rand1];
			
			//hitta siffra för större kuber
			var rand3 = parseInt(Math.random()*(size+1));//1 och 2 == 1
			if(rand3 != 0){
				toAdd = toAdd.toLowerCase();
				rand3 = parseInt((rand3-1)/2);
				if(rand3 > 0){
					toAdd = (rand3+1) + toAdd;
				}
			}
			
			//prime eller int
			if(rand2 == 0){
				toAdd += "'"
			}else if(rand2 == 1){
				toAdd += "2";
			}
	
			bland += toAdd+" ";
		}
		
		$("#blandning").html(bland);
	}
	
	function getBlandning(){
		return $("#blandning").html();
	}
	
	//blandning selector
	var curCube = 0;
	var curLength = 20;
	var size = 0;
	
	var puzzles = [
		"Välj pussel...",
		"Eget val (NxN)",
		"2x2",
		"3x3",
		"4x4",
		"5x5",
		"6x6",
		"7x7",
		"Pyraminx",
		"Megaminx",
		"Skewb",
		"Square-1",
		"Clock"
	];
	
	var scrambleLengths = [
		15,
		25,
		40,
		60,
		80,
		100,
		25, //pyra
		70, //mega
		25, //skewb
		16, //sq-1
		10, //clock
	];
	
	function addPuzzles(){
		var grej = $("#leftUp select");
		var val;
		for(var i = 0; i < puzzles.length; i++){
			val = puzzles[i];
			grej.append("<option value=\""+val+"\">"+val+"</option>");
		}
	}
	
	$("#leftUp select").change(function(){
		
		var c = this.selectedIndex;
		if(c == 0){
			return; //inget nytt pussel valdes
		}
		
		curCube = c;
		if(customSettings.length <= 0 && curCube > 1){
			curLength = scrambleLengths[curCube-2];
		}else{
			curLength = customSettings.length;
		}
		
		if(c >= 2 && c <= 7){
			size = curCube-3;
		}else if(c == 1){ //custom
			size = customSettings.size;
		}
		
		generateBlandning();
		
		$("#curPuzzle").html(puzzles[curCube]);
		leftUpFontSize();
		
		$("#leftUp select").val(puzzles[0]).change();
	});
	
	//*******ingen blandning efter denna linje****
	
	//********timeShow**************
	
	//bevara scrollpos
	var historyScroll;
	
	function showTimeShow(v){
		//history och timeShow får inte ha borders
		$("#timeShow").stop();
		$("#history").stop();
		
		historyScroll = $("#history").scrollBottom();
		
		var delay = 500;
		if(customSettings.animations == false){
			delay = 0;
		}
		
		if(v){
			//hitta procent
			var ts = $("#timeShowContent").outerHeight(true);
			var hi = $("#history").parent().height();
			var per = 100*(ts/hi);
			if(per > 100) per = 100;
			
			$("#timeShow").animate({
				height: per+"%",
			}, delay);
			
			$("#history").animate({
				height: (100-per)+"%",
			}, {
				duration: delay,
				queue: false,
				progress: function(){
					$("#history").css("overflow-y", "scroll");
					$("#history").scrollBottom(historyScroll);
				},
			});
		}else{
			$("#timeShow").animate({
				height: "0%",
			}, delay);
			
			//calc(100% - 4px)
			//var border = $("#history").outerHeight() - $("#history").innerHeight();
			//var hundra = $("#history").parent().height()-border;
			$("#history").animate({
				//height: hundra,
				height: "100%",
			}, {
				duration: delay,
				queue: false,
				progress: function(){
					$("#history").css("overflow-y", "scroll");
					$("#history").scrollBottom(historyScroll);
				},
				complete: function(){
					//$("#history").css("height", "calc(100% - "+border+"px)");
				},
			});
		}
	}
	
	$("#timeShowClose").click(function(){
		showTimeShow(false);
	});
	
	$("#timeShowBland").click(function(){
		prompt("CTRL+C för att kopiera din blandning.\n"+$(this).html(), $(this).html());
	});
	
	$("#timeShowDelete").click(function(){
		deleteTime(curTimeShowing);
		//update avg
		updateAverages();
		showTimeShow(false);
	});
	
	$("#clearBtn").click(function(){
		$(".time").remove();
		showTimeShow(false);
		updateAverages();
	});
	
	//**********ingen timeShow*******
	
	//************options window*********//
	
	var customSettings = {
		length: 0,
		size: 0, 
		inspection: false,
		skin: 0,
		forcedLayout: 0,
		forcedColumn: 0,
		animations: true,
		directStart: false,
	};
	
	function showBlack(v){
		var g;
		if(v)
			g = "block";
		else
			g = "none";
		$("#curtain").css("display", g);
		$("#blackW").css("display", g);
	}
	
	function showOptions(v){
		var g;
		if(v)
			g = "block";
		else
			g = "none";
		showBlack(v);
		$("#options").css("display", g);
	}
	
	$("#blackExit").click(function(){
		$("#blackW").children().each(function(i){
			if(i != 0){
				$(this).css("display", "none");
			}
		});
		showBlack(false);
	});
	
	$("#optionsBtn").click(function(){
		showOptions(true);
		optionsFontSize(); //för att knappen i options inte kan ändra storlek då den är display: none;
	});
	
	$("#optSpara").click(function(){
		htmlToObject();
		showOptions(false);
		changeSkin();
		checkSize();
		
		$("#leftUp select").val(puzzles[curCube]).change(); //skaffar en ny blanding
	});
	
	function showHelp(v){
		var g;
		if(v)
			g = "block";
		else
			g = "none";
		showBlack(v);
		$("#help").css("display", g);
	}
	
	$("#helpBtn").click(function(){
		showHelp(true);
		optionsFontSize(); //för att knappen i options inte kan ändra storlek då den är display: none;
	});

	function showFunc(v){
		var g;
		if(v)
			g = "block";
		else
			g = "none";
		showBlack(v);
		$("#func").css("display", g);
	}
	
	$("#funcBtn").click(function(){
		showFunc(true);
		optionsFontSize(); //för att knappen i options inte kan ändra storlek då den är display: none;
	});
	
	function htmlToObject(){
		//NxN
		var temp = $("input[name='nxn']").val();
		customSettings.size = parseInt(temp)-3;
		
		//length
		temp = $("input[name='length']").val();
		customSettings.length = parseInt(temp);
		
		//inspection
		customSettings.inspection = $("#insInspection").hasClass("checked");
		
		//skin
		customSettings.skin = $("#skinSelect")[0].selectedIndex;
		
		//forced column
		customSettings.forcedColumn = $("#layout select")[0].selectedIndex;
		
		//forced layout
		customSettings.forcedLayout = $("#layout .erikRadio.checked").index("#layout .erikRadio");
		
		//animerad mobile
		customSettings.animations = $("#layMobile").hasClass("checked");
		
		//direkt start
		customSettings.directStart = $("#insStart").hasClass("checked");
	}
	
	function objectToHtml(){
		//NxN
		$("input[name='nxn']").val(customSettings.size+3);
		
		//length
		$("input[name='length']").val(customSettings.length);
		
		//inspection
		erikCheckCheckbox($("#insInspection"), customSettings.inspection);
		
		//skin
		$("#skinSelect")[0].selectedIndex = customSettings.skin;
		changeSkin();
		
		//forced column
		$("#layout select")[0].selectedIndex = customSettings.forcedColumn;
		
		//forced layout
		erikCheck($("#layout .erikRadio")[customSettings.forcedLayout]);
		
		//animerad mobile
		erikCheckCheckbox($("#layMobile"), customSettings.animations);
		
		//direkt start
		erikCheckCheckbox($("#insStart"), customSettings.directStart);
	}
	
	//för skin select
	var skins = [
		"Cuboss",
		"Dark",
		"Pink",
	];
	
	function addSkins(){
		var select = $("#skinSelect");
		for(var i = 0; i < skins.length; i++){
			select.append("<option value=\""+skins[i]+"\">"+skins[i]+"</option>");
		}
	}
	
	function changeSkin(){
		$("#skinsLink").attr("href", "css/skins/"+skins[customSettings.skin].toLowerCase()+".css");
	}
	
	//**********ingen options window*******/
	
	//********timer********
	
	$("div").click(function(){
		//console.log($(this).attr("id"));
	});
	
	$("*").focus(function(){
		//$(this).blur();
	});
	
	$("#middle").bind("touchstart", function(e){
		timerDown();
	});
	
	$("#middle").bind("touchend", function(e){
		timerUp();
	});

	$(window).keyup(function(e){
		if(e.keyCode == 32){
			timerUp();
			buttonIsPressed = false;
		}
	});
	
	$(window).keydown(function(e){
		if(e.keyCode == 32){
			if(!buttonIsPressed){
				document.activeElement.blur();
				$("#middle").focus();
				timerDown();
				buttonIsPressed = true;
			}
		}
	});
	
	//själva timern
	var timerStarted = false;
	var intervalID;
	var curTime = 0;
	var lastTime;
	
	//hålla in
	var timerDownDelay;
	var canStart = false;
	
	//keyboard pressed
	var buttonIsPressed = false;
	
	//inspection
	var inspectionInterval;
	var isInspecting = false;
	var penalty = 0;
	var canInspect = false;
	
	function timerDown(){
		if(customSettings.inspection && !isInspecting){
			$("#middle").removeClass("red green blue").addClass("blue");
			canInspect = true;
			return;
		}
		
		if(!timerStarted){
			if(customSettings.directStart){
				canStart = true
				$("#middle").removeClass("red green blue").addClass("green");
			}else{
				$("#middle").removeClass("red green blue").addClass("red");
				timerDownDelay = setTimeout(function(){
					canStart = true
					$("#middle").removeClass("red green blue").addClass("green");
				}, 450);
			}
		}else{
			stopTimer();
			isInspecting = false;
			penalty = 0;
		}
	}
	
	function timerUp(){		
		if(customSettings.inspection && !isInspecting && canInspect){
			isInspecting = true;
			canInspect = false;
			curTime = 0;
			lastTime = $.now();
			inspectionInterval = setInterval(inspectionTick, 51);
			return;
		}
		
		$("#middle").removeClass("red green blue");
		clearTimeout(timerDownDelay);
		if(canStart){
			canStart = false;
			clearInterval(inspectionInterval);
			startTimer();
		}else{
			if(isInspecting){
				$("#middle").removeClass("red green blue").addClass("blue");
			}
		}
	}
	
	function startTimer(){
		if(!timerStarted){
			timerStarted = true;
			curTime = 0;
			lastTime = $.now();
			intervalID = setInterval(timerTick, 51);
		}
	}
	
	function stopTimer(){
		if(timerStarted){
			//lägga till extra tid
			timerTick();
		}
		
		clearInterval(intervalID);
		timerStarted = false;
		
		//lägg till tiden i historiken
		addTime(curTime, penalty, getBlandning());
		
		//ny blandning
		generateBlandning();
		
		//update avg
		updateAverages();
	}
	
	function timerTick(){
		var n = $.now();
		curTime += (n-lastTime);
		lastTime = n;
		$("#timer").html(timeToString(curTime));
	}
	
	function inspectionTick(){
		var n = $.now();
		curTime += (n-lastTime);
		lastTime = n;
		
		if(curTime >= 8*1000 && false){
			//varde ljud!
		}
		
		if(curTime >= 12*1000 && false){
			//varde ljud!
		}
		
		if(curTime > 17*1000){ //DNF
			clearTimeout(timerDownDelay);
			penalty = 2;
			clearInterval(inspectionInterval);
			curTime = 0;
			stopTimer();
			isInspecting = false;
			penalty = 0;
			canStart = false;
			$("#middle").removeClass("blue green red");
			$("#timer").html(timeToString(0));
			return;
		}else if(curTime > 15*1000){ //+2
			penalty = 1;
		}
		
		$("#timer").html(timeToString(15000-curTime));
	}
	
	function timeToString(inTime){
		var neg = false;
		if(inTime < 0){
			neg = true;
			inTime *= -1;
		}
		var finalTime = "";
		var time = "";
		var temp = inTime;
		//fixa minuter
		time = parseInt(temp/60000).toString();
		temp %= 60000;
		
		while(time.length < 2){
			time = "0"+time;
		}
		
		finalTime += time+":";
		
		//fixa sekunder
		time = parseInt(temp/1000).toString();
		temp %= 1000;
		
		while(time.length < 2){
			time = "0"+time;
		}
		
		finalTime += time+",";
		
		//millisekunder
		time = temp.toString();
		
		while(time.length < 3){
			time = "0"+time;
		}
		
		finalTime += time;
		
		if(neg){
			finalTime = "-"+finalTime;
		}
		
		return finalTime;
	}
	
	//******inte timer********
	
	//******snabbKnappar******
	
	function getLastTime(){
		var a = $(".time");
		if(a.length == 0){
			return null;
		}else{
			return a.last();
		}
	}
	
	function snabbClick(p, evt){
		evt.stopPropagation();
		var a = getLastTime();
		if(a != null){
			a.attr("penalty", p);
			updateDivText(a);
			updateAverages();
		}
	}
	
	function bindSnabbMobile(){
		$("#snabbNo, #snabb2, #snabbDNF").off("click");
		
		$("#snabbNo, #snabb2, #snabbDNF").each(function(i){
			$(this).on("touchstart", function(evt){
				snabbClick(i, evt);
			});
		});
	}
	
	function bindSnabbDesktop(){
		$("#snabbNo, #snabb2, #snabbDNF").off("touchstart");
		
		$("#snabbNo, #snabb2, #snabbDNF").each(function(i){
			$(this).on("click", function(evt){
				snabbClick(i, evt);
			});
		});
	}

	//**inga fler snabbknappar*****
	
	//*****fixa mobila/storlek*******
	
	$("#menyKnapp").click(function(){
		$("#right").stop();
		$("#left").stop();
		$("#footer").stop();
		$("#topMeny").stop();
		
		var delay = 1000;
		if(customSettings.animations == false){
			delay = 0;
		}
		
		if($("#menyKnapp").hasClass("menyClosed")){ //är stängd, öppna den
			setMenyKnappClassOpened();
			
			$("#right").animate({
				marginRight: 0,
			}, delay);
			
			$("#left").animate({
				marginLeft: 0,
			}, delay);
			
			var footerHeight = $("#footer").height();
			$("#footer").animate({
				marginTop: -footerHeight,
			}, delay);
			
			var f = getFontSize($("#topMeny"));
			var h = $("#topMeny").parent().height();
			
			$("#topMeny").css("padding-top", (0.5*h - 0.5*f)+"px");
			$("#topMeny").css("top", "-100%");
			$("#topMeny").animate({
				top: 0,
			}, delay);
			
		}else{ //är öppen, stäng den
			setMenyKnappClassClosed();
			
			$("#right").animate({
				marginRight: "-50%",
			}, delay);
			
			$("#left").animate({
				marginLeft: "-50%",
			}, delay);
			
			$("#footer").animate({
				marginTop: 0,
			}, delay);
			
			$("#topMeny").animate({
				top: "-100%",
			}, {
				duration: delay,
				complete: function(){
					$("#topMeny").css({
						top: "",
						paddingTop: "",
					});
				},
			});
		}
	});
	
	function setMenyKnappClassOpened(){
		$("#menyKnapp").removeClass("menyClosed");
		$("#menyKnapp").addClass("menyOpened");
		
		$("body").removeClass("noSelect");
	}
	
	function setMenyKnappClassClosed(){
		$("#menyKnapp").removeClass("menyOpened");
		$("#menyKnapp").addClass("menyClosed");
		
		$("body").addClass("noSelect");
	}
	
	//resizar inte på vartenda event, bara på vissa.
	//var resizeTimeout;
	$(window).resize(function(){
		//clearTimeout(resizeTimeout);
		//resizeTimeout = setTimeout(function(){
			checkSize();
		//}, 80);
	});
	
	var isMobile = true;
	function checkSize(){ //kollar ifall vi ändrar från mobil till desktop och tvärtom
		var w = $(window).width();
		if(((w > 768 && customSettings.forcedLayout != 1) || customSettings.forcedLayout == 2) && isMobile){ //desktop
			isMobile = false;
			setMenyKnappClassClosed();
			$("body").removeClass("noSelect");
			
			$("#left").removeAttr("style");
			$("#right").removeAttr("style");
			$("#footer").removeAttr("style");
			$("#topMeny").css({
				top: "",
				paddingTop: "",
			});
			
			bindSnabbDesktop();
		}else if(((w <= 768 && customSettings.forcedLayout != 2) || customSettings.forcedLayout == 1) && !isMobile){
			isMobile = true;
			bindSnabbMobile();
			$("body").addClass("noSelect");
		}
		
		//right click, times
		if(isMobile){
			disableRightClick();
			setMaxPerRow(1);
		}else{
			enableRightClick();
			if(w <= 1000){
				setMaxPerRow(1);
			}else{
				setMaxPerRow(2);
			}
		}
		
		//forced column
		if(customSettings.forcedColumn != 0){
			setMaxPerRow(customSettings.forcedColumn);
		}
		
		//ifall mobile css ska med eller inte
		if(isMobile){
			//document.getElementById("mobileLink").sheet.disabled = false;
			$("body").addClass("mobile");
		}else{
			//document.getElementById("mobileLink").sheet.disabled = true;
			$("body").removeClass("mobile");
		}
		
		//kollar timer font-size
		timerFontSize();
		
		//size på blandning
		blandningSize();
		
		//size på historiken
		historikFontSize();
		
		//size på average
		averageFontSize();
		
		//size på timeshow
		timeShowFontSize();
		
		//size på leftUp
		leftUpFontSize();
		
		//size för all text på options
		optionsFontSize();
		
		//size på rightUp
		rightUpFontSize();
		
		//storlek på top 
		topperFontSize();
		
		//storlek på de svarta sakerna
		svartaSakerFontSize();
		
		//snabbknappar
		snabbknapparFontSize();
	}
	
	//returnar font size i rena siffror
	function getFontSize(element){
		var f = element.css("font-size");
		f = f.substring(0, f.length-2);
		return f;
	}
	
	function blandningSize(){
		var timerSize = getFontSize($("#timer"));
		var middleSize = $("#middle").height();
		
		//sätt till preliminär size
		var bland = $("#blandning");
		bland.css("font-size", (timerSize*0.4)+"px");
		
		//ska flyttas upp?
		var blandningHeight = bland.height();
		var curProcent = 100*(blandningHeight/middleSize);
		var taBort = curProcent*(-10/27) + (380/27);
		//console.log(blandningHeight + " " + curProcent + " "+ taBort),
		bland.css("top", taBort+"%");
		
		//ändra font-size så att den passar i rutan
		if(taBort <= 1){
			$("#blandning").erikFontisize({
				css: {whiteSpace: "normal"},
				fitWidth: false,
				fixedWidth: true,
			});
		}
	}
	
	function timerFontSize(){
		$("#timer").erikFontisize({
			text: "-00:00,000",
		});
	}
	
	function historikFontSize(){
		//fixa storlek
		var first = $("#historySizing");
		$(first).erikFontisize({
			text: "00:00,000(DNF)",
			fixedHeight: false,
			fitHeight: false,
			fixedWidth: false,
			fitWidth: true,
		});
		
		//sätt på resten
		timeFontSize = first.css("font-size");
		$(".time").css("font-size", timeFontSize);
		
	}
	
	function averageFontSize(){
		var temp = $("#avgBest1012").children().first();
		$(temp).erikFontisize({
			fixedHeight: false,
			fitHeight: false,
			fixedWidth: false,
			fitWidth: true,
		});
		$("#average").css("font-size", $(temp).css("font-size"));
	}
	
	function timeShowFontSize(){
		$("#timeShowTime").erikFontisize({
			text: "00:00,000(DNF)",
			fixedHeight: false,
			fitHeight: false,
			fixedWidth: false,
			fitWidth: true,
		});
		$("#timeShow").css("font-size", $("#timeShowTime").css("font-size"));
	}

	function leftUpFontSize(){ 
		$("#leftUp select").each(function(){
			var longest = "";
			$(this).children("option").each(function(){
				var temp = $(this).html();
				if(temp.length > longest.length){
					longest = temp;
				}
			});
			
			$(this).erikFontisize({
				text: longest,
				fixedHeight: false,
				fitHeight: false,
				fixedWidth: false,
				fitWidth: true,
			});
		});
		
		var cp = $("#curPuzzle");
		cp.css("height", cp.parent().height()*0.9);
		cp.erikFontisize({
			fitWidth: true,
			fitHeight: true,
			fixedWidth: false,
			fixedHeight: false,
		});
		cp.css("height", "");
		
		//vertical align
		var sel = $("#leftUp select");
		sel.css("margin-top", (sel.parent().height() - sel.height())/2);
		cp.css("margin-top", (cp.parent().height() - cp.height())/2);
	}
	
	function optionsFontSize(){
		var b = $("#blackW").width()*0.02;
		$("#blackW").css("font-size", b+"px");
	}
	
	function rightUpFontSize(){
		var rut = $("#rightUpText");
		var ru = $("#rightUp");
		var btn = $("#clearBtn");
		
		rut.erikFontisize({
			fixedHeight: false,
			fitHeight: false,
			fixedWidth: false,
			fitWidth: true,
		});
		ru.css("font-size", rut.css("font-size"));
		
		//centrera rut på mitten
		var h = (ru.height()-rut.height())/2;
		rut.css("margin-top", h+"px");
		
		//centrera clearBTn på bredden
		//har 34% av bredd att röra sig på.
		var b = btn.innerWidth();
		var attMovaSigOn = ru.width()*0.34;
		var padding = (attMovaSigOn-b)/2;
		btn.css("margin-right", padding+"px");
		
		//verticalt btn
		h = (ru.height()-btn.innerHeight())/2;
		btn.css("margin-top", h+"px");
	}
	
	function topperFontSize(){
		var logga = $("#logga");
		var svg = $("#logga svg");
		var mk = $("#menyKnapp");
		
		if(isMobile){
			//menyKnapp samme bredd som höjd
			mk.css("width", mk.height()+"px");
			
			var ny = logga.parent().width() - mk.height();
			logga.css({
				width: ny+"px",
				marginLeft: mk.height()+"px",
			});
			
			svg.css("width", "");
			if(svg.width() >= logga.width()*0.9){
				svg.css("width", (logga.width()*0.9)+"px");
			}
		}else{
			logga.css({
				width: "",
				marginLeft: "",
			});
			svg.css("width", "");
			if(svg.width() >= logga.width()*0.9){
				svg.css("width", (logga.width()*0.9)+"px");
			}
		}
		
		//vertical
		var h = (logga.height() - svg.height())/2;
		svg.css("margin-top", h+"px");
		
		//topMeny
		var topMeny = $("#topMeny");
		if(isMobile){
			topMeny.css("margin-left", mk.height()+"px");
			topMeny.css("width", (topMeny.parent().width() - mk.height())+"px");
		}else{
			topMeny.css({
				marginLeft: "",
				width: "",
			});
		}
		var f = topMeny.width()*0.05;
		
		topMeny.css("font-size", f+"px");
	}
	
	function svartaSakerFontSize(){
		/*var tt = $("#topTopper div");
		tt.css("height", "90%");
		tt.erikFontisize({
			fixedHeight: false,
			fitHeight: true,
			fixedWidth: false,
			fitWidth: true,
		});
		tt.css("height", "");
		tt.css("margin-top", ((tt.parent().height() - tt.height())/2) + "px");
		*/
		var tt = $("#topTopper a");
		tt.css("height", "90%");
		tt.erikFontisize({
			fixedHeight: false,
			fitHeight: true,
			fixedWidth: false,
			fitWidth: true,
		});
		tt.css("height", "");
		tt.css("margin-top", ((tt.parent().height() - tt.height())/2) + "px");
		
		var ttt = $("#topTopper div");
		ttt.css("font-size", tt.css("font-size"));
		ttt.css("margin-top", ((ttt.parent().height() - ttt.height())/2) + "px");
		
		//footer
		tt = $("#footFooter");
		var text = "";
		tt.children().each(function(){
			text += $(this).html()+" ";
		});
		
		tt.erikFontisize({
			fixedHeight: false,
			fitHeight: true,
			fixedWidth: false,
			fitWidth: true,
			text: text,
			heightFactor: 0.95,
			widthFactor: 0.7,
		});
		
		tt.children().each(function(){
			var tt = $(this);
			tt.css("margin-top", ((tt.parent().height() - tt.height())/2) + "px");
		});
	}
	
	function snabbknapparFontSize(){
		//knapparna
		var snabb = $("#snabbPenalty");
		var tHeight = (snabb.height() - 2*snabb.width()*0.01/*padding*/);
		
		$("#snabbNo").css("height", tHeight+"px");
		
		$("#snabbNo").erikFontisize({
			fixedHeight: false,
			fitHeight: true,
			fixedWidth: false,
			fitWidth: true,
		});
		$("#snabbPenalty").css("font-size", $("#snabbNo").css("font-size"));
		
		$("#snabbNo").css("height", "");
		
		//averages
		var temp = $("#snabb1012").children().last();
		$(temp).erikFontisize({
			fixedHeight: false,
			fitHeight: true,
			fixedWidth: false,
			fitWidth: true,
		});
		$("#snabbAvg").css("font-size", $(temp).css("font-size"));
	}
	
	var defaultOnContext = window.oncontextmenu;
	
	function enableRightClick(){
		window.oncontextmenu = defaultOnContext;
	}

	function disableRightClick(){
		window.oncontextmenu = function(event) {
			event.preventDefault();
			event.stopPropagation();
			return false;
		};
	}
	
	//**********inte mobila/storlek*************
	
	//**********cookies*********
	var cookieVersion = "2";
	
	function setCookie(cname, cvalue, exdays) {
		var d = new Date();
		d.setTime(d.getTime() + (exdays*24*60*60*1000));
		var expires = "expires="+d.toUTCString();
		document.cookie = cname + "=" + cvalue + "; " + expires;
	}

	function getCookie(cname) {
		var name = cname + "=";
		var ca = document.cookie.split(';');
		for(var i=0; i<ca.length; i++) {
			var c = ca[i];
			while (c.charAt(0)==' ') c = c.substring(1);
			if (c.indexOf(name) == 0) return c.substring(name.length, c.length);
		}
		return "";
	}
	
	function saveStuff(){
		var times = [];
		var t = {};
		$(".time").each(function(){
			t = {};
			var o = $(this);
			t.scramble = o.attr("bland");
			t.time = o.attr("time");
			t.penalty = o.attr("penalty");
			times.push(t);
		});
		
		var total = [times, customSettings, cookieVersion];
		
		var seri = JSON.stringify(total);
		setCookie("cubossTimer", seri, 1000);
		
	}
	
	function readStuff(){
		var seri = getCookie("cubossTimer");
	
		if(seri != ""){
			var tillbaka = JSON.parse(seri);
			
			if(tillbaka[2] === cookieVersion){ //cookien är kompatibel		
				//options
				customSettings = tillbaka[1];
				
				//tillbaks med tider
				var times = tillbaka[0];
				for(var i = 0; i < times.length; i++){
					addTime(times[i].time, parseInt(times[i].penalty), times[i].scramble);
				}
			}
		}
		
		//visar inställningarna på sidan typ
		objectToHtml();
	}
	
	$(window).unload(function(){
		saveStuff();
	});
	//**********inte cookies************
	
	//**********chart**********
	var chart;
	
	function initChart(){
		var ctx = $("#timeCanvas")[0].getContext("2d");
		chart = new Chart(ctx).Line(
		{
			labels: [],
			datasets: [
				{
					label: "Tid",
					fillColor: "rgba(255, 0, 0, 0.3)",
					strokeColor: "rgba(255,0, 0, 1)",
					pointColor: "rgba(255,0,0,1)",
					pointStrokeColor: "#FFF",
					pointHighlightFill: "rgba(255,0,0,1)",
					pointHighlightStroke: "rgba(255,0,0,1)",
					data: []
				},
				{
					label: "Snitt 3 av 5",
					fillColor: "rgba(0,255,0,0)",
					strokeColor: "rgba(0,255,0,1)",
					pointColor: "rgba(0,255,0,1)",
					pointStrokeColor: "#fff",
					pointHighlightFill: "#fff",
					pointHighlightStroke: "rgba(151,187,205,1)",
					data: []
				},
				{
					label: "Snitt 10 av 12",
					fillColor: "rgba(0,0,255,0)",
					strokeColor: "rgba(0,0,255,1)",
					pointColor: "rgba(0,0,255,1)",
					pointStrokeColor: "#fff",
					pointHighlightFill: "#fff",
					pointHighlightStroke: "rgba(151,187,205,1)",
					data: []
				}
			]
		},
		
		{
			bezierCurve: false,
			scaleShowLabels: false,
			responsive: true,
			maintainAspectRatio: false,
			multiTooltipTemplate: function(v){
				return v.datasetLabel + ": "+v.erik;
			},
		});
	}
	//**********inte chart**********
	
	function instaBlur(){
		$(this).blur();
	}
	
	//gör så att man inte kan sätta focus på svg
	function setTabIndex(){
		$("svg").each(function(){
			$(this).focus(instaBlur);
			$(this).find("*").each(function(){
				$(this).focus(instaBlur);
			});
		});
	}
	
	//körs första gången
	jQuery.fx.interval = 1000/40; //40 fps
	setTabIndex();
	addPuzzles();
	addSkins();
	$("#leftUp select").val("3x3").change();
	bindSnabbMobile();
	readStuff(); //get info from cookies
	checkSize();
	initChart();
	updateAverages();
	
});;