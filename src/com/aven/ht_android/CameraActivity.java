package com.aven.ht_android;

import java.util.concurrent.ExecutionException;

import org.json.JSONObject;

import android.annotation.TargetApi;
import android.app.Activity;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.support.v4.app.NavUtils;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

public interface OnTaskCompleteListener { 
    void onTaskComplete(String result); 
}

public class CameraActivity extends Activity {
	
	//Default query url
	private static String basic_url = "http://falcon.acadiau.ca/~112574x/HT/HT_android_connection.php";
	
	//Define message
	public final static String msg = "message";
	Button searchButton;
	
	JSONParser jParser = new JSONParser();

	
	
	
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		
		// Get the message from the intent
	    Intent intent = getIntent();
	    final String message = intent.getStringExtra(MainActivity.result);
	     
		setContentView(R.layout.activity_camera);
		// Show the Up button in the action bar.
		setupActionBar();
		
		//Create a textview
		TextView displayCode = (TextView)findViewById(R.id.viewtext);
	    displayCode.setText(message);	
	    

	    
	    searchButton = (Button)findViewById(R.id.searchBtn);
	    
	    searchButton.setOnClickListener(new View.OnClickListener() {
	
			@Override
			public void onClick(View v) {
				//TODO
				Intent i = new Intent(getApplicationContext(), ItemDetail.class);
				i.putExtra(msg, message);
				
				
				String url = basic_url + "?upccode=";
				
				Log.e("mtest", "here");
				
				
				AsyncTask<String, Void, JSONObject> re = new RetreiveFeedTask().execute(url, message);
				
				try {
					JSONObject jsonObj = re.get();
				} catch (InterruptedException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				} catch (ExecutionException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
					
				//JSONObject re = retreive.getJason();
				int a =1;
				
			
				
				startActivity(i);
			
			}
		});
		
		
	}

	/**
	 * Set up the {@link android.app.ActionBar}, if the API is available.
	 */
	@TargetApi(Build.VERSION_CODES.HONEYCOMB)
	private void setupActionBar() {
		if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.HONEYCOMB) {
			getActionBar().setDisplayHomeAsUpEnabled(true);
		}
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.camera, menu);
		return true;
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		switch (item.getItemId()) {
		case android.R.id.home:
			// This ID represents the Home or Up button. In the case of this
			// activity, the Up button is shown. Use NavUtils to allow users
			// to navigate up one level in the application structure. For
			// more details, see the Navigation pattern on Android Design:
			//
			// http://developer.android.com/design/patterns/navigation.html#up-vs-back
			//
			NavUtils.navigateUpFromSameTask(this);
			return true;
		}
		return super.onOptionsItemSelected(item);
	}

}
