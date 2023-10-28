$(document).ready(function(){
    (function ( $ ) {
//comment out the one that you dont want
//you can either include this file with a <script> or copy/paste the function into your file.
        
//text is aligned to the left
$("#hejsan").erDivHover(
    function(){
        var width = $(this).innerWidth();
        var p = $(this).children("p");
        var textWidth = p.innerWidth();
        var padd = p.css("padding-left");
        var marg = p.css("margin-left");
        var paddAndMarg = parseInt(padd.substring(0, padd.length-2)) + parseInt(marg.substring(0, marg.length-2));
        var skillnad = width-textWidth-paddAndMarg;
        //skillnad = 10; //custom amount
        $(this).animate({
            paddingLeft: "+="+skillnad+"px",
            width: "-="+skillnad+"px",
        }, 200);
    }
    ,function(){
        var width = $(this).innerWidth();
        var p = $(this).children("p");
        var textWidth = p.innerWidth();
        var padd = p.css("padding-left");
        var marg = p.css("margin-left");
        var paddAndMarg = parseInt(padd.substring(0, padd.length-2)) + parseInt(marg.substring(0, marg.length-2));
        var skillnad = width-textWidth-paddAndMarg;
        //skillnad = 10; //custom amount
        $(this).animate({
            paddingLeft: "-="+skillnad+"px",
            width: "+="+skillnad+"px",
        }, 200);
}, -1);


//text is aligned to the right
$("#hejsan").erDivHover(
    function(){
        var width = $(this).innerWidth();
        var p = $(this).children("p");
        var textWidth = p.innerWidth();
        var padd = p.css("padding-right");
        var marg = p.css("margin-right");
        var paddAndMarg = parseInt(padd.substring(0, padd.length-2)) + parseInt(marg.substring(0, marg.length-2));
        var skillnad = width-textWidth-paddAndMarg;
        //skillnad = 10; //custom amount
        $(this).animate({
            paddingRight: "+="+skillnad+"px",
            width: "-="+skillnad+"px",
        }, 200);
    }
    ,function(){
        var width = $(this).innerWidth();
        var p = $(this).children("p");
        var textWidth = p.innerWidth();
        var padd = p.css("padding-Right");
        var marg = p.css("margin-Right");
        var paddAndMarg = parseInt(padd.substring(0, padd.length-2)) + parseInt(marg.substring(0, marg.length-2));
        var skillnad = width-textWidth-paddAndMarg;
        //skillnad = 10; //custom amount
        $(this).animate({
            paddingRight: "-="+skillnad+"px",
            width: "+="+skillnad+"px",
        }, 200);
}, -1);
            })(window.jQuery);
});