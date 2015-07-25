package com.brainwave.main;

import java.util.ArrayList;
import java.util.EnumMap;
import java.util.Iterator;
import java.util.Map;

import com.brainwave.mail.MailSystem;
import com.brainwave.mail.MailSystem.AccountType;
import com.spkdroid.dayatdalserver.R;

import android.os.Bundle;
import android.app.Activity;
import android.app.TabActivity;
import android.content.Intent;
import android.graphics.drawable.Drawable;
import android.util.Log;
import android.util.SparseArray;
import android.view.Menu;
import android.widget.TabHost;
import android.widget.TabHost.TabSpec;

/**
 * Mail class to build view and tabs for the configured accounts
 * 
 * 
 * @author Mustansar Saeed
 *
 */
public class MainActivity extends TabActivity {
	
	EnumMap<AccountType, String> accountsMap = null;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

		setContentView(R.layout.activity_main_email);
		
		if(savedInstanceState == null)
		{
			accountsMap = MailSystemApplication.mailSystem.getEmailAccounts();
		}
		else
		{
			accountsMap = (EnumMap<AccountType, String>) savedInstanceState.getSerializable("accounts");
		}
		
		populateAccounts(savedInstanceState);	

	}
	
	@Override
	protected void onSaveInstanceState(Bundle outState) {
		// TODO Auto-generated method stub
		outState.putSerializable("accounts", accountsMap);
		
		super.onSaveInstanceState(outState);
	}
	
//	@Override
//	protected void onRestoreInstanceState(Bundle state) {
//		// TODO Auto-generated method stub
//		accountsMap = (EnumMap<AccountType, String>) state.getSerializable("accounts");
//		populateAccounts(state);
//		
//		super.onRestoreInstanceState(state);
//	}

	public void populateAccounts (Bundle state)
	{
		TabHost tabHost = getTabHost();
		AccountType defaultAccountType = null;
		
//		if(state == null)
//		{
//			accountsMap = MailSystemApplication.mailSystem.getEmailAccounts();	
//		}
		
		defaultAccountType = (AccountType) getIntent().getSerializableExtra("accountType");
		
		Iterator<Map.Entry<AccountType, String>> iterator = accountsMap.entrySet().iterator();
		while(iterator.hasNext())
		{
			Map.Entry<AccountType, String> account = iterator.next();

			TabSpec tabSpec = tabHost.newTabSpec(account.getValue());
			tabSpec.setIndicator(account.getValue(), getTabIcon(account.getKey()));
			
			Log.i("mustang", "account.getKey::: " + account.getKey());
			Log.i("mustang", "account.defaultAccount::: " + defaultAccountType);
			Intent accountIntent = new Intent(this, getAccountHandler(account.getKey()));
			tabSpec.setContent(accountIntent);

			tabHost.addTab(tabSpec);
			
			if(account.getKey() == defaultAccountType)
			{
				tabHost.setCurrentTabByTag(account.getValue());
			}
		}
	}

	public  Class<? extends Object> getAccountHandler(AccountType accountType)
	{
		switch(accountType)
		{
		case GMAIL:
			return GmailActivity.class;
		case YAHOO:
			return YahooActivity.class;
		case AddAccount:
			return AddAccountActivity.class;
		}

		return AddAccountActivity.class;	
	}

	public  Drawable getTabIcon(AccountType accountType)
	{
		switch(accountType)
		{
		case GMAIL:
			return getResources().getDrawable(R.drawable.ic_launcher);
		case YAHOO:
			return getResources().getDrawable(R.drawable.ic_launcher);
		}

		return getResources().getDrawable(R.drawable.ic_launcher);
	}





	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.main, menu);
		return true;
	}

}
