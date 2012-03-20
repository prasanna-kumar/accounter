package com.vimukti.accounter.mail;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;

import org.apache.log4j.Logger;

import com.vimukti.accounter.core.Client;
import com.vimukti.accounter.core.ClientSubscription;
import com.vimukti.accounter.core.MaintananceInfoUser;
import com.vimukti.accounter.core.User;
import com.vimukti.accounter.main.PropertyParser;
import com.vimukti.accounter.main.ServerConfiguration;
import com.vimukti.accounter.web.client.Global;
import com.vimukti.accounter.web.client.core.ClientEmailAccount;
import com.vimukti.accounter.web.client.externalization.AccounterMessages;

public class UsersMailSendar {
	private static PropertyParser propertyParser;

	private static Logger LOG = Logger.getLogger(UsersMailSendar.class);

	public static PropertyParser getPropertyParser() {
		return propertyParser;
	}

	public static EMailSenderAccount getEmailAcc() {

		EMailSenderAccount acc = new EMailSenderAccount();

		acc.setOutGoingMailServer(propertyParser.getProperty(
				"outGoingMailServer", ""));
		acc.setPortNumber(Integer.parseInt(propertyParser.getProperty(
				"portNumber", "25")));
		acc.setProtocol(propertyParser.getProperty("protocol", ""));

		acc.setSmtpAuthentication(propertyParser.getProperty(
				"smtpAuthentication", "no").equalsIgnoreCase("yes"));

		acc.setSslAutheticationRequired(propertyParser.getProperty(
				"sslAutheticationRequired", "no").equalsIgnoreCase("yes"));

		acc.setEnableTls(propertyParser.getProperty("startTtlsEnables",
				"no").equalsIgnoreCase("yes"));

		String emailID = propertyParser.getProperty(
				"usermanagementSenderEmailID", "");
		acc.setSenderEmailID(emailID);

		String password = propertyParser.getProperty(
				"usermanagementSenderPassword", "");

		acc.setSenderPassword(password);
		return acc;

	}

	public static EMailSenderAccount getEmailAcc(ClientEmailAccount sender) {

		EMailSenderAccount acc = new EMailSenderAccount();

		acc.setOutGoingMailServer(sender.getSmtpMailServer());
		acc.setPortNumber(sender.getPortNumber());
		acc.setProtocol(propertyParser.getProperty("protocol", ""));

		acc.setSmtpAuthentication(true);

		acc.setSslAutheticationRequired(true);

		acc.setEnableTls(sender.isSSL());

		acc.setSenderEmailID(sender.getEmailId());

		acc.setSenderPassword(sender.getPassword());
		return acc;

	}

	public static void sendPdfMail(File file, String comapanyName,
			String subject, String content, ClientEmailAccount sender,
			String recipientEmail, String ccEmail) throws IOException {

		try {
			initPropertyParserToInviteUser();
			LOG.info("Email is being sent to external user");
		} catch (FileNotFoundException e) {
			e.printStackTrace();
			return;
		} catch (IOException e) {
			e.printStackTrace();
			return;
		}

		EMailMessage emailMsg = new EMailMessage();
		emailMsg.setContent(content);
		emailMsg.setSubject(subject);
		emailMsg.setReplayTO(sender.getEmailId());
		emailMsg.setAttachment(file);

		String[] toIds = recipientEmail.split(",");

		for (int i = 0; i < toIds.length; i++) {
			emailMsg.setRecepeant(toIds[i]);
		}
		if (ccEmail.trim().length() > 0) {
			String[] ccIds = ccEmail.split(",");
			for (int j = 0; j < ccIds.length; j++) {
				emailMsg.setccRecepeant(ccEmail);
			}
		}

		EMailJob job = new EMailJob(emailMsg, getEmailAcc(sender), comapanyName);

		EmailManager.getInstance().addJob(job);

	}

	public static void sendTestMail(ClientEmailAccount sender, String recipient)
			throws FileNotFoundException, IOException {

		initPropertyParserToInviteUser();

		String subject = propertyParser.getProperty("subjectForTestEmail", "");
		String content = propertyParser.getProperty("contentForTestEmail", "");

		EMailMessage emailMsg = new EMailMessage();
		emailMsg.setContent(content);
		emailMsg.setSubject(subject);
		emailMsg.setReplayTO(sender.getEmailId());

		String[] toIds = recipient.split(",");

		for (int i = 0; i < toIds.length; i++) {
			emailMsg.setRecepeant(toIds[i]);
		}

		EMailJob job = new EMailJob(emailMsg, getEmailAcc(sender));

		EmailManager.getInstance().addJob(job);
	}

