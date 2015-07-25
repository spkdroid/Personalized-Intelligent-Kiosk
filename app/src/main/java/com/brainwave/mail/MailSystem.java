package com.brainwave.mail;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.EnumMap;
import java.util.HashMap;
import java.util.List;
import java.util.Properties;
import javax.mail.AuthenticationFailedException;
import javax.mail.Authenticator;
import javax.mail.Flags;
import javax.mail.Folder;
import javax.mail.Message;
import javax.mail.MessagingException;
import javax.mail.Multipart;
import javax.mail.NoSuchProviderException;
import javax.mail.PasswordAuthentication;
import javax.mail.Session;
import javax.mail.Store;
import javax.mail.Transport;
import javax.mail.internet.AddressException;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeBodyPart;
import javax.mail.internet.MimeMessage;
import javax.mail.internet.MimeMultipart;
import com.brainwave.main.MainEmailActivity;
import android.util.Log;

/**
 * Single point for the mail system. Communicate with server and return the results
 *  
 * @author Mustansar Saeed
 *
 */
public class MailSystem {
	private ArrayList<String> accounts = null;
	private EnumMap <AccountType, String> accountsMap = null;
	private EnumMap <AccountType, String> emailsMap = null;
	private HashMap <String, String> passwordsMap = null;
	
	private static MailSystem mailSystem = null; 
	
	private MailSystem()
	{
		Log.i("mustang", "Calling Mail System");
		accounts = new ArrayList<String> ();
		accountsMap = new EnumMap<AccountType, String>(AccountType.class);
		accountsMap.put(AccountType.AddAccount, "Add Account");
		emailsMap = new EnumMap<AccountType, String>(AccountType.class);
		passwordsMap = new HashMap<String, String>();
	}
	
	public static MailSystem getMailSystem()
	{
		if(mailSystem == null)
		{
			return new MailSystem();
		}
		
		return mailSystem;
	}
	
	public EnumMap<AccountType, String> getEmailAccounts()
	{
//		accountsMap.put(AccountType.GMAIL, "Gmail");
		return accountsMap;
	}
	
	public void setEmailAccounts(EnumMap<AccountType, String> accounts)
	{
		accountsMap = accounts;
	}
	
	public EnumMap<AccountType, String> getEmails()
	{
		return emailsMap;
	}
	
	public HashMap<String, String> getPasswordsMap()
	{
		return passwordsMap;
	}
	
	public void addAccount(String username, String password)
	{
		if(username.contains("gmail")) 
		{
			accountsMap.put(AccountType.GMAIL, "Gmail");
			emailsMap.put(AccountType.GMAIL, username);
		}
		else if(username.contains("yahoo"))
		{
			accountsMap.put(AccountType.YAHOO, "Yahoo");
			emailsMap.put(AccountType.YAHOO, username);
		}
		
		passwordsMap.put(username, password);
	}
	
