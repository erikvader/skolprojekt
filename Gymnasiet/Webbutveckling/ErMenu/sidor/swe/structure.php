 <?php
    echo "<div id=\"structureContent\">
        <h1>Hur menyn är strukturerad</h1>
        <div id=\"structureLeft\">
            <img src=\"img/structure1.png\" />
            <p>Varje knapp har sitt egna samling av klasser:</p>
            <ul>
                <li><p>ErOpen: säger att denna Header är öppen.</p></li>
                <li><p>ErHeader: säger att detta är en header.</p></li>
                <li><p>ErDiv: säger att detta är en knapp.</p></li>
                <li><p>Er0: någonting som länkar ihop en ErHeader och ErBody.</p></li>
                <li><p>ErDepth-X: hur djupt ner denna knapp är i menyn.</p></li>
                <li><p>ErIndi-X: ett ID, varje knapp har ett eget ID.</p> </li>
            </ul>
        </div>
        <div id=\"structureRight\">
            <img src=\"img/structure2.png\" />
            <p>ErHeader är ens egna div. Alla ErDivs ligger inuti en div med id:et ErBody. De är länkade med varandra
            med ErX-klassen och erid attributet. ErBody-div:en är den div som försvinner när menyn stängs. </p>
        </div>
        <div id=\"structureBottom\">
        <p>Så här ser den genererade koden ut för menyn på denna sida:</p>
<pre class=\"prettyprint\" id=\"structurePre\">&lt;div id=\"hejsan\"&gt;
    &lt;div class=\"ErHeader Er0 depth-0 ErIndi-0\" erid=\"0\"&gt;
        &lt;p&gt;Home&lt;/p&gt;
    &lt;/div&gt;
    &lt;div class=\"ErBody Er0 depth-0\" erid=\"0\"&gt;
        &lt;ul ertitle=\"Home\"&gt; &lt;/ul&gt;
    &lt;/div&gt;
    &lt;div class=\"ErHeader Er1 depth-0 ErIndi-1\" erid=\"1\"&gt;
        &lt;p&gt;Tutorials&lt;/p&gt;
    &lt;/div&gt;
    &lt;div class=\"ErBody Er1 depth-0\" erid=\"1\"&gt;
        &lt;ul ertitle=\"Tutorials\"&gt;
            &lt;li&gt;
                &lt;div class=\"ErDiv depth-0 ErIndi-2\"&gt;
                    &lt;p&gt;Structure&lt;/p&gt;
                &lt;/div&gt;
            &lt;/li&gt;
            &lt;li&gt;
                &lt;div class=\"ErDiv depth-0 ErIndi-3\"&gt;
                    &lt;p&gt;ErDocs&lt;/p&gt;
                &lt;/div&gt;
            &lt;/li&gt;
            &lt;li&gt;
                &lt;div class=\"ErDiv depth-0 ErIndi-4\"&gt;
                    &lt;p&gt;Create your own theme&lt;/p&gt;
                &lt;/div&gt;
            &lt;/li&gt;
        &lt;/ul&gt;
    &lt;/div&gt;
    &lt;div class=\"ErHeader Er2 depth-0 ErIndi-5\" erid=\"2\"&gt;
        &lt;p&gt;Download&lt;/p&gt;
    &lt;/div&gt;
    &lt;div class=\"ErBody Er2 depth-0\" erid=\"2\"&gt;
        &lt;ul ertitle=\"Download\"&gt; &lt;/ul&gt;
    &lt;/div&gt;
    &lt;div class=\"ErHeader Er3 depth-0 ErIndi-6\" erid=\"3\"&gt;
        &lt;p&gt;Report a bug&lt;/p&gt;
    &lt;/div&gt;
    &lt;div class=\"ErBody Er3 depth-0\" erid=\"3\"&gt;
        &lt;ul ertitle=\"Report a bug\"&gt; &lt;/ul&gt;
    &lt;/div&gt;
    &lt;div class=\"ErHeader Er4 depth-0 ErIndi-7\" erid=\"4\"&gt;
        &lt;p&gt;About/changelog&lt;/p&gt;
    &lt;/div&gt;
    &lt;div class=\"ErBody Er4 depth-0\" erid=\"4\"&gt;
        &lt;ul ertitle=\"About/changelog\"&gt; &lt;/ul&gt;
    &lt;/div&gt;
&lt;/div&gt;</pre>
    </div>
    </div>";
?>