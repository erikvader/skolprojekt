<?php
    echo "
    <div id=\"downloadContent\">
        <ul id=\"centeredUL\">
            <li><p>The core is the main program, you must have this.</p></li>
            <li><p>The theme gives the menu a nice look.</p></li>
            <li><p>An effect is an animation which is optional.</p></li>
        </ul>
        <a id=\"downCore\" href=\"js/ErMenu.js\" download><div><p>Download the core!</p></div></a>
        
        <table>
            <tr>
                <td><a title=\"download effekt1\" href=\"teman/effekter/js/effekt1.js\" download><img src=\"img/effekt1.gif\"></img></a></td>
                <td><a title=\"download effekt2\" href=\"teman/effekter/js/effekt2.js\" download><img src=\"img/effekt2.gif\"></img></a></td>
            </tr>
        </table>
        <table id=\"downMeny\">
            <tr>
                <td><button class=\"btn1\" id=\"downloadsTestGold\">Test</button><a class=\"btn2\" href=\"teman/downloads/gold.zip\" download><button>Download</button></a><div>Gold</div></td>
                <td><button class=\"btn1\" id=\"downloadsTestPornhub\">Test</button><a class=\"btn2\" href=\"teman/downloads/pornhub.zip\" download><button>Download</button></a><div>Pornhub</div></td>
                <td><button class=\"btn1\" id=\"downloadsTestBoringBlue\">Test</button><a class=\"btn2\" href=\"teman/downloads/boring_blue.zip\" download><button>Download</button></a><div>Boring Blue</div></td>
                <td><button class=\"btn1\" id=\"downloadsTestIce\">Test</button><a class=\"btn2\" href=\"teman/downloads/ice.zip\" download><button>Download</button></a><div>Ice</div></td>
            </tr>
            <tr>
                <td><button class=\"btn1\" id=\"downloadsTestRubiks\">Test</button><a class=\"btn2\" href=\"teman/downloads/rubiks.zip\" download><button>Download</button></a><div>Rubiks</div></td>
                <td><button class=\"btn1\" id=\"downloadsTestNone\">Test</button><a class=\"btn2\" href=\"teman/downloads/standard.zip\" download><button>Download</button></a><div>None</div></td>
            </tr>
            
        </table>
    </div>
    <div id=\"menyCentered\">
        <div id=\"hejsan\">
            <ul ErTitle=\"A title\">
                <li>a menu Item</li>
                <li>another menu Item</li>
            </ul>
            <ul ErTitle=\"1\">
                <li>item</li>
                <li>
                    <ul ErTitle=\"2\">
                        <li>more menus!</li>
                        <li>oh my god!</li>
                    </ul>
                </li>
            </ul>
        </div>
    </div>";
?>