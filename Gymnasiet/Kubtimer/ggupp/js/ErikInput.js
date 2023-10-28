$(document).ready(function(){
	
	$(".erikCheckbox").click(function(){
		$(this).toggleClass("checked");
	});
	
	$(".erikRadio").click(function(){
		erikCheck(this);
	});
	
});


function erikCheck(toCheck){
	var ele = $(toCheck);
	var name = ele.attr("name");
	$(".erikRadio[name='"+name+"']").each(function(){
		$(this).removeClass("checked");
	});
	ele.addClass("checked");
	
	ele.trigger("change");
}

function erikCheckCheckbox(element, checked){
	var ele = $(element);
	if(checked && !ele.hasClass("checked")){
		ele.addClass("checked");
	}else if(!checked && ele.hasClass("checked")){
		ele.removeClass("checked");
	}
}