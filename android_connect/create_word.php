<?php
	/*
	   Creating a new word row
	   All words details are read from HTTP Post Request
	*/
	$response = array();
	include_once "lib/db_config.php";
	$conn = mysqli_connect(DB_HOST, DB_USER, DB_PASSWORD) or die ("Could not connect to the server.");
	mysqli_select_db($conn, DB_NAME);
	//check for required fields
	if(isset($_POST["user_id"]) && isset($_POST["word"]) && isset($_POST["description"]) && isset($_POST["examples"])) {
		$word = $_POST["word"];
		$description = $_POST["description"];
		$examples = $_POST["examples"];
		$user_id = $_POST["user_id"];
		//check for required fields
		if($word != "" && $description != "") {
			$result = mysqli_query($conn, "INSERT INTO app_word (user_id, word, description, examples, created_at) VALUES ('$user_id', '".mysql_real_escape_string($word)."', '".mysql_real_escape_string($description)."', '".mysql_real_escape_string($examples)."', NOW())");
			//check if row has been inserted or not
			if($result) {
				$response["success"] = 1;
				$response["message"] = "Word successfully created.";
				$out[] = $response;
				//echoing JSON response
				echo json_encode($out);
			} else {
				$response["success"] = 0;
				$response["message"] = "'$word' already exists.".$result;
				$out[] = $response;
				echo json_encode($out);
			}
		} else if($word == "") {
			$response["success"] = 0;
			$response["message"] = "Word field is required.";
			$out[] = $response;
			echo json_encode($out);
		} else if($description == "") {
			$response["success"] = 0;
			$response["message"] = "Description field is required.";
			$out[] = $response;
			echo json_encode($out);
		}
	} else {
		$response["success"] = 0;
		$response["message"] = "Required field(s) missing.";
		$out[] = $response;
		echo json_encode($out);
	}
	mysqli_close($conn);
?>