	public static void sendMailToInvitedUser(Client user, String password,
			String companyName) {
		try {
			initPropertyParserToInviteUser();
			LOG.info("Invitation Email is being sent to user");
		} catch (FileNotFoundException e) {
			e.printStackTrace();
			return;
		} catch (IOException e) {
			e.printStackTrace();
			return;
		}

		String content = propertyParser.getProperty("contentForInviteUser", "");
		content = content
				.replaceAll("%USER%", getUserName(user.getFirstName()));
		content = content.replaceAll("%COMPANY%", companyName);
		content = content.replaceAll("%PASSWORD%", password);
		content = content.replaceAll("%EMAILID%", user.getEmailId());
		content = replaceServerUrl(content);
		// content = content.replaceAll("%LOGINURL%", loginURL);

		String subject = propertyParser.getProperty("subjectForInviteUser", "");
		subject = subject.replaceAll("%COMPANY%", companyName);

		EMailMessage emailMsg = new EMailMessage();
		emailMsg.setContent(content);
		emailMsg.setSubject(subject);
		emailMsg.setReplayTO(user.getEmailId());
		emailMsg.setRecepeant(user.getEmailId());
		EMailJob job = new EMailJob(emailMsg, getEmailAcc(), companyName);

		EmailManager.getInstance().addJob(job);

	}

	public static PropertyParser initPropertyParserToInviteUser()
			throws FileNotFoundException, IOException {
		if (propertyParser == null) {
			propertyParser = new PropertyParser();
			propertyParser.loadFile("config/MailConfig.ini");
		}
		return propertyParser;
	}

	public static void sendMailToDefaultUser(User admin, String companyName) {
		try {
			initPropertyParserToInviteUser();
			LOG.info("Email is being sent to default user");
		} catch (FileNotFoundException e) {
			e.printStackTrace();
			return;
		} catch (IOException e) {
			e.printStackTrace();
			return;
		}

		Client client = admin.getClient();
		String content = propertyParser
				.getProperty("contentForDefaultUser", "");
		content = content.replaceAll("%USERNAME%",
				getUserName(client.getFirstName()));
		content = content.replaceAll("%COMPANY%", companyName);
		content = replaceServerUrl(content);

		String subject = propertyParser
				.getProperty("subjectForDefaultUser", "");

		EMailMessage emailMsg = new EMailMessage();
		emailMsg.setContent(content);
		emailMsg.setSubject(subject);
		emailMsg.setRecepeant(client.getEmailId());
		EMailJob job = new EMailJob(emailMsg, getEmailAcc(), companyName);

		EmailManager.getInstance().addJob(job);
	}

	public static void sendMailToSupport(String name, String emailId,
			String subject, String message) {
		try {
			initPropertyParserToInviteUser();
			LOG.info("Email is being sent to default support user");
		} catch (FileNotFoundException e) {
			e.printStackTrace();
			return;
		} catch (IOException e) {
			e.printStackTrace();
			return;
		}

		String recipient = propertyParser.getProperty("recieverMailId", "");

		String content = propertyParser.getProperty("contentForSupport", "");
		content = content.replaceAll("%NAME%", name);
		content = content.replaceAll("%EMAILID%", emailId);
		content = content.replaceAll("%MESSAGE%", message);

		EMailMessage emailMsg = new EMailMessage();
		emailMsg.setContent(content);
		emailMsg.setSubject(subject);
		emailMsg.setRecepeant(recipient);
		EMailJob job = new EMailJob(emailMsg, getEmailAcc());

		EmailManager.getInstance().addJob(job);
	}

	public static void sendResetPasswordLinkToUser(String link,
			String activationCode, String recipient) {
		try {
			initPropertyParserToInviteUser();
			LOG.info("Email is being sent to default user");
		} catch (FileNotFoundException e) {
			e.printStackTrace();
			return;
		} catch (IOException e) {
			e.printStackTrace();
			return;
		}

		String subject = propertyParser.getProperty("subjectForResetPassword",
				"");

		String content = propertyParser.getProperty("contentForResetPassword",
				"");
		content = content.replaceAll("%LINK%", link);

		content = content.replaceAll("%CODE%", activationCode);

		EMailMessage emailMsg = new EMailMessage();
		emailMsg.setContent(content);
		emailMsg.setSubject(subject);
		emailMsg.setRecepeant(recipient);
		EMailJob job = new EMailJob(emailMsg, getEmailAcc());

		EmailManager.getInstance().addJob(job);
	}

