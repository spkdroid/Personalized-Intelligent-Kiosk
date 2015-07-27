package com.spkdroid.dayatdalserver;

import java.io.IOException;
import java.net.IDN;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import com.spkdroid.dayatdalserver.dialog.AllEvent;
import com.spkdroid.faculty.FacultyInformation;
import com.spkdroid.maps.MapView;
import com.spkdroid.newsfeed.NewsFeed;
import com.spkdroid.newsfeed.bridge.Newsbridge;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.UrlQuerySanitizer.ValueSanitizer;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.StrictMode;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

/**
 * @author Ramkumar Velmurugan
 *         <p/>
 *         FileName: IdentityLauncher.java
 *         <p/>
 *         This class is the menu screen of the kiosk. Based on the users input the kiosk will navigate to the further
 *         screen.The kiosk will show their profile picture and other personalized information inside it.
 */

public class IdentityLauncher extends Activity implements OnClickListener,
        OnItemClickListener {

    TextView tv;
    Button TimeTable, ViewGrades, News, Party, CalEven, Maiyl, help;
    ImageView rpof;
    URL url;
    ProgressDialog pr;
    ImageView cv;
    ListView kiosklist;
    private ArrayAdapter<String> adapter;
    private List<String> liste;
    static String[] values;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_identity_launcher);

        StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder()
                .permitAll().build();
        StrictMode.setThreadPolicy(policy);

        tv = (TextView) findViewById(R.id.idtext);
        //	String data = getIntent().getExtras().getString("Ram");

        tv.setText("B00675218");

        cv = (ImageView) findViewById(R.id.indeximg);
        cv.setOnClickListener(this);
        try {
            checkImg();
        } catch (IOException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
        kiosklist = (ListView) findViewById(R.id.listView1);

		/*
         * The list of items to be shown in the kiosk
		 * 
		 * The values array contains the kiosk list information
		 * 
		 * 
		 */


        values = new String[]{"Time Table", "View Grades",
                "Personalized News Feed", "Personalized Event", "Party @ Dal",
                "Help", "Maps", "Faculty", "Logout"};

        // imageId contain the list of images that need to mapped with the values
        Integer[] imageId = {
                R.drawable.timetable,
                R.drawable.student,
                R.drawable.news,
                R.drawable.student,
                R.drawable.beer,
                R.drawable.cloud,
                R.drawable.help,
                R.drawable.phone2,
                R.drawable.phone1
        };

        CustomList adapter = new CustomList(IdentityLauncher.this, values, imageId);
        kiosklist.setAdapter(adapter);
        kiosklist.setOnItemClickListener(this);

    }
	
	/*
	 * 
	 * This method check for the image in the server and will report back to the kiosk
	 * 
	 */

    private void checkImg() throws IOException {
        // TODO Auto-generated method stub
        url = new URL("http://www.spkdroid.com/merlin/uploads/"
                + tv.getText().toString() + ".jpg");
        Bitmap bmp = BitmapFactory.decodeStream(url.openConnection()
                .getInputStream());
        if (bmp.getByteCount() > 0)
            cv.setImageBitmap(bmp);
        else
            cv.setImageResource(R.drawable.ic_launcher);
    }


    public void onBackPressed() {
        Toast.makeText(getApplicationContext(), "Press the Logout Button",
                Toast.LENGTH_LONG).show();
    }

    @Override
    public void onClick(View v) {
        // TODO Auto-generated method stub
        if (v == cv) {
            try {
                url = new URL("http://www.spkdroid.com/merlin/uploads/"
                        + tv.getText().toString() + ".jpg");
                Bitmap bmp = BitmapFactory.decodeStream(url.openConnection()
                        .getInputStream());
                if (bmp.getByteCount() > 0)
                    cv.setImageBitmap(bmp);
                else
                    cv.setImageResource(R.drawable.ic_launcher);
            } catch (MalformedURLException e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
            } catch (IOException e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
            }

        }

    }

    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position,
                            long id) {
        // TODO Auto-generated method stub
        // Toast.makeText(getApplicationContext(),values[position].toString(),Toast.LENGTH_LONG).show();
	
		/*
		 * when the user click the the list item appropirate action is done.
		 * 
		 */
        Intent i;
        switch (position + 1) {
            case 1:
                i = new Intent(this, TimeTable.class);
                i.putExtra("Ramkumar", tv.getText().toString());
                startActivity(i);
                break;
            case 2:
                i = new Intent(this, ViewGrades.class);
                i.putExtra("Ramkumar", tv.getText().toString());
                startActivity(i);
                break;
            case 3:
                i = new Intent(this, Newsbridge.class);
                i.putExtra("ram", tv.getText().toString());
                startActivity(i);
                break;
            case 4:
                i = new Intent(this, Calander.class);
                i.putExtra("ram", tv.getText().toString());
                startActivity(i);
                break;
            case 5:
                i = new Intent(getApplicationContext(), AllEvent.class);
                startActivity(i);
                break;
            case 6:
                i = new Intent(getApplicationContext(), HelpActivity.class);
                startActivity(i);
                break;
            case 7:
                i = new Intent(this, MapView.class);
                startActivity(i);
                break;
            case 8:
                i = new Intent(getApplicationContext(), FacultyInformation.class);
                startActivity(i);
                break;
            case 9:
                finish();
                i = new Intent(getApplicationContext(), MainActivity.class);
                startActivity(i);
                break;
        }
    }
}