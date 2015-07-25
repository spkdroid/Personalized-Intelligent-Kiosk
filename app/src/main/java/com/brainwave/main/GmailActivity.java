package com.brainwave.main;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;

import javax.mail.Address;
import javax.mail.Message;
import javax.mail.Message.RecipientType;
import javax.mail.MessagingException;

import com.brainwave.mail.MailTask;
import com.brainwave.mail.MailSystem.AccountType;
import com.brainwave.mail.MailSystem.TaskType;

import android.app.AlertDialog;
import android.app.ListActivity;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.os.StrictMode;
import android.util.Log;
import android.util.SparseArray;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.AdapterView.OnItemSelectedListener;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.Toast;

/**
 * Email code is generic but account type is neede to 
 * differentiate between the accounts
 * 
 * 
 * @author Mustansar Saeed
 *
 */
public class GmailActivity extends MainEmailActivity {
	public AccountType getAccountType() {
		// TODO Auto-generated method stub
		return AccountType.GMAIL;
	}

}
