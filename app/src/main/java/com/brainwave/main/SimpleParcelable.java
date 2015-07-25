package com.brainwave.main;

import java.sql.Date;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

import javax.mail.Message;

import android.os.Bundle;
import android.os.Parcel;
import android.os.Parcelable;

/**
 * Data serialization class
 * 
 * @author Mustansar Saeed
 *
 */
public class SimpleParcelable implements Parcelable{
	private Map<String, Message[]> emailsMap;
	private Message[] messageArray;
	
	public SimpleParcelable(HashMap<String, Message[]> messages)
	{
		emailsMap = messages;
	}
	
	public SimpleParcelable(Message[] messages)
	{
		messageArray = messages;
	}
	
	public static final Parcelable.Creator<SimpleParcelable> CREATOR 
		= new Parcelable.Creator<SimpleParcelable>() {

			@Override
			public SimpleParcelable createFromParcel(Parcel source) {
				return new SimpleParcelable((Message[])source.readArray(Message.class.getClassLoader()));
			}

			@Override
			public SimpleParcelable[] newArray(int size) {
				// TODO Auto-generated method stub
				return new SimpleParcelable[size];
			}
		};
	@Override
	public int describeContents() {
		// TODO Auto-generated method stub
		return 0;
	}

	@Override
	public void writeToParcel(Parcel dest, int flags) {
//		Bundle b = new Bundle();
//		b.putSerializable("map", (HashMap<String, Message[]>)emailsMap);
		
//		ArrayList<Message[]> messages = new ArrayList<Message[]>(emailsMap.values());
//		
//		dest.writeList(messages);
		dest.writeArray(emailsMap.values().toArray());
	}
	
	public Message[] getMessage()
	{
		return this.messageArray;
	}
	
	public Map<String, Message[]> getEmailsMap()
	{
		return emailsMap;
	}
}
