<?php

	if(
		isset($_POST["username"]) && $_POST["username"] != "" &&
		isset($_POST["password0"]) && $_POST["password0"] != ""
	){
		require_once("../inc/connect.inc.php");
		
		$username = $_POST["username"];
		$password0 = $_POST["password0"];
		
		try{
			$stmt = $db->prepare("SELECT * FROM users WHERE username=:username");
			$stmt->bindParam(":username", $username);
			$stmt->execute();
			
			$password0 = md5($password0);
			
			$result = $stmt->setFetchMode(PDO::FETCH_ASSOC);
			foreach($stmt->fetchAll() as $row){
				if(/*$username == $row["username"] && */$password0 == $row["passw"]){
					echo "du fanns i min fantastiska databas!";
					session_start();
					$_SESSION["anvandare"] = $username;
					$_SESSION["loggedIn"] = true;
					$_SESSION["level"] = $row["level"];
					header("location: blogg.php");
					exit;
				}
			}
			echo "Du existerar inte!";
		}catch(PDOException $e){
			echo "Error: ".$e->getMessage();
		}
		$stmt = null;
		
	}

?>