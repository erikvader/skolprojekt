 <?php
    echo "<div id=\"structureContent\">
        <h1>How the menu is structured</h1>
        <div id=\"structureLeft\">
            <img src=\"img/structure1.png\" />
            <p>Every button has its own set of classes:</p>
            <ul>
                <li><p>ErOpen: Says that this header is currently opened.</p></li>
                <li><p>ErHeader: States that this is a header.</p></li>
                <li><p>ErDiv: states that this is a button.</p></li>
                <li><p>Er0: Something that links ErBody with ErHeader.</p></li>
                <li><p>ErDepth-X: How deep this button is in the menu.</p></li>
                <li><p>ErIndi-X: An ID, every button has its own ID.</p> </li>
            </ul>
        </div>
        <div id=\"structureRight\">
            <img src=\"img/structure2.png\" />
            <p>ErHeader is its own div. All ErDivs are located inside a div with the id ErBody. They are linked to each other
            with the ErX class and erid attribute. The ErBody-div is the div that disappears when the menu is closed. </p>
        </div>
        <div id=\"structureBottom\">
        <p>This is how the genereated code looks like for the menu on this website:</p>
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