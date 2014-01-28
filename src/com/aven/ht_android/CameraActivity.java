package com.aven.ht_android;


import org.json.JSONException;
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
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.Spinner;
import android.widget.TextView;



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
	    
	    Spinner spinner = (Spinner) findViewById(R.id.Region);
	    // Create an ArrayAdapter using the string array and a default spinner layout
        ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(this,
 	         R.array.Region, android.R.layout.simple_spinner_item);
	    // Specify the layout to use when the list of choices appears
	    adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
	    // Apply the adapter to the spinner
	    spinner.setAdapter(adapter);
	    

	    
	    searchButton = (Button)findViewById(R.id.searchBtn);
	    
	    searchButton.setOnClickListener(new View.OnClickListener() {
	
			@Override
			public void onClick(View v) {
				//TODO
				//Intent i = new Intent(getApplicationContext(), ItemDetail.class);
				//i.putExtra(msg, message);
				
				
				String url = basic_url + "?upccode=";
				
				Log.e("mtest", "here");
				
				
				
				//inner class
				class MyRetreiveFeedTask extends AsyncTask<String, Void, JSONObject> {
					private Exception exception;
					public JSONObject json;
					
					
					protected void onPostExecute(JSONObject result){
						Log.e("mtest","on finish");
						TextView productName = (TextView)findViewById(R.id.product_name);
						try {
							productName.setText(result.getString("product_name"));
						} catch (JSONException e) {
							// TODO Auto-generated catch block
							e.printStackTrace();
						}
						
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

				new MyRetreiveFeedTask().execute(url, message);
				
				//MyRetreiveFeedTask aTask = new MyRetreiveFeedTask();
				
					
				//JSONObject re = retreive.getJason();
				
			
				
				//startActivity(i);
			
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
