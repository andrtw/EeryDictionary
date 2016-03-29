<?php
	/*
	   Getting single word details
	*/
	$response = array();
	include_once "lib/db_config.php";
	$conn = mysqli_connect(DB_HOST, DB_USER, DB_PASSWORD) or die ("Could not connect to the server.");
	mysqli_select_db($conn, DB_NAME);
	if(isset($_POST["word"])) {
		$word = $_POST["word"];
		$result = mysqli_query($conn, "SELECT * FROM app_word WHERE word LIKE '%$word%'");
		$rows_count = mysqli_num_rows($result);
		//echo $rows_count." rows found ";
		if($rows_count > 0) {
			while($row = mysqli_fetch_assoc($result)) {
				$rows[] = $row;
			}
			$response["success"] = 1;
			$response["word"] = $rows;
			$out[] = $response;
			echo json_encode($out);
		} else {
			$response["success"] = 0;
			$response["message"] = "No matching words found.";
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