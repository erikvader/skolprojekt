<?php
    echo "<table id=\"erDocsTable\">
            <tr>
                <td>Funktioner</td>
                <td>Beskrivning</td>
            </tr>
            <tr>
                <td class=\"fakeLink erdocsLink\">ermenu({});</td>
                <td>Initierar och skapar menyn med de angivna inställningarna.</td>
            </tr>
            <tr>
                <td class=\"fakeLink erdocsLink\">erCloseAll();</td>
                <td>Stängar allt.</td>
            </tr>
            <tr>
                <td class=\"fakeLink erdocsLink\">erHeaderHover(ehover, lhover, level);</td>
                <td>Lägger till en hover-effekt på alla headers på det angivna djupet.</td>
            </tr>
            <tr>
                <td class=\"fakeLink erdocsLink\">erDivHover(ehover, lhover, level);</td>
                <td>Lägger till en hover-effekt på alla knappar på det angivna djupet.</td>
            </tr>
            <tr>
                <td class=\"fakeLink erdocsLink\">erIndiHover(ehover, lhover);</td>
                <td>Lägger till en hover-effekt på den angivna headern eller knappen.</td>
            </tr>
            <tr>
                <td class=\"fakeLink erdocsLink\">erOpen(id);</td>
                <td>Öppnar hela vägen till målet.</td>
            </tr>
            <tr>
                <td class=\"fakeLink erdocsLink\">addImg(path, class, imgclass);</td>
                <td>Lägger till en bild till målklassen.</td>
            </tr>
            <tr>
                <td class=\"fakeLink erdocsLink\">randColor([],[]);</td>
                <td>Lägger till slumpade färger till alla specifierade klasser.</td>
            </tr>
            <tr>
                <td class=\"fakeLink erdocsLink\">colorSequence([],[]);</td>
                <td>Lägger till färger till alla specifierade klasser i ordning. </td>
            </tr>
        </table>
        <div id=\"fler\">
            <div>
                <p>ermenu({});</p>
                <p>Initierar och skapar menyn med de angivna inställningarna.</p><br />
                <pre class=\"prettyprint\">$(\"#hejsan\").ermenu({
    speed: 150, //i millisekunder
    offeet: 10, //i pixlar
    offsetLeft: true, //om de borde förskjutas till vänstar
    offsetRight: false, //om de borde förskjutas till höger (man kan ha båda)
    varyingSpeeds: true, //kommer att ta speed*elements att öppna
    debug: true, //visar ett tooltip med alla klasser
    directClose: false, //stänger hela menyn efter att den har skapats (titta på startsidan för mer info)
});</pre>
            </div>
            <div>
                <p>erCloseAll();</p>
                <p>Closes all open tabs in the menu.</p><br />
                <pre class=\"prettyprint\">$(\"#hejsan\").erCloseAll();</pre>
            </div>
            <div>
                <p>erHeaderHover(ehover, lhover, level);</p>
                <p>Lägger till en hover-effekt på alla headers. Om du vill att den ska läggas till på alla headers sätter di djuper till -1.</p>
                <p>Om djuper är specifierat kommer effekten endast att appliceras på alla headers på det djupet. Det börjar på 0.</p>
                <pre class=\"prettyprint\">$(\"#hejsan\").erHeaderHover(function(){}, function(){}, -1);</pre>
            </div>
            <div>
                <p>erDivHover(ehover, lhover, level);</p>
                <p>Lägger till en hover-effekt på alla knappar. Om du vill att den ska läggas till på alla knappar sätter di djuper till -1.</p>
                <p>Om djuper är specifierat kommer effekten endast att appliceras på alla knappar på det djupet. Det börjar på 0.</p>
                <pre class=\"prettyprint\">$(\"#hejsan\").erDivHover(function(){}, function(){}, -1);</pre>
            </div>
            <div>
                <p>erIndiHover(ehover, lhover);</p>
                <p>Tar bord den gamla hover-effekten och lägger till en ny på den angivna knappen.</p>
                <pre class=\"prettyprint\">$(\"#hejsan .ErIndi-1\").erIndiHover(function(){}, function(){});</pre>
            </div>
            <div>
                <p>erOpen(id);</p>
                <p>Öppnar hela vägen till målknappen. id:et är numret på \"Er-x\"-klassen.</p>
                <pre class=\"prettyprint\">$(\"#hejsan\").erOpen(2);</pre>
            </div>
            <div>
                <p>addImg(path, class, imgclass);</p>
                <p>lägger till en <img> med den spcifierade imgclass om med bilden från path. Den kan läggas till på vilken sorts klass som helst i ErMenu. erHeader, ErIndi-x, etc.</p>
                <pre class=\"prettyprint\">$(\"#hejsan\").addImg(\"img.png\", \"ErIndi-4\", \"imgClass\");</pre>
            </div>
            <div>
                <p>randColor([],[]);</p>
                <p>Lägger till slumpmässiga färger till alla specifierade klasser. Den ändrar \"background\". Färger skrivs in som strings.</p>
                <pre class=\"prettyprint\">$(\"#hejsan\").randColor([\".ErDiv\", \".ErHeader\"], [\"#fff301\", \"#015eea\", \"#fc9303\", \"#d6241f\"]);</pre>
            </div>
            <div>
                <p>colorSequence([],[]);</p>
                <p>Lägger till i ordning färgerna till alla specifierade klasser. Den ändrar \"background\". Färger skrivs in som strings.</p>
                <pre class=\"prettyprint\">$(\"#hejsan\").colorSequence([\".ErDiv\", \".ErHeader\"], [\"#fff301\", \"#015eea\", \"#fc9303\", \"#d6241f\"]);</pre>
            </div>
        </div>
        ";
?>