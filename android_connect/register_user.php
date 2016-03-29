<?php
	/*
	   Register user
	*/
	include_once "lib/db_functions.php";
	$db = new DbFunctions();
	$response = array();
	if(isset($_POST["username"]) && isset($_POST["email"]) && isset($_POST["password"])) {
		$username = $_POST["username"];
		$email = $_POST["email"];
		$password = $_POST["password"];
		if($username != "" && $email != "" && $password != "") {
			//email things
			$subject = "Eery Dictionary - Signing up";
			$message = "Hello $username,nnYou have successfully registered to Eery Dictionary.nnRegards,nStaff.";
			$from = "myemail@email.com";
			$headers = "From: " . $from;
			if($db->isUserExistedByUsername($username)) {
				$response["success"] = 0;
				$response["message"] = "An user with that username address already exists";
				$out[] = $response;
				echo json_encode($out);
			} else if($db->isUserExistedByEmail($email)) {
				$response["success"] = 0;
				$response["message"] = "An user with that e-mail address already exists";
				$out[] = $response;
				echo json_encode($out);
			} else if(!$db->validateEmail($email)) {
				$response["success"] = 0;
				$response["message"] = "Invalid e-mail address";
				$out[] = $response;
				echo json_encode($out);
			} else {
				//store the user
				$user = $db->storeUser($username, $email, $password);
				if($user) {
					$response["success"] = 1;
					$response["user"]["username"] = $user["username"];
					$response["user"]["email"] = $user["email"];
					$response["user"]["user_id"] = $user["user_id"];
					$response["user"]["created_at"] = $user["created_at"];
					//mail($email, $subject, $message, $headers);
					$response["message"] = "You have successfully registered to Eery Dictionary";
					$out[] = $response;
					echo json_encode($out);
				} else {
					$response["success"] = 0;
					$response["message"] = "Failed to signing up";
					$out[] = $response;
					echo json_encode($out);
				}
			}
		} else if($username == "") {
			$response["success"] = 0;
			$response["message"] = "Username field is required";
			$out[] = $response;
			echo json_encode($out);
		} else if($email == "") {
			$response["success"] = 0;
			$response["message"] = "E-mail field is required";
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
