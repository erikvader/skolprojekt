$(document).ready(function(){
	//variabler till allting
	var antal = 0;
    var x = 0;
	var player = "#p1";
	var inteTvaGanger = "";
    var playerToSkip = [];

    //variabler till 501
    var playersLastScores = [];
    var totalScore = 0;
    var lastWasADouble = "false";
    var firstWasADouble = "false";
    var doubelStartActive = "false";
    var doubleEndActive = "false"; 
    
    //variabler till tre ben
    var lastScore = 0;

	$("#dartboard #areas g").children().hover(
		function(){
			$(this).css("opacity","0.6");
		},
		function(){
			$(this).css("opacity","1");
		}
    )

	//Räkna poäng
	$("#dartboard #areas g").children().click(function(){
		x++;
		inteTvaGanger = 2;
		
		if(x <= 3){
			var ruta = $(this).attr("id");
			var forsta = ruta.charAt(0);
			var andra = ruta.charAt(1)+ruta.charAt(2);
			var summa = "";
			
			if(forsta == "s"){
				summa = andra * 1;
			}
			else if(forsta == "d"){
				summa = andra * 2;
			}
			else if(forsta == "t"){
				summa = andra * 3;
			}
			else if(forsta == "B"){
				summa = 50;
			}
			else if(forsta == "O"){
				summa = 25;
			}
			else if(forsta == "m"){
				summa = 0;
			}
			
			$("#senastePilen").html(summa);
            
			var tidigare = $(player+" .score").html();
			tidigare = parseInt(tidigare);
			
            if(mode == 1){
                summa = tidigare - summa;
            }else{
                summa = summa + tidigare;
            }
			
			$(player+" .score").html(summa);
            if(mode == 1){//för 501
                if(forsta == "d"){
                    lastWasADouble = "true";
                }else{
                    lastWasADouble = "false";
                }
                if(x == 1){
                    if(forsta == "d"){
                        firstWasADouble = "true";
                    }else{
                        firstWasADouble = "false";
                    }
                }
            }
            checkGameStatus();
			if(x == 3){//asd = nuvarande spelaren
                
                $(player).css("color", "white");
				var f1 = $(player).attr("id").charAt(1);//kan ju bara ta charAt() på player
				var f2 = $(player).attr("id").charAt(2);
				var f3 = $(player).attr("id").charAt(3);
				
				var asd = parseInt(f1+f2+f3);
                var asd2 = asd;
                //hitta nästa spelare som inte är åkt ut eller har vunnit
                asd--;
                while(true){
                    console.log(asd);
                    asd += 2;
                    if(asd > antal){
                        asd = 1;
                    }
                    asd--;
                    if(playerToSkip[asd] == "false"){
                        asd++;
                        break;
                    }else{
                        asd++;
                        if(asd == asd2){
                            alert("Alla spelare har vunnit eller åkt ut!");
                            asd = antal + 1;
                            break;
                        }else{
                            asd--;
                        }
                    }
                }
				
				player = "#p"+asd;
				x = 0;
                $(player).css("color", "red");
                
			}
		}
	});

	//ångra senaste poäng
	$("#fel").click(function(){
		if(inteTvaGanger == 2){
			if(x == 0){
				x = 2;
				
				if( player.attr("id") == "p2" ){ player = $("#p1"); }
				else if( player.attr("id") == "p3" ){ player = $("#p2"); }
				else if( player.attr("id") == "p4" ){ player = $("#p3"); }
				else if( player.attr("id") == "p1" ){ player = $("#p4"); }
			}
			else{
				x--;
			}
			
			senPil = $("#senastePilen").html();
			senPil = parseInt(senPil);
			
			$("#senastePilen").html("-");
			
			summa = player.html();
			summa = summa - senPil;
			
			player.html(summa);
			inteTvaGanger = 1;
		}
	});
    
    //kollar hur spelet ligger till, vem vinner osv.
    function checkGameStatus(){
        if(mode == 1){//501
            var value = $(player+" .score").html();
            
            var g1 = $(player).attr("id").charAt(1);
            var g2 = $(player).attr("id").charAt(2);
            var g3 = $(player).attr("id").charAt(3);
				
            var curPlayerIndex = parseInt(g1+g2+g3);
            curPlayerIndex--;
            
            if(doubelStartActive == "true" && x == 1){
                if(firstWasADouble == "false"){
                    if(playersLastScores[curPlayerIndex] == totalScore){//man är på sin första runda
                        alert("Din första pil var inte en dubbel, då får börja om!");
                        value = playersLastScores[curPlayerIndex];
                        $(player+" .score").html(value);
                        x = 3;
                    }
                }
            }
            
            if(value < 0){
                alert("Du fick för lite poäng! Går upp till ditt förra värde");
                $(player+" .score").html(playersLastScores[curPlayerIndex]);
                x = 3;
            }else if(value == 1){
                if(doubleEndActive == "true"){
                    alert("Du fick 1 poäng kvar, det går inte att gå ut med en dubbel på det! Går upp till ditt förra värde");
                    $(player+" .score").html(playersLastScores[curPlayerIndex]);
                    x = 3;
                }
            }else if(value == 0){
                if(lastWasADouble == "true" || doubleEndActive == false){
                    alert("Grattis! "+$(player+" .playerName").html()+" vann!");
                    playerToSkip[curPlayerIndex] = "true";
                }else{
                    alert("Tyvärr! man måste gå ut på en dubbel. Går upp till ditt förra värde");
                    $(player+" .score").html(playersLastScores[curPlayerIndex]);
                }
                x = 3;
            }else{
                if(x == 3){
                    playersLastScores[curPlayerIndex] = value;
                }
            }
            
            if(baraEnSpelareKvar()){
                alert("Spelet är slut! var vänlig tryck på en gamemode-knapp för att starta ett nytt");
            }
            
        }else if(mode == 2){//tre ben
            if(x == 3){
                var value = $(player+" .score").html();
                value = parseInt(value);

                console.log("Value: "+value);
                console.log("LastScore: "+lastScore);

                if(lastScore == 0){
                    lastScore = value;
                }else if(value > lastScore){
                    lastScore = value;
                }else if(value <= lastScore){
                    lastScore = 0;
                    var temp = $(player+" .legs").html();
                    temp--;
                    $(player+" .legs").html(temp);
                    if(temp == 0){
                        alert($(player+" .playerName").html()+" har åkt ut!");

                        var g1 = $(player).attr("id").charAt(1);
                        var g2 = $(player).attr("id").charAt(2);
                        var g3 = $(player).attr("id").charAt(3);

                        var curPlayerIndex = parseInt(g1+g2+g3);
                        curPlayerIndex--;

                        playerToSkip[curPlayerIndex] = "true";
                    }
                }

                if(baraEnSpelareKvar()){
                    var winningPlayer = 0;
                    for(var i = 0; i < playerToSkip.length; i++){
                        if(playerToSkip[i] == "false"){
                            winningPlayer = i+1;
                        }
                    }
                    alert("Grattis "+$("#p"+winningPlayer+" .playerName").html()+"! du vann!");
                    alert("För att starta ett nytt spel tryck på en av gamemode-knapparna");
                }

                $(player+" .score").html(0);
                $("#lastScore").html(lastScore);
            }
        }else if(mode == 3){
            //$(player+" .score").html(0);
            
        }
    }
    
    //skriv in spelare
    function init(){
        antal = 0;
        $("#spelarna").empty();
        playersLastScores = [];
        lastScore = 0;
        playerToSkip = [];
        x = 0;
        totalScore = 0;
        lastWasADouble = "false";
        firstWasADouble = "false";
        doubelStartActive = "false";
        doubleEndActive = "false";
        
        if(mode == 1){ //501
            var input = "";
            
            //hitta totalScore
            $("input[name=score]").each(function(i){
                var cur = $(this);
                if(cur.prop("checked")){
                    if(cur.val() == "free"){
                        totalScore = parseInt($("input[name=freeText]").val());
                    }else{
                        totalScore = parseInt(cur.val());
                    }
                }
            });
            
            doubelStartActive = $("input[name=ds]").prop("checked").toString();
            doubleEndActive = $("input[name=du]").prop("checked").toString();

            while(true){
                antal++;
                input = prompt("Namn på spelare "+antal+":");
                if(input == null){
                    antal--;
                    break;
                }
                var grej0 = $(
                    "<div class=\"spelare\" id=\"p"+antal+"\">"+
                    "<div class=\"playerName\">"+input+"</div>" +
                    "<div>Poäng: <span class=\"score\">"+totalScore+"</span></div>"+
                    "</div>"
                );
                
                $("#spelarna").append(grej0);
                playerToSkip[antal-1] = "false";
                playersLastScores[antal-1] = totalScore;
	       }
        }else if(mode == 2){//tre ben
            $("#spelarna").append($("<div>Poäng att slå: <span id=\"lastScore\">0</span></div>"));
            var input = "";

            while(true){
                antal++;
                input = prompt("Namn på spelare "+antal+":");
                if(input == null){
                    antal--;
                    break;
                }
                var grej0 = $(
                    "<div class=\"spelare\" id=\"p"+antal+"\">"+
                    "<div class=\"playerName\">"+input+"</div>" +
                    "<div>Poäng: <span class=\"score\">0</span></div>"+
                    "<div>Ben: <span class=\"legs\">3</span></div>"+
                    "</div>"
                );
                
                $("#spelarna").append(grej0);
                playerToSkip[antal-1] = "false";
	       }
        }else if(mode == 3){//free
            antal = 1;
            $("#spelarna").append($(
                "<div class=\"spelare\" id=\"p1\">"+
                "<div>Poäng: <span class=\"score\">0</span></div>"+
                "<button id=\"freeReset\">Reset</button>"+
                "</div>"
            ));//p1
            
        }
        player = "#p1";
        $(player).css("color", "red");
    }
    
    $("#spelarna").on("click", "#freeReset", function(){
        $(player + " .score").html(0);
    });
    
    function baraEnSpelareKvar(){
        var a = 0;
        for(var i = 0; i < playerToSkip.length; i++){
            if(playerToSkip[i] == "false"){
                a++;
            }
        }
        if(a <= 1){
            return true;
        }else{
            return false;
        }
    }
    
    //mode kontroll
    var mode = -1;
    $("#mode501").click(function(){
        mode = 1;
        init();
    });
    $("#modeTre_ben").click(function(){
        mode = 2;
        init();
    }); 
    $("#modeFree").click(function(){
        mode = 3;
        init();
    });
    
    //debug
    $("#debugRefresh").click(function(){
        $("#dplayers").html("<tr><td>Name</td><td>score</td><td>legs</td><td>toSkip</td><td>lastScore</td></tr>");
        
        $("#dantal").val(antal);
        $("#dx").val(x);
        $("#dplayer").val(player);
        $("#dinteTvaGanger").val(inteTvaGanger);
        $("#dtotalScore").val(totalScore);
        $("#dlastWasADouble").val(lastWasADouble);
        $("#dfirstWasADouble").val(firstWasADouble);
        $("#ddoubelStartActive").val(doubelStartActive);
        $("#ddoubleEndActive").val(doubleEndActive);
        $("#dlastScore").val(lastScore);
        
        for(var i = 0; i < antal; i++){
            var curPlayer = "#p"+(i+1);
             
            var $grej = $(
                "<tr>"+
                    "<td><input type=\"text\" id=\"dPlayerNamep"+i+"\" value=\""+$(curPlayer+" .playerName").html()+"\" /></td>"+
                    "<td><input type=\"text\" id=\"dScorep"+i+"\" value=\""+$(curPlayer+" .score").html()+"\" /></td>"+
                    "<td><input type=\"text\" id=\"dLegsp"+i+"\" value=\""+$(curPlayer+" .legs").html()+"\" /></td>"+
                    "<td><input type=\"text\" id=\"dPlayerToSkipp"+i+"\" value=\""+playerToSkip[i]+"\" /></td>"+
                    "<td><input type=\"text\" id=\"dPlayersLastScoresp"+i+"\" value=\""+playersLastScores[i]+"\" /></td>"+  
                "</tr>"
            );
            
            $("#dplayers").append($grej);
        }
    });
    
    $("#debugSet").click(function(){
        antal = $("#dantal").val();
        x = $("#dx").val();
        player = $("#dplayer").val();
        inteTvaGanger = $("#dinteTvaGanger").val();
        totalScore = $("#dtotalScore").val();
        lastWasADouble = $("#dlastWasADouble").val();
        firstWasADouble = $("#dfirstWasADouble").val();
        doubelStartActive = $("#ddoubelStartActive").val();
        doubleEndActive = $("#ddoubleEndActive").val();
        lastScore = $("#dlastScore").val();
        
        for(var i = 0; i < antal; i++){
            var curPlayer = "#p"+(i+1);
            
            $(curPlayer+" .playerName").html($("#dPlayerNamep"+i).val());
            $(curPlayer+" .score").html($("#dScorep"+i).val());
            $(curPlayer+" .legs").html($("#dLegsp"+i).val());
            playerToSkip[i] = $("#dPlayerToSkipp"+i).val();
            playersLastScores[i] = $("#dPlayersLastScoresp"+i).val();
            
            //ändrar till rätt färger
            if(player == curPlayer){
                $(curPlayer).css("color", "red");
            }else{
                $(curPlayer).css("color", "white");
            }
        }
    });
});