$(document).ready(function(){

	for(var i = 0; i < 2000; i++){
		var $newOption = $("<option>"+(i+1)+"</option>");
		$("select[name=grej]").append($newOption);
	}
	
});
