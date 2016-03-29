package com.andrew.examsapp.eerydictionary.user;

import android.app.Fragment;
import android.app.ProgressDialog;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.util.Log;
import android.view.View;
import android.widget.RadioButton;
import android.widget.TextView;
import android.widget.Toast;

import com.andrew.examsapp.eerydictionary.MainDrawerActivity;
import com.andrew.examsapp.eerydictionary.R;
import com.andrew.examsapp.eerydictionary.library.JSONParser;
import com.andrew.examsapp.eerydictionary.library.NetworkCheck;
import com.andrew.examsapp.eerydictionary.library.SessionManager;
import com.andrew.examsapp.eerydictionary.library.UserFunctions;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

public class MainUserActivity extends ActionBarActivity implements LoginFragment.OnForgotIntentClicked, LoginFragment.OnLoginClicked,
		RegisterFragment.OnRegisterClicked, ForgotFragment.OnForgotPasswordClicked {

	private Fragment f;

	private RadioButton radioLogin;
	private RadioButton radioRegister;

	private TextView ncErr;
	private NetworkCheck nc;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main_user);

		ncErr = (TextView) findViewById(R.id.ncErr);
		nc = new NetworkCheck(this);
		nc.setResponse(ncErr);

		radioLogin = (RadioButton) findViewById(R.id.radioLogin);
		radioRegister = (RadioButton) findViewById(R.id.radioRegister);
		f = new LoginFragment();
		changeFragment();
	}


	public void onRadioButtonClicked(View view) {
		switch (view.getId()) {
			case R.id.radioLogin:
				nc.setResponse(ncErr);
				f = new LoginFragment();
				changeFragment();
				break;
			case R.id.radioRegister:
				nc.setResponse(ncErr);
				f = new RegisterFragment();
				changeFragment();
				break;
		}
	}


	@Override
	public void passwordForgotIntent() {
		nc.setResponse(ncErr);
		radioLogin.setChecked(false);
		radioRegister.setChecked(false);
		f = new ForgotFragment();
		changeFragment();
	}

	@Override
	public void login(String username, String password) {
		if (nc.setResponse(ncErr))
			new LoginAsyncTask().execute(username, password);
	}

	@Override
	public void register(String username, String email, String password) {
		if (nc.setResponse(ncErr)) {
			new RegisterAsyncTask().execute(username, email, password);
			new LoginAsyncTask().execute(username, password);
		}
	}

	@Override
	public void passwordForgot(String email) {
		if (nc.setResponse(ncErr))
			new ForgotPasswordAsyncTask().execute(email);
	}

	private void changeFragment() {
		getFragmentManager().beginTransaction()
				.replace(R.id.frameContainer, f)
				.commit();
		if (f instanceof LoginFragment) {
			radioLogin.setChecked(true);
		} else if (f instanceof RegisterFragment) {
			radioRegister.setChecked(true);
		}
	}


	/*
	* The LoginAsyncTask lets the user login and sets the Session to get the user logged in
	* */
	private class LoginAsyncTask extends AsyncTask<String, Void, JSONArray> {
		private ProgressDialog progressDialog;

		@Override
		protected JSONArray doInBackground(String... params) {
			String username = params[0];
			String password = params[1];
			return UserFunctions.loginUser(username, password);
		}

		@Override
		protected void onPreExecute() {
			super.onPreExecute();
			progressDialog = new ProgressDialog(MainUserActivity.this);
			progressDialog.setMessage("Logging in...");
			progressDialog.setIndeterminate(false);
			progressDialog.setCancelable(true);
			progressDialog.show();
		}

		@Override
		protected void onPostExecute(JSONArray jsonArray) {
			super.onPostExecute(jsonArray);
			try {
				int success = jsonArray.getJSONObject(0).getInt(JSONParser.SUCCESS_TAG);
				if (success == 1) {
					JSONObject jsonUser = jsonArray.getJSONObject(0).getJSONObject(JSONParser.USER_TAG);

					SessionManager sm = new SessionManager(MainUserActivity.this);
					sm.createLoginSession(
							jsonUser.getString(UserFunctions.KEY_USERNAME),
							jsonUser.getString(UserFunctions.KEY_EMAIL),
							jsonUser.getString(UserFunctions.KEY_USER_ID),
							jsonUser.getString(UserFunctions.KEY_CREATED_AT)
					);
					Intent intent = new Intent(MainUserActivity.this, MainDrawerActivity.class);
					/*
					If set, and the activity being launched is already running in the current task,
					then instead of launching a new instance of that activity, all of the other activities on top of it will be closed
					and this Intent will be delivered to the (now on top) old activity as a new Intent.
					 */
					intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
					startActivity(intent);
					finish();
				} else if (success == 0) {
					String jsonMessage = jsonArray.getJSONObject(0).getString(JSONParser.MESSAGE_TAG);
					Toast.makeText(MainUserActivity.this, jsonMessage, Toast.LENGTH_LONG).show();
				}
			} catch (JSONException e) {
				Log.e("JSON_ERROR", "Failed to get something " + e.toString());
			}
			progressDialog.dismiss();
		}
	}


	/*
	* The RegisterAsyncTask is used to register a new user
	* */
	private class RegisterAsyncTask extends AsyncTask<String, Void, JSONArray> {

		private ProgressDialog progressDialog;

		@Override
		protected JSONArray doInBackground(String... params) {
			String username = params[0];
			String email = params[1];
			String password = params[2];
			return UserFunctions.registerUser(username, email, password);
		}

		@Override
		protected void onPreExecute() {
			super.onPreExecute();
			progressDialog = new ProgressDialog(MainUserActivity.this);
			progressDialog.setMessage("Registering...");
			progressDialog.setIndeterminate(false);
			progressDialog.setCancelable(true);
			progressDialog.show();
		}

		@Override
		protected void onPostExecute(JSONArray jsonArray) {
			super.onPostExecute(jsonArray);
			try {
				int success = jsonArray.getJSONObject(0).getInt(JSONParser.SUCCESS_TAG);
				if (success == 1) {
					JSONObject jsonUser = jsonArray.getJSONObject(0).getJSONObject(JSONParser.USER_TAG);

					SessionManager sm = new SessionManager(MainUserActivity.this);
					//removes all the previous data in SharedPreferences
					sm.logoutUser(false);
					sm.createLoginSession(
							jsonUser.getString(UserFunctions.KEY_USERNAME),
							jsonUser.getString(UserFunctions.KEY_EMAIL),
							jsonUser.getString(UserFunctions.KEY_USER_ID),
							jsonUser.getString(UserFunctions.KEY_CREATED_AT));

					String jsonMessage = jsonArray.getJSONObject(0).getString(JSONParser.MESSAGE_TAG);
					Toast.makeText(MainUserActivity.this, jsonMessage, Toast.LENGTH_LONG).show();
					finish();
				} else if (success == 0) {
					String jsonMessage = jsonArray.getJSONObject(0).getString(JSONParser.MESSAGE_TAG);
					Toast.makeText(MainUserActivity.this, jsonMessage, Toast.LENGTH_LONG).show();
				}
			} catch (JSONException e) {
				Log.e("JSON_ERROR", "Failed to get something " + e.toString());
			}
			progressDialog.dismiss();
		}
	}


	/*
	* The ForgotPasswordAsyncTask is going to send a random password to the email indicated and set it as the current password,
	* then the user will be able to log in and change the password with a new one
	* */
	private class ForgotPasswordAsyncTask extends AsyncTask<String, Void, JSONArray> {

		private ProgressDialog progressDialog;

		@Override
		protected JSONArray doInBackground(String... params) {
			String email = params[0];
			return UserFunctions.forgotPassword(email);
		}

		@Override
		protected void onPreExecute() {
			super.onPreExecute();
			progressDialog = new ProgressDialog(MainUserActivity.this);
			progressDialog.setMessage("Getting data...");
			progressDialog.setIndeterminate(false);
			progressDialog.setCancelable(true);
			progressDialog.show();
		}

		@Override
		protected void onPostExecute(JSONArray jsonArray) {
			super.onPostExecute(jsonArray);
			try {
				int success = jsonArray.getJSONObject(0).getInt(JSONParser.SUCCESS_TAG);
				String jsonMessage = jsonArray.getJSONObject(0).getString(JSONParser.MESSAGE_TAG);
				Toast.makeText(MainUserActivity.this, jsonMessage, Toast.LENGTH_LONG).show();
				if (success == 1) {
					f = new LoginFragment();
					changeFragment();
				}
			} catch (JSONException e) {
				Log.e("JSON_ERROR", "Failed to get something " + e.toString());
			}
			progressDialog.dismiss();
		}
	}
}
