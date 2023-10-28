<!DOCTYPE html>
<html>
<head>
	<meta charset="utf-8">
	<title></title>
	<link href="css/stilmall.css" type="text/css" rel="stylesheet">
</head>
<body>
	<div id="layout">
		<div id="header">
		
		</div>
		<div id="menu">
			<ul>
				<li><a href="index.php?sida=hem">hem</a></li>
				<li><a href="index.php?sida=om">om</a></li>
				<li><a href="index.php?sida=lankar">länkar</a></li>
			</ul>
		</div>
		<div id="main">
			<?php
				$viktor = $_GET["sida"];
				if($viktor == "hem"){
					require_once('undersidor/hem.php');
				}else if($viktor == "om"){
					echo "om";
				}else if($viktor == "lankar"){
					echo "länkar";
				}else{
					echo "hem";
				}
			?>
		</div>
		<div id="footer">
		
		</div>
	</div>
</body>
</html>