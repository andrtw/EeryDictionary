<?php
	/*
		Get the likes and dislikes of word.
		app_like table type field:
		1 => true (like)
		0 => false (dislike)
	*/
	$response = array();
	include_once "lib/db_config.php";
	$conn = mysqli_connect(DB_HOST, DB_USER, DB_PASSWORD) or die ("Could not connect to the server.");
	mysqli_select_db($conn, DB_NAME);
	if(isset($_POST["word_id"]) && isset($_POST["user_id"])) {
		$word_id = $_POST["word_id"];
		$user_id = $_POST["user_id"];
		$select_likes = mysqli_query($conn, "SELECT likes_count FROM app_word WHERE word_id = '$word_id'");
		$select_dislikes = mysqli_query($conn, "SELECT dislikes_count FROM app_word WHERE word_id = '$word_id'");
		$user_already_liked_or_disliked = mysqli_query($conn, "SELECT appreciation_type FROM app_appreciation WHERE word_id = '$word_id' AND user_id = '$user_id'");
		$rows_count = mysqli_num_rows($user_already_liked_or_disliked);
		
		$response["success"] = 1;
		//user has a like or a dislike
		if($rows_count == 1){
			//if like => $response["user_appreciation"] = 1
			//if dislike => $response["user_appreciation"] = 0
			$row = mysqli_fetch_row($user_already_liked_or_disliked);
			$response["user_appreciation"] = $row[0];
		}
		//user has neither like nor dislike
		else if($rows_count == 0){
			$response["user_appreciation"] = -1;
		}
		$row_likes = mysqli_fetch_row($select_likes);
		$row_dislikes = mysqli_fetch_row($select_dislikes);
		$response["appreciation"] = $row_likes[0].";".$row_dislikes[0];
		$out[] = $response;
		echo json_encode($out);
	} else {
		$response["success"] = 0;
		$response["message"] = "Required field(s) missing";
		$out[] = $response;
		echo json_encode($out);
	}
	mysqli_close($conn);
?>
