package com.brainwave.main;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;

import javax.mail.Flags.Flag;
import javax.mail.Message;
import javax.mail.MessagingException;

import com.brainwave.models.MailMessage;
import com.spkdroid.dayatdalserver.R;

import android.app.Activity;
import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.CompoundButton.OnCheckedChangeListener;
import android.widget.TextView;
/**
 * Generate custom list adapter
 * 
 * @author Mustansar Saeed
 *
 */
public class GMailAdapter extends ArrayAdapter<MailMessage>{

	private ArrayList<MailMessage> emails;
	private final MainEmailActivity context;
	
	private ArrayList<Integer> checkedPositions;
	private HashMap<Integer, CompoundButton> buttons = new HashMap<Integer, CompoundButton>();
	boolean[] checkedState;
	
	public GMailAdapter(MainEmailActivity context, ArrayList<MailMessage> objects) {
		super(context, R.layout.gmail_list_layout, objects);
		this.emails = objects;
		this.context = context;
		checkedPositions = new ArrayList<Integer>();
		checkedState = new boolean[objects.size()];
	}
	
	static class ViewHolder {
		protected TextView subjectView;
		protected TextView fromView;
		protected TextView timeView;
		protected CheckBox checkView;
		protected ViewGroup rootLayout;
	}
	
	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		View view = convertView;
		ViewHolder viewHolder = null;
		if(view == null) 
		{
			LayoutInflater inflator = context.getLayoutInflater();
			view = inflator.inflate(R.layout.gmail_list_layout, null);
			
			viewHolder = new ViewHolder();
			
			viewHolder.checkView = (CheckBox) view.findViewById(R.id.check);
			viewHolder.timeView = (TextView) view.findViewById(R.id.time_content);
			viewHolder.fromView = (TextView) view.findViewById(R.id.from_content);
			viewHolder.subjectView = (TextView) view.findViewById(R.id.subject_content);
			viewHolder.rootLayout = (ViewGroup) view.findViewById(R.id.rootGmail);
			
			view.setTag(viewHolder);
			
			
		}
		else
		{
			viewHolder = (ViewHolder) view.getTag();
		}
		
		MailMessage msg = emails.get(position);
		
		viewHolder.checkView.setOnCheckedChangeListener(null);
		viewHolder.checkView.setChecked(msg.isSelected());
		
		viewHolder.checkView.setOnCheckedChangeListener(checkListener);
		
		viewHolder.checkView.setTag(position);
		
		
		viewHolder.fromView.setText(msg.getFromArray().get(0));
		viewHolder.subjectView.setText(msg.getSubject());
		viewHolder.timeView.setText(msg.getReceivedDate());

		if(!msg.getIsSeen())
		{
			viewHolder.rootLayout.setBackgroundColor(context.getResources().getColor(R.color.white));
		}
		
		return view;
		
	}
		
	OnCheckedChangeListener checkListener = new OnCheckedChangeListener() {
		
		@Override
		public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
			Integer position = (Integer)buttonView.getTag();
			MailMessage message = emails.get(position);
			message.setSelected(isChecked);
			
			checkedState[position] = isChecked;
			if(isChecked)
			{
				checkedPositions.add(position);
			}
			else
			{
				checkedPositions.remove(position);
			}

			Log.i("mustang", "Positions Array: " + checkedPositions.size());

			context.itemsSelected(checkedPositions);	
		}
	};
	
	public ArrayList<Integer> getCheckedPositions()
	{
		return checkedPositions;
	}
	
	public void setCheckedPositions(ArrayList<Integer> positions)
	{
		checkedPositions = positions;
	}
	
	public void clearList()
	{
		checkedPositions.clear();
	}
	
}
