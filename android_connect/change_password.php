<?php
	
	/*
	   Change password
	*/
	include_once "lib/db_functions.php";
	$db = new DbFunctions();
	$response = array();
	if(isset($_POST["email"]) && isset($_POST["newpassword"])) {
		$email = $_POST["email"];
		$newpassword = $_POST["newpassword"];
		if($newpassword != "") {
			$hash = $db->hashSSHA($newpassword);
			$encrypted_password = $hash["encrypted"];
			$salt = $hash["salt"];
			
			$user = $db->forgotPassword($email, $encrypted_password, $salt);
			if($user) {
				$response["success"] = 1;
				$response["message"] = "Password has been changed";
				$out[] = $response;
				echo json_encode($out);
			} else {
				$response["success"] = 0;
				$response["message"] = "An error occurred";
				$out[] = $response;
				echo json_encode($out);
			}
		} else {
			$response["success"] = 0;
			$response["message"] = "New password field is required";
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
