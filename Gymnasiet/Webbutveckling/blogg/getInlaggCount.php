<?php

	require_once("../inc/connect.inc.php");
	
	$stmt = $db->prepare("SELECT COUNT(*) FROM tjatjabloggen");
	$stmt->execute(/*array(":var" => $var)*/);
	
	$row = $stmt->fetch();
	
	echo $row[0];
?>