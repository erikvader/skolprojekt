<?php
		/*
		if(isset($_SESSION["anvandare"])){
			echo $_SESSION["anvandare"];
		}
		*/
		
		$offset = $_GET["offset"];
		$limit = $_GET["limit"];
	
		require_once("../inc/connect.inc.php");
		
		$stmt = $db->prepare("SELECT * FROM tjatjabloggen ORDER BY time DESC LIMIT :limit OFFSET :offset");
		$stmt->bindParam(":limit", $limit);
		$stmt->bindParam(":offset", $offset);
		$stmt->execute(/*array(":var" => $var)*/);
		
		while($row = $stmt->fetch(PDO::FETCH_ASSOC)){
			$id = $row["id"];
			$time = $row["time"];
			$user = $row["user"];
			$header = $row["header"];
			$post = $row["post"];
			
			$id = filter_var($id, FILTER_SANITIZE_NUMBER_INT);
			$time = filter_var($time, FILTER_SANITIZE_STRING);
			$user = filter_var($user, FILTER_SANITIZE_STRING);
			$header = filter_var($header, FILTER_SANITIZE_STRING);
			$post = filter_var($post, FILTER_SANITIZE_STRING);
			
			echo"
			<div class=\"blogpost\">
				<p class=\"username\">".$user."</p>
				<h3>".$header."</h3>
				<p class=\"blogtext\">".$post."</p>
				<p class=\"timestamp\">".$time."</p>
			</div>";
		}
		
		//header("Location: blogg.php");
		
	?>