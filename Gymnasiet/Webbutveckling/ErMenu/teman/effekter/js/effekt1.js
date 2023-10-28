$(document).ready(function(){
    (function ( $ ) {
        //you can either include this file with a <script> or copy/paste the function into your file.
        $("#hejsan").erDivHover(function(){
                $(this).animate({
                    marginLeft: "-=10px",
                    width: "+=10px",
                }, 200);
            }
            ,function(){
                $(this).animate({
                    marginLeft: "+=10px",
                    width: "-=10px",
                }, 200);
            }, -1);
    })(window.jQuery);
});