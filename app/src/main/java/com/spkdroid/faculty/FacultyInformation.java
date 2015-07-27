package com.spkdroid.faculty;

import com.spkdroid.dayatdalserver.R;

import android.app.Activity;
import android.os.Bundle;
import android.widget.ArrayAdapter;
import android.widget.ListView;



/**
 * 
 * 
 * This is just a simple listview containting the faculty information
 * 
 * @author Prasna Elangovan
 *
 * when the user click the faculty infomation in the kiosk. The Application will list the
 *
 * faculty information in an ListView
 *
 */

public class FacultyInformation extends Activity{

	
	ListView faculty;
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
	    setContentView(R.layout.xfiles);
	
	    faculty=(ListView)findViewById(R.id.xfileslist);


		/*
		* Faculty Information are stored in the Values array
		 */

	    String[] values = new String[] { 
	    		"Dr. A. Rau-Chaplin \n 902-494-2732 \n arc@cs.dal.ca\n",
	    		"Dr. Christian Blouin \n 902-494-6702 \n cblouin@cs.dal.ca \n",
	    		"Dr. Evangelos E. Milios \n 902-494-7111 \n eem@cs.dal.ca \n",
	    		"Dr. Philip T. Cox \n 902-494-6460 \n pcox@cs.dal.ca \n",
	    		"Dr. Raza Abidi \n 902-494-2129 \n sraza@cs.dal.ca \n",
	    		"Dr. J. Blustein \n 902-494-6104\n jamie@cs.dal.ca\n",
	    		"Dr. Peter Bodorik \n 902-494-6452 \n bodorik@cs.dal.ca \n",
	    		"Dr. Stephen Brooks \n 902-494-2512 \n sbrooks@cs.dal.ca \n",
                "Dr. Qigang Gao \n 902-494-3356 \n qggao@cs.dal.ca \n",
	    		"Dr. Kirstie Hawkey \n 902-494-1599 \n hawkey@cs.dal.ca \n",
	    		"Dr. Meng He \n 902-494-4056 \n mhe@cs.dal.ca \n",
	    		"Dr. Stan Matwin \n 902-494-4320 \n stan@cs.dal.ca \n",
	    		"Dr. Derek Reilly \n 902-494-4057 \n reilly@cs.dal.ca \n",
	    		"Dr. N. Zincir-Heywood \n 902-494-3157 \n zincir@cs.dal.ca \n",
	    		"Dr. Srinivas Sampalli \n 902-494-1657 \n srini@cs.dal.ca \n"
	    };

// Array Adapter to List the Values
ArrayAdapter<String> adapter = new ArrayAdapter<String>(this,
android.R.layout.simple_list_item_1, android.R.id.text1, values);


// Assign adapter to ListView
faculty.setAdapter(adapter); 
	    
	    
	
	}
}
