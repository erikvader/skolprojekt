<?php
    echo "<table id=\"erDocsTable\">
            <tr>
                <td>Function</td>
                <td>Description</td>
            </tr>
            <tr>
                <td class=\"fakeLink erdocsLink\">ermenu({});</td>
                <td>Initializes and makes the menu with the passed in options.</td>
            </tr>
            <tr>
                <td class=\"fakeLink erdocsLink\">erCloseAll();</td>
                <td>Closes all tabs.</td>
            </tr>
            <tr>
                <td class=\"fakeLink erdocsLink\">erHeaderHover(ehover, lhover, level);</td>
                <td>Adds a hover effect to every header on that level.</td>
            </tr>
            <tr>
                <td class=\"fakeLink erdocsLink\">erDivHover(ehover, lhover, level);</td>
                <td>Adds a hover effect to every button on that level.</td>
            </tr>
            <tr>
                <td class=\"fakeLink erdocsLink\">erIndiHover(ehover, lhover);</td>
                <td>Adds a hover effect to the target button or header.</td>
            </tr>
            <tr>
                <td class=\"fakeLink erdocsLink\">erOpen(id);</td>
                <td>Opens all the way to the target tab.</td>
            </tr>
            <tr>
                <td class=\"fakeLink erdocsLink\">addImg(path, class, imgclass);</td>
                <td>adds a image to the target class. </td>
            </tr>
            <tr>
                <td class=\"fakeLink erdocsLink\">randColor([],[]);</td>
                <td>Adds random colors to all specified classes.</td>
            </tr>
            <tr>
                <td class=\"fakeLink erdocsLink\">colorSequence([],[]);</td>
                <td>Adds colors to all specified classes in sequence.</td>
            </tr>
        </table>
        <div id=\"fler\">
            <div>
                <p>ermenu({});</p>
                <p>Creates the menu with the passed in options. </p><br />
                <pre class=\"prettyprint\">$(\"#hejsan\").ermenu({
    speed: 150, //in millisecound
    offeet: 10, //in pixels
    offsetLeft: true, //if the offset should be on the left side
    offsetRight: false, //if the offset should be on the right side (can have both)
    varyingSpeeds: true, //will take speed*elements to open
    debug: true, //shows a tooltip with the classes the item has
    directClose: false, //Closes every div when created. (look at the start page for more info)
});</pre>
            </div>
            <div>
                <p>erCloseAll();</p>
                <p>Closes all open tabs in the menu.</p><br />
                <pre class=\"prettyprint\">$(\"#hejsan\").erCloseAll();</pre>
            </div>
            <div>
                <p>erHeaderHover(ehover, lhover, level);</p>
                <p>Adds a hover effect to all headers. If you want it to be added on all headers, set the level to -1.</p>
                <p>If the level is specified the effects will only be added to headers on that level. The first level is 0.</p>
                <pre class=\"prettyprint\">$(\"#hejsan\").erHeaderHover(function(){}, function(){}, -1);</pre>
            </div>
            <div>
                <p>erDivHover(ehover, lhover, level);</p>
                <p>Adds a hover effect to all buttons. If you want it to be added on all buttons, set the level to -1.</p>
                <p>If the level is specified the effects will only be added to buttons on that level. The first level is 0.</p>
                <pre class=\"prettyprint\">$(\"#hejsan\").erDivHover(function(){}, function(){}, -1);</pre>
            </div>
            <div>
                <p>erIndiHover(ehover, lhover);</p>
                <p>removes the old hover effects and adds a new one on the target element.</p>
                <pre class=\"prettyprint\">$(\"#hejsan .ErIndi-1\").erIndiHover(function(){}, function(){});</pre>
            </div>
            <div>
                <p>erOpen(id);</p>
                <p>Opens all the way to the target tab. The id is the number on the \"Er-x\" class.</p>
                <pre class=\"prettyprint\">$(\"#hejsan\").erOpen(2);</pre>
            </div>
            <div>
                <p>addImg(path, class, imgclass);</p>
                <p>Adds a <img> with the specified imgclass and with a picture from path. it will be added to any kind of class that exists in ErMenu. erHeader, ErIndi-x, etc.</p>
                <pre class=\"prettyprint\">$(\"#hejsan\").addImg(\"img.png\", \"ErIndi-4\", \"imgClass\");</pre>
            </div>
            <div>
                <p>randColor([],[]);</p>
                <p>Adds random colors to all specified classes. It changes the \"background\" property. The colors are specified as strings.</p>
                <pre class=\"prettyprint\">$(\"#hejsan\").randColor([\".ErDiv\", \".ErHeader\"], [\"#fff301\", \"#015eea\", \"#fc9303\", \"#d6241f\"]);</pre>
            </div>
            <div>
                <p>colorSequence([],[]);</p>
                <p>Changes the \"background\" property of all specified classes to the specified colors in sequence. Colors are specified as strings. </p>
                <pre class=\"prettyprint\">$(\"#hejsan\").colorSequence([\".ErDiv\", \".ErHeader\"], [\"#fff301\", \"#015eea\", \"#fc9303\", \"#d6241f\"]);</pre>
            </div>
        </div>
        ";
?>