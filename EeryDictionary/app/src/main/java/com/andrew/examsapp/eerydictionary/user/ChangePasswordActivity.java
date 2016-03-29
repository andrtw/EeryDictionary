package com.andrew.examsapp.eerydictionary.user;

import android.app.ProgressDialog;
import android.content.EntityIterator;
import android.content.Intent;
import android.graphics.Typeface;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.text.method.PasswordTransformationMethod;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.view.inputmethod.EditorInfo;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.andrew.examsapp.eerydictionary.MainDrawerActivity;
import com.andrew.examsapp.eerydictionary.R;
import com.andrew.examsapp.eerydictionary.library.JSONParser;
import com.andrew.examsapp.eerydictionary.library.NetworkCheck;
import com.andrew.examsapp.eerydictionary.library.SessionManager;
import com.andrew.examsapp.eerydictionary.library.UserFunctions;
import com.andrew.examsapp.eerydictionary.model.SimpleUser;

import org.json.JSONArray;
import org.json.JSONException;

public class ChangePasswordActivity extends ActionBarActivity {

	private EditText changePswEditText;
	private TextView ncErr;
	private NetworkCheck nc;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_change_password);
		getSupportActionBar().setDisplayHomeAsUpEnabled(true);

		ncErr = (TextView) findViewById(R.id.ncErr);
		nc = new NetworkCheck(this);
		nc.setResponse(ncErr);

		changePswEditText = (EditText) findViewById(R.id.changePswEditText);
		changePswEditText.setTypeface(Typeface.DEFAULT);
		changePswEditText.setTransformationMethod(new PasswordTransformationMethod());
		changePswEditText.setOnEditorActionListener(new TextView.OnEditorActionListener() {
			@Override
			public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
				if(actionId == EditorInfo.IME_ACTION_DONE) {
					changePswButtonClicked();
					return true;
				}
				return false;
			}
		});

		Button changePswButton = (Button) findViewById(R.id.changePswButton);
		changePswButton.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				changePswButtonClicked();
			}
		});
	}

	private void changePswButtonClicked() {
		if (nc.setResponse(ncErr))
			new ChangePasswordAsyncTask().execute();
	}


	private class ChangePasswordAsyncTask extends AsyncTask<Void, Void, JSONArray> {

		private ProgressDialog progressDialog;

		@Override
		protected JSONArray doInBackground(Void... params) {
			String newPsw = changePswEditText.getText().toString();
			SessionManager sm = new SessionManager(ChangePasswordActivity.this);
			SimpleUser user = sm.getUserDetails();
			return UserFunctions.changePassword(newPsw, user.getEmail());
		}

		@Override
		protected void onPreExecute() {
			super.onPreExecute();
			progressDialog = new ProgressDialog(ChangePasswordActivity.this);
			progressDialog.setMessage("Getting data...");
			progressDialog.setIndeterminate(false);
			progressDialog.setCancelable(true);
			progressDialog.show();
		}

		@Override
		protected void onPostExecute(JSONArray jsonArray) {
			super.onPostExecute(jsonArray);
			progressDialog.dismiss();
			try {
				int success = jsonArray.getJSONObject(0).getInt(JSONParser.SUCCESS_TAG);
				String jsonMessage = jsonArray.getJSONObject(0).getString(JSONParser.MESSAGE_TAG);
				Toast.makeText(ChangePasswordActivity.this, jsonMessage, Toast.LENGTH_LONG).show();
				if (success == 1) {
					Intent i = new Intent(ChangePasswordActivity.this, MainDrawerActivity.class);
					i.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
					startActivity(i);
					finish();
				}
			} catch (JSONException e) {
				Log.e("JSON_ERROR", "Failed to get something " + e.toString());
			}
		}
	}
}
