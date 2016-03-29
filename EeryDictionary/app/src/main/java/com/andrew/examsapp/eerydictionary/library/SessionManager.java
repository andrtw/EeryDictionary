package com.andrew.examsapp.eerydictionary.library;


import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;

import com.andrew.examsapp.eerydictionary.model.SimpleUser;
import com.andrew.examsapp.eerydictionary.user.MainUserActivity;

public class SessionManager {

	private Context context;
	private SharedPreferences pref;
	private SharedPreferences.Editor editor;

	//shared preferences name and keys
	public static final String PREF_NAME = "eery_preferences";
	public static final String PREF_KEY_LOGIN = "is_logged_in";
	public static final String PREF_KEY_USERNAME = "username";
	public static final String PREF_KEY_EMAIL = "email";
	public static final String PREF_KEY_USER_ID = "user_id";
	public static final String PREF_KEY_CREATED_AT = "created_at";


	public SessionManager(Context context) {
		this.context = context;
		pref = context.getSharedPreferences(PREF_NAME, Context.MODE_PRIVATE);
		editor = pref.edit();
	}


	/*
	* Remembers whether the lsat time the user has logged in or not
	* */
	public void createLoginSession(String username, String email, String user_id, String created_at) {
		editor.putBoolean(PREF_KEY_LOGIN, true);
		editor.putString(PREF_KEY_USERNAME, username);
		editor.putString(PREF_KEY_EMAIL, email);
		editor.putString(PREF_KEY_USER_ID, user_id);
		editor.putString(PREF_KEY_CREATED_AT, created_at);
		editor.apply();
	}

	/*
	* Returns a SimpleUser object containing all the current user data
	* */
	public SimpleUser getUserDetails() {
		SimpleUser user = new SimpleUser();
		user.setUserId(Integer.parseInt(pref.getString(PREF_KEY_USER_ID, null)));
		user.setUsername(pref.getString(PREF_KEY_USERNAME, null));
		user.setEmail(pref.getString(PREF_KEY_EMAIL, null));
		user.setCreatedAt(pref.getString(PREF_KEY_CREATED_AT, null));
		return user;
	}

	/*
	* Checks if user is logged in and can redirect to the MainUserActivity
	* */
	public boolean checkLogin(boolean redirect) {
		boolean isLoggedIn = pref.getBoolean(PREF_KEY_LOGIN, false);
		if (!isLoggedIn && redirect) {
			Intent i = new Intent(context, MainUserActivity.class);
			//closing all the activities
			i.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
			//add new flag to start new activity
			i.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
			context.startActivity(i);
		}
		return isLoggedIn;
	}

	/*
	* Clear the user data and can redirect to MainUserActivity
	* */
	public void logoutUser(boolean redirect) {
		//clear all data from preferences
		editor.clear();
		editor.apply();
		if (redirect) {
			Intent i = new Intent(context, MainUserActivity.class);
			i.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
			i.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
			context.startActivity(i);
		}
	}


}