	public ArrayList<String> getAccountLabels(AccountType accountType)
	{
		ArrayList<String> accountLabels = new ArrayList<String>();
		
		try {
			String username = emailsMap.get(accountType);
			String password = passwordsMap.get(username);
			
			Store store = this.getStore();
			store.connect(getHost(accountType), username, password);
			
			Folder[] folders = store.getDefaultFolder().list("*");
			
			for ( Folder folder : folders)
			{
				accountLabels.add(folder.getFullName());
			}
			
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		return accountLabels;
	}
	
	private Store getStore()
	{
		Properties props = new Properties();
		props.put("mail.store.protocol", "imaps");
		Session session = Session.getInstance(props, null);
		Store store = null;
		try {
			store = session.getStore();
		} catch (NoSuchProviderException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		return store;
	}
	
	
	public void processMailTask(MainEmailActivity activity, AccountType accountType, String folder, TaskType task)
	{
		String username = emailsMap.get(accountType);
		String password = passwordsMap.get(username);
		
		new MailTask(activity).execute(username, password, accountType, folder, getHost(accountType), null,
				task);
	}
	
	public void deleteMailsTask(MainEmailActivity activity, AccountType accountType, String folder, TaskType task,
			ArrayList<Integer> messages)
	{
		String username = emailsMap.get(accountType);
		String password = passwordsMap.get(username);
		
		new MailTask(activity).execute(username, password, accountType, folder, getHost(accountType), null,
				task, messages);
	}
	
	public void deleteEmail(String host, String username, String password, ArrayList<Integer> messagesToDelete,
			String folderName)
	{
		Store store = this.getStore();
		
		try {
			store.connect(host, username, password);
			Folder folder = store.getFolder(folderName);
			int[] array = new int[messagesToDelete.size()];
			
			for(int count = 0; count < messagesToDelete.size(); count++)
			{
				array[count] = messagesToDelete.get(count);
			}
			
			Message[] msgs = folder.getMessages(array);
			for(Message msg : msgs)
			{
				msg.setFlag(Flags.Flag.DELETED, true);
			}
			
			folder.close(true);
			
		} catch (MessagingException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			
		}
	}
	
	
	HashMap<AccountType, Store> storesMap = new HashMap<AccountType, Store>();
	
	public List<Message> fetchEmailMessages(String username, String password, String folderName,
			AccountType accountType, String host)
	{
		Message[] mails = null; 
		List<Message> messages = null;
		Store store = this.getStore();
		
		try {
			store.connect(host, username, password);
			Folder folder = store.getFolder(folderName);
			folder.open(Folder.READ_ONLY);
			
			mails = folder.getMessages();
			
			messages = Arrays.asList(mails);
			
			return messages;
		} catch (MessagingException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			
			return null;
		}catch(Exception ex)
		{
			ex.printStackTrace();
			return new ArrayList<Message>();
		}
	}
	
	private String getHost(AccountType accountType) {
    	switch(accountType)
    	{
    	case GMAIL:
    		return "imap.gmail.com";
    	case YAHOO:
    		return "imap.mail.yahoo.com";
    	default:
    			return "imap.gmail.com";
    			
    	}
    }
    
    private Properties getProperties(AccountType accountType, String username, String password)
    {
    
    	String hostname = "", port = "";
    	Properties props = new Properties();
    	if(accountType == AccountType.YAHOO)
    	{
    		hostname = "smtp.mail.yahoo.com";
    		port = "465";
    	}
    	else if(accountType == AccountType.GMAIL)
    	{
    		hostname = "smtp.gmail.com";
    		port = "587";
    	}
    	
    	props.put("mail.smtp.host", hostname);
		props.put("mail.port", port);
		props.put("mail.smtp.user", username);
		props.put("mail.smtp.password", password);
		props.put("mail.smtp.auth", "true");
		props.put("mail.smtp.starttls.enable", "true");

    	return props;
    }
    
    
    public void composeEmailAndSend(String from, List<String> toList, String emailSubject, 
    		String emailBody, AccountType accountType, ArrayList<String> attachments)
    {
    	final String username = emailsMap.get(accountType);
    	final String password = passwordsMap.get(username);
    	
    	final PasswordAuthentication auth = new PasswordAuthentication(username, password);
    	
    	Session session = Session.getInstance(getProperties(accountType, username, password), new Authenticator() {
    		@Override
    		protected PasswordAuthentication getPasswordAuthentication() {
    			// TODO Auto-generated method stub
    			return auth;
    		}
		});
		
		try {
			MimeMessage emailMessage = new MimeMessage(session);
			emailMessage.setFrom(new InternetAddress(username));
			
			for(String toEmail : toList)
			{
				emailMessage.addRecipient(Message.RecipientType.TO, new InternetAddress(toEmail));
			}
			
			emailMessage.setSubject(emailSubject);
			
			
			if(attachments.size() > 0)
			{
				Multipart multiPart = new MimeMultipart();
				
				// content body part
				MimeBodyPart messageBodyPart = new MimeBodyPart();
				messageBodyPart.setContent(emailBody, "text/html");

				multiPart.addBodyPart(messageBodyPart);
				
				MimeBodyPart attachPart = null;
				for(String attachment : attachments)
				{
					// content attachment part
					attachPart = new MimeBodyPart();
					try {
						attachPart.attachFile(attachment);
					} catch (IOException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
					
					multiPart.addBodyPart(attachPart);
				}
				
				//set the multipart as message content
				emailMessage.setContent(multiPart);
			}
			else
			{
				emailMessage.setContent(emailBody, "text/html");
			}
			
			Transport transport = session.getTransport("smtp");
			transport.connect();
			transport.sendMessage(emailMessage, emailMessage.getAllRecipients());
			transport.close();
			
			
		}
		catch (AddressException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} 
		catch (MessagingException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		    	
    	
    }
    
    public boolean checkCredentials(String username, String password, AccountType accountType)
    {
    	Store store = this.getStore();
		
		try {
			store.connect(getHost(accountType), username, password);
//			store.close();
		    return true;
		} 
		catch(AuthenticationFailedException ex)
		{
			return false;
		}
		catch (MessagingException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			return false;
		}
    }
    
    
    
    public boolean isAlreadyExist(String username)
    {
    	if(username.contains("gmail") && emailsMap.get(AccountType.GMAIL) != null)
    	{
    		return true;
    	}
    	else if(username.contains("yahoo") && emailsMap.get(AccountType.YAHOO) != null)
    	{
    		return true;
    	}
    	
    	return false;
    }
    
	
	public static enum AccountType {
		GMAIL, YAHOO, AddAccount
	}
	
	public static enum TaskType {
		SEND_EMAIL, REFRESH_EMAIL, FETCH_EMAIL, VERIFY_CREDENTIALS, FETCH_LABELS, NONE, DELETE
	}
	
}
