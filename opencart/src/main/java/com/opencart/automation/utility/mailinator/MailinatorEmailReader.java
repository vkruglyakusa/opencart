/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.opencart.automation.utility.mailinator;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.apache.log4j.Logger;
import org.apache.log4j.PropertyConfigurator;

import com.opencart.automation.utility.mailinator.InboxMessage;
import com.opencart.automation.utility.mailinator.Mailinator;

/**
 *
 * @author Valeriy Kruglyak
 */
public class MailinatorEmailReader {
	public static final Logger log = Logger.getLogger(MailinatorEmailReader.class.getName());
	private static final String API_KEY = "4028d7ec7ec24e4891cdff73004b82c2";
	private static String msgId = "";
	private static String msgSbjc = "";
	
	public MailinatorEmailReader() {
		String log4jConfPath = "configuration/log4j.properties";
		PropertyConfigurator.configure(log4jConfPath);
	}
		
	/**
	 * Get emails from email box
	 */
	private static List<InboxMessage> getInboxMessages(String emailAddress) throws Exception {
		//emailAddress = "account_regestration@dgilliam-doitt.mailinator.com";
		List<InboxMessage> inboxMessages = Mailinator.getInboxMessages(API_KEY, emailAddress, true);
		for (InboxMessage imsg : inboxMessages) {
			msgSbjc = imsg.getSubject();
			log.info("Subject: " + msgSbjc);
			msgId = imsg.getId();
			log.info("Mail ID: " + msgId);
		}
		return inboxMessages;
	}

	/**
	 * Method is extracting confirmation URL
	 * @param emailID
	 * @param subject
	 * @param baseUrl
	 * @return returns single URL
	 * @throws Exception
	 */
	private static String extractUrlfromBody(String emailID, String subject, String baseUrl) throws Exception {
		String doittUrl = null;
		//log.info("------------getting confirmation URL -----------------");
		String emailId = emailID;
		ArrayList<String> bodyResults = Mailinator.getEmailBody(emailId, API_KEY, true);
		String actualLink;
		for (String body : bodyResults) {
			List<String> extractedUrls = extractUrls(body);
			for (String url : extractedUrls) {
				if (url.startsWith(baseUrl)) {
					actualLink = extractRequestedUrl(url, subject, baseUrl);
					if (!actualLink.toLowerCase().contains("deactivate".toLowerCase())
							&& !actualLink.toLowerCase().contains("amp;".toLowerCase())) {
						if (actualLink.toLowerCase().contains("www.mailinator.com".toLowerCase())) {
							doittUrl = actualLink.substring(0, actualLink.indexOf('<'));
						} else {
							if(actualLink.toLowerCase().contains("<".toLowerCase())) {
								doittUrl = actualLink.substring(0, actualLink.indexOf('<'));
							}else {
								doittUrl = actualLink;
							}
							//doittUrl = actualLink;
						}

					}
				}
			}
		}
		log.info("Confirmation URL is : " + doittUrl);
		return doittUrl;
	}
	
	/**
	 * Method is extracting deactivation URL
	 * @param emailID
	 * @param subject
	 * @param baseUrl
	 * @return returns single URL
	 * @throws Exception
	 */
	private static String extractDeactivationUrlfromBody(String emailID, String subject, String baseUrl) throws Exception {
		String doittUrl = null;
		//log.info("------------getting deactivation URL -----------------");
		String emailId = emailID;
		ArrayList<String> bodyResults = Mailinator.getEmailBody(emailId, API_KEY, true);
		String actualLink;
		for (String body : bodyResults) {
			List<String> extractedUrls = extractUrls(body);
			for (String url : extractedUrls) {
				if (url.startsWith(baseUrl)) {
					actualLink = extractRequestedUrl(url, subject, baseUrl);
					if (actualLink.toLowerCase().contains("deactivate".toLowerCase())
							&& !actualLink.toLowerCase().contains("amp;".toLowerCase())) {
						if (actualLink.toLowerCase().contains("www.mailinator.com".toLowerCase())) {
							doittUrl = actualLink.substring(0, actualLink.indexOf('<'));
						} else {
							doittUrl = actualLink;
						}

					}
				}
			}
		}
		log.info("Deactivation URL is : " + doittUrl);
		return doittUrl;
	}

