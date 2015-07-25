package com.brainwave.main;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;

import javax.mail.Address;
import javax.mail.Flags;
import javax.mail.Message;
import javax.mail.MessagingException;
import javax.mail.Message.RecipientType;

import com.brainwave.mail.MailTask;
import com.brainwave.mail.MailSystem.AccountType;
import com.brainwave.mail.MailSystem.TaskType;
import com.brainwave.models.MailMessage;
import com.brainwave.utils.Utilities;
import com.spkdroid.dayatdalserver.R;

import android.app.AlertDialog;
import android.app.ListActivity;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.os.Parcelable;
import android.os.StrictMode;
import android.provider.Telephony.Mms;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.ListView;
import android.widget.Toast;

/**
 * Main class to list the items and providing the features
 * for fetch, show, refresh emails and go to labels 
 * 
 * @author Mustansar Saeed
 *
 */
public class MainEmailActivity extends ListActivity {
	
	protected ArrayList<String> folders = null;
	protected HashMap<String, ArrayList<MailMessage>> emailsMap = new HashMap<String, ArrayList<MailMessage>>();
	private GMailAdapter emails = null;
	private ListView emailListView = null;
	public MainEmailActivity emailActivity = this;
	private String currentFolder;
	
	public AccountType getAccountType(){return null;}
	
