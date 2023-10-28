$(document).ready(function(){
    (function ( $ ) {
        
        var path = "img/icicle.png";
        var imgWidth = 46;
        $("#hejsan .ErHeader").each(function(a){
            //console.log("new header");
            var width = $(this).width();
            //console.log("width1 "+width);
            //width = width.substring(0, width.length-2);
            //console.log("width "+width);
            var imgThatFits = Math.floor(width/imgWidth);
            //console.log("imgThatFits "+imgThatFits);
            var newImgWidth = width/imgThatFits;
            //console.log("newImgWidth "+newImgWidth);
            for(var i = 0; i < width; i += newImgWidth){
                //console.log("i "+i);
                var newImg = $("<img src="+path+" class=Eicicle></img>");
                $(this).append(newImg);
                newImg.css("left", i);
                newImg.css("width", newImgWidth);
            }
        });
        
        path = "img/frost.png";
        imgWidth = 50*0.3;
        var imgHeight = 44*0.3;
        $("#hejsan .ErHeader").each(function(a){
            grejer(this); 
        });
        /*$("#hejsan .ErDiv").each(function(a){
            grejer(this); 
        });*/
        
        function grejer(a){
            var width = $(a).width();
            var height = $(a).height();
            var imgThatFitsW = Math.floor(width/imgWidth);
            var newImgWidth = width/imgThatFitsW;
            var imgThatFitsH = Math.floor(height/imgHeight);
            var newImgHeight = height/imgThatFitsH;
            
            for(var i = 0; i < width; i += newImgWidth){
                var newImg = $("<img src="+path+" class=Efrost></img>");
                $(a).append(newImg);
                newImg.css("left", i);
                newImg.css("width", newImgWidth);
                newImg.css("opacity", "0");
                newImg.css("height", imgHeight);
                newImg.css("top", 0);
                
                var newImg = $("<img src="+path+" class=Efrost></img>");
                $(a).append(newImg);
                newImg.css("left", i);
                newImg.css("width", newImgWidth);
                newImg.css("opacity", "0");
                newImg.css("height", imgHeight);
                newImg.css("top", 17);
            }
            /*Does not look good on this low height(30)
            for(var i = 0; i < height; i += newImgHeight){
                var newImg = $("<img src="+path+" class=Efrost></img>");
                $(a).append(newImg);
                newImg.css("top", i);
                newImg.css("height", newImgHeight);
                newImg.css("opacity", "0");
                newImg.css("width", imgWidth);
                newImg.css("left", "0");
                
                var newImg = $("<img src="+path+" class=Efrost></img>");
                $(a).append(newImg);
                newImg.css("top", i);
                newImg.css("height", newImgHeight);
                newImg.css("opacity", "0");
                newImg.css("width", imgWidth);
                newImg.css("left", "180px");
            }
            */
        }

        $("#hejsan").erHeaderHover(
            function(){
                $(this).children(".Efrost").css("opacity", "1");
            },  
            function(){
                $(this).children(".Efrost").css("opacity", "0");
            }, -1);

        $("#hejsan").erDivHover(
            function(){

            }, 
            function(){

            },-1);
    
    })(window.jQuery);
});