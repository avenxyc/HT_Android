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
	    
	     
		setContentView(R.layout.activity_camera);
		// Show the Up button in the action bar.
		setupActionBar();
		
		//Create a textview
		TextView displayCode = (TextView)findViewById(R.id.viewtext);
	    displayCode.setText(message);	
	    
	    Spinner spinner = (Spinner) findViewById(R.id.region);
	    // Create an ArrayAdapter using the string array and a default spinner layout
        ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(this,
 	         R.array.region, android.R.layout.simple_spinner_item);
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
				class MyRetreiveFeedTask extends AsyncTask<String, Void, JSONArray> {
					private Exception exception;
					public JSONArray json;
					
					
					protected void onPostExecute(JSONArray json){
						//Display the product image
						String img_url = "http://falcon.acadiau.ca/~112574x/HT/pics/" + message + ".jpg";
						/*try {
							  ImageView iv = (ImageView)findViewById(R.id.pimage);
							  Bitmap bitmap = BitmapFactory.decodeStream((InputStream)new URL(url + message + ".jpg").getContent());
							  
							  String testval = url + message + ".jpg";
							  iv.setImageBitmap(bitmap); 
							} catch (MalformedURLException e) {
							  e.printStackTrace();
							} catch (IOException e) {
							  e.printStackTrace();
							}*/
						// show The Image
						new DownloadImageTask((ImageView) findViewById(R.id.pimage))
						            .execute(img_url);
						
						
						
						//Display item name and weight
						try {
							JSONObject info = json.getJSONObject(1);
							String name = info.getString("product_name");
							TextView tv = (TextView)findViewById(R.id.item_name);
							tv.setText(name);
							
						} catch (JSONException e1) {
							e1.printStackTrace();
						}
						
						//Display constituents information
						HashMap<String, String> title_map = new HashMap<String, String>();
						title_map.put("cname", "Constituent Name:");
						title_map.put("classification", "Where to throw:");
						oslist.add(title_map);
						
						Log.e("mtest","on finish");
						try {
							//Store json to an array
							for(int i=0;i<json.length();i++){
								HashMap<String, String> map = new HashMap<String, String>();
								JSONObject e = json.getJSONObject(i);
								//Storing JSON item to a Variable
								String c_name = e.getString("cname");
								String throw_where = e.getString("classification");
								
								//Add value HashMap key => value
								map.put("cname", c_name );
								map.put("classification", throw_where);
								
								oslist.add(map);
								
								
								list=(ListView)findViewById(R.id.item_detail);
								ListAdapter adapter = new SimpleAdapter(CameraActivity.this, oslist, 
										R.layout.item_list, new String[] {"cname","classification"}, new int[] {
											R.id.cname, R.id.classification});
								list.setAdapter(adapter);
								
							}

						   } catch (JSONException e) {
			                   e.printStackTrace();
			               }
						
						
					}

					@Override
					protected JSONArray doInBackground(String... params) {
							// getting JSON string from URL
							json = JSONParser.makeHttpRequest(params[0], "GET", params[1]);
							// check your log for json response
							Log.d("Single Product Details", json.toString());
							
							return json;		
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
	
	private class DownloadImageTask extends AsyncTask<String, Void, Bitmap> {
	    ImageView bmImage;

	    public DownloadImageTask(ImageView bmImage) {
	        this.bmImage = bmImage;
	    }

	    protected Bitmap doInBackground(String... urls) {
	        String urldisplay = urls[0];
	        Bitmap mIcon11 = null;
	        try {
	            InputStream in = new java.net.URL(urldisplay).openStream();
	            mIcon11 = BitmapFactory.decodeStream(in);
	        } catch (Exception e) {
	            Log.e("Error", e.getMessage());
	            e.printStackTrace();
	        }
	        return mIcon11;
	    }

	    protected void onPostExecute(Bitmap result) {
	        bmImage.setImageBitmap(result);
	    }
	}
	

}
