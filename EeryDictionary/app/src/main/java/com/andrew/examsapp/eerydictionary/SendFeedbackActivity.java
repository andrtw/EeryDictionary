package com.andrew.examsapp.eerydictionary;

import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.andrew.examsapp.eerydictionary.library.JSONParser;
import com.andrew.examsapp.eerydictionary.library.NetworkCheck;
import com.andrew.examsapp.eerydictionary.library.SessionManager;
import com.andrew.examsapp.eerydictionary.library.UserFunctions;

import org.json.JSONArray;
import org.json.JSONException;


public class SendFeedbackActivity extends ActionBarActivity {

	private EditText feedbackEditText;
	private EditText otherType;

	private Spinner typeSpinner;
	private String typeString;
	private String[] feedbackOptions;

	private TextView ncErr;
	private NetworkCheck nc;


	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_send_email);
		getSupportActionBar().setHomeButtonEnabled(true);

		ncErr = (TextView) findViewById(R.id.ncErr);
		nc = new NetworkCheck(this);
		nc.setResponse(ncErr);

		feedbackEditText = (EditText) findViewById(R.id.feedbackEditText);
		otherType = (EditText) findViewById(R.id.otherType);
		feedbackOptions = getResources().getStringArray(R.array.feedback_spinner);
		//set up the type Spinner
		typeSpinner = (Spinner) findViewById(R.id.typeSpinner);
		ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(this, R.array.feedback_spinner, android.R.layout.simple_spinner_item);
		adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
		typeSpinner.setAdapter(adapter);
		typeSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
			@Override
			public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
				typeString = typeSpinner.getSelectedItem().toString();
				if (typeString.equals(feedbackOptions[feedbackOptions.length - 1])) {
					otherType.setVisibility(View.VISIBLE);
				} else {
					otherType.setVisibility(View.GONE);
				}
			}

			@Override
			public void onNothingSelected(AdapterView<?> parent) {
			}
		});
	}


	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		getMenuInflater().inflate(R.menu.menu_send_email, menu);
		return true;
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		switch (item.getItemId()) {
			case R.id.action_send:
				if (nc.setResponse(ncErr)) {
					String feedback = feedbackEditText.getText().toString();
					Log.i("SP_TYPE", typeString.equals(feedbackOptions[feedbackOptions.length - 1]) + "");
					if (feedback.equals("") || (typeString.equals(feedbackOptions[feedbackOptions.length - 1]) && otherType.getText().toString().equals(""))) {

						Toast.makeText(SendFeedbackActivity.this, "Fill in every field", Toast.LENGTH_LONG).show();

					} else {
						SessionManager sm = new SessionManager(SendFeedbackActivity.this);

						String type = typeString.equals(feedbackOptions[feedbackOptions.length - 1]) ?
								otherType.getText().toString() : typeString;

						new SendFeedbackAsyncTask().execute(
								String.valueOf(sm.getUserDetails().getUserId()),
								type,
								feedback
						);
					}
				}
				return true;
			case R.id.action_clear_message:
				nc.setResponse(ncErr);
				final AlertDialog.Builder builder = new AlertDialog.Builder(this);
				builder.setMessage(getString(R.string.delete_feedback_message))
						.setCancelable(true)
						.setPositiveButton(R.string.delete_feedback_positive, new DialogInterface.OnClickListener() {
							@Override
							public void onClick(DialogInterface dialog, int which) {
								feedbackEditText.setText("");
							}
						})
						.setNegativeButton(R.string.delete_feedback_negative, new DialogInterface.OnClickListener() {
							@Override
							public void onClick(DialogInterface dialog, int which) {
								dialog.cancel();
							}
						});
				AlertDialog dialog = builder.create();
				dialog.show();
				return true;
			default:
				return super.onOptionsItemSelected(item);
		}
	}


	private class SendFeedbackAsyncTask extends AsyncTask<String, Void, JSONArray> {
		private ProgressDialog progressDialog;

		@Override
		protected JSONArray doInBackground(String... params) {
			int user_id = Integer.parseInt(params[0]);
			String feedback_type = params[1];
			String feedback = params[2];
			return UserFunctions.sendFeedback(user_id, feedback_type, feedback);
		}

		@Override
		protected void onPreExecute() {
			super.onPreExecute();
			progressDialog = new ProgressDialog(SendFeedbackActivity.this);
			progressDialog.setMessage("Sending your feedback...");
			progressDialog.setIndeterminate(false);
			progressDialog.setCancelable(true);
			progressDialog.show();
		}

		@Override
		protected void onPostExecute(JSONArray jsonArray) {
			super.onPostExecute(jsonArray);
			try {
				String message = jsonArray.getJSONObject(0).getString(JSONParser.MESSAGE_TAG);
				Toast.makeText(SendFeedbackActivity.this, message, Toast.LENGTH_LONG).show();
				finish();
			} catch (JSONException e) {
				Log.e("JSON_ERROR", "Failed to get something " + e.toString());
			}
			progressDialog.dismiss();
		}
	}
}
