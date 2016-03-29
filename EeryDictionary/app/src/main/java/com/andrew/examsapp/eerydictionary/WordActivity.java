package com.andrew.examsapp.eerydictionary;

import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.util.Log;
import android.util.TypedValue;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.TableRow;
import android.widget.TextView;
import android.widget.Toast;

import com.andrew.examsapp.eerydictionary.library.JSONParser;
import com.andrew.examsapp.eerydictionary.library.NetworkCheck;
import com.andrew.examsapp.eerydictionary.library.SessionManager;
import com.andrew.examsapp.eerydictionary.library.UserFunctions;
import com.andrew.examsapp.eerydictionary.library.WordFunctions;
import com.andrew.examsapp.eerydictionary.model.SimpleUser;
import com.andrew.examsapp.eerydictionary.model.WordListAdapter;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.concurrent.ExecutionException;


public class WordActivity extends ActionBarActivity {

	public static final String EXTRA_USER_ID = "user_id";
	public static final String EXTRA_USERNAME = "username";
	public static final String EXTRA_EMAIL = "email";
	public static final String EXTRA_CREATED_AT = "created_at";

	private Bundle extras;
	private SessionManager sm;
	private int word_id;
	private SimpleUser user;

	private TextView ncErr;
	private NetworkCheck nc;

