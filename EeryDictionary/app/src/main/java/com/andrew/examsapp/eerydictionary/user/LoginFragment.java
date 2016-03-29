package com.andrew.examsapp.eerydictionary.user;


import android.app.Activity;
import android.app.Fragment;
import android.graphics.Typeface;
import android.os.Bundle;
import android.text.method.PasswordTransformationMethod;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.EditorInfo;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.andrew.examsapp.eerydictionary.R;


public class LoginFragment extends Fragment {

	private OnForgotIntentClicked onForgotIntentClicked;
	private OnLoginClicked onLoginClicked;


	@Override
	public void onAttach(Activity activity) {
		super.onAttach(activity);
		onForgotIntentClicked = (OnForgotIntentClicked) activity;
		onLoginClicked = (OnLoginClicked) activity;
	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
		View view = inflater.inflate(R.layout.fragment_login, container, false);

		final EditText username = (EditText) view.findViewById(R.id.usernameEditText);
		//set correct password font and behaviour
		final EditText password = (EditText) view.findViewById(R.id.passwordEditText);
		password.setOnEditorActionListener(new TextView.OnEditorActionListener() {
			@Override
			public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
				if (actionId == EditorInfo.IME_ACTION_DONE) {
					onLoginClicked.login(username.getText().toString(), password.getText().toString());
					return true;
				}
				return false;
			}
		});
		password.setTypeface(Typeface.DEFAULT);
		password.setTransformationMethod(new PasswordTransformationMethod());

		TextView forgot = (TextView) view.findViewById(R.id.forgotTextView);
		forgot.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				onForgotIntentClicked.passwordForgotIntent();
			}
		});

		Button loginButton = (Button) view.findViewById(R.id.loginButton);
		loginButton.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				onLoginClicked.login(username.getText().toString(), password.getText().toString());
			}
		});

		return view;
	}


	public interface OnForgotIntentClicked {
		void passwordForgotIntent();
	}

	public interface OnLoginClicked {
		void login(String username, String password);
	}

}