	public static void sendActivationMail(String token, Client client) {

		try {
			initPropertyParserToInviteUser();
			LOG.info("Email is being sent to default user");
		} catch (FileNotFoundException e) {
			e.printStackTrace();
			return;
		} catch (IOException e) {
			e.printStackTrace();
			return;
		}

		String subject = propertyParser.getProperty("subjectForActivation", "");

		StringBuffer content = new StringBuffer();
		content.append("Hello " + getUserName(client.getFirstName()) + " "
				+ getUserName(client.getLastName()) + ",\n");
		content.append(propertyParser.getProperty("contentForActivation", ""));
		String contentStr = content.toString().replaceAll("%TOKEN%", token);
		contentStr = replaceServerUrl(contentStr);

		System.out.println("************* ACTIVATION CODE : " + token);

		EMailMessage emailMsg = new EMailMessage();
		emailMsg.setContent(contentStr);
		emailMsg.setSubject(subject);
		emailMsg.setRecepeant(client.getEmailId());
		EMailJob job = new EMailJob(emailMsg, getEmailAcc());

		EmailManager.getInstance().addJob(job);
	}

	public static void sendMobileActivationMail(String activationCode,
			String userEmailId) {

		try {
			initPropertyParserToInviteUser();
			LOG.info("Email is being sent to default user");
		} catch (FileNotFoundException e) {
			e.printStackTrace();
			return;
		} catch (IOException e) {
			e.printStackTrace();
			return;
		}

		String subject = propertyParser.getProperty("subjectForActivation", "");

		StringBuffer content = new StringBuffer();
		content.append("Hello " + ",\n");
		content.append(propertyParser.getProperty("contentForActivation", ""));
		String contentStr = content.toString().replaceAll("%TOKEN%",
				activationCode);
		contentStr = replaceServerUrl(contentStr);

		System.out.println("************* ACTIVATION CODE : " + activationCode);

		EMailMessage emailMsg = new EMailMessage();
		emailMsg.setContent(contentStr);
		emailMsg.setSubject(subject);
		emailMsg.setRecepeant(userEmailId);
		EMailJob job = new EMailJob(emailMsg, getEmailAcc());

		EmailManager.getInstance().addJob(job);
	}

	public static void sendMailToOtherCompanyUser(Client invitedClient,
			String companyName, Client inviter) {
		try {
			initPropertyParserToInviteUser();
			LOG.info("Invitation Email is being sent to user");
		} catch (FileNotFoundException e) {
			e.printStackTrace();
			return;
		} catch (IOException e) {
			e.printStackTrace();
			return;
		}

		// String content = getContentForExternalUser();
		String content = propertyParser.getProperty(
				"contentForInviteExternalUser", "");
		content = content.replaceAll("%USER%",
				getUserName(invitedClient.getFirstName()));
		content = content.replaceAll("%USERID%", invitedClient.getEmailId());
		content = content.replaceAll("%INVITED_USER_NAME%",
				inviter.getFullName());
		content = content.replaceAll("%COMPANY%", companyName);
		content = content.replaceAll("%URL%", "http://accounterlive.com");
		content = content.replaceAll("%PASSWORD%", invitedClient.getPassword());
		// content = content.replaceAll("%LOGINURL%", loginURL);

		String subject = propertyParser.getProperty("subjectForInviteUser", "");
		subject = subject.replaceAll("%COMPANY%", companyName);

		EMailMessage emailMsg = new EMailMessage();
		emailMsg.setContent(content);
		emailMsg.setSubject(subject);
		emailMsg.setReplayTO(inviter.getEmailId());
		emailMsg.setRecepeant(invitedClient.getEmailId());
		EMailJob job = new EMailJob(emailMsg, getEmailAcc(), companyName);
		EmailManager.getInstance().addJob(job);
	}

	private static String replaceServerUrl(String content) {
		return content.replaceAll("%SERVERURL%",
				ServerConfiguration.getMainServerDomain());
	}

	private static String getUserName(String name) {
		String firstName = name;
		String firstChar = firstName.substring(0, 1);
		String substring = firstName.substring(1);

		StringBuffer buffer = new StringBuffer();
		buffer.append(firstChar.toUpperCase()).append(substring);
		return buffer.toString();
	}

