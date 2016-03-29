package com.andrew.examsapp.eerydictionary.model;


import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.andrew.examsapp.eerydictionary.R;

import java.util.ArrayList;

public class WordListAdapter extends BaseAdapter {

	public static final String EXTRA_WORD_ID = "word_id";
	public static final String EXTRA_USER_ID = "user_id";
	public static final String EXTRA_WORD = "word";
	public static final String EXTRA_DESCRIPTION = "description";
	public static final String EXTRA_EXAMPLES = "examples";

	private LayoutInflater inflater;
	private ArrayList<SimpleWord> wordsList;


	public WordListAdapter(Context context, ArrayList<SimpleWord> wordsList) {
		inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
		this.wordsList = wordsList;
	}

	@Override
	public int getCount() {
		return wordsList.size();
	}

	@Override
	public Object getItem(int position) {
		return wordsList.get(position);
	}

	@Override
	public long getItemId(int position) {
		return position;
	}

	static class ViewHolder {
		TextView wordTextView;
		TextView dateTextView;
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		ViewHolder holder;
		//inflate a new view only when convertView is null, otherwise it reuses it
		if (convertView == null) {
			convertView = inflater.inflate(R.layout.word_list_item, parent, false);
			//store references to the children views
			holder = new ViewHolder();
			holder.wordTextView = (TextView) convertView.findViewById(R.id.wordTextView);
			holder.dateTextView = (TextView) convertView.findViewById(R.id.dateTextView);
			convertView.setTag(holder);
		} else {
			//get the holder back to fast access to TextViews
			holder = (ViewHolder) convertView.getTag();
		}
		//interchange background color
		//convertView.setBackgroundColor((position % 2 == 0) ? Color.parseColor("#c9c9c9") : Color.parseColor("#dcdcdc"));
		SimpleWord simpleWord = (SimpleWord) getItem(position);

		holder.wordTextView.setText(simpleWord.getWord());
		holder.dateTextView.setText(simpleWord.getCreatedAt());
		return convertView;
	}


	/*
	private class GetUserDetailsAsyncTask extends AsyncTask<Integer, Void, Void> {
		@Override
		protected Void doInBackground(Integer... params) {
			JSONArray jsonArray = UserFunctions.getUserByUserId(params[0]);
			try {
				int success = jsonArray.getJSONObject(0).getInt(JSONParser.SUCCESS_TAG);
				if (success == 1) {
					JSONObject jsonUser = jsonArray.getJSONObject(0).getJSONObject(JSONParser.USER_TAG);
					userWhoAdded = new SimpleUser();
					userWhoAdded.setUsername(jsonUser.getString(UserFunctions.KEY_USERNAME));
					userWhoAdded.setEmail(jsonUser.getString(UserFunctions.KEY_EMAIL));
					userWhoAdded.setCreatedAt(jsonUser.getString(UserFunctions.KEY_CREATED_AT));
				} else if (success == 0) {
					String jsonMessage = jsonArray.getJSONObject(0).getString(JSONParser.MESSAGE_TAG);
					Toast.makeText(context, jsonMessage, Toast.LENGTH_SHORT).show();
				}
			} catch (JSONException e) {
				Log.e("JSON_ERROR", "Failed to get something " + e.toString());
			}
			return null;
		}
	}
	*/
}