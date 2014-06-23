package com.thanksandroid.example.httppost;

import android.app.Activity;
import android.app.ProgressDialog;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.View;
import android.widget.Toast;

public class MainActivity extends Activity {
	
	protected ProgressDialog progressDialog;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);
	}	

	// called on click of Send Request button
	public void sendRequest(View view) {
		// create an async task
		HttpPostTask task = new HttpPostTask();
		// form param keys
		String[] keys = new String[]{"param1", "param2"};
		// form param values
		String[] values = new String[]{"value1", "value2"};
		// execute async task
		task.execute(keys, values);
	}

	// async task to make network requests in separate thread
	private class HttpPostTask extends AsyncTask<String[], Void, Response> {
		
		@Override
		protected void onPreExecute() {
			super.onPreExecute();
			// display a ProgressDialog with message
			showProgressDialog("", getString(R.string.please_wait));
		}

		@Override
		protected Response doInBackground(String[]... params) {
			// build url
			String url = Constants.SITE_HOST + Constants.HTTP_POST;
			// call method to initiate HTTP request
			return NetworkHelper.doPost(url, params[0], params[1], getApplicationContext());
		}

		@Override
		protected void onPostExecute(Response result) {
			hideProgressDialog();
			super.onPostExecute(result);
			// show the result
			if (result.getStatusCode() == Constants.OK) {
				showToast(result.getResponseText());
			} else {
				showToast(getString(result.getMessageId()));
			}
		}
	}
	
	protected void showProgressDialog(String title, String message) {
		if(progressDialog != null && progressDialog.isShowing()) {
			progressDialog.dismiss();
		}
		progressDialog = ProgressDialog.show(this, title, message);
	}
	
	protected void hideProgressDialog() {
		if(progressDialog != null && progressDialog.isShowing()) {
			progressDialog.dismiss();
		}
		progressDialog = null;
	}

	protected void showToast(String message) {
		Toast.makeText(this, message, Toast.LENGTH_SHORT).show();
	}
	
	@Override
	protected void onPause() {
		hideProgressDialog();
		super.onPause();
	}
}