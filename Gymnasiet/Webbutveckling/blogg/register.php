<?php
	
	if(
		isset($_POST["firstName"]) && $_POST["firstName"] != "" &&
		isset($_POST["lastName"]) && $_POST["lastName"] != "" &&
		isset($_POST["email"]) && $_POST["email"] != "" &&
		isset($_POST["tel"]) && $_POST["tel"] != "" &&
		isset($_POST["url"]) && $_POST["url"] != "" &&
		isset($_POST["password0"]) && $_POST["password0"] != "" &&
		isset($_POST["password1"]) && $_POST["password1"] != ""
	){
		$firstName = $_POST["firstName"];
		$lastName = $_POST["lastName"];
		$email = $_POST["email"];
		$tel = $_POST["tel"];
		$url = $_POST["url"];
		$password0 = $_POST["password0"];
		$password1= $_POST["password1"];
		
		if(filter_var($email, FILTER_VALIDATE_EMAIL) && filter_var($email, FILTER_SANITIZE_EMAIL)){
			//email är bra
		}else{
			header("Location: index.php?error=badEmail");
			exit;
		}
		
		if($password0 == $password1){
			//lösenorden stämmer
		}else{
			header("Location: index.php?error=badPass");
			exit;
		}
		
		require_once("../inc/connect.inc.php");
		
		try{
			$stmt = $db->prepare("SELECT * FROM users");
			$stmt->execute();
			
			$result = $stmt->setFetchMode(PDO::FETCH_ASSOC);
			foreach($stmt->fetchAll() as $row){//kolla om användare/ epost redan finns
				
				if($lastName == $row["username"] || $email == $row["email"]){
					//användarnamn och/eller email finns redan
					header("Location: index.php?error=userFinnsRedan");
					exit;
				}
				
			}
		}catch(PDOException $e){
			echo "Error: ".$e->getMessage();
		}
		$stmt = null;
		
		$password0 = md5($password0);
		
		$stmt = $db->prepare("INSERT INTO users(username, passw, email, first_name, level) VALUES(:username, :passw, :email, :first_name, 0)");
		$stmt->bindParam(":username", $lastName);
		$stmt->bindParam(":passw", $password0);
		$stmt->bindParam(":email", $email);
		$stmt->bindParam(":first_name", $firstName);
		
		$stmt->execute();
	}else{
		header("Location: index.php?error=alltVarInteAngivet");
		exit;
	}

	echo "du är nu registrerad! gratis! du kan nu titta på grattis porr! bara att runka på!";
	echo "<a href=\"index.php\">återgå till startsidan</a>";
	
?>