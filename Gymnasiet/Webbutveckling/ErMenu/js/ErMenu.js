$(document).ready(function(){
    (function ( $ ) {
//(window.location.href = '...'; window.location.replace('...');)
//window.open(url, "_blank");
        var opts = {
            speed: 150, //in millis
            offset: 10, //in pixels
            offsetLeft: true,
            offsetRight: false,
            varyingSpeeds: true, //vill take speed*elements to open
            //autoLength: true, //shortens the width if you have offset
            //direction: "vertical", //horizontal or vertical
            debug: true, //shows a tooltip with the classes the item has
            directClose: false, //closes every div when created. Some themes may want the divs open when applied to them.
        };
        var holderName = "";
        var idSiffra = 0;

        $.fn.ermenu = function(o){
            if(o != null){
              opts = $.extend(opts, o);
            }
            
            idSiffra = 0;
            holderName = this.attr("id");
            
            init();

        }; 
        
        function init(){
            $("#"+holderName).find("ul").each(function(i){
                //omringar rätt del med divs
                var headerText = $(this).attr("ErTitle");
                var $this = $(this);
                var parent = $(this).parent();
                $(this).remove();
                var $header = $("<div><p>"+headerText+"</p></div>")
                var $body = $("<div></div>");
                
                //lägger dit rätt klasser och attribut
                $body.addClass("ErBody");
                $header.addClass("ErHeader");
                $body.addClass("Er"+i);
                $header.addClass("Er"+i);
                $body.attr("erid", i);
                $header.attr("erid", i);
                $header.addClass("ErOpen");
                
                //fixar li
                //var $thisCpy = $this.clone();
                $this.children("li").each(function(i){
                    if($(this).children("ul").length <= 0){
                        var text = $(this).html();
                        //$thisCpy.append("<li><div><p>"+text+"</p></div></li>");
                        $(this).html("<div><p>"+text+"</p></div>");
                    }else{
                        //$thisCpy.append($(this));
                    }
                });
                //$this = $thisCpy.clone();
                
                //lägger till allt
                $body.append($this);
                parent.append($header);
                parent.append($body);
                
                //kollar depth
                var depth = $this.parents('ul').length;
                $header.addClass('depth-' + depth);
                $body.addClass('depth-' + depth);
                //if(opts.direction == "vertical"){
                    //$header.css("margin-left", (0*opts.offset)+"px"); //det har stårr depth istället för 0
                    if(opts.offsetLeft){
                        $body.css("margin-left", ((0+1)*opts.offset)+"px");
                    }
                    //if(opts.autoLength){
                        /*var hOld = $header.width();
                        $header.width(hOld-(0*opts.offset)+"px");*/
                        var bOld = $body.width();
                        if(opts.offsetLeft && opts.offsetRight){
                            $body.width(bOld-((0+1)*opts.offset*2)+"px");
                        }else if(opts.offsetLeft == false && opts.offsetRight == false){
                        
                        }else{
                            $body.width(bOld-((0+1)*opts.offset)+"px");
                        }
                    //}
                /*}else if(opts.direction == "horizontal"){
                    $body.css("margin-top", ((0+1)*opts.offset)+"px");
                    if(opts.autoLength){
                        var bOld = $body.height();
                        $body.height(bOld-((0+1)*opts.offset)+"px");
                    }
                }else{
                    console.log(opts.direction +" is not supported as a direction.");
                }*/
                
                //fixar fler klasser
                $this.children("li").children("div").addClass("ErDiv").addClass("depth-"+depth);
                $header.addClass("ErIndi-"+idSiffra++);
                $this.children("li").children("div").each(function(a){
                    $(this).addClass("ErIndi-"+idSiffra++);
                });
                
                //fixar lite css
                $this.css("list-style-type", "none");
                
            });
            
            if(opts.directClose){
                $(this).erCloseAll();
            }
            
            //fixar rätt listener
            //if(opts.direction == "vertical"){
                verticalListeners();
            /*}else if(opts.direction == "horizontal"){
                horizontalListeners();
            }else{
                console.log(opts.direction +" is not supported as a direction.");
            }*/
            
            if(opts.debug){
                $("#"+holderName+" .ErHeader").each(function(i){
                    var classList = $(this).attr("class").split(/\s+/);
                    $(this).attr("title", classList);
                });
                $("#"+holderName+" .ErDiv").each(function(i){
                    var classList = $(this).attr("class").split(/\s+/);
                    $(this).attr("title", classList);
                });
            }
            
        }
        
        $.fn.erCloseAll = function(){
            var id = this.attr("id"); //tror inte jag kan använda holderName
            $("#"+id+" .ErBody").toggle(false);
            $("#"+id+" .ErHeader").toggleClass("ErOpen", false);
        }
        
        function verticalListeners(){
            $("#"+holderName+" .ErHeader").click(function(){
                var id = $(this).attr("erid");
                var holderid = findId(this);
                console.log(holderid);
                if(opts.varyingSpeed == false){
                    $("#"+holderid+" .Er"+id+".ErBody").slideToggle(opts.speed);
                }else{
                    var body = $("#"+holderid+" .Er"+id+".ErBody");
                    var newSpeed = (body.children("ul").children("li").length) + body.children("ul").children("div").length;
                    newSpeed = newSpeed * opts.speed;
                    body.slideToggle(newSpeed);
                }
                $(this).toggleClass("ErOpen");
            });

        }
        
        function findId(a){
            var cur = $(a);
            $(a).parents(".ErBody").each(function(){
                //console.log(this);
                cur = $(this);
            });
            return cur.parent().attr("id");
        }
        
        function horizontalListeners(){
        
        }
        
        $.fn.erHeaderHover = function(a,b,level){
            var id = this.attr("id"); //tror inte jag kan använda holderName
            if(level == -1){
                $("#"+id+" .ErHeader").hover(a, b);
            }else{
                $("#"+id+" .ErHeader.depth-"+level).hover(a, b);
            }
            
        };
        
         $.fn.erDivHover = function(a,b,level){
            var id = this.attr("id"); //tror inte jag kan använda holderName
            if(level == -1){
                $("#"+id+" .ErDiv").hover(a, b);
            }else{
                $("#"+id+" .ErDiv.depth-"+level).hover(a, b);
            }
            
        };
        
        $.fn.erIndiHover = function(a, b){
            //this.unbind("mouseenter mouseleave");
            this.off("mouseenter");
            this.off("mouseleave");
            this.hover(a, b);
        };

        /*$.fn.skriv = function(){
            //alert(opts.asd);
            this.html(this.attr("id"));
        };*/
        
        $.fn.erOpen = function(a){//opens this id and all of its parents. a=Er-klasserna
            var id = this.attr("id"); //tror inte jag kan använda holderName
            openTemp(id, a);
            
        }
        
        function openTemp(id, a){
            $("#"+id+" .ErBody.Er"+a).toggle(true);
            $("#"+id+" .ErHeader.Er"+a).toggleClass("ErOpen", true);
            var parent = $("#"+id+" .ErHeader.Er"+a).parent("li").parent("ul").parent(".ErBody");
            if(parent.length > 0){
                var ab = parent.attr("erid");
                openTemp(id, ab);
            }
        }
        
        $.fn.addImg = function(path, classAddTo, imgClass){
            var id = this.attr("id"); //tror inte jag kan använda holderName
            $("#"+id+" ."+classAddTo).each(function(i){
                var newImg = $("<img src="+path+" class="+imgClass+"></img>");
                $(this).append(newImg);
            });
            
        }
        
        $.fn.randColor = function(classes, colors){
            var id = this.attr("id"); //tror inte jag kan använda holderName
            for(var a = 0; a < classes.length; a++){
                $("#"+id+" "+classes[a]).each(function(i){
                    var numb = Math.floor(Math.random()*colors.length);
                    $(this).css("background", colors[numb]);
                });
            }
                
            
            
        }
        
        var pos = 0;
        $.fn.colorSequence = function(classes, colors){
            var id = this.attr("id"); //tror inte jag kan använda holderName
            for(var a = 0; a < classes.length; a++){
                $("#"+id+" "+classes[a]).each(function(i){
                    $(this).css("background", colors[pos]);
                    pos++;
                    if(pos >= colors.length){
                        pos = 0;
                    }
                });
            }
            
        }
        
        $.fn.randColorA = function(classes, colors){
            var id = this.attr("id"); //tror inte jag kan använda holderName
            for(var a = 0; a < classes.length; a++){
                $("#"+id+" "+classes[a]).each(function(i){
                    var numb = Math.floor(Math.random()*colors.length);
                    $(this).css(colors[numb]);
                });
            }
                
            
            
        }
        
        var pos1 = 0;
        $.fn.colorSequenceA = function(classes, colors){
            var id = this.attr("id"); //tror inte jag kan använda holderName
            for(var a = 0; a < classes.length; a++){
                $("#"+id+" "+classes[a]).each(function(i){
                    $(this).css(colors[pos1]);
                    pos1++;
                    if(pos1 >= colors.length){
                        pos1 = 0;
                    }
                });
            }
            
        }
        
    })(window.jQuery);
});