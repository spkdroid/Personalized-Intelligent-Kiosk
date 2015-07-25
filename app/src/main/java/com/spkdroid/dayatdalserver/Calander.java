package com.spkdroid.dayatdalserver;

import com.spkdroid.dayatdalserver.dialog.AllEvent;
import com.spkdroid.dayatdalserver.dialog.DayInfo;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.CalendarView;
import android.widget.Toast;
import android.widget.CalendarView.OnDateChangeListener;

/**
 *
 * @author Ramkumar Velmurugan
 *  Filename: Calander.java
 *  
 *  This class file is a simple calendar.Based on the user selection of date.This function will display 
 *  
 *  weather there is an event or not on that date.
 *  
 *  
 */

public class Calander extends Activity {
	
	//http://www.spkdroid.com/merlin/fetchevent.php?dal_id=B00675218&dal_date=2015-03-28
	/*
	 * 
	 * Service
	 * 
	 * 
	 */
	CalendarView cal;
	String f;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.cal_droid);
	
	 f=getIntent().getExtras().getString("ram");
	
	 cal=(CalendarView)findViewById(R.id.ssda);
		cal.setOnDateChangeListener(new OnDateChangeListener(){

            @Override
            public void onSelectedDayChange(CalendarView view, int year,
                    int month, int dayOfMonth) {
            	String c_mon;
				String c_day;
				month=month+1;
				if(month<10)
				{
				c_mon="0"+month;	
				}
				else
				{
			    c_mon=month+"";
				}
				if(dayOfMonth<10)
				{
				c_day="0"+dayOfMonth;	
				}
				else
				{
					c_day=dayOfMonth+"";
				}
				String r=year+"-"+c_mon+"-"+c_day;
			//	args.putString("date",year+"-"+c_mon+"-"+c_day);
			Toast.makeText(getApplicationContext(),f+r,Toast.LENGTH_LONG).show();
Intent i=new Intent(getApplicationContext(),DayInfo.class);
i.putExtra("key",f);
i.putExtra("value",r);
startActivity(i);
            }

    });
	
	}
	
	public void onBackPressed()
	{
		finish();
	}


}
