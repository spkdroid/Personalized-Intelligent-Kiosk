package com.brainwave.main;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.EnumMap;
import java.util.HashMap;
import java.util.List;

import com.brainwave.mail.MailTask;
import com.brainwave.mail.MailSystem.AccountType;
import com.brainwave.mail.MailSystem.TaskType;
import com.brainwave.utils.SimpleFileDialog;
import com.spkdroid.dayatdalserver.R;

import android.app.ActionBar.LayoutParams;
import android.app.Activity;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.webkit.WebView.FindListener;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemSelectedListener;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;


/**
 * Compose and send email
 * @author Mustansar Saeed
 *
 */
public class ComposeEmailActivity extends Activity implements OnItemSelectedListener{
	Spinner fromView = null;
	EditText toView = null, subjectView = null, bodyView = null; 
	String choosenFile = null;
	HashMap<Integer, String> attachements = new HashMap<Integer, String>();
	AccountType accountType = null;
	int counter = 1;
	
	LinearLayout attachmentContainer = null;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		
		setContentView(R.layout.send_email_layout);
		
		accountType = (AccountType)getIntent().getSerializableExtra("accountType");
		
		fromView = (Spinner) findViewById(R.id.fromView);
		toView = (EditText) findViewById(R.id.toView);
		subjectView = (EditText) findViewById(R.id.subjectView);
		bodyView = (EditText) findViewById(R.id.bodyView);
		attachmentContainer = (LinearLayout) findViewById(R.id.attachmentContainer);
		
		EnumMap<AccountType, String> accountsMap = MailSystemApplication.mailSystem.getEmails();
		
		List<String> accounts = new ArrayList<String>(accountsMap.values());
		ArrayAdapter<String> adapter = new ArrayAdapter<String>(getApplicationContext(), 
				android.R.layout.simple_dropdown_item_1line, accounts);
		fromView.setAdapter(adapter);
		
		fromView.setOnItemSelectedListener(this);
	}

	@Override
	public void onItemSelected(AdapterView<?> arg0, View arg1, int arg2,
			long arg3) {
		// TODO Auto-generated method stub
	}

	@Override
	public void onNothingSelected(AdapterView<?> arg0) {
		// TODO Auto-generated method stub
	}
	
	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// TODO Auto-generated method stub
