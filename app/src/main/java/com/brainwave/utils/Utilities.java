package com.brainwave.utils;

import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

import javax.mail.Address;
import javax.mail.Flags;
import javax.mail.Message;
import javax.mail.MessagingException;

import com.brainwave.main.MailSystemApplication;
import com.brainwave.models.MailMessage;

/**
 * Generic functions/utilities
 * 
 * @author Mustansar Saeed
 *
 */
public class Utilities {
	
	public static ArrayList<String> getFromArray(Message message)
	{
		ArrayList<String> fromList = new ArrayList<String>();
		
		try {
			for(Address address : message.getFrom())
			{
				fromList.add(address.toString());
			}
		} catch (MessagingException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		return fromList;
	}
	
	public static ArrayList<String> getToArray(Message message)
	{
		ArrayList<String> toList = new ArrayList<String>();
		
		try {
			for(Address address : message.getAllRecipients())
			{
				toList.add(address.toString());
			}
		} catch (MessagingException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		return toList;
	}
	
	public static String getFormattedString(String format, Date date)
	{
		return new SimpleDateFormat(format).format(date);
	}

	public static ArrayList<MailMessage> getMailMessages(Message[] messages) throws MessagingException, IOException
	{
		ArrayList<MailMessage> messges = new ArrayList<MailMessage>();
		Message message = null;
		
		for (int msg = 0; msg < messages.length; msg++)
		{
			message = messages[msg];
			if(message != null)
			{
				messges.add(new MailMessage("", 
						message.getSubject(), false, getFromArray(message), getToArray(message), 
						getFormattedString("yyyy MMM dd", message.getReceivedDate()), message.isSet(Flags.Flag.SEEN),
						message.getMessageNumber()));
			}
		}
		
		return messges;
	}
}
