package com.aven.ht_android;

import org.json.JSONObject;

import android.content.Context;
import android.os.AsyncTask;
import android.util.Log;
import android.widget.TextView;


public class RetreiveFeedTask extends AsyncTask<String, Void, JSONObject> {
	private Exception exception;
	public JSONObject json;

	public interface MyAsyncTaskListener {
	       void onPreExecuteConcluded();
	       void onPostExecuteConcluded(JSONObject result);  
	    }
	
	private MyAsyncTaskListener mListener;

    final public void setListener(MyAsyncTaskListener listener) {
       mListener = listener;
    }
	
	
    @Override
    final protected void onPreExecute() {
        // common stuff
        
        if (mListener != null) 
            mListener.onPreExecuteConcluded();
    }

    @Override
    final protected void onPostExecute(JSONObject result) {
        // common stuff
        
        if (mListener != null) 
            mListener.onPostExecuteConcluded(result);
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
