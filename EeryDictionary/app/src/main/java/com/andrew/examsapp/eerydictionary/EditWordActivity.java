package com.andrew.examsapp.eerydictionary;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.andrew.examsapp.eerydictionary.library.JSONParser;
import com.andrew.examsapp.eerydictionary.library.NetworkCheck;
import com.andrew.examsapp.eerydictionary.library.WordFunctions;
import com.andrew.examsapp.eerydictionary.model.WordListAdapter;

import org.json.JSONArray;
import org.json.JSONException;


public class EditWordActivity extends ActionBarActivity implements View.OnFocusChangeListener {

	private EditText editDescriptionEditText;
	private EditText editExamplesEditText;
	private int id;
	private String description, examples;

	private TextView ncErr;
	private NetworkCheck nc;


	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_edit_word);
		getSupportActionBar().setDisplayHomeAsUpEnabled(true);

		ncErr = (TextView) findViewById(R.id.ncErr);
		nc = new NetworkCheck(this);
		nc.setResponse(ncErr);

		Intent intent = getIntent();
		Bundle extras = intent.getExtras();
		id = extras.getInt(WordListAdapter.EXTRA_WORD_ID);
		String word = extras.getString(WordListAdapter.EXTRA_WORD);
		description = extras.getString(WordListAdapter.EXTRA_DESCRIPTION);
		examples = extras.getString(WordListAdapter.EXTRA_EXAMPLES);

		setTitle("Edit \"" + word + "\"");
		editDescriptionEditText = (EditText) findViewById(R.id.editDescriptionEditText);
		editExamplesEditText = (EditText) findViewById(R.id.editExamplesEditText);
		editDescriptionEditText.setText(description);
		editExamplesEditText.setText(examples);

		editDescriptionEditText.setOnFocusChangeListener(this);
		editExamplesEditText.setOnFocusChangeListener(this);
	}


	public void editWordButtonClick(View view) {
		if (nc.setResponse(ncErr))
			new EditWordAsyncTask().execute();
	}

	@Override
	public void onFocusChange(View v, boolean hasFocus) {
		nc.setResponse(ncErr);
	}


	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		getMenuInflater().inflate(R.menu.menu_edit_word, menu);
		return super.onCreateOptionsMenu(menu);
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		switch (item.getItemId()) {
			case R.id.action_reset_description:
				nc.setResponse(ncErr);
				editDescriptionEditText.setText(description);
				return true;
			case R.id.action_reset_examples:
				nc.setResponse(ncErr);
				editExamplesEditText.setText(examples);
				return true;
			default:
				return super.onOptionsItemSelected(item);
		}
	}


	private class EditWordAsyncTask extends AsyncTask<Void, Void, JSONArray> {

		private ProgressDialog progressDialog;

		@Override
		protected JSONArray doInBackground(Void... params) {
			String description = editDescriptionEditText.getText().toString();
			String examples = editExamplesEditText.getText().toString();
			JSONArray json = WordFunctions.updateWord(id, description, examples);
			Log.i("CREATED_JSON_RESPONSE", json.toString());
			return json;
		}

		@Override
		protected void onPreExecute() {
			super.onPreExecute();
			progressDialog = new ProgressDialog(EditWordActivity.this);
			progressDialog.setMessage("Updating word...");
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
				Toast.makeText(EditWordActivity.this, jsonMessage, Toast.LENGTH_SHORT).show();
				if (success == 1) {
					Intent intent = new Intent(EditWordActivity.this, MainDrawerActivity.class);
					startActivity(intent);
					finish();
				}
			} catch (JSONException e) {
				Log.e("JSON_ERROR", "Failed to get something " + e.getMessage());
			}
		}
	}
}