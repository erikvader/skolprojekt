 $(document).ready(function(){
    (function ( $ ) {
        $.fn.importantify = function(){
            $(".obs").each(function(i){
                $(this).css({
                    background: "#F2FF00",
                    //border: "5px ridge red",
                    boxShadow: "6px 6px 5px 0px rgba(50, 50, 50, 0.75)",
                    marginBottom: "5px",
                    padding: "25px",
                    display: "inline-block",
                    position: "relative",
                    paddingLeft: "60px",
                    fontWeight: "bold",
                });
                $img = $("<img src=\"img/important.png\"></img>");
                $img.css({
                    position: "absolute",
                    left: 5,
                    top: (($(this).height()/2) - 25 + 25),
                    width: "50px",
                    height: "50px",
                });
                $(this).append($img);
            });
            
        };
    })(window.jQuery);
});