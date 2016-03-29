package com.andrew.examsapp.eerydictionary.library;


import android.util.Log;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
import org.apache.http.client.HttpClient;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.protocol.HTTP;
import org.json.JSONArray;
import org.json.JSONException;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.UnknownHostException;
import java.util.List;

public class JSONParser {

	static InputStream in = null;
	static JSONArray jsonArray = null;
	static String json = "";

	public static final String HOST = "http://10.0.2.2";

	//JSON response TAGs from php scripts
	public static final String SUCCESS_TAG = "success";
	public static final String MESSAGE_TAG = "message";
	public static final String WORD_TAG = "word";
	public static final String USER_TAG = "user";
	public static final String APPRECIATION_TAG = "appreciation";
	public static final String USER_APPRECIATION_TAG = "user_appreciation";
	public static final String WORDS_COUNT_TAG = "words_count";

	public JSONParser() {
	}


	public static JSONArray getJsonArray(String url, List<NameValuePair> params) {
		//make HTTP POST request
		HttpClient httpClient = new DefaultHttpClient();
		HttpResponse httpResponse = null;
		HttpEntity httpEntity;
		try {
			HttpPost request = new HttpPost(url);
			request.setEntity(new UrlEncodedFormEntity(params, HTTP.UTF_8));

			httpResponse = httpClient.execute(request);         // <-- throws IOException
			httpEntity = httpResponse.getEntity();
			if (httpEntity != null) {
				in = httpEntity.getContent();
			}
		} catch (IOException e) {
			Log.e("FAILED_POST_REQUEST", "Error making the post request " + e.toString());
		}

		if(httpResponse != null) {

			//convert response to string
			try {
				BufferedReader reader = new BufferedReader(new InputStreamReader(in, HTTP.UTF_8));
				StringBuilder strBuilder = new StringBuilder();
				String line;
				while ((line = reader.readLine()) != null) {
					strBuilder.append(line);
				}
				in.close();
				reader.close();
				//json response as string
				json = strBuilder.toString();
			} catch (IOException e) {
				Log.e("BUFFER_ERROR", "Error converting result " + e.toString());
			}
			//try to parse the string to a JSON object
			try {
				jsonArray = new JSONArray(json);
			} catch (JSONException e) {
				Log.e("ERROR_JSON_TO_ARRAY", "Error converting json string (\"" + json + "\") into JSONArray: " + e.getMessage());
			}
		}
		return jsonArray;
	}

}
