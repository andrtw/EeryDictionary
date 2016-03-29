package com.andrew.examsapp.eerydictionary.user;


import android.app.Activity;
import android.app.Fragment;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.EditorInfo;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.andrew.examsapp.eerydictionary.R;


public class ForgotFragment extends Fragment {

	private OnForgotPasswordClicked onForgotPasswordClicked;

	@Override
	public void onAttach(Activity activity) {
		super.onAttach(activity);
		onForgotPasswordClicked = (OnForgotPasswordClicked) activity;
	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
		View view = inflater.inflate(R.layout.fragment_forgot, container, false);

		final EditText emailForgot = (EditText) view.findViewById(R.id.emailForgotEditText);
		emailForgot.setOnEditorActionListener(new TextView.OnEditorActionListener() {
			@Override
			public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
				if(actionId == EditorInfo.IME_ACTION_DONE) {
					onForgotPasswordClicked.passwordForgot(emailForgot.getText().toString());
					return true;
				}
				return false;
			}
		});

		Button forgotPasswordButton = (Button) view.findViewById(R.id.forgotPasswordButton);
		forgotPasswordButton.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				onForgotPasswordClicked.passwordForgot(emailForgot.getText().toString());
			}
		});

		return view;
	}


	public interface OnForgotPasswordClicked {
		void passwordForgot(String email);
	}


}
