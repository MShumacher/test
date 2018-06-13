package com.sygno.po.mail.service;

import java.io.IOException;

import javax.mail.MessagingException;

public interface IEmailSenderService {

	void sendEmail(String firstName, String lastName, String mailTo) throws MessagingException, IOException;

}
