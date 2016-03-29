package com.andrew.examsapp.eerydictionary.fragments;


import android.app.Activity;
import android.app.Fragment;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.andrew.examsapp.eerydictionary.R;
import com.andrew.examsapp.eerydictionary.library.SessionManager;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;


public class UserFragment extends Fragment {

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
		View view = inflater.inflate(R.layout.fragment_user, container, false);

		SessionManager sm = new SessionManager(activity);

		TextView userTextView = (TextView) view.findViewById(R.id.userTextView);
		String username = sm.getUserDetails().getUsername();
		userTextView.setText(username + (username.endsWith("s") ?
				activity.getString(R.string.partial_user_text_no_s) : activity.getString(R.string.partial_user_text_s)));

		TextView myDetailsMember = (TextView) view.findViewById(R.id.myDetailsMember);
		TextView myDetailsWords = (TextView) view.findViewById(R.id.myDetailsWords);
		TextView myDetailsEmail = (TextView) view.findViewById(R.id.myDetailsEmail);

		String date = "";
		try {
			String dbDate = sm.getUserDetails().getCreatedAt();
			Date d = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").parse(dbDate);
			date = new SimpleDateFormat("dd/MM/yyyy").format(d);
		} catch (ParseException e) {
			Log.e("ERROR_MEMBER_SINCE", e.getMessage());
		}
		myDetailsMember.setText(date);

		if (sm.getUserDetails().getWordsCount() > 0) {
			myDetailsWords.setText(String.valueOf(sm.getUserDetails().getWordsCount()));
		} else {
			myDetailsWords.setText("No words yet");
		}

		myDetailsEmail.setText(sm.getUserDetails().getEmail());


		return view;
	}


}
