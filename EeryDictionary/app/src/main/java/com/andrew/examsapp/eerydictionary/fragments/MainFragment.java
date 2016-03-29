package com.andrew.examsapp.eerydictionary.fragments;


import android.app.Activity;
import android.app.Fragment;
import android.app.ProgressDialog;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.EditorInfo;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;

import com.andrew.examsapp.eerydictionary.MainDrawerActivity;
import com.andrew.examsapp.eerydictionary.R;
import com.andrew.examsapp.eerydictionary.WordActivity;
import com.andrew.examsapp.eerydictionary.library.JSONParser;
import com.andrew.examsapp.eerydictionary.library.NetworkCheck;
import com.andrew.examsapp.eerydictionary.library.SessionManager;
import com.andrew.examsapp.eerydictionary.library.UserFunctions;
import com.andrew.examsapp.eerydictionary.library.WordFunctions;
import com.andrew.examsapp.eerydictionary.model.SimpleWord;
import com.andrew.examsapp.eerydictionary.model.WordListAdapter;

import org.json.JSONArray;
import org.json.JSONException;


public class MainFragment extends Fragment {

	private static final String KEY_WORD = "word";
	private static final String KEY_EVER_SEARCHED = "ever_searched";

	private Activity activity;
	private EditText wordInput;
	private ListView wordsListView;
	private TextView noWordsFoundButton;

	private TextView ncErr;
	private NetworkCheck nc;

	private boolean everSearched = false;

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
	public void onSaveInstanceState(Bundle outState) {
		super.onSaveInstanceState(outState);
		outState.putString(KEY_WORD, wordInput.getText().toString());
		outState.putBoolean(KEY_EVER_SEARCHED, everSearched);
	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
		View view = inflater.inflate(R.layout.fragment_main, container, false);

		//check the connection
		ncErr = (TextView) view.findViewById(R.id.ncErr);
		nc = new NetworkCheck(activity);
		nc.setResponse(ncErr);


		SessionManager sm = new SessionManager(activity);

		//word EditText
		wordInput = (EditText) view.findViewById(R.id.wordInput);
		wordInput.setOnEditorActionListener(new TextView.OnEditorActionListener() {
			@Override
			public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
				if (actionId == EditorInfo.IME_ACTION_SEARCH) {
					searchWord(v);
					((MainDrawerActivity) activity).hideKeyboard(wordInput);
					return true;
				}
				return false;
			}
		});
		//words ListView
		wordsListView = (ListView) view.findViewById(R.id.wordsListView);
		wordsListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
			@Override
			public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
				SimpleWord sw = (SimpleWord) wordsListView.getAdapter().getItem(position);
				Intent intent = new Intent(activity, WordActivity.class);
				//putting in the intent all the data of the word
				intent.putExtra(WordListAdapter.EXTRA_WORD_ID, sw.getWordId());
				intent.putExtra(WordListAdapter.EXTRA_USER_ID, sw.getUserId());
				intent.putExtra(WordListAdapter.EXTRA_WORD, sw.getWord());
				intent.putExtra(WordListAdapter.EXTRA_DESCRIPTION, sw.getDescription());
				intent.putExtra(WordListAdapter.EXTRA_EXAMPLES, sw.getExamples());
				activity.startActivity(intent);
			}
		});
		//search Button
		final Button searchButton = (Button) view.findViewById(R.id.searchButton);
		searchButton.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				searchWord(v);
				((MainDrawerActivity) activity).hideKeyboard(wordInput);
			}
		});
		//no words found Button
		noWordsFoundButton = (TextView) view.findViewById(R.id.noWordsFoundButton);
		noWordsFoundButton.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				addWord(v);
			}
		});
		if (savedInstanceState != null) {
			boolean isEverSearched = savedInstanceState.getBoolean(KEY_EVER_SEARCHED);
			String isWordSaved = savedInstanceState.getString(KEY_WORD);
			if (!isWordSaved.equals("")) {
				if (nc.setResponse(ncErr) && isEverSearched)
					new GetWordsLikeAsyncTask().execute(isWordSaved);
			}
		}

		//to get the number of words for the UserFragment to avoid slowdowns
		if (nc.setResponse(ncErr))
			new GetUserWordsAsyncTask().execute(sm.getUserDetails().getUserId());

		return view;
	}


	private void searchWord(View view) {
		everSearched = true;
		String wordToSearch = wordInput.getText().toString();
		if(nc.setResponse(ncErr) && !wordToSearch.equals(""))
			new GetWordsLikeAsyncTask().execute(wordToSearch);
	}


	private void addWord(View view) {
		((MainDrawerActivity) activity).selectItem(2);
	}


	private class GetWordsLikeAsyncTask extends AsyncTask<String, Void, JSONArray> {
		private ProgressDialog progressDialog;

		@Override
		protected JSONArray doInBackground(String... params) {
			JSONArray json = WordFunctions.getWordsLike(params[0]);
			Log.i("CREATED_JSON_RESPONSE", json.toString());
			return json;
		}

		@Override
		protected void onPreExecute() {
			super.onPreExecute();
			progressDialog = new ProgressDialog(activity);
			progressDialog.setMessage("Searching...");
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
					noWordsFoundButton.setVisibility(View.GONE);
					WordFunctions.populateListView(activity, wordsListView, jsonArray.getJSONObject(0).getJSONArray(JSONParser.WORD_TAG));
				} else if (success == 0) {
					WordFunctions.clearListView(activity, wordsListView);

					noWordsFoundButton.setVisibility(View.VISIBLE);

					//String jsonMessage = jsonArray.getJSONObject(0).getString(JSONParser.MESSAGE_TAG);
					//Toast.makeText(activity, jsonMessage, Toast.LENGTH_SHORT).show();
				}
			} catch (JSONException e) {
				Log.e("JSON_ERROR", "Failed to get something " + e.getMessage());
			}
			progressDialog.dismiss();
		}
	}

	private class GetUserWordsAsyncTask extends AsyncTask<Integer, Void, Void> {
		@Override
		protected Void doInBackground(Integer... params) {
			JSONArray jsonArray = UserFunctions.getUserWords(params[0]);
			try {
				int success = jsonArray.getJSONObject(0).getInt(JSONParser.SUCCESS_TAG);
				if (success == 1) {
					int wordsCount = jsonArray.getJSONObject(0).getInt(JSONParser.WORDS_COUNT_TAG);
					SessionManager sm = new SessionManager(activity);
					sm.getUserDetails().setWordsCount(wordsCount);
				}
			} catch (JSONException e) {
				Log.e("JSON_ERROR", "Failed to get something " + e.getMessage());
			}
			return null;
		}
	}
}
