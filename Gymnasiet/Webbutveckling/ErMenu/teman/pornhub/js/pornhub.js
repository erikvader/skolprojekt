$(document).ready(function(){
    (function ( $ ) {
            
        $("#hejsan").erHeaderHover(
            function(){
                $(this).css("border", "3px solid rgba(235,162,56,1)");
                $(this).width($(this).width()-6);
                $(this).height($(this).height()-6);
                $(this).children("p").css("padding-top", 0); //3-0
            },  
            function(){
                $(this).css("border", "");
                $(this).width($(this).width()+6);
                $(this).height($(this).height()+6);
                $(this).children("p").css("padding-top", 3); //0+3
            },-1);

        var bg = null;
        $("#hejsan").erDivHover(
            function(){
                bg = $(this).css("background");
                $(this).css({
                    background: "rgb(234,144,18)",
                    background: "-moz-linear-gradient(top,  rgba(234,144,18,1) 0%, rgba(219,86,15,1) 100%)",
                    background: "-webkit-gradient(linear, left top, left bottom, color-stop(0%,rgba(234,144,18,1)), color-stop(100%,rgba(219,86,15,1)))",
                    background: "-webkit-linear-gradient(top,  rgba(234,144,18,1) 0%,rgba(219,86,15,1) 100%)",
                    background: "-o-linear-gradient(top,  rgba(234,144,18,1) 0%,rgba(219,86,15,1) 100%)",
                    background: "-ms-linear-gradient(top,  rgba(234,144,18,1) 0%,rgba(219,86,15,1) 100%)",
                    background: "linear-gradient(to bottom,  rgba(234,144,18,1) 0%,rgba(219,86,15,1) 100%)",
                    filter: "progid:DXImageTransform.Microsoft.gradient( startColorstr='#ea9012', endColorstr='#db560f',GradientType=0 )"

                });
            }, 
            function(){
                $(this).css("background", bg);
            },-1);
    
    })(window.jQuery);
});