package com.spkdroid.dayatdalserver;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import com.spkdroid.servicehandle.ServiceHandler;

import android.app.Activity;
import android.app.ProgressDialog;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.TextView;
/**
 * @author Ramkumar Velmurugan
 * FileName: TimeTable.java
 * This class is used to show the time table information of the user
 */
public class TimeTable extends Activity {
	//  Fetch TimeTable Service
	//	http://www.spkdroid.com/merlin/timetable.php?login_id=B00675218
    String r="";
	ProgressDialog pr;
	private static String jsonStr="ram";
	String url;
	String tokenresult,class1,class2,class3,class4,class5;
	TextView rest;
	String studentid;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_time_table);
	
		studentid=getIntent().getExtras().getString("Ramkumar");
		rest=(TextView)findViewById(R.id.time_table_details);
     	
		/*
		 * 
		 * user name is sent to the server and the server repiles back with the timetable details
		 * 
		 */
		
		url="http://www.spkdroid.com/merlin/timetable.php?login_id="+studentid;	
	
	new AsyncTask<Void, Void,Void>() {
			
			@Override
			protected void onPreExecute()
			{
				pr=new ProgressDialog(TimeTable.this);
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
					tokenresult=jsonObj.getString("student_id");
					class1=jsonObj.getString("class1");
					class2=jsonObj.getString("class2");
					class3=jsonObj.getString("class3");
					class4=jsonObj.getString("class4");
					class5=jsonObj.getString("class5");
				} catch (JSONException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
				return null;
			}
			@Override
			protected void onPostExecute(Void result) {
				super.onPostExecute(result);
				pr.dismiss();

				if(tokenresult!=null)
				{
				r=r+tokenresult+"\n";	
				}
				if(class1!=null)
				{
					r=r+class1+"\n";	
				}
				if(class2!=null)
				{
					r=r+class2+"\n";		
				}
				if(class3!=null)
				{
					r=r+class3+"\n";	
				}
				if(class4!=null)
				{
					r=r+class4+"\n";	
				}
				if(class5!=null)
				{
					r=r+class5+"\n";						
				}
			rest.setText(r);	
			}
		}.execute();	
	}
	public void onBackPressed()
	{
		finish();
	}
	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.time_table, menu);
		return true;
	}
	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		// Handle action bar item clicks here. The action bar will
		// automatically handle clicks on the Home/Up button, so long
		// as you specify a parent activity in AndroidManifest.xml.
		int id = item.getItemId();
		if (id == R.id.action_settings) {
			return true;
		}
		return super.onOptionsItemSelected(item);
	}
}