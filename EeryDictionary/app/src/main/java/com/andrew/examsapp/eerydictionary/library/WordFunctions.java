package com.andrew.examsapp.eerydictionary.library;


import android.content.Context;
import android.util.Log;
import android.widget.ArrayAdapter;
import android.widget.ListAdapter;
import android.widget.ListView;

import com.andrew.examsapp.eerydictionary.model.SimpleWord;
import com.andrew.examsapp.eerydictionary.model.WordListAdapter;

import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class WordFunctions {

	//URL of PHP APIs
	private static final String GET_WORDS_LIKE_URL = JSONParser.HOST + "/android_connect/get_words_like.php";
	private static final String CREATE_WORD_URL = JSONParser.HOST + "/android_connect/create_word.php";
	private static final String UPDATE_WORD_URL = JSONParser.HOST + "/android_connect/update_word.php";
	private static final String SET_APPRECIATION_WORD_URL = JSONParser.HOST + "/android_connect/set_appreciation.php";
	private static final String GET_APPRECIATION_WORD_URL = JSONParser.HOST + "/android_connect/get_appreciation.php";

	//database KEYs
	private static final String KEY_WORD_ID = "word_id";
	private static final String KEY_USER_ID = "user_id";
	private static final String KEY_WORD = "word";
	private static final String KEY_DESCRIPTION = "description";
	private static final String KEY_EXAMPLES = "examples";
	private static final String KEY_CREATED_AT = "created_at";

	private static final String APPRECIATION_TAG = "appreciation_tag";
	private static final String APPRECIATION_LIKE_TAG = "like";
	private static final String APPRECIATION_DISLIKE_TAG = "dislike";


	public WordFunctions() {
	}


	/*
	* Creates a list containing words to display
	* */
	public static ArrayList<SimpleWord> getListOfWordsByJSONArray(JSONArray jsonArray) {
		ArrayList<SimpleWord> list = new ArrayList<>();
		JSONObject jsonObject;
		for (int i = 0; i < jsonArray.length(); i++) {
			try {
				jsonObject = jsonArray.getJSONObject(i);
				int word_id = jsonObject.getInt(KEY_WORD_ID);
				int user_id = jsonObject.getInt(KEY_USER_ID);
				String word = jsonObject.getString(KEY_WORD);
				String description = jsonObject.getString(KEY_DESCRIPTION);
				String examples = jsonObject.getString(KEY_EXAMPLES);

				String dbDate = jsonObject.getString(KEY_CREATED_AT);
				Date d = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").parse(dbDate);
				String date = new SimpleDateFormat("dd/MM/yyyy").format(d);
				//Log.i("LOG_DATETIME_CONVERTED", dbDate + " / " + d + " / " + date);
				list.add(new SimpleWord(word_id, user_id, word, description, examples, date));
			} catch (JSONException e) {
				Log.e("ERROR_GETTING_DATA", e.getMessage());
			} catch (ParseException e) {
				Log.e("TAG_DATETIME_ERROR", e.getMessage());
			}
		}
		return list;
	}

	/*
	* Populates the listView with the words in the json array
	* */
	public static void populateListView(Context context, ListView listView, JSONArray jsonArray) {
		ArrayList<SimpleWord> list = WordFunctions.getListOfWordsByJSONArray(jsonArray);
		ListAdapter adapter = new WordListAdapter(context, list);
		listView.setAdapter(adapter);
	}

	/*
	* Remove all the views from the wordsListView by setting a new empty adapter
	* */
	public static void clearListView(Context context, ListView listView) {
		listView.setAdapter(new ArrayAdapter<>(context, android.R.layout.simple_list_item_1, new String[]{}));
	}


	/*
	* Method to create a new word
	* */
	public static JSONArray createWord(int user_id, String word, String description, String examples) {
		List<NameValuePair> params = new ArrayList<>();
		params.add(new BasicNameValuePair(UserFunctions.KEY_USER_ID, String.valueOf(user_id)));
		params.add(new BasicNameValuePair(KEY_WORD, word.trim()));
		params.add(new BasicNameValuePair(KEY_DESCRIPTION, description.trim()));
		params.add(new BasicNameValuePair(KEY_EXAMPLES, examples.trim()));
		return JSONParser.getJsonArray(CREATE_WORD_URL, params);
	}

	/*
	* Method to get words like
	* */
	public static JSONArray getWordsLike(String word) {
		List<NameValuePair> params = new ArrayList<>();
		params.add(new BasicNameValuePair(KEY_WORD, word.trim()));
		return JSONParser.getJsonArray(GET_WORDS_LIKE_URL, params);
	}

	/*
	* Method to update a word
	* */
	public static JSONArray updateWord(int id, String description, String examples) {
		List<NameValuePair> params = new ArrayList<>();
		params.add(new BasicNameValuePair(KEY_WORD_ID, String.valueOf(id)));
		params.add(new BasicNameValuePair(KEY_DESCRIPTION, description.trim()));
		params.add(new BasicNameValuePair(KEY_EXAMPLES, examples.trim()));
		return JSONParser.getJsonArray(UPDATE_WORD_URL, params);
	}


	public static JSONArray setLikes(int word_id, int user_id) {
		List<NameValuePair> params = new ArrayList<>();
		params.add(new BasicNameValuePair(APPRECIATION_TAG, APPRECIATION_LIKE_TAG));
		params.add(new BasicNameValuePair(KEY_WORD_ID, String.valueOf(word_id)));
		params.add(new BasicNameValuePair(KEY_USER_ID, String.valueOf(user_id)));
		return JSONParser.getJsonArray(SET_APPRECIATION_WORD_URL, params);
	}


	public static JSONArray setDislikes(int word_id, int user_id) {
		List<NameValuePair> params = new ArrayList<>();
		params.add(new BasicNameValuePair(APPRECIATION_TAG, APPRECIATION_DISLIKE_TAG));
		params.add(new BasicNameValuePair(KEY_WORD_ID, String.valueOf(word_id)));
		params.add(new BasicNameValuePair(KEY_USER_ID, String.valueOf(user_id)));
		return JSONParser.getJsonArray(SET_APPRECIATION_WORD_URL, params);
	}


	public static JSONArray getLikesAndDislikes(int word_id, int user_id) {
		List<NameValuePair> params = new ArrayList<>();
		params.add(new BasicNameValuePair(KEY_WORD_ID, String.valueOf(word_id)));
		params.add(new BasicNameValuePair(KEY_USER_ID, String.valueOf(user_id)));
		return JSONParser.getJsonArray(GET_APPRECIATION_WORD_URL, params);
	}

}
