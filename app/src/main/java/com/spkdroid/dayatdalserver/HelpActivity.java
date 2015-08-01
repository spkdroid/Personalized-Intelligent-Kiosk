package com.spkdroid.dayatdalserver;

import android.app.Activity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.webkit.WebView;

/**
 * 
 * 
 * @author Prasana Elangovan
 * 
 * Filename: HelpActivity.java
 * This is a simple file that is used to show a simple webview. The webview shows a html file from the assert folder.
 *
 */

public class HelpActivity extends Activity {

	
	WebView wv;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_help);


    // The Help information are loaded from the HTML file that has been loaded in the raw folder. Using the webview we are loading to the screen
	wv=(WebView) findViewById(R.id.prasna);
	wv.loadUrl("file:///android_res/raw/dal.html");
		wv.getSettings().setLoadWithOverviewMode(true);
		wv.getSettings().setUseWideViewPort(true);


	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.help, menu);
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
