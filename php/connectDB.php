<?php
function connect_db(){
	$servername = "localhost";
	$username = "sthlmrunning_com";
	$password = "QAsJAvGp";
	$db =  = 'sthmlrunning_com';

	// Create connection
	$conn = new mysqli($servername, $username, $password, $db);

	// Check connection
	if ($conn->connect_error) {
		die("Connection failed: " . $conn->connect_error);
	} 
	else echo "Connected successfully";
	
	return $conn;
}
?>