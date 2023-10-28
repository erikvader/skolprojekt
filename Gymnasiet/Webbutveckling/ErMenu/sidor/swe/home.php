<?php
    echo "<div id=\"arrow\">
            <img src=\"img/Left_Arrow_Red.png\" alt=\"arrow\" />
        </div>
        <div id=\"arrowText\">
            <p>Den här menyn är gjord med ErMenu!</p>
        </div>
        
        <div id=\"homeContent\">
            <h1>Kom igång</h1><br />
            <h2>importera</h2>
            <p>Det här är basstrukturen. \"hejsan\" är id:et av menyn och \"gold\" är namnet på temat.</p>
            <pre class=\"prettyprint\">
&lt;head&gt;
    &lt;link href=\"css/gold.css\" rel=\"stylesheet\" type=\"text/css\"&gt;
    
    &lt;script src=\"js/jquery.js\"&gt;&lt;/script&gt;
    &lt;script src=\"js/ErMenu.js\"&gt;&lt;/script&gt;
    &lt;script&gt;
        $(document).ready(function(){
            $(\"#hejsan\").ermenu();
        });
    &lt;/script&gt;
    
    //Den här delen behövs endast om ditt tema har en .js-fil.
    &lt;script src=\"js/gold.js\"&gt;&lt;/script&gt;
    &lt;script&gt;
        $(document).ready(function(){
            $(\"#hejsan\").erCloseAll();
        });
    &lt;/script&gt;
    //end on \"is needed\".
    
&lt;/head&gt;</pre>

            <p>Först behöver vi inkludera temats css-fil. Sedan behöver vi importera JQuery och pluginets .js-fil (se till att du importera JQuery före pluginet). Sedan använder vi funktionen <span class=\"kodera\">.ermenu()</span> på meny-diven.</p><br />
            <div class=\"obs\"><p>Om ditt tema inte har en .js-fil så måste du sätta <span class=\"helpOption fakeLink\">option</span> \"directClose\" till \"true\"</p></div>
             <br /><br />
            <div class=\"obs\"><p>För att använda ett tema måste du ändra id:en inuti temafilerna och du kanske måste ändra några sökvägar också.</p></div>
            <br /><br /><br />
            
            <h2>Bygg menyn</h2>
            <p></p>
            <pre class=\"prettyprint\">
&lt;body&gt;
    &lt;div id=\"hejsan\"&gt;
        &lt;ul ErTitle=\"A title\"&gt;
            &lt;li&gt;a menu Item&lt;/li&gt;
            &lt;li&gt;another menu Item&lt;/li&gt;
        &lt;/ul&gt;
        &lt;ul ErTitle=\"1\"&gt;
            &lt;li&gt;item&lt;/li&gt;
            &lt;li&gt;
                &lt;ul ErTitle=\"2\"&gt;
                    &lt;li&gt;more menus!&lt;/li&gt;
                    &lt;li&gt;oh my god!&lt;/li&gt;
                &lt;/ul&gt;
            &lt;/li&gt;
        &lt;/ul&gt;
    
&lt;/body&gt;</pre>

            <p>Den här koden kommer att resultera i en meny som ser ut såhär:</p>
            <img id=\"testPic\" src=\"img/testMenu.png\" alt=\"testPic\" /><br /><br />
            <p>Varje &lt;ul&gt; kommer att bli en header. Attributet \"ErTitle\" kommer att avgöra vad texten inuti header:n kommer att bli. En &lt;li&gt; är en knapp som kommer att läggas till under menyn.</p><br />
            
            <h2>Få knapparna att funka</h2>
            <p>Det enklaste sättet att göra en knapp klickbar (funkar endast på &lt;li&gt;) är att lägga en &lt;a&gt;-tagg.</p>
            <pre class=\"prettyprint\">
&lt;li&gt;&lt;a href=\"toSomeWhere\"&gt;link&lt;/a&gt;&lt;/li&gt;</pre>
            
            <p>Om du vill att en header ska vara klickbar så måste du använda javascript.</p>
            <pre class=\"prettyprint\">
&lt;script&gt;
    $(\"#hejsan .ErIndi-7\").click(function(){
        window.location.href = \"changelog.html\";
    });
&lt;/script&gt;</pre>

            <p><span class=\"kodera\">.ErIndi-x</span> är en klass som varje element (header and li) i menyn har. 
             För att ta reda på vilket index ett element har föreställ dig hela menyn expanderad (som i bilden ovan) 
             och börja från toppen. Första knappen har index 0, den andra har 1 och så vidare. Ett enklare sätt är att aktivera
              <span class=\"helpOption fakeLink\">option</span> \"debug\" till \"true\". Det kommer att aktivera ett tooltip som visar alla klassar en knapp har.</p>
             
        </div>
        ";
?>