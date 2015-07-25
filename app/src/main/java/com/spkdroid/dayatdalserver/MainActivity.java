package com.spkdroid.dayatdalserver;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.UUID;

import com.skyfishjy.ripplebackground.sample.RippleBackground;

import android.animation.Animator;
import android.animation.AnimatorSet;
import android.animation.ObjectAnimator;
import android.app.Activity;
import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.bluetooth.BluetoothServerSocket;
import android.bluetooth.BluetoothSocket;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.animation.AccelerateDecelerateInterpolator;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;
/**
 * @author Ramkumar Velmurugan
 *
 * FileName: MainActivity.java
 * 
 * This class file is the main screen of the kiosk.This screen will be showing you a ripple background
 * 
 * and will be waiting for any device to get connected to the kiosk.
 *
 * When the kiosk detects a accept request from the phone.This will read the id tag information from the 
 * 
 * users phone and will show the personalized information inside it.
 *
 */
public class MainActivity extends Activity implements OnClickListener {

	BluetoothAdapter mBluetoothAdapter;
	BluetoothDevice device;
	private static final int REQUEST_ENABLE_BT = 2;  
	private static final UUID MY_UUID = UUID.fromString("fa87c0d0-afac-11de-8a39-0800200c9a66");
	private static final String NAME = "PDAD";
	
    private ImageView foundDevice;

	
	BluetoothDevice remoteDevice;
	TextView output,key;
	//Button btnServer, btnScan, btnClient;
	Button initServer;
	RippleBackground rippleBackground;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.main_radar);
		
		    rippleBackground=(RippleBackground)findViewById(R.id.content);
			output=(TextView)findViewById(R.id.message);
			key=(TextView)findViewById(R.id.key);
		
		  final Handler handler=new Handler();

	        foundDevice=(ImageView)findViewById(R.id.foundDevice);
	        ImageView button=(ImageView)findViewById(R.id.centerImage);
	        button.setOnClickListener(new View.OnClickListener() {
	            @Override
	            public void onClick(View view) {
	                rippleBackground.startRippleAnimation();
	                handler.postDelayed(new Runnable() {
	                    @Override
	                    public void run() {
	                        foundDevice();
	                    }

						private void foundDevice() {
							// TODO Auto-generated method stub
						     AnimatorSet animatorSet = new AnimatorSet();
						        animatorSet.setDuration(400);
						        animatorSet.setInterpolator(new AccelerateDecelerateInterpolator());
						        ArrayList<Animator> animatorList=new ArrayList<Animator>();
						        ObjectAnimator scaleXAnimator = ObjectAnimator.ofFloat(foundDevice, "ScaleX", 0f, 1.2f, 1f);
						        animatorList.add(scaleXAnimator);
						        ObjectAnimator scaleYAnimator = ObjectAnimator.ofFloat(foundDevice, "ScaleY", 0f, 1.2f, 1f);
						        animatorList.add(scaleYAnimator);
						        animatorSet.playTogether(animatorList);
						        foundDevice.setVisibility(View.VISIBLE);
						        animatorSet.start();
						 
						}
	                },3000);
	            }
	        });
	
	        blueToothCheck();
			startServer();
			
	}


		
	//	initServer=(Button)findViewById(R.id.initiateServer);
	//	initServer.setOnClickListener(this);
		
		
	private void startServer() {
		// TODO Auto-generated method stub
		new Thread(new AcceptThread()).start();
		//	startServer();
	}

	private boolean blueToothCheck() {
		// TODO Auto-generated method stub
		mBluetoothAdapter =null;
		mBluetoothAdapter = BluetoothAdapter.getDefaultAdapter();
    
		//if (mBluetoothAdapter == null) {
    	    // Device does not support Bluetooth
    	//	return false;
    	//}
    	//make sure bluetooth is enabled.
    	
		if (!mBluetoothAdapter.isEnabled()) 
    	 {
    	     Intent enableBtIntent = new Intent(BluetoothAdapter.ACTION_REQUEST_ENABLE);
    	     startActivityForResult(enableBtIntent, REQUEST_ENABLE_BT);
    	     return false;	 
    	 }
    	 else
    	 {
    		 return true;
    	 }
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.main, menu);
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

	private Handler handler = new Handler() {
	        @Override
	        public void handleMessage(Message msg) {
	        	String r=msg.getData().getString("msg").trim().substring(0,7);
	        	output.append(r);
	        //	Toast.makeText(MainActivity.class,"Ping",Toast.LENGTH_LONG).show();
	        	
	        	Toast.makeText(MainActivity.this,r,Toast.LENGTH_LONG).show();
	        	if(r.equals("SUCCESS"))
	        	{
	    //    	Toast.makeText(getApplicationContext(),"Ping Ends",Toast.LENGTH_LONG).show();
		    finish();
		    Intent i=new Intent(getApplicationContext(),IdentityLauncher.class);
		    i.putExtra("Ram",msg.getData().getString("msg").substring(7));
		    startActivity(i);	
	        	}
	        }
	    };
	
	 public void mkmsg(String str) {
			//handler junk, because thread can't update screen!
			Message msg = new Message();
			Bundle b = new Bundle();
			b.putString("msg", str);
			msg.setData(b);
		    handler.sendMessage(msg);
	    }
    /**
     * This thread runs while listening for incoming connections. It behaves
     * like a server-side client. It runs until a connection is accepted
     * (or until cancelled).
     * 
     * This part of the thread class AcceptThread is adopted from 
	 * 
	 * http://developer.android.com/guide/topics/connectivity/bluetooth.html
     * 
     */
    private class AcceptThread extends Thread {
        // The local server socket
        private final BluetoothServerSocket mmServerSocket;

        public AcceptThread() {
            BluetoothServerSocket tmp = null;
            // Create a new listening server socket
            try {
                tmp = mBluetoothAdapter.listenUsingRfcommWithServiceRecord(NAME, MY_UUID);
            } catch (IOException e) {
                mkmsg("Failed to start server\n");
            }
            mmServerSocket = tmp;
        }
        public void run() {
        	mkmsg("waiting on accept");
        	BluetoothSocket socket = null;
        	try {
        		// This is a blocking call and will only return on a
        		// successful connection or an exception
        		socket = mmServerSocket.accept();
        	} catch (IOException e) {
        		mkmsg("Failed to accept\n");
        	}
        	// If a connection was accepted
        	if (socket != null) {
        		//Note this is copied from the TCPdemo code.
        		try {
        			BufferedReader in = new BufferedReader(new InputStreamReader(socket.getInputStream())); 
        			String str = in.readLine();
        			mkmsg(str);
        			PrintWriter out = new PrintWriter( new BufferedWriter( new OutputStreamWriter(socket.getOutputStream())),true);
        			out.println("Reponse from Bluetooth Demo Server");
        			out.flush();
        		} catch(Exception e) {
      
        		} finally {
        			try {
						socket.close();
					} catch (IOException e) {
					}
        		}
        	} else {
        	}
        }	
}
	@Override
	public void onClick(View v) {
		// TODO Auto-generated method stub
		}
}  