	/**
	 * Find stsring by the pattern in the text
	 * @param text
	 * @return
	 */
	private static List<String> extractUrls(String text) {
		List<String> containedUrls = new ArrayList<String>();
		String urlRegex = "((https?|ftp|gopher|telnet|file):((//)|(\\\\))+[\\w\\d:#@%/;$()~_?\\+-=\\\\\\.&]*)";
		Pattern pattern = Pattern.compile(urlRegex, Pattern.CASE_INSENSITIVE);
		Matcher urlMatcher = pattern.matcher(text);
		while (urlMatcher.find()) {
			containedUrls.add(text.substring(urlMatcher.start(0), urlMatcher.end(0)));
		}
		return containedUrls;
	}

	private static String extractRequestedUrl(String url, String subject, String baseUrl) {

		String malinatorUrl = null;
		String urlRegex = null;

		if (url.toLowerCase().contains("validateToken".toLowerCase())) {
			urlRegex = baseUrl + "validateToken";
		} else if (url.toLowerCase().contains("validateReset".toLowerCase())) {
			urlRegex = baseUrl + "validateReset";
		} else if (url.toLowerCase().contains("validateChangeEmail".toLowerCase())) {
			urlRegex = baseUrl + "user/validateChangeEmail";
		} else if (url.toLowerCase().contains("feedback".toLowerCase())) {
			urlRegex = baseUrl + "feedback";
		} else {
			urlRegex = baseUrl + "deactivate";
		}

		Pattern pattern = Pattern.compile(urlRegex, Pattern.CASE_INSENSITIVE);
		Matcher urlMatcher = pattern.matcher(url);
		if (urlMatcher.find()) {
			malinatorUrl = url;
		}

		return malinatorUrl;
	}

	public static String getConfirmationUrl(String baseURL, String emailAddress) throws Exception {
		String url = null;
		int tryCount = 0;
		List<InboxMessage> inboxMessages = null;
		Thread.sleep(5000);
		do {
			log.info("Trying to extract confirmation URL from email");
			inboxMessages = getInboxMessages(emailAddress);
			tryCount++;
			Thread.sleep(10000);
		} while (inboxMessages.size() == 0 && tryCount <= 60);
		
		if (!msgId.equals("")) {
			url = extractUrlfromBody(msgId, msgSbjc, baseURL);
		}else {
			url = baseURL;
			log.info("Can't extract confirmation URL from email");
		}
		clearSpecificMailbox(emailAddress);
		return url;
	}
	
	public static String getDeactivationUrl(String baseURL, String emailAddress) throws Exception {
		String url = null;
		int tryCount = 0;
		List<InboxMessage> inboxMessages = null;
		Thread.sleep(5000);
		do {
			log.info("Trying to extract deactivation URL from email");
			inboxMessages = getInboxMessages(emailAddress);
			tryCount++;
			Thread.sleep(10000);
		} while (inboxMessages.size() == 0 && tryCount <= 60);
		
		if (!msgId.equals("")) {
			url = extractDeactivationUrlfromBody(msgId, msgSbjc, baseURL);
		}else {
			url = baseURL;
			log.info("Can't extract deactivation URL from email");
		}
		clearSpecificMailbox(emailAddress);
		return url;
	}
	
	/**
	 * Method is deleting all messages from private group email box
	 */
	public static void clearMailbox(){
		log.info("Trying to clear mailbox");
		try {
			Mailinator.deleteAllEmails(API_KEY,true, true);
		} catch (IOException e) {
			log.info("Can't clear mailbox");
			e.printStackTrace();
		}
		log.info("Mailbox has been cleared");
	}
	
	/**
	 * Method is deleting all messages from specific mail box
	 */
	public static void clearSpecificMailbox(String emailAddress){
		log.info("Trying to clear mailbox for address - " + emailAddress);
		try {
			getInboxMessages(emailAddress);
			Mailinator.deleteEmail(API_KEY, msgId, true);
		} catch (Exception e) {
			log.info("Can't clear mailbox");
			e.printStackTrace();
		}
		log.info("Mailbox has been cleared");
	}

	public static void main(String[] args) throws Exception {
		String log4jConfPath = "configuration/log4j.properties";
		PropertyConfigurator.configure(log4jConfPath);
		
		String baseUrl = "https://accounts-nonprd.nyc.gov/account/";
		String emailId = "account_regestration-1564161570-23536129";
		String subject = "NYC.gov - NYC.ID Confirm Your Email Address";

		// String baseUrl = "https://accounts-nonprd.nyc.gov/account/";
		// String emailId = "account_regestration-1564156250-23295423";
		// String subject = "NYC.gov - NYC.ID Reset Your Password";
//		getInboxMessages();
//		extractUrlfromBody(emailId, subject, baseUrl);
		clearMailbox();
//		getConfirmationUrl(baseUrl,"test1564590024675@dgilliam-doitt.mailinator.com");
		//getDeactivationUrl(baseUrl);
	}
}
