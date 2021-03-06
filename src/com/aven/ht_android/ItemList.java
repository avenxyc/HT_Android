package com.aven.ht_android;

import java.io.InputStream;
import java.util.ArrayList;
import java.util.HashMap;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import android.annotation.TargetApi;
import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.support.v4.app.NavUtils;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.SimpleAdapter;
import android.widget.TextView;

public class ItemList extends Activity {
	
	//Default query url
	private static String basic_url = "http://falcon.acadiau.ca/~112574x/HT/HT_android_connection.php";
	
	private static final int RESULT_SETTINGS = 1;
	
	ListView list;
    TextView cname;
    TextView weight;
    ArrayList<HashMap<String, String>> oslist = new ArrayList<HashMap<String, String>>();
	
	JSONParser jParser = new JSONParser();
	
	

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_item_list);
		// Show the Up button in the action bar.
		setupActionBar();
		
		// Get the message from the intent
	    Intent intent = getIntent();
	    final String upc = intent.getStringExtra(CameraActivity.msg);
	    //final String region = intent.getStringExtra(CameraActivity.rgn);
	    
	    //Log.d("region", region);
	    setContentView(R.layout.activity_item_list);
		// Show the Up button in the action bar.
		setupActionBar();
		
		
	    
	    String url = basic_url + "?upccode=";
		
		Log.e("mtest", "here");
		
		
		
		//inner class 
		class MyRetreiveFeedTask extends AsyncTask<String, Void, JSONArray> {
			//private Exception exception;
			public JSONArray json;

		    //Total recyclable package weight
		    double recyclable_weight;
		    //Total package weight
		    double package_weight;
		    //Calculated recyclability rate
		    double recyclability_rate;
			
			
			protected void onPostExecute(JSONArray json){
				//Display the product image
				String img_url = "http://falcon.acadiau.ca/~112574x/HT/pics/" + upc + ".jpg";
				new DownloadImageTask((ImageView) findViewById(R.id.pimage))
				            .execute(img_url);
				
				
				//Display item name and weight
				try {
					//Get the product name
					JSONObject info = json.getJSONObject(0);
					String name = info.getString("product_name");
					Log.d("product", name);
					TextView tv_item_name = (TextView)findViewById(R.id.item_name);
					tv_item_name.setText(name);
					
					//Get the product weight
					String weight = info.getString("weight");
					TextView tv_weight = (TextView)findViewById(R.id.weight);
					tv_weight.setText(weight + "g/L");
					
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
						String recyclable = e.getString("classification");
						double pweight = e.getDouble("part_weight");
						
						//Add up to the total recyclable weight
						if(recyclable.equals("Blue Bag #1: Paper")
								||recyclable.equals("Blue Bag #2: Recyclables")){
							recyclable_weight =+ pweight;
						}
						
						//Add up to the total package weight
						package_weight =+ pweight;
						
						
						//Add value HashMap key => value
						map.put("cname", c_name );
						map.put("classification", throw_where);
						
						//Add to the list
						oslist.add(map);
						
						
						list=(ListView)findViewById(R.id.item_detail);
						ListAdapter adapter = new SimpleAdapter(ItemList.this, oslist, 
								R.layout.item_list, new String[] {"cname","classification"}, new int[] {
									R.id.cname, R.id.classification});
						list.setAdapter(adapter);
						list.setVisibility(View.VISIBLE);
						
					}
					
					
					TextView tv_rate = (TextView)findViewById(R.id.recyclability);
					//Calculate the recyclability result
					if(package_weight > 0){
						recyclability_rate = recyclable_weight / package_weight * 100;
						String S_rate = Double.toString(recyclability_rate);
						tv_rate.append(S_rate + "%");
						 
					} else {
						recyclability_rate = 0;
						String rate_error = "There is some errors here.";
						tv_rate.append(rate_error);
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
		
		
		
		//Check network status
		if(isNetworkAvailable(this))
		{
			new MyRetreiveFeedTask().execute(url, upc);
		}else {
			new AlertDialog.Builder(ItemList.this)
		    .setTitle(R.string.No_network_connection_title)
		    .setMessage(R.string.No_network_connection_content)
		    .setNeutralButton(android.R.string.ok, new DialogInterface.OnClickListener() {
		        public void onClick(DialogInterface dialog, int which) { 
		        	//Do nothing 
		        }
			    })
		    .setIcon(R.drawable.ic_dialog_alert)
		    .show();
		}
		
	    
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
		getMenuInflater().inflate(R.menu.item_list, menu);
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
			
		case R.id.action_settings:
            Intent i = new Intent(this, UserSettingsActivity.class);
            startActivityForResult(i, RESULT_SETTINGS);
            break;	
		}
		return super.onOptionsItemSelected(item);
	}
	
	//Check if network is available
	public boolean isNetworkAvailable(Context ctx)
	 {
	     ConnectivityManager cm = (ConnectivityManager)ctx.getSystemService(Context.CONNECTIVITY_SERVICE);
	     NetworkInfo netInfo = cm.getActiveNetworkInfo();
	     if (netInfo != null && netInfo.isConnectedOrConnecting()&& cm.getActiveNetworkInfo().isAvailable()&& cm.getActiveNetworkInfo().isConnected()) 
	     {
	         return true;
	     }
	     else
	     {
	         return false;
	     }
	 }
	
	/**
	 * Load image from database
	 */	
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
