package com.aven.ht_android;


import java.util.ArrayList;
import java.util.HashMap;

import android.annotation.TargetApi;
import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Build;
import android.os.Bundle;
import android.support.v4.app.NavUtils;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.Spinner;
import android.widget.TextView;



public class CameraActivity extends Activity {
	
	//Default query url
	private static String basic_url = "http://falcon.acadiau.ca/~112574x/HT/HT_android_connection.php";
	
	//Define message
	public final static String msg = "message";
	
	//Define Region
	public final static String rgn = "region";
	
	//Variable that holds region value
	String rgn_val;
	
	//Restored region val
	String rgn_saved;
	
	//Preference name
	public static final String PREFS_NAME = "MyPrefsFile";
	//Search Button
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
		
		// Restore preferences
	    SharedPreferences settings = getSharedPreferences(PREFS_NAME, 0);
	    rgn_saved = settings.getString("region", "no_value");
		
	    //If user has set preference
	    if(rgn_saved != "no_value"){
	    	//Create a textview
			TextView displayCode = (TextView)findViewById(R.id.viewtext);
		    displayCode.setText("You have chosen the the region: " + rgn_saved);	
	    } else {
	    	Spinner spinner = (Spinner) findViewById(R.id.region);
	    	
	    	//Change the spinner to visible
	    	spinner.setVisibility(View.VISIBLE);
		    
		    // Create an ArrayAdapter using the string array and a default spinner layout
	        ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(this,
	 	         R.array.region, android.R.layout.simple_spinner_item);
		    
	        // Specify the layout to use when the list of choices appears
		    adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
		     
		    // Apply the adapter to the spinner
		    spinner.setAdapter(adapter);
		    
		    spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
		        public void onItemSelected(AdapterView<?> parent, View view, int pos, long id) {
		        	rgn_val = (String)parent.getItemAtPosition(pos);	  
		            i.putExtra(rgn, rgn_val);
		        }
		        public void onNothingSelected(AdapterView<?> parent) {
		        	//Default value would be "Cape Breton"
		        }
		    });
	    }
	    
	    
	    
	    searchButton = (Button)findViewById(R.id.searchBtn);
	    searchButton.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				if(rgn_saved == "no_value"){
					new AlertDialog.Builder(CameraActivity.this)
				    .setTitle(R.string.region_preference_title)
				    .setMessage(R.string.region_preference_content)
				    .setPositiveButton(android.R.string.yes, new DialogInterface.OnClickListener() {
				        public void onClick(DialogInterface dialog, int which) { 
				        	//Set User preference on region
				        	SharedPreferences settings = getSharedPreferences(PREFS_NAME, 0);
				            SharedPreferences.Editor editor = settings.edit();
				            editor.putString("region", rgn_val );
				            
				            // Commit the edits.
				            editor.commit();
				        
				            startActivity(i);
				        }
					    })
				    .setNegativeButton(android.R.string.no, new DialogInterface.OnClickListener() {
				        public void onClick(DialogInterface dialog, int which) { 
				        	startActivity(i);
				        }
				     })
				    .setIcon(R.drawable.ic_dialog_alert)
				    .show();
				} else {
					i.putExtra(rgn, rgn_val);
					startActivity(i);
				}
					
				
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