	public static void sendMailToMaintanaceInfoUsers(
			MaintananceInfoUser mainInfoUser) throws IOException {
		try {
			initPropertyParserToInviteUser();
			LOG.info("Maintanace Email is being sent to user");
		} catch (FileNotFoundException e) {
			e.printStackTrace();
			throw new IOException();
		} catch (IOException e) {
			e.printStackTrace();
			throw new IOException();
		}

		String content = propertyParser.getProperty("contentForMaintanaceInfo",
				"");
		content = content.replaceAll("%USER%",
				getUserName(mainInfoUser.getUserEmail()));

		String subject = propertyParser.getProperty("subjectForMaintanaceInfo",
				"");

		EMailMessage emailMsg = new EMailMessage();
		emailMsg.setContent(content);
		emailMsg.setSubject(subject);
		emailMsg.setRecepeant(mainInfoUser.getUserEmail());
		EMailJob job = new EMailJob(emailMsg, getEmailAcc());
		EmailManager.getInstance().addJob(job);
	}

	/**
	 * write subject and content for the below three mails sending methods
	 * 
	 * @param client
	 * @throws IOException
	 */

	public static void sendMailToSubscribedUser(Client client)
			throws IOException {
		try {
			initPropertyParserToInviteUser();
			LOG.info("Subscription Email is being sent to user");
		} catch (FileNotFoundException e) {
			e.printStackTrace();
			throw new IOException();
		} catch (IOException e) {
			e.printStackTrace();
			throw new IOException();
		}

		String content = propertyParser.getProperty("contentForSubscribedUser",
				"");
		content = content
				.replaceAll("%USER%", getUserName(client.getEmailId()))
				.replaceAll(
						"%TYPE%",
						getTypeStringByType(client.getClientSubscription()
								.getPremiumType()))
				.replaceAll("%EXPIRY%",
						client.getClientSubscription().getExpiredDateAsString());

		String subject = propertyParser.getProperty("subjectForSubscribedUser",
				"");

		EMailMessage emailMsg = new EMailMessage();
		emailMsg.setContent(content);
		emailMsg.setSubject(subject);
		emailMsg.setRecepeant(client.getEmailId());
		EMailJob job = new EMailJob(emailMsg, getEmailAcc());
		EmailManager.getInstance().addJob(job);
	}

	private static String getTypeStringByType(int premiumType) {
		AccounterMessages messages = Global.get().messages();
		switch (premiumType) {
		case ClientSubscription.ONE_USER:
			return messages.subscription(messages.OneUser());
		case ClientSubscription.TWO_USERS:
			return messages.subscription(messages.TwoUsers());
		case ClientSubscription.FIVE_USERS:
			return messages.subscription(messages.FiveUsers());
		case ClientSubscription.UNLIMITED_USERS:
			return messages.subscription(messages.UnlimitedUsers());
		default:
			break;
		}
		return "";
	}

	public static void sendMailToSubscriptionExpiredUser(Client client)
			throws IOException {
		try {
			initPropertyParserToInviteUser();
			LOG.info("Subscription Expired Email is being sent to user");
		} catch (FileNotFoundException e) {
			e.printStackTrace();
			throw new IOException();
		} catch (IOException e) {
			e.printStackTrace();
			throw new IOException();
		}

		String content = propertyParser.getProperty(
				"contentForSubscriptionExpiredUser", "");
		content = content
				.replaceAll("%USER%", getUserName(client.getEmailId()))
				.replaceAll(
						"%TYPE%",
						getTypeStringByType(client.getClientSubscription()
								.getPremiumType()))
				.replaceAll("%EXPIRY%",
						client.getClientSubscription().getExpiredDateAsString());

		String subject = propertyParser.getProperty(
				"subjectForSubscriptionExpiredUser", "");

		EMailMessage emailMsg = new EMailMessage();
		emailMsg.setContent(content);
		emailMsg.setSubject(subject);
		emailMsg.setRecepeant(client.getEmailId());
		EMailJob job = new EMailJob(emailMsg, getEmailAcc());
		EmailManager.getInstance().addJob(job);
	}

	public static void sendMailToUserWithPasswordRecoverKey(String emailId,
			String userSecret) throws IOException {
		try {
			initPropertyParserToInviteUser();
			LOG.info("Subscription Expired Email is being sent to user");
		} catch (FileNotFoundException e) {
			e.printStackTrace();
			throw new IOException();
		} catch (IOException e) {
			e.printStackTrace();
			throw new IOException();
		}

		String content = propertyParser.getProperty(
				"contentForUserWithPasswordRecoverKey", "");
		content = content.replaceAll("%USER%", getUserName(emailId))
				.replaceAll("%KEY%", userSecret);

		String subject = propertyParser.getProperty(
				"subjectForUserWithPasswordRecoverKey", "");

		EMailMessage emailMsg = new EMailMessage();
		emailMsg.setContent(content);
		emailMsg.setSubject(subject);
		emailMsg.setRecepeant(emailId);
		EMailJob job = new EMailJob(emailMsg, getEmailAcc());
		EmailManager.getInstance().addJob(job);
	}
}
