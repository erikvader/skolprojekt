$(document).ready(function(){	
	
	var curOffset = 0;
	var limit = 3;
	var max = 0;
	
	function getInlaggCount(){
		$.ajax({
		  url: 'getInlaggCount.php',
		  success: function(data) {
			max = data;
		  }
		});
	}
	
	function getMoreInlagg(){
		$.ajax({
		  url: 'loadBlogg.php?limit='+limit+'&offset='+curOffset,
		  success: function(data) {
			$("#inlagg").append(data);
			if(curOffset <= max){
				curOffset += limit;
			}
			if(curOffset >= max){
				$("#getMerInlagg").hide();
			}
		  }
		});
	}
	
	//startinläggen
	getInlaggCount();
	
	getMoreInlagg();
	
	$("#getMerInlagg").click(function(){
		getMoreInlagg();
	});
	
	$("#showNewInlagg").click(function(){
		$("#newInlagg").slideToggle();
	});

});