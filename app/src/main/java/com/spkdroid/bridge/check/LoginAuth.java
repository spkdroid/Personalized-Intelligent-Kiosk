package com.spkdroid.bridge.check;

import com.spkdroid.dayatdalserver.MainActivity;
import com.spkdroid.dayatdalserver.R;

import android.app.Activity;
import android.app.AlertDialog;
import android.bluetooth.BluetoothAdapter;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;


/**
 * @author Ramkumar Velmurugan
 *  
 *  Filename: LoginAuth.java
 *  
 *  This is the first java file that will be launched by the kiosk. This will check for the hardware about the
 *  
 *  bluetooth availablilty and will give an alert message. If the device is not supported the application will
 *  
 *  not launch.
 *
 */


public class LoginAuth extends Activity{

	boolean flag1=false,flag2;
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.server_start);

		BluetoothAdapter mBluetoothAdapter = BluetoothAdapter.getDefaultAdapter();

		
		/*
		 * This function is used to check weather the bluetooth driver is available or not
		 * 
		 */
		
		
		if (!mBluetoothAdapter.isEnabled()) {
		        // Bluetooth is not enable :)
			AlertDialog.Builder alertDialogBuilder =new AlertDialog.Builder(LoginAuth.this);
					// set title
					alertDialogBuilder.setTitle("Please Switch On the Bluetooth");
					// set dialog message
					alertDialogBuilder
						.setMessage("Click yes to exit!")
						.setCancelable(false)
						.setPositiveButton("Yes",new DialogInterface.OnClickListener() {
							public void onClick(DialogInterface dialog,int id) {
								// if this button is clicked, close
								// current activity
								finish();
							}
						  });
						// create alert dialog
						AlertDialog alertDialog = alertDialogBuilder.create();
						// show it
						alertDialog.show();
		}
		else
		{
			flag1=true;
		}
        /**
         * when the flag1 is true then the MainActivity class is lanunched. The MainActivity is the Main Screen of the Kiosk
         */
		
		if(flag1==true)
		{
			finish();
			Intent i=new Intent(this,MainActivity.class);
			startActivity(i);
		}
	}
	}

