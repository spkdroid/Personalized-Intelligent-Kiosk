package com.brainwave.main;

import com.brainwave.mail.MailSystem.AccountType;

import android.app.Activity;
import android.os.Bundle;

/**
 * Email code is generic but account type is neede to 
 * differentiate between the accounts
 * 
 * @author Mustansar Saeed
 *
 */
public class YahooActivity extends MainEmailActivity {

	@Override
	public AccountType getAccountType() {
		// TODO Auto-generated method stub
		return AccountType.YAHOO;
	}
	
}
