package com.brainwave.main;

import com.brainwave.mail.MailSystem;

import android.app.Application;

/**
 * Global context mail system
 * 
 * @author Mustansar Saeed
 *
 */
public class MailSystemApplication extends Application {
	public static MailSystem mailSystem = MailSystem.getMailSystem();
}
