package com.hau.ketnguyen.service;

import javax.mail.MessagingException;

import com.hau.ketnguyen.context.AbstractEmailContext;

public interface ISendMailService {
	void sendMail(AbstractEmailContext email) throws MessagingException;
}
