package com.aven.ht_android;

import org.json.JSONObject;

import android.os.AsyncTask;
import android.util.Log;
import android.widget.TextView;


public class RetreiveFeedTask extends AsyncTask<String, Void, JSONObject> {
	private Exception exception;
	public JSONObject json;

	protected void onPostExecute(JSONObject result){
		Log.e("mtest","on finish");
		
	}
	
	@Override
	protected JSONObject doInBackground(String... params) {
		try {
            
			// getting JSON string from URL
			json = JSONParser.makeHttpRequest(params[0], "GET", params[1]);
			// check your log for json response
			Log.d("Single Product Details", json.toString());
			
			return json;
			
			
			
        } catch (Exception e) {
            this.exception = e;
            return null;
        }
	}

	
}
