package com.andrew.examsapp.eerydictionary.library;


import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;
import org.json.JSONArray;

import java.util.ArrayList;
import java.util.List;

public class UserFunctions {

	//URL of PHP APIs
	private static final String LOGIN_URL = JSONParser.HOST + "/android_connect/login_user.php";
	private static final String REGISTER_URL = JSONParser.HOST + "/android_connect/register_user.php";
	private static final String FORGOT_PSW_URL = JSONParser.HOST + "/android_connect/forgot_password.php";
	private static final String CHANGE_PSW_URL = JSONParser.HOST + "/android_connect/change_password.php";
	private static final String GET_USER_BY_USER_ID_URL = JSONParser.HOST + "/android_connect/get_user_by_user_id.php";
	private static final String GET_USER_WORDS_URL = JSONParser.HOST + "/android_connect/get_user_words.php";
	private static final String SEND_FEEDBACK_URL = JSONParser.HOST + "/android_connect/send_feedback.php";

	//database KEYs
	public static final String KEY_USER_ID = "user_id";
	public static final String KEY_USERNAME = "username";
	public static final String KEY_EMAIL = "email";
	public static final String KEY_PASSWORD = "password";
	public static final String KEY_CREATED_AT = "created_at";
	public static final String KEY_FOR_PSW = "forgotpsw";
	public static final String KEY_NEW_PSW = "newpassword";
	public static final String KEY_FEEDBACK_TYPE = "feedback_type";
	public static final String KEY_FEEDBACK = "feedback";


	public UserFunctions() {
	}


	/*
	* Get all user details by its user_id
	* */
	public static JSONArray getUserByUserId(int user_id) {
		List<NameValuePair> params = new ArrayList<>();
		params.add(new BasicNameValuePair(KEY_USER_ID, String.valueOf(user_id)));
		return JSONParser.getJsonArray(GET_USER_BY_USER_ID_URL, params);
	}


	/*
	* Login an existing user
	* */
	public static JSONArray loginUser(String username, String password) {
		List<NameValuePair> params = new ArrayList<>();
		params.add(new BasicNameValuePair(KEY_USERNAME, username.trim()));
		params.add(new BasicNameValuePair(KEY_PASSWORD, password.trim()));
		return JSONParser.getJsonArray(LOGIN_URL, params);
	}

	/*
	* Change the password
	* */
	public static JSONArray changePassword(String newPassword, String email) {
		List<NameValuePair> params = new ArrayList<>();
		params.add(new BasicNameValuePair(KEY_NEW_PSW, newPassword.trim()));
		params.add(new BasicNameValuePair(KEY_EMAIL, email.trim()));
		return JSONParser.getJsonArray(CHANGE_PSW_URL, params);
	}

	/*
	* Reset the password by setting a new random one
	* */
	public static JSONArray forgotPassword(String email) {
		List<NameValuePair> params = new ArrayList<>();
		params.add(new BasicNameValuePair(KEY_FOR_PSW, email.trim()));
		return JSONParser.getJsonArray(FORGOT_PSW_URL, params);
	}

	/*
	* Register a new user
	* */
	public static JSONArray registerUser(String username, String email, String password) {
		List<NameValuePair> params = new ArrayList<>();
		params.add(new BasicNameValuePair(KEY_USERNAME, username.trim()));
		params.add(new BasicNameValuePair(KEY_EMAIL, email.trim()));
		params.add(new BasicNameValuePair(KEY_PASSWORD, password.trim()));
		return JSONParser.getJsonArray(REGISTER_URL, params);
	}

	/*
	* Gets the number of words and the words of that user
	* */
	public static JSONArray getUserWords(int user_id) {
		List<NameValuePair> params = new ArrayList<>();
		params.add(new BasicNameValuePair(KEY_USER_ID, String.valueOf(user_id)));
		return JSONParser.getJsonArray(GET_USER_WORDS_URL, params);
	}

	/*
	* Send me the user's feedback via email
	* */
	public static JSONArray sendFeedback(int user_id, String feedback_type, String feedback) {
		List<NameValuePair> params = new ArrayList<>();
		params.add(new BasicNameValuePair(KEY_USER_ID, String.valueOf(user_id)));
		params.add(new BasicNameValuePair(KEY_FEEDBACK_TYPE, feedback_type));
		params.add(new BasicNameValuePair(KEY_FEEDBACK, feedback));
		return JSONParser.getJsonArray(SEND_FEEDBACK_URL, params);
	}

}