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


public class RegisterFragment extends Fragment {

	private OnRegisterClicked onRegisterClicked;


	@Override
	public void onAttach(Activity activity) {
		super.onAttach(activity);
		onRegisterClicked = (OnRegisterClicked) activity;
	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
		View view = inflater.inflate(R.layout.fragment_register, container, false);

		final EditText username = (EditText) view.findViewById(R.id.usernameEditText);
		final EditText email = (EditText) view.findViewById(R.id.emailEditText);
		final EditText password = (EditText) view.findViewById(R.id.passwordEditText);
		password.setOnEditorActionListener(new TextView.OnEditorActionListener() {
			@Override
			public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
				if(actionId == EditorInfo.IME_ACTION_DONE) {
					onRegisterClicked.register(username.getText().toString(), email.getText().toString(), password.getText().toString());
					return true;
				}
				return false;
			}
		});
		password.setTypeface(Typeface.DEFAULT);
		password.setTransformationMethod(new PasswordTransformationMethod());

		Button registerButton = (Button) view.findViewById(R.id.registerButton);
		registerButton.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				onRegisterClicked.register(username.getText().toString(), email.getText().toString(), password.getText().toString());
			}
		});

		return view;
	}


	public interface OnRegisterClicked {
		void register(String username, String email, String password);
	}


}
