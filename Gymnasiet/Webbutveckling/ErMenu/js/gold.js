$(document).ready(function(){
    (function ( $ ) {
            var orgColor = null;
            $("#menyn").erHeaderHover(
                function(){
                    orgColor = $(this).css("background");
                    $(this).css({
                        //generated from colorzilla.com
                        background: "#f79c56",
                        background: "-moz-linear-gradient(top,  #f79c56 0%, #e86210 100%)",
                        background: "-webkit-gradient(linear, left top, left bottom, color-stop(0%,#f79c56), color-stop(100%,#e86210))",
                        background: "-webkit-linear-gradient(top,  #f79c56 0%,#e86210 100%)",
                        background: "-o-linear-gradient(top,  #f79c56 0%,#e86210 100%)",
                        background: "-ms-linear-gradient(top,  #f79c56 0%,#e86210 100%)",
                        background: "linear-gradient(to bottom,  #f79c56 0%,#e86210 100%)",
                        filter: "progid:DXImageTransform.Microsoft.gradient( startColorstr='#f79c56', endColorstr='#e86210',GradientType=0 )",

                    });
                },  
                function(){
                    $(this).css("background", orgColor);
                },-1);
            
            var orgColor1 = null;
            $("#menyn").erDivHover(
                function(){
                    orgColor1 = $(this).css("background");
                    $(this).css({
                        background: "#d0d8d0",
                        background: "-moz-linear-gradient(top,  #d0d8d0 0%, #d1dbce 40%, #7c877f 100%)",
                        background: "-webkit-gradient(linear, left top, left bottom, color-stop(0%,#d0d8d0), color-stop(40%,#d1dbce), color-stop(100%,#7c877f))",
                        background: "-webkit-linear-gradient(top,  #d0d8d0 0%,#d1dbce 40%,#7c877f 100%)",
                        background: "-o-linear-gradient(top,  #d0d8d0 0%,#d1dbce 40%,#7c877f 100%)",
                        background: "-ms-linear-gradient(top,  #d0d8d0 0%,#d1dbce 40%,#7c877f 100%)",
                        background: "linear-gradient(to bottom,  #d0d8d0 0%,#d1dbce 40%,#7c877f 100%)",
                        filter: "progid:DXImageTransform.Microsoft.gradient( startColorstr='#d0d8d0', endColorstr='#7c877f',GradientType=0 )",

                    });
                }, 
                function(){
                    $(this).css("background", orgColor1);
                },-1);
    
    })(window.jQuery);
});