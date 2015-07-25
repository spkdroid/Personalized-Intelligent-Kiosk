package com.brainwave.main;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.brainwave.mail.MailTask;
import com.brainwave.mail.MailSystem.AccountType;
import com.brainwave.mail.MailSystem.TaskType;
import com.spkdroid.dayatdalserver.R;

/**
 * Main view to add new account
 * 
 * @author Mustansar Saeed
 *
 */
public class AddAccountActivity extends Activity {
	
	private EditText userName = null;
	private EditText password = null;
	private TextView errorView = null;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		
		if(savedInstanceState != null && savedInstanceState.getString("username") != null)
		{
			setContentView(R.layout.add_account);
			
			userName = (EditText) findViewById(R.id.edit_user);
			password = (EditText) findViewById(R.id.edit_password);
			errorView = (TextView) findViewById(R.id.errorText);
			
			String username = savedInstanceState.getString("username");
			String passwrd = savedInstanceState.getString("password");
			String error = savedInstanceState.getString("errorText"); 
			
			userName.setText(username);
			password.setText(passwrd);
			errorView.setText(error);
			if(!error.isEmpty())
			{
				errorView.setTextColor(getResources().getColor(R.color.red));
				errorView.setVisibility(TextView.VISIBLE);
			}
		}
		else
		{
			setContentView(R.layout.no_account);
			
			Button addAccount = (Button) findViewById(R.id.addAccount);
			addAccount.setOnClickListener(new View.OnClickListener() {
				
				@Override
				public void onClick(View v) {
					// TODO Auto-generated method stub
					setContentView(R.layout.add_account);				
					
					userName = (EditText) findViewById(R.id.edit_user);
					password = (EditText) findViewById(R.id.edit_password);
					errorView = (TextView) findViewById(R.id.errorText);
				}
			});
		}
	}
	
	@Override
	protected void onSaveInstanceState(Bundle outState) {
		// TODO Auto-generated method stub
		if(userName != null)
		{
			outState.putString("username", userName.getText().toString());
		}
		
		if(password != null)
		{
			outState.putString("password", password.getText().toString());
		}
		
		if(errorView != null)
		{
			outState.putString("errorText", errorView.getText().toString());
		}
		super.onSaveInstanceState(outState);
	}
	
	public void createAccount(View view)
	{
		String username = this.userName.getText().toString();
		String password = this.password.getText().toString();
		
		checkIsValid(username, password);
	}

	private void checkIsValid(String username, String password)
	{
		if(username.isEmpty() || password.isEmpty())
		{
			setErrorView(ErrorType.EMPTY);
		}
		else if(!isValidFormat(username))
		{
			setErrorView(ErrorType.INVALID);
		}
		else if(MailSystemApplication.mailSystem.isAlreadyExist(username))
		{
			setErrorView(ErrorType.ALREADY_EXIST);
		}
		else
		{
			new MailTask(this).execute(username, password, getAccountType(username), null, null, null, TaskType.VERIFY_CREDENTIALS);
		}
	}
	
	private AccountType getAccountType(String username)
	{
		if(username.contains("yahoo"))
		{
			return AccountType.YAHOO;
		}
		else if(username.contains("gmail"))
		{
			return AccountType.GMAIL;
		}
		
		return AccountType.GMAIL;
	}
	
	private void setErrorView(ErrorType errorType)
	{
		errorView.setText(getErrorText(errorType));
		errorView.setTextColor(getResources().getColor(R.color.red));
		errorView.setVisibility(TextView.VISIBLE);
	}
	
	public void invalidUserOrPassword(boolean isValid)
	{
		if(isValid)
		{
			correctCredentials();
		}
		else
		{
			setErrorView(ErrorType.INVALID);	
		}
		
	}
	
	public void correctCredentials()
	{
		errorView.setText("");
		errorView.setVisibility(TextView.INVISIBLE);
		
		String username = this.userName.getText().toString();
		String password = this.password.getText().toString();
		
		MailSystemApplication.mailSystem.addAccount(username, password);
		
		Intent mainIntent = new Intent(this, MainActivity.class);
		mainIntent.putExtra("accountType", getAccountType(username));
		startActivity(mainIntent);
		finish();
	}
	
	private boolean isValidFormat(String username)
	{
		return username.matches("[a-zA-Z0-9\\-\\_\\.]+@[a-zA-Z0-9]+\\.[a-zA-Z0-9]{3}");
	}
	
	private String getErrorText(ErrorType error)
	{
		switch(error)
		{
		case EMPTY:
			return "Please enter username and password";
		case INVALID:
			return "Invalid username/password";
		case ALREADY_EXIST:
			return "Oops. Currently only one account can be configured for same service provider.";
		default:
				return "";
		}
	}
	
	enum ErrorType
	{
		EMPTY, INVALID, ALREADY_EXIST
	}

}

