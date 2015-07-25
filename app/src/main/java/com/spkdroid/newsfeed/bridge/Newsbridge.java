package com.spkdroid.newsfeed.bridge;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import com.spkdroid.dayatdalserver.R;
import com.spkdroid.dayatdalserver.TimeTable;
import com.spkdroid.newsfeed.NewsFeed;
import com.spkdroid.newsfeed.Research;
import com.spkdroid.newsfeed.Sports;
import com.spkdroid.newsfeed.StudentLife;
import com.spkdroid.servicehandle.ServiceHandler;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.widget.Toast;
/**
 * @author Ramkumar Velmurugan
 * File Name: Newsbridge.java
 * 
 * This class file is used configure the news feed for the user.
 * 
 * This will checks the users news feed preference and will redirect the user to the 
 * 
 * prefeared news feed.
 * 
 */
public class Newsbridge extends Activity {

	
	String jsonStr;
	ProgressDialog pr;
	String url="http://www.spkdroid.com/merlin/findpref.php?login_id=";
	String id;
	String d;
	
	  protected void onCreate(Bundle savedInstanceState) {
			super.onCreate(savedInstanceState);
		    setContentView(R.layout.newsbridge);
		    id=getIntent().getExtras().getString("ram");
		    
		    new AsyncTask<Void, Void,Void>() {
				
				@Override
				protected void onPreExecute()
				{
					pr=new ProgressDialog(Newsbridge.this);
					url=url+id;
					pr.setCancelable(false);
					pr.setMessage("Please Wait...!!!!");
					pr.show();
				}
				@Override
				protected Void doInBackground(Void... params) {
					ServiceHandler sh = new ServiceHandler();
					jsonStr = sh.makeServiceCall(url, ServiceHandler.POST);
					try {
						JSONArray jr=new JSONArray(jsonStr);
						JSONObject jsonObj=new JSONObject();
						jsonObj=jr.getJSONObject(0);
			            d=jsonObj.getString("result");			
					} catch (JSONException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
					return null;
				}
				@Override
				protected void onPostExecute(Void result) {
					super.onPostExecute(result);
			
					if(d.toLowerCase().equals("sports"))
					{
						finish();
						Intent i=new Intent(getApplicationContext(),Sports.class);
						startActivity(i);
					}
					else if(d.toLowerCase().equals("Research"))
					{
						finish();
						Intent i=new Intent(getApplicationContext(),Research.class);
						startActivity(i);
					}
					else if(d.toLowerCase().equals("student"))
					{
						finish();
						Intent i=new Intent(getApplicationContext(),StudentLife.class);
						startActivity(i);
					}
					else
					{
						finish();
						Intent i=new Intent(getApplicationContext(),NewsFeed.class);
						startActivity(i);
					}
					pr.dismiss();
				}
			}.execute();	
	  }
	  
	  public void onBackPressed()
	  {
		  finish();
	  }
}