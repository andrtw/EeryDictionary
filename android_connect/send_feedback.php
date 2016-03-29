<?php
	$response = array();
    include_once "lib/db_config.php";
    $conn = mysqli_connect(DB_HOST, DB_USER, DB_PASSWORD) or die ("Could not connect to the server.");
    mysqli_select_db($conn, DB_NAME);
    if(isset($_POST["user_id"]) && isset($_POST["feedback_type"]) && isset($_POST["feedback"])) {
    	$user_id = $_POST["user_id"];
        $feedback_type = $_POST["feedback_type"];
        $feedback = $_POST["feedback"];
        
        $email = "email@email.com";
        $subject = "Eery Dictionary - feedback from user $user_id";
        $from = "email@email.com";
		$headers = "From: " . $from;
        mail($email, $subject, $feedback_type."\n\n".$feedback, $headers);
        
        $response["success"] = 1;
        $response["message"] = "Thank you. Your feedback has been sent";
        $out[] = $response;
        echo json_encode($out);
        
    } else {
    	$response["success"] = 0;
		$response["message"] = "Required field(s) missing";
		$out[] = $response;
		echo json_encode($out);
    }
?>
