package com.andrew.examsapp.eerydictionary;

import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.util.Log;
import android.widget.TextView;

import com.andrew.examsapp.eerydictionary.library.JSONParser;
import com.andrew.examsapp.eerydictionary.library.NetworkCheck;
import com.andrew.examsapp.eerydictionary.library.UserFunctions;
import com.andrew.examsapp.eerydictionary.model.SimpleUser;

import org.json.JSONArray;
import org.json.JSONException;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.concurrent.ExecutionException;


public class OtherUserActivity extends ActionBarActivity {

	private int wordsCount;
	private String messageIfNoWords;


	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_other_user);
		getSupportActionBar().setDisplayHomeAsUpEnabled(true);

		Bundle userDetails = getIntent().getExtras();
		SimpleUser user = new SimpleUser(
				userDetails.getInt(WordActivity.EXTRA_USER_ID),
				userDetails.getString(WordActivity.EXTRA_USERNAME),
				userDetails.getString(WordActivity.EXTRA_EMAIL),
				userDetails.getString(WordActivity.EXTRA_CREATED_AT)
		);

		TextView ncErr = (TextView) findViewById(R.id.ncErr);
		NetworkCheck nc = new NetworkCheck(this);
		if (nc.setResponse(ncErr)) {
			try {
				new GetUserWordsAsyncTask().execute(user.getUserId()).get();
			} catch (InterruptedException e) {
				e.printStackTrace();
			} catch (ExecutionException e) {
				e.printStackTrace();
			}
		}

		TextView otherUserTextView = (TextView) findViewById(R.id.otherUserTextView);
		String username = user.getUsername();
		otherUserTextView.setText(username + (username.endsWith("s") ?
				getString(R.string.partial_user_text_no_s) : getString(R.string.partial_user_text_s)));

		TextView otherDetailsMember = (TextView) findViewById(R.id.otherDetailsMember);
		TextView otherDetailsWords = (TextView) findViewById(R.id.otherDetailsWords);


		String date = "";
		try {
			String dbDate = user.getCreatedAt();
			Date d = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").parse(dbDate);
			date = new SimpleDateFormat("dd/MM/yyyy").format(d);
		} catch (ParseException e) {
			Log.e("ERROR_MEMBER_SINCE", e.getMessage());
		}
		otherDetailsMember.setText(date);

		if (wordsCount > 0) otherDetailsWords.setText(String.valueOf(wordsCount));
		else otherDetailsWords.setText(messageIfNoWords);

	}


	private class GetUserWordsAsyncTask extends AsyncTask<Integer, Void, Void> {
		@Override
		protected Void doInBackground(Integer... params) {
			JSONArray jsonArray = UserFunctions.getUserWords(params[0]);
			try {
				int success = jsonArray.getJSONObject(0).getInt(JSONParser.SUCCESS_TAG);
				if (success == 1) {
					wordsCount = jsonArray.getJSONObject(0).getInt(JSONParser.WORDS_COUNT_TAG);
				} else if (success == 0) {
					messageIfNoWords = jsonArray.getJSONObject(0).getString(JSONParser.MESSAGE_TAG);
				}
			} catch (JSONException e) {
				Log.e("JSON_ERROR", "Failed to get something " + e.getMessage());
			}
			return null;
		}
	}

}
