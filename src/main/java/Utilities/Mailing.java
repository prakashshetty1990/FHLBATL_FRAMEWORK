package Utilities;


import java.util.Properties;

import javax.activation.DataHandler;
import javax.activation.DataSource;
import javax.activation.FileDataSource;
import javax.mail.BodyPart;
import javax.mail.Message;
import javax.mail.MessagingException;
import javax.mail.Multipart;
import javax.mail.PasswordAuthentication;
import javax.mail.Session;
import javax.mail.Transport;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeBodyPart;
import javax.mail.internet.MimeMessage;
import javax.mail.internet.MimeMultipart;



public class Mailing
{
	public Mailing() {}

	public static String[] getArrayOfEmails(String keyword)
	{
		String configProperty = Common.getConfigProperty(keyword);
		if (configProperty.length() != 0)
		{
			String[] arr = configProperty.split(";");
			Common.writeToLogFile("INFO", "Splitting email addresses : " + keyword);
			for (int i = 0; i < arr.length; i++)
				Common.writeToLogFile("INFO", "Email :" + (i + 1) + " \"" + arr[i] + "\"");
			return arr;
		}

		Common.writeToLogFile("INFO", "No email addresses found for : " + keyword);
		return null;
	}


	public static void sendMail()
	{
		final String host = Common.getConfigProperty("HostName");
		final String user = Common.getConfigProperty("SenderMailId");
		final String password = Common.getConfigProperty("Password");        		
		final String suiteName = Common.getConfigProperty("SuiteName");
		final String[] to = getArrayOfEmails("ToEmail");
		final String[] cc = getArrayOfEmails("CcEmail");
		final Properties props = new Properties();
		props.put("mail.smtp.auth", "true");
		props.put("mail.smtp.host", host);
		props.put("mail.smtp.port", "587");
		props.put("mail.smtp.starttls.enable", "true");

		Session session = Session.getDefaultInstance(props,
				new javax.mail.Authenticator() {
			@Override
			protected PasswordAuthentication getPasswordAuthentication() {
				return new PasswordAuthentication(user, password);
			}
		});
		try {
			final MimeMessage message = new MimeMessage(session);
			message.setFrom(new InternetAddress("SWAUT <" + user + ">"));
			for (int i = 0; i < to.length; ++i) {
				message.addRecipient(Message.RecipientType.TO, new InternetAddress(to[i]));
			}
			for (int i = 0; i < cc.length; ++i) {
				message.addRecipient(Message.RecipientType.CC, new InternetAddress(cc[i]));
			}
			final int totalExecuted = GenericKeywords.testFailureCount + GenericKeywords.testSkippedCount + GenericKeywords.testSuccessCount;
			message.setSubject(String.valueOf(suiteName) + " Test | " + GenericKeywords.testSuccessCount * 100 / totalExecuted + "% Pass | " + GenericKeywords.timeStamp);
			BodyPart messageBodyPart = new MimeBodyPart();
			messageBodyPart.setText(String.valueOf(suiteName.toUpperCase()) + " TEST REPORT" + '\n' + '\n' + '\n' + GenericKeywords.testSuccessCount * 100 / totalExecuted + "% Passed" + '\n' + GenericKeywords.testFailureCount * 100 / totalExecuted + "% Failed" + '\n' + '\n' + "Total Executed-" + (GenericKeywords.testFailureCount + GenericKeywords.testSkippedCount + GenericKeywords.testSuccessCount) + '\n' + "Total Passed-" + GenericKeywords.testSuccessCount + '\n' + "Total Failed-" + GenericKeywords.testFailureCount + '\n' + "Total Skipped-" + GenericKeywords.testSkippedCount);
			final Multipart multipart = new MimeMultipart();
			multipart.addBodyPart(messageBodyPart);
			messageBodyPart = new MimeBodyPart();
			final String filename = String.valueOf(GenericKeywords.timeStamp) + ".zip";
			final DataSource source = new FileDataSource(filename);
			messageBodyPart.setDataHandler(new DataHandler(source));
			messageBodyPart.setFileName(filename);
			multipart.addBodyPart(messageBodyPart);
			message.setContent(multipart);
			Common.writeToLogFile("INFO", "<<<<<<<<<<<<<Please Wait>>>>>>>>>>>>>>>>>");
			Transport.send(message);
			Common.writeToLogFile("INFO", "Mail sent successfully...");
		}
		catch (MessagingException e) {
			e.printStackTrace();
		}
	}
}
