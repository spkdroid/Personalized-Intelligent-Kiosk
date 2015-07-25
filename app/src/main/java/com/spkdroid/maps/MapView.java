package com.spkdroid.maps;

import com.spkdroid.dayatdalserver.R;

import android.app.Activity;
import android.os.Bundle;

/**
 * 
 * 
 * 
 * @author Prasana Elangovan
 *
 * This is just a simple map view. The map is loaded from the resource folder into the screen.
 *
 *
 *
 */

public class MapView extends Activity
{
	
	protected void onCreate(Bundle savedInstanceState) {
	   super.onCreate(savedInstanceState);
	   setContentView(R.layout.remote);
	//   zz=(ZoomableImageView) findViewById(R.id.zoomimg);
	 //  zz.setImageResource(R.drawable.maps);
	}
}