	private Button deleteItems = null;
	private ArrayList<MailMessage> messges = new ArrayList<MailMessage>();
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.email_list_layout);
		StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitAll().build();
		StrictMode.setThreadPolicy(policy);
		
		deleteItems = (Button) findViewById(R.id.deleteItems);
		deleteItems.setVisibility(Button.INVISIBLE);
		deleteItems.setOnClickListener(deleteItemsListener);
		
		emailListView = (ListView) findViewById(android.R.id.list);
		
		if(savedInstanceState == null)
		{
			currentFolder = "INBOX";
			MailSystemApplication.mailSystem.processMailTask(this, getAccountType(), "INBOX", TaskType.FETCH_EMAIL);
		
//			try {
//				Message[] mess = new Message[25];
//				emailsFetched(mess, "Inbox", TaskType.FETCH_EMAIL);
//			} catch (MessagingException e) {
//				// TODO Auto-generated catch block
//				e.printStackTrace();
//			} catch (IOException e) {
//				// TODO Auto-generated catch block
//				e.printStackTrace();
//			}
		}
	}

	/**
	 * Delete the email when user checked the boxes and 
	 * press delete
	 * 
	 */
	private OnClickListener deleteItemsListener = new OnClickListener() {
		
		@Override
		public void onClick(View v) {
			// TODO Auto-generated method stub
			ArrayList<Integer> positions = emails.getCheckedPositions();
			ArrayList<Integer> messagesToDelete = new ArrayList<Integer>();
			MailMessage message = null;
			for(int i = emails.getCount() - 1; i != -1; i--)
			{
				message = emails.getItem(i); 
				if(message.isSelected())
				{
					emails.remove(message);
				}	
			}
			emails.clearList();
			MailSystemApplication.mailSystem.deleteMailsTask(MainEmailActivity.this, getAccountType(), 
					currentFolder, TaskType.DELETE, messagesToDelete);
			
			deleteItems.setText("");
			deleteItems.setVisibility(Button.INVISIBLE);
		}
	};
	
	/**
	 * Function to be called when email fetching, refreshing is completed
	 * 
	 * @param objects
	 * @param folder
	 * @param taskType
	 * @throws MessagingException
	 * @throws IOException
	 */
	public void emailsFetched(ArrayList<MailMessage> objects, String folder, TaskType taskType) throws MessagingException, IOException
	{
		messges = objects;		
		if(emailsMap.get(folder) == null || taskType == TaskType.REFRESH_EMAIL)
		{
			emailsMap.put(folder, messges);
		}
		emails = new GMailAdapter(this, messges);
		emailListView.setAdapter(emails);
	}
	
	/**
	 * When user selected checkbox
	 * @param positions
	 */
	
	public void itemsSelected(ArrayList<Integer> positions)
	{
		deleteItems.setText("Delete " + positions.size() + " mails?");
		deleteItems.setVisibility(positions.size() > 0 ? Button.VISIBLE : Button.INVISIBLE);
	}
	
	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// TODO Auto-generated method stub
		getMenuInflater().inflate(R.menu.activity_menu, menu);
		
		return true;
	}
	
	@Override
	public boolean onOptionsItemSelected(MenuItem item){
		switch(item.getItemId())
		{
		case R.id.refresh:
			this.refreshEmail();
			return true;
		case R.id.compose:
			this.composeEmail();
			return true;
		case R.id.goToLabels:
			if(folders == null)
			{
				new MailTask(this).execute(getAccountType(), null, null, null, null, null, TaskType.FETCH_LABELS);
			}
			else
			{
				try {
					showLabels(folders);
				} catch (MessagingException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				} catch (IOException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}
			return true;
		case R.id.settings:
			this.showSettings();
			return true;
		default:
	            return super.onOptionsItemSelected(item);
		}
	}
	
	private void showSettings()
	{
		Toast.makeText(getApplicationContext(), "Settings will be added soon ...", Toast.LENGTH_SHORT).show();
	}
	
	private void refreshEmail() 
	{
		MailSystemApplication.mailSystem.processMailTask(this, getAccountType(), "INBOX", TaskType.REFRESH_EMAIL);
	}
	
	public void composeEmail()
	{
		Intent composeIntent = new Intent(getApplicationContext(), ComposeEmailActivity.class);
		composeIntent.putExtra("accountType", getAccountType());
		
		startActivity(composeIntent);
	}
	
	/**
	 * User can have multiple folders on the account. Fetch all labels
	 * created on account
	 * 
	 * @param labels
	 * @throws MessagingException
	 * @throws IOException
	 */
	
	public void showLabels(ArrayList<String> labels) throws MessagingException, IOException
	{
		AlertDialog.Builder dialog = new AlertDialog.Builder(this);
		dialog.setTitle("Folders");

		folders = labels;
		final ArrayAdapter<String> listAdapter = 
				new ArrayAdapter<String>(this, android.R.layout.select_dialog_item, labels);
		dialog.setAdapter(listAdapter, new DialogInterface.OnClickListener() {
			
			@Override
			public void onClick(DialogInterface dialog, int which) {
				// TODO Auto-generated method stub
				String value = (String) listAdapter.getItem(which);
				
				try {
					fetchMailsWithFolder(value);
				} catch (MessagingException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				} catch (IOException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}
		});
		
		dialog.show();
	}
	
	private void fetchMailsWithFolder(String value) throws MessagingException, IOException{
		currentFolder = value;
		if(emailsMap.get(value) == null)
		{
			MailSystemApplication.mailSystem.processMailTask(this, getAccountType(), value, TaskType.FETCH_EMAIL);	
		}
		else
		{
			this.emailsFetched(emailsMap.get(value), value, TaskType.NONE);
		}	
	}

	@Override
	protected void onSaveInstanceState(Bundle outState) {
		outState.putParcelableArrayList("messages", messges);
		outState.putIntegerArrayList("checkedPositions", emails.getCheckedPositions());
		outState.putStringArrayList("labels", folders);
		outState.putSerializable("emailsMap", emailsMap);
		
		super.onSaveInstanceState(outState);
	}
	
	@Override
	protected void onRestoreInstanceState(Bundle state) {
		// TODO Auto-generated method stub
		super.onRestoreInstanceState(state);
		
		messges = state.getParcelableArrayList("messages");
		folders = state.getStringArrayList("labels");
		emailsMap = (HashMap<String, ArrayList<MailMessage>>) state.getSerializable("emailsMap");
		ArrayList<Integer> positions = state.getIntegerArrayList("checkedPositions");
		
		

		emails = new GMailAdapter(this, messges);
		emailListView.setAdapter(emails);
		
		emails.setCheckedPositions(positions);
		
		int counter = positions.size();
		deleteItems.setText("Delete " + counter + " mails?");
		deleteItems.setVisibility(counter > 0 ? Button.VISIBLE : Button.INVISIBLE);
	}
	
	/**
	 * Show the content of emails
	 * 
	 */
	@Override
	protected void onListItemClick(ListView l, View v, int position, long id) {
		// TODO Auto-generated method stub
		super.onListItemClick(l, v, position, id);
		
		MailMessage data = (MailMessage) l.getItemAtPosition(position);
		Intent intent = new Intent(getApplicationContext(), EmailContentActivity.class);
		intent.putExtra("data", "");
		intent.putExtra("subject", data.getSubject());
		intent.putExtra("from", data.getFromArray());
		intent.putExtra("to", data.getToArray());

		startActivity(intent);
	}
}
