<?php
	
	class DbFunctions {
		private $db;
		
		function __construct() {
			include_once "lib/db_config.php";
			$this->db = mysqli_connect(DB_HOST, DB_USER, DB_PASSWORD) or die ("Could not connect to the server.");
			mysqli_select_db($this->db, DB_NAME);
		}
		//will be called as soon as there are no other references to the object
		function __destruct() {
			mysqli_close($this->db);
		}
		
		/*
		   Random string which is sent by email to reset password
		*/
		public function randomString() {
			$character_set_array = array();
			$character_set_array[] = array("count" => 7, "characters" => "abcdefghijklmnopqrstuvwxyz");
			$character_set_array[] = array("count" => 1, "characters" => "0123456789");
			$temp_array = array();
			foreach($character_set_array as $character_set) {
				for($i = 0; $i < $character_set["count"]; $i++) {
					$temp_array[] = $character_set["characters"][rand(0, strlen($character_set["characters"]) - 1)];
				}
			}
			shuffle($temp_array);
			return implode("", $temp_array);
		}
		
		public function forgotPassword($email, $newPassword, $salt) {
			$result = mysqli_query($this->db, "UPDATE app_user SET encrypted_password = '$newPassword', salt = '$salt' WHERE email = '$email'");
			if($result)
				return true;
			return false;
		}
		
		/*
		   Adding new user to database. Returns user details
		*/
		public function storeUser($username, $email, $password) {
			$hash = $this->hashSSHA($password);
			$encrypted_password = $hash["encrypted"];
			$salt = $hash["salt"];
			$result = mysqli_query($this->db, "INSERT INTO app_user (username, email, encrypted_password, salt, created_at) VALUES ('$username', '$email', '$encrypted_password', '$salt', NOW())");
			if($result) {
				//get user details
				$user_id = mysqli_insert_id($this->db); //last id inserted
				$result = mysqli_query($this->db, "SELECT * FROM app_user WHERE user_id = '$user_id'");
				return mysqli_fetch_array($result);
			}
			return false;
		}
		
		/*
		   Verifies user by username and password
		*/
		public function getUserByUsernameAndPassword($username, $password) {
			$result = mysqli_query($this->db, "SELECT * FROM app_user WHERE username = '$username'");
			if(mysqli_num_rows($result) > 0) {
				$result = mysqli_fetch_array($result);
				$salt = $result["salt"];
				$encrypted_password = $result["encrypted_password"];
				$hash = $this->checkhashSSHA($salt, $password);
				if($encrypted_password == $hash)
					return $result;
				return false;
			}
			return false;
		}
		
		/*
		   Gets all user details given the user_id
		*/
		public function getUserByUserId($user_id) {
			$result = mysqli_query($this->db, "SELECT * FROM app_user WHERE user_id = '$user_id'");
			if(mysqli_num_rows($result) > 0) {
				return mysqli_fetch_array($result);
			}
			return false;
		}
    public function getUsernameByEmail($email) {
			$result = mysqli_query($this->db, "SELECT username FROM app_user WHERE email = '$email'");
			if(mysqli_num_rows($result) > 0) {
				$row = mysqli_fetch_array($result);
				return $row[0];
			}
			return false;
    }
		
		/*
		   Checks whether the email is valid or not
		*/
		public function validateEmail($email) {
			$isValid = true;
			$atIndex = strrpos($email, "@");
			if(is_bool($atIndex) && !$atIndex) {
				$isValid = false;
			} else {
				$domain = substr($email, $atIndex+1);
				$local = substr($email, 0, $atIndex);
				$domainLen = strlen($domain);
				$localLen = strlen($local);
				if($localLen < 1 || $localLen > 64) {
					//local part length exceeded
					$isValid = false;
				} else if($domainLen < 1 || $domainLen > 255) {
					//domain part length exceeded
					$isValid = false;
				} else if($local[0] == "." || $local[$localLen-1] == ".") {
					//local part starts or ends with '.'
					$isValid = false;
				} else if(preg_match("/\.\./", $local)) {
					//local part has two consecutive dots
					$isValid = false;
				} else if(!preg_match("/^[A-Za-z0-9\-\.]+$/", $domain)) {
					//character not valid in domain part
					$isValid = false;
				} else if(preg_match("/\.\./", $domain)) {
					//domain part has two consecutove dots
					$isValid = false;
				} else if(!preg_match("/^(\\.|[A-Za-z0-9!#%&`_=\/$'*+?^{}|~.-])+$/", str_replace("\\", "", $local))) {
					//character not valid in local part unless local part is quoted
					if(!preg_match('/^"(\\"|[^"])+"$/', str_replace("\\", "", $local))) {
						$isValid = false;
					}
				}
				if($isValid && !(checkdnsrr($domain, "MX") || checkdnsrr($domain, "A"))) {
					//domain not found in DNS
					$isValid = false;
				}
			}
			return $isValid;
		}
		
		/*
		   Checks if user already exists
		*/
		public function isUserExistedByUsername($username) {
			$result = mysqli_query($this->db, "SELECT * FROM app_user WHERE username = '$username'");
			if(mysqli_num_rows($result) > 0) {
				return true;
			}
			return false;
		}
		public function isUserExistedByEmail($email) {
			$result = mysqli_query($this->db, "SELECT * FROM app_user WHERE email = '$email'");
			if(mysqli_num_rows($result) > 0) {
				return true;
			}
			return false;
		}
		
		
		/*
		   Encrypting password. Returns salt and encrypted password
		*/
		public function hashSSHA($password) {
			$salt = sha1(rand());
			$salt = substr($salt, 0, 10);
			$encrypted = base64_encode(sha1($password . $salt, true) . $salt);
			$hash = array("salt" => $salt, "encrypted" => $encrypted);
			return $hash;
		}
		
		/*
		   Decrypting password. Returns hash string
		*/
		public function checkhashSSHA($salt, $password) {
			$hash = base64_encode(sha1($password . $salt, true) . $salt);
			return $hash;
		}
	}
	
?>
