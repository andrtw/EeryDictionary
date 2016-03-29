package com.andrew.examsapp.eerydictionary;

import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.widget.ImageView;


public class HelpActivity extends ActionBarActivity {


	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_help);
		getSupportActionBar().setDisplayHomeAsUpEnabled(true);


		ImageView image = (ImageView) findViewById(R.id.image);
		image.setAlpha(.3f);
	}

}
