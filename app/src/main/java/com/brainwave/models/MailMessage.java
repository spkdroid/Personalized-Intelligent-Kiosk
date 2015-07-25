package com.brainwave.models;

import java.util.ArrayList;

import javax.mail.Message;

import android.os.Parcel;
import android.os.Parcelable;
import android.util.Log;

/**
 * 
 * @author Mustansar Saeed
 *
 */
public class MailMessage implements Parcelable{
	
	private Object bodyContent;
	private String subject;
	private boolean isSelected;
	private ArrayList<String> fromArray;
	private ArrayList<String> toArray;
	private String receivedDate;
	private boolean isSeen;
	private int messageNumber;
	
	public MailMessage(Object bodyContent, String subject, boolean isSelected,
			ArrayList<String> fromArray, ArrayList<String> toArray,
			String receivedDate, boolean isSeen, int messageNumber) {
		super();
		this.bodyContent = bodyContent;
		this.subject = subject;
		this.isSelected = isSelected;
		this.fromArray = fromArray;
		this.toArray = toArray;
		this.receivedDate = receivedDate;
		this.isSeen = isSeen;
		this.messageNumber = messageNumber;
	}
	
	public Object getBodyContent() {
		return bodyContent;
	}
	public void setBodyContent(Object bodyContent) {
		this.bodyContent = bodyContent;
	}
	public String getSubject() {
		return subject;
	}
	public void setSubject(String subject) {
		this.subject = subject;
	}
	public boolean isSelected() {
		return isSelected;
	}
	public void setSelected(boolean isSelected) {
		this.isSelected = isSelected;
	}
	public ArrayList<String> getFromArray() {
		return fromArray;
	}
	public void setFromArray(ArrayList<String> fromArray) {
		this.fromArray = fromArray;
	}
	public ArrayList<String> getToArray() {
		return toArray;
	}
	public void setToArray(ArrayList<String> toArray) {
		this.toArray = toArray;
	}
	public String getReceivedDate() {
		return receivedDate;
	}
	public void setReceivedDate(String receivedDate) {
		this.receivedDate = receivedDate;
	}
	
	public void setIsSeen(boolean isSeen)
	{
		this.isSeen = isSeen;
	}
	
	public boolean getIsSeen()
	{
		return this.isSeen;
	}
	
	public void setMessageNumber(int msgNo)
	{
		this.messageNumber = msgNo;
	}
	
	public int getMessageNumber()
	{
		return this.messageNumber;
	}

	@Override
	public int describeContents() {
		// TODO Auto-generated method stub
		return 0;
	}

	@Override
	public void writeToParcel(Parcel out, int arg1) {
		// TODO Auto-generated method stub
		
		out.writeValue(bodyContent);
		out.writeString(subject);
		out.writeValue(isSelected);
		out.writeList(fromArray);
		out.writeList(toArray);
		out.writeString(receivedDate);
		out.writeValue(isSeen);
		out.writeInt(messageNumber);
		
		
	}
	
	public static final Parcelable.Creator<MailMessage> CREATOR = new Creator<MailMessage>() {
		
		@Override
		public MailMessage[] newArray(int size) {
			// TODO Auto-generated method stub
			return new MailMessage[size];
		}
		
		@Override
		public MailMessage createFromParcel(Parcel source) {
			// TODO Auto-generated method stub
			return new MailMessage(source.readValue(Object.class.getClassLoader()), source.readString(), 
					(Boolean)source.readValue(Boolean.class.getClassLoader()), 
					source.readArrayList(String.class.getClassLoader()), 
					source.readArrayList(String.class.getClassLoader()), 
					source.readString(), (Boolean)source.readValue(Boolean.class.getClassLoader()), 
					source.readInt());
		}
	};
}
