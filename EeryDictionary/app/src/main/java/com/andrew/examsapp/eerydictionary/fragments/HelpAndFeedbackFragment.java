package com.andrew.examsapp.eerydictionary.fragments;


import android.app.Activity;
import android.app.Fragment;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import com.andrew.examsapp.eerydictionary.HelpActivity;
import com.andrew.examsapp.eerydictionary.R;
import com.andrew.examsapp.eerydictionary.SendFeedbackActivity;


public class HelpAndFeedbackFragment extends Fragment {

	private Activity activity;

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
		View view = inflater.inflate(R.layout.fragment_help_and_feedback, container, false);

		ListView helpFeedListView = (ListView) view.findViewById(R.id.helpFeedListView);
		ArrayAdapter<String> adapter = new ArrayAdapter<>(activity, android.R.layout.simple_list_item_1, activity.getResources().getStringArray(R.array.help_feedback_items));
		helpFeedListView.setAdapter(adapter);
		helpFeedListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
			@Override
			public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
				switch (position) {
					//help
					case 0:
						Intent help = new Intent(activity, HelpActivity.class);
						startActivity(help);
						break;
					//feedback
					case 1:
						Intent sendFeedback = new Intent(activity, SendFeedbackActivity.class);
						startActivity(sendFeedback);
						break;
				}
			}
		});


		return view;
	}


}