//		return super.onCreateOptionsMenu(menu);
		
		getMenuInflater().inflate(R.menu.compose_menu, menu);
		return true;
	}
	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		// TODO Auto-generated method stub
		
		switch(item.getItemId())
		{
		case R.id.sendMenu:
			this.sendEmail();
			return true;
		case R.id.cancelMenu:
			this.cancelEmail();
			return true;
		case R.id.addAttachmentMenu:
			this.addAttachment();
			return true;
		case R.id.saveAsDraftMenu:
			this.saveMailInDrafts();
			return true;
		default:
				
		}
		
		return super.onOptionsItemSelected(item);
		
		
	}
	
	private void sendEmail()
	{
		Log.i("mustang", "sendEmail: Attachements count: " + attachements.values().size());
		List<String> toList = Arrays.asList(toView.getText().toString().split(",|;"));

		new MailTask(ComposeEmailActivity.this).execute(fromView.getSelectedItem().toString(), toList, 
				subjectView.getText().toString(), bodyView.getText().toString(), accountType, 
				new ArrayList<String>(attachements.values()), TaskType.SEND_EMAIL);
		
	}
	
	private void cancelEmail ()
	{
		toView.setText("");
		subjectView.setText("");
		bodyView.setText("");
		
		finish();
		
	}
	
	/**
	 * Add and cancel attachment
	 * 
	 */
	
	private void addAttachment()
	{
		SimpleFileDialog FileOpenDialog =  new SimpleFileDialog(ComposeEmailActivity.this, "FileOpen",
				new SimpleFileDialog.SimpleFileDialogListener()
		{
			String m_chosen;
			@Override
			public void onChosenDir(String chosenDir) 
			{
				// The code in this function will be executed when the dialog OK button is pushed 

				choosenFile = chosenDir;
				int index = choosenFile.lastIndexOf("/");
				String lastIndexString = choosenFile.substring(index + 1, choosenFile.length());
				TextView fileName = new TextView(getApplicationContext());
				fileName.setText(lastIndexString);
				fileName.setId(1);
				
				attachements.put(counter, lastIndexString);
				
				RelativeLayout relativeContainer = new RelativeLayout(getApplicationContext());
				
				ImageView crossImage = new ImageView(getApplicationContext());
				crossImage.setImageDrawable(getResources().getDrawable(R.drawable.cross1));
				crossImage.setId(counter * 4);
				crossImage.setOnClickListener(new OnClickListener() {
					
					@Override
					public void onClick(View v) {
						// TODO Auto-generated method stub
						int id = v.getId() / 4;
						RelativeLayout view = (RelativeLayout)attachmentContainer.findViewById(id);
						((ViewGroup)view.getParent()).removeView(view);	

						attachements.remove(id);
					}
				});
				
				RelativeLayout.LayoutParams relativeParamsCrossImage = 
						new RelativeLayout.LayoutParams(RelativeLayout.LayoutParams.WRAP_CONTENT, 
								RelativeLayout.LayoutParams.WRAP_CONTENT);
				
				RelativeLayout.LayoutParams relativeParams = 
						new RelativeLayout.LayoutParams(RelativeLayout.LayoutParams.FILL_PARENT,
								RelativeLayout.LayoutParams.WRAP_CONTENT);
				RelativeLayout.LayoutParams relativeParamsFile = 
						new RelativeLayout.LayoutParams(RelativeLayout.LayoutParams.WRAP_CONTENT,
								RelativeLayout.LayoutParams.WRAP_CONTENT);
				relativeParamsFile.addRule(RelativeLayout.ALIGN_PARENT_LEFT, fileName.getId());
				relativeContainer.addView(fileName, relativeParamsFile);
				
				relativeParamsCrossImage.addRule(RelativeLayout.ALIGN_PARENT_RIGHT, crossImage.getId());
				relativeParamsCrossImage.addRule(RelativeLayout.ALIGN_TOP, fileName.getId());
				relativeParamsCrossImage.addRule(RelativeLayout.ALIGN_BOTTOM, fileName.getId());
				
				relativeContainer.addView(crossImage, relativeParamsCrossImage);
				relativeContainer.setId(counter++);
				
				attachmentContainer.addView(relativeContainer, relativeParams);
				
				
			}
		});
		
		//You can change the default filename using the public variable "Default_File_Name"
		FileOpenDialog.Default_File_Name = "";
		FileOpenDialog.chooseFile_or_Dir();

		// TODO Maintain list for adding and removing attachment 
//		choosenFile = "/first/second/third/HelloWorld " + counter;
//		int index = choosenFile.lastIndexOf("/");
//		String lastIndexString = choosenFile.substring(index + 1, choosenFile.length());
//		TextView fileName = new TextView(getApplicationContext());
//		fileName.setText(lastIndexString);
//		fileName.setId(1);
//		
//		attachements.put(counter, lastIndexString);
//		
//		RelativeLayout relativeContainer = new RelativeLayout(getApplicationContext());
//		
//		ImageView crossImage = new ImageView(getApplicationContext());
//		crossImage.setImageDrawable(getResources().getDrawable(R.drawable.cross1));
//		crossImage.setId(counter * 4);
//		crossImage.setOnClickListener(new OnClickListener() {
//			
//			@Override
//			public void onClick(View v) {
//				// TODO Auto-generated method stub
//				int id = v.getId() / 4;
//				RelativeLayout view = (RelativeLayout)attachmentContainer.findViewById(id);
//				((ViewGroup)view.getParent()).removeView(view);	
//
//				attachements.remove(id);
//			}
//		});
//		
//		RelativeLayout.LayoutParams relativeParamsCrossImage = 
//				new RelativeLayout.LayoutParams(RelativeLayout.LayoutParams.WRAP_CONTENT, 
//						RelativeLayout.LayoutParams.WRAP_CONTENT);
//		
//		RelativeLayout.LayoutParams relativeParams = 
//				new RelativeLayout.LayoutParams(RelativeLayout.LayoutParams.FILL_PARENT,
//						RelativeLayout.LayoutParams.WRAP_CONTENT);
//		RelativeLayout.LayoutParams relativeParamsFile = 
//				new RelativeLayout.LayoutParams(RelativeLayout.LayoutParams.WRAP_CONTENT,
//						RelativeLayout.LayoutParams.WRAP_CONTENT);
//		relativeParamsFile.addRule(RelativeLayout.ALIGN_PARENT_LEFT, fileName.getId());
//		relativeContainer.addView(fileName, relativeParamsFile);
//		
//		relativeParamsCrossImage.addRule(RelativeLayout.ALIGN_PARENT_RIGHT, crossImage.getId());
//		relativeParamsCrossImage.addRule(RelativeLayout.ALIGN_TOP, fileName.getId());
//		relativeParamsCrossImage.addRule(RelativeLayout.ALIGN_BOTTOM, fileName.getId());
//		
//		relativeContainer.addView(crossImage, relativeParamsCrossImage);
//		relativeContainer.setId(counter++);
//		
//		attachmentContainer.addView(relativeContainer, relativeParams);
		
	}
	
	
	private void saveMailInDrafts()
	{
		Toast.makeText(getApplicationContext(), "Please wait for update for this feature.", Toast.LENGTH_SHORT).show();
	}
}
