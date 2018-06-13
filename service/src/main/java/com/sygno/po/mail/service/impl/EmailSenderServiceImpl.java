package com.sygno.po.mail.service.impl;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.util.Date;
import java.util.Properties;

import javax.mail.Message;
import javax.mail.MessagingException;
import javax.mail.Multipart;
import javax.mail.Session;
import javax.mail.Transport;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeBodyPart;
import javax.mail.internet.MimeMessage;
import javax.mail.internet.MimeMultipart;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.sygno.po.mail.service.IEmailSenderService;

@Component
public class EmailSenderServiceImpl implements IEmailSenderService {

	private static final Logger LOGGER = LoggerFactory.getLogger("emailSenderLogger");

	@Autowired
	private Session mailSession;

	private void send(final String to, final String subject, final String textBody, final String htmlBody)
			throws MessagingException {
		final MimeMessage message = new MimeMessage(mailSession);
		try {
			message.setFrom(new InternetAddress(mailSession.getProperty("mail.from")));
			final InternetAddress[] address = { new InternetAddress(to) };
			message.setRecipients(Message.RecipientType.TO, address);
			message.setSubject(subject);
			message.setSentDate(new Date());

			final Multipart content = new MimeMultipart("alternative");
			final MimeBodyPart textPart = new MimeBodyPart();
			textPart.setContent(textBody, "text/plain");
			content.addBodyPart(textPart);
			final MimeBodyPart htmlPart = new MimeBodyPart();
			htmlPart.setContent(htmlBody, "text/html;charset=\"UTF-8\"");
			message.setSubject(subject, StandardCharsets.UTF_8.name());
			content.addBodyPart(htmlPart);
			message.setContent(content);
			Transport.send(message);
		} catch (final MessagingException e) {
			LOGGER.warn(e.getMessage());
			throw e;
		}
	}

	@Override
	public void sendEmail(final String firstName, final String lastName, final String mailTo) throws MessagingException, IOException {
		Properties props = new Properties();
		try {
			props.load(new FileInputStream(new File(mailTo)));
		} catch (IOException e) {
			LOGGER.warn("IOException {}.", e.getMessage());
			throw e;
		}
		final String email = props.getProperty("email");
		final String subject = "Greetings";
		final String text = String.format(
				"Приветствую Вас, дорогой %s %s.\n Не отвечайте, пожалуйста, на этот email.\n С уважением, бот.",
				firstName, lastName);
		final String html = String.format(
				"Приветствую Вас, дорогой %s %s.<br> Не отвечайте, пожалуйста, на этот email.<br> С уважением, бот.",
				firstName, lastName);
		try {
			send(email, subject, text, html);
			LOGGER.info("email was send to {}", email);
		} catch (MessagingException e) {
			LOGGER.warn("email was not send to {}. {}", email, e.getMessage());
			throw e;
		}
	}

}
