 <?php
    echo "<div id=\"structureContent\">
        <h1>How the menu is structured</h1>
        <img src=\"img/structure1.png\" />
        <p>Every button has its own set of classes:</p>
        <ul>
            <li>ErOpen: Says that this header is currently opened.</li>
            <li>ErHeader: States that this is a header.</li>
            <li>ErDiv: states that this is a button.</li>
            <li>Er0: Something that links ErBody with ErHeader.</li>
            <li>ErDepth-X: How deep this button is in the menu.</li>
            <li>ErIndi-X: An ID, every button has its own ID. </li>
        </ul>
        <img src=\"img/structure2.png\" />
        <p>ErHeader is its own div. All ErDivs are located inside a div with the id ErBody. They are linked to each other
        with the ErX class and erid attribute. </p>
<pre><div id=\"menyn\">
<div class=\"ErHeader Er0 depth-0 ErIndi-0\" erid=\"0\">
<p>Home</p>
</div>
<div class=\"ErBody Er0 depth-0\" erid=\"0\">
<ul ertitle=\"Home\"> </ul>
</div>
<div class=\"ErHeader Er1 depth-0 ErIndi-1\" erid=\"1\">
<p>Tutorials</p>
</div>
<div class=\"ErBody Er1 depth-0\" erid=\"1\">
<ul ertitle=\"Tutorials\">
<li>
<div class=\"ErDiv depth-0 ErIndi-2\">
<p>Structure</p>
</div>
</li>
<li>
<div class=\"ErDiv depth-0 ErIndi-3\">
<p>ErDocs</p>
</div>
</li>
<li>
<div class=\"ErDiv depth-0 ErIndi-4\">
<p>Create your own theme</p>
</div>
</li>
</ul>
</div>
<div class=\"ErHeader Er2 depth-0 ErIndi-5\" erid=\"2\">
<p>Download</p>
</div>
<div class=\"ErBody Er2 depth-0\" erid=\"2\">
<ul ertitle=\"Download\"> </ul>
</div>
<div class=\"ErHeader Er3 depth-0 ErIndi-6\" erid=\"3\">
<p>Report a bug</p>
</div>
<div class=\"ErBody Er3 depth-0\" erid=\"3\">
<ul ertitle=\"Report a bug\"> </ul>
</div>
<div class=\"ErHeader Er4 depth-0 ErIndi-7\" erid=\"4\">
<p>About/changelog</p>
</div>
<div class=\"ErBody Er4 depth-0\" erid=\"4\">
<ul ertitle=\"About/changelog\"> </ul>
</div>
</div></pre>
    </div>";
?>