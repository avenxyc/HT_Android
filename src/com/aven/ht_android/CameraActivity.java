package com.aven.ht_android;


import java.io.InputStream;
import java.util.ArrayList;
import java.util.HashMap;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import android.annotation.TargetApi;
import android.app.Activity;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.support.v4.app.NavUtils;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.SimpleAdapter;
import android.widget.Spinner;
import android.widget.TextView;



public class CameraActivity extends Activity {
	
	//Default query url
	private static String basic_url = "http://falcon.acadiau.ca/~112574x/HT/HT_android_connection.php";
	
	//Define message
	public final static String msg = "message";
	//Define Region
	public final static String rgn = "region";
	Button searchButton;
	ListView list;
    TextView cname;
    TextView weight;
    ArrayList<HashMap<String, String>> oslist = new ArrayList<HashMap<String, String>>();
	
	JSONParser jParser = new JSONParser();

	
	
	
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		
		// Get the message from the intent
	    Intent intent = getIntent();
	    final String message = intent.getStringExtra(MainActivity.result);
	    
	    
	    //TODO
		final Intent i = new Intent(getApplicationContext(), ItemList.class);
		i.putExtra(msg, message);
		
		
		setContentView(R.layout.activity_camera);
		// Show the Up button in the action bar.
		setupActionBar();
		
		/*//Create a textview
		TextView displayCode = (TextView)findViewById(R.id.viewtext);
	    displayCode.setText("Please choose your region");	*/
	    
	    Spinner spinner = (Spinner) findViewById(R.id.region);
	    // Create an ArrayAdapter using the string array and a default spinner layout
        ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(this,
 	         R.array.region, android.R.layout.simple_spinner_item);
	    // Specify the layout to use when the list of choices appears
	    adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
	    // Apply the adapter to the spinner
	    spinner.setAdapter(adapter);
	    
	    spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
	        public void onItemSelected(AdapterView<?> parent, View view, int pos, long id) {
	            String region = (String)parent.getItemAtPosition(pos);	  
	            i.putExtra(rgn, region);
	        }
	        public void onNothingSelected(AdapterView<?> parent) {
	        	//TODO
	        }
	    });
	    
	    searchButton = (Button)findViewById(R.id.searchBtn);
	    searchButton.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
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
