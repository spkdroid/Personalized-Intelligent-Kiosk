package com.spkdroid.dayatdalserver;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import com.spkdroid.servicehandle.ServiceHandler;

import android.app.Activity;
import android.app.ProgressDialog;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.TextView;

/**
 * @author Ramkumar Velmurugan
 *
 * Filename: ViewGrades.java
 * 
 * This is used to view the grade information of the user.
 *
 */


public class ViewGrades extends Activity {

	static String r="";
	private static String jsonStr="ram";
	String url;
	ProgressDialog pr;
	String studentid;
    TextView tv;
    String id_tag,grade_information;
    
//http://www.spkdroid.com/merlin/grades.php?login_id=B00675218	

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_view_grades);
		studentid=getIntent().getExtras().getString("Ramkumar");
		tv=(TextView)findViewById(R.id.gradesresult);
	
		/*
		 * 
		 * when the user name is passed via the url string below
		 * 
		 * The server replies with the grade information.
		 */
		
	url="http://www.spkdroid.com/merlin/grades.php?login_id="+studentid;
	
	new AsyncTask<Void, Void,Void>() {
		
		@Override
		protected void onPreExecute()
		{
			pr=new ProgressDialog(ViewGrades.this);
			pr.setMessage("Please Wait");
			pr.setCancelable(false);
			pr.show();
		}
		@Override
		protected Void doInBackground(Void... params) {
			ServiceHandler sh = new ServiceHandler();
			jsonStr = sh.makeServiceCall(url, ServiceHandler.GET);
			try {
				JSONArray jr=new JSONArray(jsonStr);
				JSONObject jsonObj=new JSONObject();
				jsonObj=jr.getJSONObject(0);
				id_tag=jsonObj.getString("student_id");
				grade_information=jsonObj.getString("grades");
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
		    tv.setText(id_tag+"\n"+grade_information.replace(":","\n"));	
		}
	}.execute();		
	}
	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.view_grades, menu);
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
	
	public void onBackPressed()
	{
		finish();
	}
	
	
}