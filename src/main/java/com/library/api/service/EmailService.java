package com.library.api.service;

import java.util.List;

public interface EmailService {

	void sendEmails(String mensagem, List<String> mailsList);

}
