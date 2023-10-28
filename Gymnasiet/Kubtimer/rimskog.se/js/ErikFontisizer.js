(function( $ ) {
	
	//det blir inte exakt för att metoden för att mäta textstorleken inte ger ett exakt värde.
	//När den anpassar storleken av en div till en text så blir det endast på hela pixlar (inte subpixlar).
	//Det finns en metod som man kan mäta textstorleken på med hjälp av canvas.
	//Men där tror jag inte att man kan enkelt få texten att automatiskt byta rad ifall den är för lång,
	//samt så ger den bredden på subpixel-nivå i vissa webläsare men inte i andra webläsare som Chrome.

	//tar detta element och anpassar font size för att passa. 
	$.fn.erikFontisize = function(options){
		checkSizingSpan(); //kolla ifall sizingSpan finns. denna div testar vilka dimensioner den får vid en viss font-size.
		curElement = this; //sparar till andra funktioner
		
		curOpts = $.extend(true, {}, defaultOptions, options); //spara alla options
		
		var newCss = $.extend({}, sizingSpanCss, curOpts.css); //spara css
		sizingSpan.css(newCss); //sätter css:en
		sizingSpan.css("display", "block"); //tar fram den så att vi kan använda den. 
		sizingSpan.css("visibility", "visible");
		if(curOpts.fixedWidth){ //ifall vi vill ha en fixerad bredd på sizingSpan.
			sizingSpan.css("width", curElement.width()+"px");
		}
		if(curOpts.fixedHeight){
			sizingSpan.css("height", curElement.height()+"px");
		}
		
		if(curOpts.text == null){ //sätter till en annan text än det som står i elementet vi vill anpassa font-sizen på.
			sizingSpan.html(curElement.html());
		}else{
			sizingSpan.html(curOpts.text);
		}
		
		if(curOpts.fontFamily == null){
			sizingSpan.css("font-family", curElement.css("font-family"));
		}else{
			sizingSpan.css("font-family", curOpts.fontFamily);
		}
		
		var newFont = binsearch(curOpts.minFont, curOpts.maxFont); //söker efter den nya fonten
		
		//dubbelkollar
		if(curOpts.doubleCheck){
			sizingSpan.css("font-style", newFont+"px");
			if((sizingSpan.height() >= curElement.width() && curOpts.fitHeight) || (sizingSpan.width() >= curElement.width() && curOpts.fitWidth)){
				newFont -= 1; //borde räcka med 1, då binSearch borde ha fått det till en font-storlek som är i närheten.
			}
		}
		
		resetSizing(); //resettar sizingSpan så att den kan användas på nytt senare. 
		curElement.css("font-size", newFont+"px");
	};
	
	var curElement;
	var sizingSpan;
	var curOpts;
	
	var sizingSpanCss = { //default css
		position: "absolute",
		whiteSpace: "nowrap",
		visibility: "hidden",
		display: "none",
	};
	
	var defaultOptions = {
		text: null, //denna text kommer att testas. om den är null så används texten i elementet. 
		fitWidth: true, //vi vill se till att bredden passar
		fitHeight: true, //vi anpassar höjden
		fixedWidth: false, //bredden är fixerad
		fixedHeight: false, //höjden är fixerad
		css: {}, //custom css för sizingSpan.
		minFont: 0, //lower bound
		maxFont: 500, //max storlek
		binaryOmega: 0.01, //hur nära vänster- och högersidan ska vara varandra i font-size för att det ska räknas som om att vi har hittat svaret.
		fontFamily: null,
		doubleCheck: true, //dubbelkollar den nya font-storleken då metoden inte är exakt.
		widthFactor: 1,
		heightFactor: 1,
	};
	//fitHeight och fixedHeight kan inte båda vara true (samma för width). De säger emot sig lite, om vi vill anpassa höjden kan den inte vara fixerad, då kan vi ju inte anpassa den.
	
	//kollar ifall sizingSpan finns och eventuellt skapar den
	function checkSizingSpan(){
		if(sizingSpan == null){ //den finns inte
			$("body").append("<div id=\"erikSizingSpan\"></div>");
			sizingSpan = $("#erikSizingSpan");
			resetSizing();
		}
	}
	
	//återställer sizingSpan för senare användning
	function resetSizing(){
		sizingSpan.removeAttr("style");
		sizingSpan.css(sizingSpanCss);
		sizingSpan.html("");
	}
	
	//utför binärsökningen
	function binsearch(min, max){
		var l = min;
		var r = max;
		var m;
		var fVal;
		while(r-l >= curOpts.binaryOmega){
			m = (r+l)/2;
			fVal = binF(m);
			if(fVal == 0){
				l = m;
			}else{
				r = m;
			}
		}
		return m;
	}
	
	//vår funktion för binärsökningen. ifall font-sizen gjorde att någon sida blev för stor returnas 1, annars ifall den blev för liten returnas 0
	function binF(x){
		sizingSpan.css("font-size", x+"px");
		var ch = curElement.height() * curOpts.heightFactor;
		var sh = sizingSpan.height();
		var cw = curElement.width() * curOpts.widthFactor;
		var sw = sizingSpan.width();
		
		//console.log("height " + ch+" : "+sh+" width "+cw + " : "+sw);
		
		if((sh >= ch && curOpts.fitHeight) || (sw >= cw && curOpts.fitWidth)){
			return 1; //bli mindre
		}else{
			return 0; //bli större
		}
	}
	
})( jQuery );