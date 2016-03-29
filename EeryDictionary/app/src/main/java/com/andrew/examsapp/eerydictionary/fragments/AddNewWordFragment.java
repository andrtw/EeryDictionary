package com.andrew.examsapp.eerydictionary.fragments;


import android.app.Activity;
import android.app.Fragment;
import android.app.ProgressDialog;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.andrew.examsapp.eerydictionary.MainDrawerActivity;
import com.andrew.examsapp.eerydictionary.R;
import com.andrew.examsapp.eerydictionary.library.JSONParser;
import com.andrew.examsapp.eerydictionary.library.NetworkCheck;
import com.andrew.examsapp.eerydictionary.library.SessionManager;
import com.andrew.examsapp.eerydictionary.library.WordFunctions;

import org.json.JSONArray;
import org.json.JSONException;


public class AddNewWordFragment extends Fragment implements View.OnFocusChangeListener {

	private Activity activity;
	private SessionManager sm;
	private EditText wordEditText;
	private EditText descriptionEditText;
	private EditText examplesEditText;

	private TextView ncErr;
	private NetworkCheck nc;

	@Override
	public void onAttach(Activity activity) {
		super.onAttach(activity);
		try {
			this.activity = activity;
		} catch (ClassCastException e) {
			Log.e("CAST_CLASS", e.getMessage());
		}
	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
		View view = inflater.inflate(R.layout.fragment_add_new_word, container, false);

		ncErr = (TextView) view.findViewById(R.id.ncErr);
		nc = new NetworkCheck(activity);
		nc.setResponse(ncErr);

		sm = new SessionManager(activity);

		wordEditText = (EditText) view.findViewById(R.id.wordEditText);
		descriptionEditText = (EditText) view.findViewById(R.id.descriptionEditText);
		examplesEditText = (EditText) view.findViewById(R.id.examplesEditText);
		wordEditText.setOnFocusChangeListener(this);
		descriptionEditText.setOnFocusChangeListener(this);
		examplesEditText.setOnFocusChangeListener(this);

		Button addNewWordButton = (Button) view.findViewById(R.id.addNewWordButton);
		addNewWordButton.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				if(nc.setResponse(ncErr)) {
					new CreateWordAsyncTask().execute();
					((MainDrawerActivity) activity).hideKeyboard(v);
				}
			}
		});

		return view;
	}


	//on gain or lost focus on word, description and examples EditTexts it checks for connection availability
	@Override
	public void onFocusChange(View v, boolean hasFocus) {
		nc.setResponse(ncErr);
	}


	private class CreateWordAsyncTask extends AsyncTask<Void, Void, JSONArray> {

		private ProgressDialog progressDialog;

		@Override
		protected JSONArray doInBackground(Void... params) {
			String word = wordEditText.getText().toString();
			String description = descriptionEditText.getText().toString();
			String examples = examplesEditText.getText().toString();

			JSONArray json = WordFunctions.createWord(sm.getUserDetails().getUserId(), word, description, examples);
			Log.i("CREATED_JSON_RESPONSE", json.toString());
			return json;
		}

		/*
		* Before starting background thread
		* Runs on the UI thread before doInBackground(Params...)
		* */
		@Override
		protected void onPreExecute() {
			super.onPreExecute();
			//show progress dialog
			progressDialog = new ProgressDialog(activity);
			progressDialog.setMessage("Creating word...");
			progressDialog.setIndeterminate(false);
			progressDialog.setCancelable(true);
			progressDialog.show();
		}

		/*
		* After completing background task
		* */
		@Override
		protected void onPostExecute(JSONArray jsonArray) {
			super.onPostExecute(jsonArray);
			//dismiss the dialog once done
			progressDialog.dismiss();
			try {
				int success = jsonArray.getJSONObject(0).getInt(JSONParser.SUCCESS_TAG);
				String jsonMessage = jsonArray.getJSONObject(0).getString(JSONParser.MESSAGE_TAG);
				Toast.makeText(activity, jsonMessage, Toast.LENGTH_SHORT).show();
				if (success == 1) {
					((MainDrawerActivity) activity).selectItem(1);
				}
			} catch (JSONException e) {
				Log.e("JSON_ERROR", "Failed to get something " + e.toString());
			}
		}
	}
}
