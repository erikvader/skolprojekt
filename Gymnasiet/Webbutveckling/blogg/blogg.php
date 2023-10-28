<?php
	session_start();
	/*
	if(!isset($_SESSION["anvandare"])){
		header("Location: index.php?error=inteInloggad");
	}
	*/
?>
<!DOCTYPE html>
<html>
<head>
	<meta charset="utf-8">
	<title>member area</title>
	<link href="css/bloggCSS.css" type="text/css" rel="stylesheet">
	
	<script type="text/javascript" src="js/jquery.js"></script>
	<script type="text/javascript" src="js/blogg.js"></script>
	
</head>
<body>

	<a href="index.php">gå till startsidan</a>

	<div id="layout">
		<div id="inlagg">
			
		</div>
		<div id="getMerInlagg">mer</div>
	</div>
	
	<?php if(isset($_SESSION["loggedIn"]) && $_SESSION["loggedIn"] == true && isset($_SESSION["level"]) && $_SESSION["level"] >= 1) : ?>
	
		<div id="showNewInlagg">skriv nytt</div>
	
		<div id="newInlagg">
			<form method="POST" action="blogg.php" id="skrivNytt">
				<input type="text" name="header" placeholder="rubrik" />
				<textarea placeholder="meddelande" name="blogText"></textarea>
				<input type="submit" name="newPost" value="skicka inlägg" />
			</form>
		</div>
		
		<?php
			if(isset($_POST["newPost"])){
				require_once("../inc/connect.inc.php");
				
				$header = $_POST["header"];
				$post = $_POST["blogText"];
				
				$header = filter_var($header, FILTER_SANITIZE_STRING);
				$post = filter_var($post, FILTER_SANITIZE_STRING);
				
				$stmt = $db->prepare("INSERT INTO tjatjabloggen(user, header, post) VALUES(:user, :header, :post)");
				$stmt->execute(array(":user" => $_SESSION["anvandare"], ":header" => $header, ":post" => $post));
				
				header("location: blogg.php");
			}
			
		?>
	
	<?php endif; ?>
	
</body>
</html>
