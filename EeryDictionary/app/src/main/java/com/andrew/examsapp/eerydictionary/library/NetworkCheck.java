package com.andrew.examsapp.eerydictionary.library;

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.AsyncTask;
import android.view.View;

import java.util.concurrent.ExecutionException;

/*
* Checks whether internet connection if working or not
* */
public class NetworkCheck {

	private Context context;
	private boolean connected = false;

	public NetworkCheck(Context context) {
		this.context = context;
	}

	public boolean setResponse(View ncErr) {
		checkConnection();
		if (connected) ncErr.setVisibility(View.GONE);
		else ncErr.setVisibility(View.VISIBLE);
		return connected;
	}

	public boolean isConnected() {
		return connected;
	}

	private void checkConnection() {
		try {
			new NetworkCheckAsyncTask().execute().get();
		} catch (InterruptedException e) {
			e.printStackTrace();
		} catch (ExecutionException e) {
			e.printStackTrace();
		}
	}



	private class NetworkCheckAsyncTask extends AsyncTask<Void, Void, Void> {
		@Override
		protected Void doInBackground(Void... params) {
			//gets current device state
			ConnectivityManager cm = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
			NetworkInfo networkInfo = cm.getActiveNetworkInfo();

			if (networkInfo != null && networkInfo.getState() == NetworkInfo.State.CONNECTED) {
				connected = true;
			} else {
				networkInfo = cm.getNetworkInfo(1);
				if (networkInfo != null && networkInfo.getState() == NetworkInfo.State.CONNECTED)
					connected = true;
			}
			return null;
		}
	}

}
