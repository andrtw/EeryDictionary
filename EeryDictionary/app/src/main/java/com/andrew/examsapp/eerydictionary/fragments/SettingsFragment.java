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

import com.andrew.examsapp.eerydictionary.AboutActivity;
import com.andrew.examsapp.eerydictionary.R;
import com.andrew.examsapp.eerydictionary.library.SessionManager;
import com.andrew.examsapp.eerydictionary.user.ChangePasswordActivity;


public class SettingsFragment extends Fragment {

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
		View view = inflater.inflate(R.layout.fragment_settings, container, false);

		ListView settingsListView = (ListView) view.findViewById(R.id.settingsListView);
		final ArrayAdapter<String> adapter = new ArrayAdapter<>(activity, android.R.layout.simple_list_item_1, activity.getResources().getStringArray(R.array.settings_items));
		settingsListView.setAdapter(adapter);
		settingsListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
			@Override
			public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
				Intent i;
				switch (position) {
					//change password
					case 0:
						i = new Intent(activity, ChangePasswordActivity.class);
						startActivity(i);
						break;
					//logout
					case 1:
						SessionManager sm = new SessionManager(activity);
						sm.logoutUser(true);
						activity.finish();
						break;
					//about
					case 2:
						i = new Intent(activity, AboutActivity.class);
						startActivity(i);
						break;
				}
			}
		});

		return view;
	}


}
