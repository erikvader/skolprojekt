<!DOCTYPE html>
<html>
<head>
	<meta charset="utf-8">
	<title>Formulär</title>
	<link href="css/stilmall.css" type="text/css" rel="stylesheet">
	<script src="js/jquery.js"></script>
	<script src="js/javagrejer.js"></script>
	
</head>
<body>

	<?php
	
		if(isset($_POST['knapp'])){
			$textruta = $_POST['textruta'];
			$select = $_POST['grej'];
			$anva = $_POST['anvandarnamn'];
			$pass = $_POST['pass'];
			$check = $_POST['checkgrej'];
			$radio = $_POST['rixFM'];
			$knapp = $_POST['knapp'];
			$mail = $_POST['email'];
			
			
			
			/*
			$hela_meddelandet = 
				$textruta. "\n".
				$select. "\n".
				$anva. "\n".
				$pass. "\n".
				$check. "\n".
				$radio;
			
			$to = $mail;
			$subject = 'min mail';
			$message = $hela_meddelandet;
			$headers = 'From: no-reply@ermergerd.com';
			
			//$headers .= "Content-type: text/html; charset=\"UTF-8\"; format=flowed \r\n";
			//$headers .= "Mime-Version: 1.0 \r\n";
			//$headers .= "Content-Transfer-Encoding: quoted-printable \r\n";
		
			mail($to, $subject, $message, $headers);
			*/
		}
		
		
		
		
		
	?>

	<div id="layout">
	
		<form method="POST" action="index.php">
			<input type="text" name="term1" placeholder="term1" /><br />
			<input type="text" name="term2" placeholder="term2" /><br />
			+<input type="radio" name="raknesatt" value="add" /><br />
			/<input type="radio" name="raknesatt" value="div" /><br />
			*<input type="radio" name="raknesatt" value="mult" /><br />
			-<input type="radio" name="raknesatt" value="minu" /><br />
			
			<input type="submit" name="go" value="Giva me da answer" />
		</form>
		
		svar:
		
		<?php
			if(isset($_POST['go'])){
		
			$tal1 = $_POST['term1'];
			$tal2 = $_POST['term2'];
			$satt = $_POST['raknesatt'];
			
			if($satt == "add"){
				echo $tal1 + $tal2;
			}else if($satt == "minu"){
				echo $tal1 - $tal2;
			} else if($satt == "div"){
				echo $tal1 / $tal2;
			} else if($satt == "mult"){
				echo $tal1 * $tal2;
			} 
		
			}
		?>
	
		<form id="registration_form" method="POST" action="index.php">
			
			<textarea name="textruta" placeholder="vad tänker du på?" required></textarea><br />
			
			<select name="grej" required>
				<option value="">ermergerd!</option>
			</select><br />
		
			<input type="text" name="anvandarnamn" placeholder="username" required /><br />
			<input type="password" name="pass" placeholder="password" required /><br />
			<input type="text" name="email" placeholder="E-post" required /><br />
			Jag accepterar avnändarvillkoren <input type="checkbox" name="checkgrej" required /><br />
			kvinna <input type="radio" name="rixFM" value="kvinna" required /><br />
			ariska rasen <input type="radio" name="rixFM"  value="arisk" /><br />
			neger <input type="radio" name="rixFM"  value="neger" /><br />
			<input type="submit" name="knapp" />
			
		</form>
	</div>
</body>
</html>