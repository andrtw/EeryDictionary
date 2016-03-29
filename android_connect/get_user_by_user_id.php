<?php
	/*
		Returns all user data given the user_id
	*/
	include_once "lib/db_functions.php";
	$response = array();
	$db = new DbFunctions();
	if(isset($_POST["user_id"])) {
		$user_id = $_POST["user_id"];
		$user = $db->getUserByUserId($user_id);
		if($user) {
			$response["success"] = 1;
			$response["user"]["username"] = $user["username"];
			$response["user"]["email"] = $user["email"];
			$response["user"]["created_at"] = $user["created_at"];
			$out[] = $response;
			echo json_encode($out);
		} else {
			$response["success"] = 0;
			$response["message"] = "No user found.";
			$out[] = $response;
			echo json_encode($out);
		}
	} else {
		$response["success"] = 0;
		$response["message"] = "Required field(s) missing.";
		$out[] = $response;
		echo json_encode($out);
	}
	
?>