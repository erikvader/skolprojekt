function garInteAttLoggaIn(){
	confirm("Synd då!");
}

function loginBtnn(){
	var user = document.getElementsByName("usernm")[0].value;
	var pass = document.getElementsByName("passwd")[0].value;
	var acc = confirm("Är \""+user+"\" och \""+pass+"\" rätt uppgifter?");
	
	if(acc){
		confirm("Error 37! Ring Rikhard!");
	}else{
		confirm("Skriv rätt då!");
	}
}

function init(){
	//år
	var year = document.getElementById("yearSelect");

	for(var i = 2014; i >= 1900; i--){
		var option = document.createElement("option");
		option.text = i;
		year.add(option);
	}
	
	//månader
	var month = document.getElementById("monthSelect");
	var manader = [
			"Januari", "Februari", "Mars", "April", "Maj", "Juni", "Juli", "Augusti", "September"
			,"oktober", "November", "December"
							];
							
	for(var i = 0; i < manader.length; i++){
		var option = document.createElement("option");
		option.text = manader[i];
		month.add(option);
	}
	
	//dagar
	var dagar = document.getElementById("daySelect");
		
	for(var i = 1; i <= 31; i++){
		var option = document.createElement("option");
		option.text = i;
		dagar.add(option);
	}
}






