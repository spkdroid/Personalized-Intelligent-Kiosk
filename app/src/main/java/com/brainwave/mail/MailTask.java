package com.brainwave.mail;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import javax.mail.Message;
import javax.mail.MessagingException;

import com.brainwave.mail.MailSystem.AccountType;
import com.brainwave.mail.MailSystem.TaskType;
import com.brainwave.main.AddAccountActivity;
import com.brainwave.main.MainEmailActivity;
import com.brainwave.main.GmailActivity;
import com.brainwave.main.MailSystemApplication;
import com.brainwave.utils.Utilities;

import android.app.Activity;
import android.app.ProgressDialog;
import android.os.AsyncTask;
import android.util.Log;

/**
 * Single point to run tasks in background, credentials verification,
 * fetching emails, sending emails etc. 
 * 
 * @author Mustansar Saeed
 *
 */
public class MailTask extends AsyncTask {

	private ProgressDialog statusDialog;
	private Activity callerActivity;
	
	public MailTask(Activity activity)
	{
		this.callerActivity = activity;
	}
	
	@Override
	protected void onPreExecute() {
		statusDialog = new ProgressDialog(callerActivity);
//		statusDialog.setMessage("Preparing email ...");
		statusDialog.setIndeterminate(false);
		statusDialog.setCancelable(false);
		statusDialog.show();
	}
	
	@Override
	protected Object[] doInBackground(Object... object) {
		
		TaskType taskType = (TaskType) object[6];
		AccountType accountType = null;
		String username = "", password = "", host = "", folderName = "";
		Object[] objects = null;
		
		switch(taskType)
		{
		case SEND_EMAIL:
			String from = object[0].toString(), subject = object[2].toString(), body = object[3].toString();
			List<String> toList = (List<String>)object[1];
			accountType = (AccountType)object[4];
			
			sendEmail(from, toList, subject, body, accountType, (ArrayList<String>)object[5]);
			
			objects = new Object[2];
			objects[0] = null;
			objects[1] = taskType;
			return objects;
		case FETCH_EMAIL:
		case REFRESH_EMAIL:
			username = object[0].toString();
			password = object[1].toString();
			accountType = (AccountType) object[2];
			folderName = object[3].toString();
			host = object[4].toString();
			
			objects = new Object[3];
			objects[0] = fetchEmails(username, password, folderName, accountType, host);
			objects[1] = taskType;
			objects[2] = folderName;
			
			return objects;
		case DELETE:
			username = object[0].toString();
			password = object[1].toString();
			accountType = (AccountType) object[2];
			folderName = object[3].toString();
			host = object[4].toString();
			
			ArrayList<Integer> messagesToDelete = (ArrayList<Integer>)object[7];
			publishProgress("Deleting emails  ...");
			
			MailSystemApplication.mailSystem.deleteEmail(host, username, password, messagesToDelete, folderName);
			
			publishProgress("Emails deleted.  ...");
			
			objects = new Object[2];
			objects[0] = null;
			objects[1] = taskType;
			
			return objects;
		case VERIFY_CREDENTIALS:
			username = object[0].toString();
			password = object[1].toString();
			accountType = (AccountType)object[2];
			
			objects = new Object[2];
			
			publishProgress("Verifying credentials ...");
			
			objects[0] = MailSystemApplication.mailSystem.checkCredentials(username, password, accountType);
			objects[1] = taskType;
			return objects;
		case FETCH_LABELS:
			publishProgress("Fetching labels ...");
			accountType = (AccountType)object[0];
			
			objects = new Object[2];
			objects[0] = MailSystemApplication.mailSystem.getAccountLabels(accountType);
			objects[1] = taskType;
			
			return objects;
		default:
			return null;
		}
	}
	
	private Message[] fetchEmails(String username, String password, String folderName,
			AccountType accountType, String host)
	{
		Message[] mails = null;
		publishProgress("Fetching emails ...");
		List<Message> messages = MailSystemApplication.mailSystem.fetchEmailMessages(username, password, 
				folderName, accountType, host);
		publishProgress("Fetching completed.");
		mails = (Message[])messages.toArray();
		
		return Arrays.copyOfRange(mails, 0, 15);
	}
	
	private void sendEmail(String from, List<String> toList, String subject, String body, AccountType accountType,
			ArrayList<String> attachments)
	{
		publishProgress("Sending email ...");
		
		MailSystemApplication.mailSystem.composeEmailAndSend(from, toList, subject, body, accountType, attachments);
		
		publishProgress("Email sent.");
	}
	
	@Override
	protected void onProgressUpdate(Object... values) {
		statusDialog.setMessage(values[0].toString());
	}
	
	@Override
	protected void onPostExecute(Object result){
		
		statusDialog.dismiss();
		
		if(result instanceof Object[])
		{
			Object[] objects = (Object[])result;
			TaskType task = (TaskType) objects[1];
			
			switch(task)
			{
			case FETCH_EMAIL:
			case REFRESH_EMAIL:
				try {
					((MainEmailActivity)callerActivity).emailsFetched(Utilities.getMailMessages((Message[])objects[0]), 
							objects[2].toString(), task);
				} catch (MessagingException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				} catch (IOException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
				break;
			case DELETE:
				break;
			case SEND_EMAIL:
				callerActivity.finish();
				break;
			case VERIFY_CREDENTIALS:
				Boolean ret = (Boolean) objects[0];
				((AddAccountActivity)callerActivity).invalidUserOrPassword(ret.booleanValue());
				break;
			case FETCH_LABELS:
				try {
					((MainEmailActivity)callerActivity).showLabels((ArrayList<String>)objects[0]);
				} catch (MessagingException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				} catch (IOException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}
		}
	}

}
