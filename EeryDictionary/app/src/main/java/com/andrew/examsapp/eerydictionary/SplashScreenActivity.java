package com.andrew.examsapp.eerydictionary;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;

import com.andrew.examsapp.eerydictionary.library.SessionManager;
import com.andrew.examsapp.eerydictionary.user.MainUserActivity;


public class SplashScreenActivity extends Activity {

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_splash_screen);

		new Thread() {
			@Override
			public void run() {
				try {
					int timer = 0;
					while (timer < 2000) {
						sleep(100);
						timer += 100;
					}

					SessionManager sm = new SessionManager(SplashScreenActivity.this);
					boolean isLogged = sm.checkLogin(false);
					Intent intent;
					if (isLogged) {
						intent = new Intent(SplashScreenActivity.this, MainDrawerActivity.class);
					} else {
						intent = new Intent(SplashScreenActivity.this, MainUserActivity.class);
					}
					startActivity(intent);
				} catch (InterruptedException e) {
					Log.e("ERROR_SPLASH_THREAD", e.getMessage());
				} finally {
					finish();
				}
			}
		}.start();
	}
}
