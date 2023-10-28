<?php
    echo "<div id=\"arrow\">
            <img src=\"img/Left_Arrow_Red.png\"></img>
        </div>
        <div id=\"arrowText\">
            <p>This menu is made with ErMenu!</p>
        </div>
        
        <div id=\"homeContent\">
            <h1>How to use</h1><br />
            <h2>import</h2>
            <p>This is the base structure. \"hejsan\" is the id of the menu and \"gold\" is the name of the theme.</p>
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
    
    //This part is only needed if the theme has a .js-file.
    &lt;script src=\"js/gold.js\"&gt;&lt;/script&gt;
    &lt;script&gt;
        $(document).ready(function(){
            $(\"#hejsan\").erCloseAll();
        });
    &lt;/script&gt;
    //end on \"is needed\".
    
&lt;/head&gt;</pre>

            <p>First we need to include a theme css-file. Then we need to import JQuery and the plugin .js (make sure that
            you import JQuery before the plugin). Then we apply the <span class=\"kodera\">.ermenu()</span> function to the menu-div.</p><br />
            
            <div class=\"obs\"><p>If your theme doesn't have a .js-file you have to set the <span class=\"helpOption fakeLink\">option</span> \"directClose\" to \"true\"</p></div>
             <br /><br />
            <div class=\"obs\"><p>To apply a theme you have to change the id inside the theme files. And you maybe have to change som paths to images and stuff inside some themes.</p></div>
            <br /><br /><br />
            
            <h2>Build the menu</h2>
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

            <p>This code will result in a menu looking like this: </p>
            <img id=\"testPic\" src=\"img/testMenu.png\"></img><br /><br />
            <p>Each &lt;ul&gt; vill become a header. The attribut \"ErTitle\" will decide the text inside
            the header. A &lt;li&gt; is a button to be added under the menu.</p><br />
            
            <h2>Make the buttons work</h2>
            <p>The easiest way to make a button clickable (works only on &lt;li&gt;) is to add a &lt;a&gt;.</p>
            <pre class=\"prettyprint\">
&lt;li&gt;&lt;a href=\"toSomeWhere\"&gt;link&lt;/a&gt;&lt;/li&gt;</pre>
            
            <p>If you want a header to be clickable you have to use some javascript.</p>
            <pre class=\"prettyprint\">
&lt;script&gt;
    $(\"#hejsan .ErIndi-7\").click(function(){
        window.location.href = \"changelog.html\";
    });
&lt;/script&gt;</pre>

            <p><span class=\"kodera\">.ErIndi-x</span> is a class that every element (header and li) in the menu has. 
             To figure out what index an element has imagine the whole manu expanded (as in the picture above) 
             and start from the top. The first item is index 0 the second one is 1 and so on. A easier way is to enable
             the <span class=\"helpOption fakeLink\">option</span> \"debug\" to \"true\". That will enable hover messages to show every class that element has.</p>
             
        </div>
        ";
?>