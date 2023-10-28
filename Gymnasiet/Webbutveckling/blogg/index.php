<?php session_start(); ?>
<!DOCTYPE html>
<html>
<head>
	<meta charset="utf-8">
	<title>hejsan</title>
	<link href="css/stil.css" rel="stylesheet" type="text/css">
</head>
<body>

	<?php if(!isset($_SESSION["email"])) : ?>
		
	<?php endif; ?>

	<div id="showLoginForm">
		<?php
			if(isset($_SESSION["loggedIn"]) && $_SESSION["loggedIn"] == true){
				echo "<span class=\"logKnapp\" id=\"logutKnapp\">logga ut</span>"; 
			}else{
				echo "<span class=\"logKnapp\" id=\"loginKnapp\">logga in</span>";
			}
		?>
	</div>

	<div id="loginForm">
	
		<form method="POST" action="login.php" id="logForm">
			
			<input type="test" name="username" placeholder="username" required />
			<input type="password" name="password0" placeholder="lösenord" required />
			
			<input type="submit" name="submit" value="logga in" />
			
		
			<div id="regKlick"><p>Vill du registreara dig på min superbra sida?</p></div>
			<div id="retKlick"><p>Har du glömt ditt awesome lösenord? Tryck här med ditt finger eller muspekare för att återställa ditt lösenord!</p></div>
		</form>
	
		<form method="POST" action="index.php" id="retForm">
		
			<input type="email" name="email" placeholder="email" required />
			
			<input type="submit" name="submit" value="återställ lösenord" />
			
		</form>
	
		<form method="POST" action="register.php" id="regForm">
			
			<input type="text" name="firstName" placeholder="förnamn" required />
			<input type="text" name="lastName" placeholder="efternamn" required />
			<input type="email" name="email" placeholder="email" required />
			<input type="tel" name="tel" placeholder="mobilnummer" />
			<input type="url" name="url" placeholder="din hemsida" />
			<input type="password" name="password0" placeholder="lösenord" required />
			<input type="password" name="password1" placeholder="lösenord igen ty" required />
			
			<input type="submit" name="submit" value="registrera" />
			
		</form>
		
	</div>
	
	<a href="blogg.php">gå till bloggen</a>
	
	<?php
		if(isset($_SESSION["loggedIn"]) && $_SESSION["loggedIn"] == true){
			echo "<p>Du är inloggad som: ".$_SESSION["anvandare"]."</p>";
		}
	?>
	

	<script type="text/javascript" src="js/jquery.js"></script>
	<script type="text/javascript" src="js/mina_script.js"></script>
</body>
</html>