	//appreciation
	private TextView likeBar, dislikeBar;
	private Button likeButton, dislikeButton;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_word);
		getSupportActionBar().setDisplayHomeAsUpEnabled(true);

		ncErr = (TextView) findViewById(R.id.ncErr);
		nc = new NetworkCheck(this);
		nc.setResponse(ncErr);

		//referencing
		likeBar = (TextView) findViewById(R.id.likeBar);
		dislikeBar = (TextView) findViewById(R.id.dislikeBar);
		likeButton = (Button) findViewById(R.id.likeButton);
		dislikeButton = (Button) findViewById(R.id.dislikeButton);
		sm = new SessionManager(WordActivity.this);

		//handling like button click
		likeButton.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				if (nc.setResponse(ncErr)) {
					int startLikes = Integer.valueOf(likeButton.getText().toString());
					int startDislikes = Integer.valueOf(dislikeButton.getText().toString());

					if (likeButton.isSelected()) {
						startLikes--;
						likeButton.setSelected(false);
						likeButton.setText(String.valueOf(startLikes));
					} else if (dislikeButton.isSelected()) {
						startLikes++;
						startDislikes--;
						likeButton.setSelected(true);
						dislikeButton.setSelected(false);
						likeButton.setText(String.valueOf(startLikes));
						dislikeButton.setText(String.valueOf(startDislikes));
					} else {
						startLikes++;
						likeButton.setSelected(true);
						likeButton.setText(String.valueOf(startLikes));
					}

					setAppreciationBars(startLikes, startDislikes);
					new SetAppreciationAsyncTask().execute(1);
				}
			}
		});
		//handling dislike button click
		dislikeButton.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				if (nc.setResponse(ncErr)) {
					int startLikes = Integer.valueOf(likeButton.getText().toString());
					int startDislikes = Integer.valueOf(dislikeButton.getText().toString());

					if (dislikeButton.isSelected()) {
						startDislikes--;
						dislikeButton.setSelected(false);
						dislikeButton.setText(String.valueOf(startDislikes));
					} else if (likeButton.isSelected()) {
						startDislikes++;
						startLikes--;
						dislikeButton.setSelected(true);
						likeButton.setSelected(false);
						dislikeButton.setText(String.valueOf(startDislikes));
						likeButton.setText(String.valueOf(startLikes));
					} else {
						startDislikes++;
						dislikeButton.setSelected(true);
						dislikeButton.setText(String.valueOf(startDislikes));
					}

					setAppreciationBars(startLikes, startDislikes);
					new SetAppreciationAsyncTask().execute(0);
				}
			}
		});

		//getting data of the word from intent
		Intent intent = getIntent();
		extras = intent.getExtras();
		word_id = extras.getInt(WordListAdapter.EXTRA_WORD_ID);
		int user_id = extras.getInt(WordListAdapter.EXTRA_USER_ID);
		String word = extras.getString(WordListAdapter.EXTRA_WORD);
		String description = extras.getString(WordListAdapter.EXTRA_DESCRIPTION);
		String examples = extras.getString(WordListAdapter.EXTRA_EXAMPLES);

		//setting title and description and examples text
		setTitle(word);
		TextView descriptionTextView = (TextView) findViewById(R.id.descriptionTextView);
		TextView examplesTextView = (TextView) findViewById(R.id.examplesTextView);
		TextView addedByTextView = (TextView) findViewById(R.id.addedByTextView);
		descriptionTextView.setText(description);
		examplesTextView.setText(examples);

		if (nc.setResponse(ncErr)) {
			new GetAppreciationAsyncTask().execute();

			try {
				new GetUserDetailsAsyncTask().execute(user_id).get();
			} catch (InterruptedException e) {
				e.printStackTrace();
			} catch (ExecutionException e) {
				e.printStackTrace();
			}
			String username = user.getUsername();
			addedByTextView.setText(getString(R.string.added_by_text) + username);
		}

	}


	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		getMenuInflater().inflate(R.menu.menu_word, menu);
		return true;
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		switch (item.getItemId()) {
			case R.id.action_edit:
				Intent intent = new Intent(this, EditWordActivity.class);
				intent.putExtras(extras);
				startActivity(intent);
				return true;
			case R.id.action_view_profile:
				Intent viewProfile = new Intent(this, OtherUserActivity.class);
				//putting in the intent the user_id
				viewProfile.putExtra(EXTRA_USER_ID, user.getUserId());
				viewProfile.putExtra(EXTRA_USERNAME, user.getUsername());
				viewProfile.putExtra(EXTRA_EMAIL, user.getEmail());
				viewProfile.putExtra(EXTRA_CREATED_AT, user.getCreatedAt());
				startActivity(viewProfile);
				return true;
			default:
				return super.onOptionsItemSelected(item);
		}
	}

	private void setButtons(int user_appreciation, int likes, int dislikes) {
		switch (user_appreciation) {
			//none
			case -1:
				likeButton.setSelected(false);
				dislikeButton.setSelected(false);
				break;
			//dislike
			case 0:
				dislikeButton.setSelected(true);
				likeButton.setSelected(false);
				break;
			//like
			case 1:
				likeButton.setSelected(true);
				dislikeButton.setSelected(false);
				break;
		}
		//set the text of the two button as the number of likes and dislikes
		likeButton.setText(String.valueOf(likes));
		dislikeButton.setText(String.valueOf(dislikes));
	}


	//sets the length of the bars in relation of the number of likes and dislikes
	private void setAppreciationBars(int likes, int dislikes) {
		int fromDPtoPX = (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, 3, getResources().getDisplayMetrics());
		TableRow.LayoutParams likeParams = new TableRow.LayoutParams(0, fromDPtoPX, likes);
		TableRow.LayoutParams dislikeParams = new TableRow.LayoutParams(0, fromDPtoPX, dislikes);
		likeBar.setLayoutParams(likeParams);
		dislikeBar.setLayoutParams(dislikeParams);
		//hide bars if no likes and dislikes
		if (likes == 0 && dislikes == 0) {
			likeBar.setVisibility(View.INVISIBLE);
			dislikeBar.setVisibility(View.INVISIBLE);
		} else {
			likeBar.setVisibility(View.VISIBLE);
			dislikeBar.setVisibility(View.VISIBLE);
		}
	}


	private class GetAppreciationAsyncTask extends AsyncTask<Void, Void, JSONArray> {
		@Override
		protected JSONArray doInBackground(Void... params) {
			JSONArray json = WordFunctions.getLikesAndDislikes(word_id, sm.getUserDetails().getUserId());
			Log.i("CREATED_JSON_RESPONSE", json.toString());
			return json;
		}

		@Override
		protected void onPostExecute(JSONArray jsonArray) {
			super.onPostExecute(jsonArray);
			try {
				int success = jsonArray.getJSONObject(0).getInt(JSONParser.SUCCESS_TAG);
				if (success == 1) {
					int user_appreciation = jsonArray.getJSONObject(0).getInt(JSONParser.USER_APPRECIATION_TAG);
					String[] appreciation = jsonArray.getJSONObject(0).getString(JSONParser.APPRECIATION_TAG).split(";");

					int likes = Integer.valueOf(appreciation[0]);
					int dislikes = Integer.valueOf(appreciation[1]);

					setButtons(user_appreciation, likes, dislikes);
					setAppreciationBars(likes, dislikes);
				} else {
					String jsonMessage = jsonArray.getJSONObject(0).getString(JSONParser.MESSAGE_TAG);
					Toast.makeText(WordActivity.this, jsonMessage, Toast.LENGTH_SHORT).show();
				}
			} catch (JSONException e) {
				Log.e("JSON_ERROR", "Failed to get something " + e.toString());
			}
		}
	}


	private class SetAppreciationAsyncTask extends AsyncTask<Integer, Void, JSONArray> {
		@Override
		protected JSONArray doInBackground(Integer... params) {
			//1 = like
			//0 = dislike
			if (params[0] == 1) {
				return WordFunctions.setLikes(word_id, sm.getUserDetails().getUserId());
			} else if (params[0] == 0) {
				return WordFunctions.setDislikes(word_id, sm.getUserDetails().getUserId());
			}
			return null;
		}

		@Override
		protected void onPostExecute(JSONArray jsonArray) {
			super.onPostExecute(jsonArray);
			try {
				int success = jsonArray.getJSONObject(0).getInt(JSONParser.SUCCESS_TAG);
				if (success == 0) {
					String jsonMessage = jsonArray.getJSONObject(0).getString(JSONParser.MESSAGE_TAG);
					Toast.makeText(WordActivity.this, jsonMessage, Toast.LENGTH_SHORT).show();
				}
			} catch (JSONException e) {
				Log.e("JSON_ERROR", "Failed to get something " + e.toString());
			}
		}
	}


	private class GetUserDetailsAsyncTask extends AsyncTask<Integer, Void, Void> {
		@Override
		protected Void doInBackground(Integer... params) {
			JSONArray jsonArray = UserFunctions.getUserByUserId(params[0]);
			try {
				int success = jsonArray.getJSONObject(0).getInt(JSONParser.SUCCESS_TAG);
				if (success == 1) {
					JSONObject jsonUser = jsonArray.getJSONObject(0).getJSONObject(JSONParser.USER_TAG);
					user = new SimpleUser(
							params[0],
							jsonUser.getString(UserFunctions.KEY_USERNAME),
							jsonUser.getString(UserFunctions.KEY_EMAIL),
							jsonUser.getString(UserFunctions.KEY_CREATED_AT));
				} else if (success == 0) {
					String jsonMessage = jsonArray.getJSONObject(0).getString(JSONParser.MESSAGE_TAG);
					Toast.makeText(WordActivity.this, jsonMessage, Toast.LENGTH_SHORT).show();
				}
			} catch (JSONException e) {
				Log.e("JSON_ERROR", "Failed to get something " + e.toString());
			}
			return null;
		}
	}


}