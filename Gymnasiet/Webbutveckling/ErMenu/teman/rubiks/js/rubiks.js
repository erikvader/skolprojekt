$(document).ready(function(){
    (function ( $ ) {
        
            $("#hejsan").colorSequence([".ErDiv", ".ErHeader"], [/*"#EDEDED",*/ "#fff301", "#015eea", "#fc9303", "#01d051", "#d6241f"]);
        
            $("#hejsan").erHeaderHover(
                function(){
                    $(this).css("font-weight", "900");
                },  
                function(){
                    $(this).css("font-weight", "normal");
                },-1);
            
            $("#hejsan").erDivHover(
                function(){
                    $(this).css("font-weight", "900");
                }, 
                function(){
                    $(this).css("font-weight", "normal");
                },-1);
     
    })(window.jQuery);
});