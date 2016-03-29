<?php
	/*
		Like/dislike a word.
		The relation between word liked and user who liked is added/removed to/from the app_appreciation table.
		Script called every time like or dislike button is pressed.
		app_appreciation table, appreciation_type field:
		1 => true (like)
		0 => false (dislike)
	*/
	$response = array();
	include_once "lib/db_config.php";
	$conn = mysqli_connect(DB_HOST, DB_USER, DB_PASSWORD) or die ("Could not connect to the server.");
	mysqli_select_db($conn, DB_NAME);
	if(isset($_POST["appreciation_tag"]) && isset($_POST["word_id"]) && isset($_POST["user_id"])) {
		$appreciation_tag = $_POST["appreciation_tag"];
		$word_id = $_POST["word_id"];
		$user_id = $_POST["user_id"];
		$user_already_liked_or_disliked = mysqli_query($conn, "SELECT appreciation_type FROM app_appreciation WHERE word_id = '$word_id' AND user_id = '$user_id'");
		$rows_count = mysqli_num_rows($user_already_liked_or_disliked);
		//*-*-*-*-*-
		//*- LIKE -*
		//*-*-*-*-*-
		if($appreciation_tag == "like") {
			//this user has neither like nor dislike on this word. Add like
			if($rows_count == 0){
				$update_word = mysqli_query($conn, "UPDATE app_word SET likes_count = likes_count+1 WHERE word_id = '$word_id'");
				$add_like = mysqli_query($conn, "INSERT INTO app_appreciation (appreciation_type, word_id, user_id) VALUES (1, '$word_id', '$user_id')");
				if(!$update_word || !$add_like){
					$response["success"] = 0;
					$response["message"] = "this user has neither like nor dislike on this word. Add like";
					$out[] = $response;
					echo json_encode($out);
				} else {
					$response["success"] = 1;
					$out[] = $response;
					echo json_encode($out);
				}
			}
			//this user already has like or dislike
			else if($rows_count == 1){
				//this user has a like. Remove it
				$row = mysqli_fetch_row($user_already_liked_or_disliked);
				if($row[0] == 1){
					$update_word = mysqli_query($conn, "UPDATE app_word SET likes_count = likes_count-1 WHERE word_id = '$word_id'");
					$remove_like = mysqli_query($conn, "DELETE FROM app_appreciation WHERE word_id = '$word_id' AND user_id = '$user_id'");
					if(!$update_word || !$remove_like){
						$response["success"] = 0;
						$response["message"] = "this user has a like. Remove it";
						$out[] = $response;
						echo json_encode($out);
					} else {
						$response["success"] = 1;
						$out[] = $response;
						echo json_encode($out);
					}
				}
				//this user has a dislike. Remove it and add like
				else if($row[0] == 0){
					$update_word = mysqli_query($conn, "UPDATE app_word SET dislikes_count = dislikes_count-1, likes_count = likes_count+1 WHERE word_id = '$word_id'");
					$remove_dislike_add_like = mysqli_query($conn, "UPDATE app_appreciation SET appreciation_type = 1 WHERE word_id = '$word_id' AND user_id = '$user_id'");
					if(!$update_word || !$remove_dislike_add_like){
						$response["success"] = 0;
						$response["message"] = "this user has a dislike. Remove it and add like";
						$out[] = $response;
						echo json_encode($out);
					} else {
						$response["success"] = 1;
						$out[] = $response;
						echo json_encode($out);
					}
				}
			}
		}
		//*-*-*-*-*-*-*
		//*- DISLIKE -*
		//*-*-*-*-*-*-*
		else if($appreciation_tag == "dislike") {
			//this user has neither like nor dislike on this word. Add dislike
			if($rows_count == 0){
				$update_word = mysqli_query($conn, "UPDATE app_word SET dislikes_count = dislikes_count+1 WHERE word_id = '$word_id'");
				$add_dislike = mysqli_query($conn, "INSERT INTO app_appreciation (appreciation_type, word_id, user_id) VALUES (0, '$word_id', '$user_id')");
				if(!$update_word || !$add_dislike){
					$response["success"] = 0;
					$response["message"] = "this user has neither like nor dislike on this word. Add dislike";
					$out[] = $response;
					echo json_encode($out);
				} else {
					$response["success"] = 1;
					$out[] = $response;
					echo json_encode($out);
				}
			}
			//this user already has like or dislike
			else if($rows_count == 1){
				//this user has a like. Remove it and add dislike
				$row = mysqli_fetch_row($user_already_liked_or_disliked);
				if($row[0] == 1){
					$update_word = mysqli_query($conn, "UPDATE app_word SET likes_count = likes_count-1, dislikes_count = dislikes_count+1 WHERE word_id = '$word_id'");
					$remove_like_add_dislike = mysqli_query($conn, "UPDATE app_appreciation SET appreciation_type = 0 WHERE word_id = '$word_id' AND user_id = '$user_id'");
					if(!$update_word || !$remove_like_add_dislike){
						$response["success"] = 0;
						$response["message"] = "this user has a like. Remove it and add dislike";
						$out[] = $response;
						echo json_encode($out);
					} else {
						$response["success"] = 1;
						$out[] = $response;
						echo json_encode($out);
					}
				}
				//this user has a dislike. Remove it
				else if($row[0] == 0){
					$update_word = mysqli_query($conn, "UPDATE app_word SET dislikes_count = dislikes_count-1 WHERE word_id = '$word_id'");
					$remove_dislike = mysqli_query($conn, "DELETE FROM app_appreciation WHERE word_id = '$word_id' AND user_id = '$user_id'");
					if(!$update_word || !$remove_dislike){
						$response["success"] = 0;
						$response["message"] = "this user has a dislike. Remove it";
						$out[] = $response;
						echo json_encode($out);
					} else {
						$response["success"] = 1;
						$out[] = $response;
						echo json_encode($out);
					}
				}
			}
		}
	} else {
		$response["success"] = 0;
		$response["message"] = "Required field(s) missing.";
		$out[] = $response;
		echo json_encode($out);
	}
	mysqli_close($conn);
?>
