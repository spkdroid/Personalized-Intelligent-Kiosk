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
 *         <p/>
 *         FileName: MainActivity.java
 *         <p/>
 *         This class file is the main screen of the kiosk.This screen will be showing you a ripple background
 *         <p/>
 *         and will be waiting for any device to get connected to the kiosk.
 *         <p/>
 *         When the kiosk detects a accept request from the phone.This will read the id tag information from the
 *         <p/>
 *         users phone and will show the personalized information inside it.
 */
public class MainActivity extends Activity {

    // Bluetooh Class variable that used to make the connection
    BluetoothAdapter mBluetoothAdapter;
    BluetoothDevice device;

    private static final int REQUEST_ENABLE_BT = 2;
    // This is an Unique id that is used to identify the client application
    private static final UUID MY_UUID = UUID.fromString("fa87c0d0-afac-11de-8a39-0800200c9a66");
    private static final String NAME = "PDAD";
    private ImageView foundDevice;
    BluetoothDevice remoteDevice;
    TextView output, key;
    Button initServer;
    // Ripple Class to Provide a Ripple Effect in the Background
    RippleBackground rippleBackground;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main_radar);


        // Linking the UI with the XML
        rippleBackground = (RippleBackground) findViewById(R.id.content);
        output = (TextView) findViewById(R.id.message);
        key = (TextView) findViewById(R.id.key);

        final Handler handler = new Handler();

        foundDevice = (ImageView) findViewById(R.id.foundDevice);
        ImageView button = (ImageView) findViewById(R.id.centerImage);

        /**
         *  Provides the beautiful Ripple Wave Animation for the UI screen
         */

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
                        ArrayList<Animator> animatorList = new ArrayList<Animator>();
                        ObjectAnimator scaleXAnimator = ObjectAnimator.ofFloat(foundDevice, "ScaleX", 0f, 1.2f, 1f);
                        animatorList.add(scaleXAnimator);
                        ObjectAnimator scaleYAnimator = ObjectAnimator.ofFloat(foundDevice, "ScaleY", 0f, 1.2f, 1f);
                        animatorList.add(scaleYAnimator);
                        animatorSet.playTogether(animatorList);
                        foundDevice.setVisibility(View.VISIBLE);
                        animatorSet.start();

                    }
                }, 3000);
            }
        });


        // Test Check Function to find the device capability to connect with Kiosk
        blueToothCheck();
        // Starting the Server
        startServer();

    }

    /**
     * This StartServer will initiate a thread that runs in the background and will be checking for the device availablilty
     * <p/>
     * when a device is connected successfully. This will redirect to the appropriate page
     */

    private void startServer() {
        // TODO Auto-generated method stub
        new Thread(new AcceptThread()).start();
        //	startServer();
    }

    private boolean blueToothCheck() {
        // TODO Auto-generated method stub
        mBluetoothAdapter = null;
        mBluetoothAdapter = BluetoothAdapter.getDefaultAdapter();

        if (!mBluetoothAdapter.isEnabled()) {
            Intent enableBtIntent = new Intent(BluetoothAdapter.ACTION_REQUEST_ENABLE);
            startActivityForResult(enableBtIntent, REQUEST_ENABLE_BT);
            return false;
        } else {
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

    /**
     * The Function that check for the device connectivity. When the device connects with the kiosk. A SUCCESS message is fired
     * <p/>
     * this in turn Navigates to the next page.
     * <p/>
     * IdntityLancher is fired along with the user information
     */

    private Handler handler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            String r = msg.getData().getString("msg").trim().substring(0, 7);
            output.append(r);

            Toast.makeText(MainActivity.this, r, Toast.LENGTH_LONG).show();
            if (r.equals("SUCCESS")) {
                finish();
                Intent i = new Intent(getApplicationContext(), IdentityLauncher.class);
                i.putExtra("Ram", msg.getData().getString("msg").substring(7));
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
     * <p/>
     * This part of the thread class AcceptThread is adopted from
     * <p/>
     * http://developer.android.com/guide/topics/connectivity/bluetooth.html
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
                    PrintWriter out = new PrintWriter(new BufferedWriter(new OutputStreamWriter(socket.getOutputStream())), true);
                    out.println("Reponse from Bluetooth Demo Server");
                    out.flush();
                } catch (Exception e) {

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

}  