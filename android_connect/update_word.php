<?php
	/*
	   Update a word information
	*/
	$response = array();
	include_once "lib/db_config.php";
	$conn = mysqli_connect(DB_HOST, DB_USER, DB_PASSWORD) or die ("Could not connect to the server.");
	mysqli_select_db($conn, DB_NAME);
	if(isset($_POST["word_id"]) && isset($_POST["description"]) && isset($_POST["examples"])) {
		$word_id = $_POST["word_id"];
		$description = $_POST["description"];
		$examples = $_POST["examples"];
		if($description != "") {
			$result = mysqli_query($conn, "UPDATE app_word SET description = '".mysql_real_escape_string($description)."', examples = '".mysql_real_escape_string($examples)."' WHERE word_id = '$word_id'");
			if($result) {
				$response["success"] = 1;
				$response["message"] = "Word successfully updated.";
				$out[] = $response;
				echo json_encode($out);
			} else {
				$response["success"] = 0;
				$response["message"] = "Word not updated.";
				$out[] = $response;
				echo json_encode($out);
			}
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