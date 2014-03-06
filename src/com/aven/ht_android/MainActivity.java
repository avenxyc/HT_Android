package com.aven.ht_android;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;

public class MainActivity extends Activity {
	
	 private static final int RESULT_SETTINGS = 1;

	 public final static String result = "result";
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);
		
		
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.main, menu);
		return true;
	}
	
	@Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
 
        case R.id.action_settings:
            Intent i = new Intent(this, UserSettingsActivity.class);
            startActivityForResult(i, RESULT_SETTINGS);
            break;
 
        }
 
        return true;
    }

	public void launchCamera (View view){
		 Intent intent = new Intent("com.google.zxing.client.android.SCAN");
	     startActivityForResult(intent, 0);
	}
	
	@Override
	public void onActivityResult(int requestCode, int resultCode, Intent intent) {
	    if (requestCode == 0) {
	        if (resultCode == RESULT_OK) {
	            String contents = intent.getStringExtra("SCAN_RESULT");

	            Intent passValue = new Intent(this, CameraActivity.class);
	            passValue.putExtra(result, contents);
	            startActivity(passValue);
	            
	        } else if (resultCode == RESULT_CANCELED) {
	            // Handle cancel
	        	Log.i("fail", "the barcode scanner failed to fetch");
	        }
	    }
	}
	
	
}
