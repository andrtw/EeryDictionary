<?php
	/*
	   Getting the number of user's words and the words themselves
	*/
	$response = array();
	include_once "lib/db_config.php";
	$conn = mysqli_connect(DB_HOST, DB_USER, DB_PASSWORD) or die ("Could not connect to the server.");
	mysqli_select_db($conn, DB_NAME);
	if(isset($_POST["user_id"])) {
		$user_id = $_POST["user_id"];
		$result = mysqli_query($conn, "SELECT * FROM app_word WHERE user_id = '$user_id'");
		$rows_count = mysqli_num_rows($result);
		if($rows_count > 0) {
			while($row = mysqli_fetch_assoc($result)) {
				$words[] = $row;
			}
			$response["success"] = 1;
			$response["words_count"] = $rows_count;
			$response["word"] = $words;
			$out[] = $response;
			echo json_encode($out);
		} else {
			$response["success"] = 1;
            $response["words_count"] = 0;
			$response["message"] = "Still no words";
			$out[] = $response;
			echo json_encode($out);
		}
	} else {
		$response["success"] = 0;
		$response["message"] = "Required field(s) missing";
		$out[] = $response;
		echo json_encode($out);
	}
	mysqli_close($conn);
?>