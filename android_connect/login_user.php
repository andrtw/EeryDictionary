<?php
	
	/*
	   Login
	*/
	include_once "lib/db_functions.php";
	$db = new DbFunctions();
	$response = array();
	if(isset($_POST["username"]) && isset($_POST["password"])) {
		$username = $_POST["username"];
		$password = $_POST["password"];
		if($username != "" && $password != "") {
			//check for user
			$user = $db->getUserByUsernameAndPassword($username, $password);
			if($user) {
				$response["success"] = 1;
				$response["user"]["username"] = $user["username"];
				$response["user"]["email"] = $user["email"];
				$response["user"]["user_id"] = $user["user_id"];
				$response["user"]["created_at"] = $user["created_at"];
				$out[] = $response;
				echo json_encode($out);
			} else {
				$response["success"] = 0;
				$response["message"] = "Incorrect username or password";
				$out[] = $response;
				echo json_encode($out);
			}
		} else if($username == "") {
			$response["success"] = 0;
			$response["message"] = "Username field is required";
			$out[] = $response;
			echo json_encode($out);
		} else if($password == "") {
			$response["success"] = 0;
			$response["message"] = "Password field is required";
			$out[] = $response;
			echo json_encode($out);
		}
	} else {
		$response["success"] = 0;
		$response["message"] = "Required field(s) missing";
		$out[] = $response;
		echo json_encode($out);
	}
	
?>





