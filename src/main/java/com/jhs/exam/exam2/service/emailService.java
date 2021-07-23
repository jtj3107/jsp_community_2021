package com.jhs.exam.exam2.service;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.jhs.exam.exam2.util.Ut;

import lombok.Data;

@JsonIgnoreProperties(ignoreUnknown = true)
@Data
public class emailService {
	private String gmailId;
	private String gmailPw;
	private String from;
	private String fromName;
	
	public void init(String gmailId, String gmailPw, String from, String fromName) {
		this.gmailId = gmailId;
		this.gmailPw = gmailPw;
		this.from = from;
		this.fromName = fromName;
	}

	public int send(String to, String title, String body) {
		return Ut.sendMail(gmailId, gmailPw, from, fromName, to, title, body);
	}
}
