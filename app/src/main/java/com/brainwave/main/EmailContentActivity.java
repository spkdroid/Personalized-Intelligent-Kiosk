package com.brainwave.main;

import java.util.ArrayList;

import com.spkdroid.dayatdalserver.R;

import android.app.Activity;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.EditText;
import android.widget.TextView;

/**
 * Show the content of email when user selected an email
 * 
 * @author Mustansar Saeed
 *
 */
public class EmailContentActivity extends Activity{
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.email_detail_item);
		
		String receivedData = getIntent().getStringExtra("data");
		ArrayList<String> from = getIntent().getStringArrayListExtra("from");
		ArrayList<String> to = getIntent().getStringArrayListExtra("to");
		String subject = getIntent().getStringExtra("subject");
		
		TextView subjectView = (TextView) findViewById(R.id.subjectDetailView);
		EditText fromView = (EditText) findViewById(R.id.fromDetailView);
		EditText toView = (EditText) findViewById(R.id.toDetailView);
		
		
		subjectView.setText(subject);
		fromView.setText(from.toString());
		toView.setText(to.toString());
	}
	
	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		
		return true;
	}

}
