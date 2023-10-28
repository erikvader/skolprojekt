$(document).ready(function(){
	
	$("#logutKnapp").click(function(){
		window.open("logout.php", "_self");
	});
	
	$("#loginKnapp").click(function(){
		$("#loginForm").stop();
		
		$("#loginForm").slideToggle(500, function(){
			$("#logForm").show();
			$("#retForm").hide();
			$("#regForm").hide();
		});

		//window.open("https://www.youtube.com/watch?v=nHc288IPFzk", '_blank');
		
	});
	
	$("#regKlick").click(function(){
		$("#logForm").css("display", "none");
		$("#regForm").fadeToggle(500);
	});
	
	$("#retKlick").click(function(){
		$("#logForm").css("display", "none");
		$("#retForm").fadeToggle(130);
	});
	
	//för att kolla om det är fel! i reg
	function isNumeric(n) {
		return !isNaN(parseFloat(n)) && isFinite(n);
	}
	
	function setValid(a){
		$(a).removeClass("fel");
	}
	
	function setInvalid(a){
		$(a).addClass("fel");
	}
	
	var isValid = [false, false, false]; //firstname, tel, pass
	
	function checkValid(){
		for(var i = 0; i < isValid.length; i++){
			if(isValid[i] == false){
				return false;
			}
		}
		return true;
	}
	
	$("#regForm input[name=firstName]").focusout(function(){
		var name = $(this).val();
		
		for(var i = 0; i < name.length; i++){
			var c = name.charAt(i);
			if(isNumeric(c)){
				isValid[0] = false;
				setInvalid(this);
				break;
			}else{
				isValid[0] = true;
				setValid(this);
			}
		}
	});
	
	$("#regForm input[name=tel]").focusout(function(){
		var name = $(this).val();
		
		name = name.replace(/-/g, "");
		name = name.replace(/ /g, "");
		
		//checka siffror
		
		if(name.length != 10){
			isValid[1] = false;
			setInvalid(this);
		}else{
			isValid[1] = true;
			setValid(this);
		}
	});
	
	function passwordCheck(){
		var denFirst = $("#regForm input[name=password0]");
		var denAndra = $("#regForm input[name=password1]");
		var pass1 = denFirst.val();
		var pass0 = denAndra.val();
		
		if(pass1 != pass0){
			setInvalid(denFirst);
			setInvalid(denAndra);
			isValid[2] = false;
		}else{
			setValid(denFirst);
			setValid(denAndra);
			isValid[2] = true;
		}
	
	}
	
	$("#regForm input[name=password1]").focusout(passwordCheck);
	$("#regForm input[name=password0]").focusout(passwordCheck);
	
	$("#regForm input[name=submit]").click(function(){
		var v = checkValid();
		//console.log(v);
		if(!v){
			event.preventDefault();
		}
	});
	//slut på kolla om det är fel! i reg
	
	
});