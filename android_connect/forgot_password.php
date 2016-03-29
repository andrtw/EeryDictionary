<?php
	
	/*
	   Forgot password
	*/
	include_once "lib/db_functions.php";
	$db = new DbFunctions();
	$response = array();
	if(isset($_POST["forgotpsw"])) {
		$email = $_POST["forgotpsw"];
		if($email != "") {
			if($db->validateEmail($email)){
				$random_code = $db->randomString();
				$hash = $db->hashSSHA($random_code);
				$encrypted_password = $hash["encrypted"];
				$salt = $hash["salt"];
				
				$username = $db->getUsernameByEmail($email);
				$subject = "Eery Dictionary - Password recovery";
				$message = "Hello $username,\nYour password has successfully changed. Your new password is $random_code. Login with the new password and change it.\n\nRegards,\nStaff.";
				$from = "eerydictionary@altervista.org";
				$headers = "From: " . $from;
				
				if($db->isUserExistedByEmail($email)) {
					$user = $db->forgotPassword($email, $encrypted_password, $salt);
					if($user) {
						$response["success"] = 1;
						$response["message"] = "A recovery e-mail was sent to you, see it for more details";
						mail($email, $subject, $message, $headers);
						$out[] = $response;
						echo json_encode($out);
					} else {
						$response["success"] = 0;
						$response["message"] = "That user doesn't seem to exist";
						$out[] = $response;
						echo json_encode($out);
					}
				} else {
					$response["success"] = 0;
					$response["message"] = "Your e-mail address doesn't seem to exist";
					$out[] = $response;
					echo json_encode($out);
				}
			} else {
				$response["success"] = 0;
				$response["message"] = "Invalid e-mail address";
				$out[] = $response;
				echo json_encode($out);
			}
		} else {
			$response["success"] = 0;
			$response["message"] = "The field is required";
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
