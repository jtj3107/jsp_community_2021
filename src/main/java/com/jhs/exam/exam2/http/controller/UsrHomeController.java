package com.jhs.exam.exam2.http.controller;

import com.jhs.exam.exam2.app.App;
import com.jhs.exam.exam2.container.Container;
import com.jhs.exam.exam2.http.Rq;
import com.jhs.exam.exam2.service.EmailService;
import com.jhs.exam.exam2.util.Ut;

public class UsrHomeController extends Controller {
	private EmailService emailService;

	public void init() {
		emailService = Container.emailService;
	}
	
	@Override
	public void performAction(Rq rq) {
		switch (rq.getActionMethodName()) {
		case "main":
			actionShowMain(rq);
			break;
		case "doSendMail":
			actionDoSendMail(rq);
			break;
		case "doEmailCertification":
			actionDoEmailCertification(rq);
			break;
		default:
			rq.println("존재하지 않는 페이지 입니다.");
			break;
		}
	}

	private void actionDoEmailCertification(Rq rq) {
		String generationCode = rq.getSessionAttr("bodyJson", "");
		String inputCode = rq.getParam("certificationNumber", "");
		
		if(generationCode.length() == 0) {
			rq.historyBack("인증번호 생성이 안되었습니다.");
			return;
		}
		
		if(inputCode.length() == 0) {
			rq.historyBack("인증번호를 입력해주세요.");
			return;
		}
		
		if(generationCode.equals(inputCode) == false) {
			rq.historyBack("인증번호가 틀립니다.");
			return;
		}
		
	}

	// 해당 페이지로 이동하는 메서드
	private void actionShowMain(Rq rq) {
		rq.jsp("usr/home/main");
	}

	// 메일발송하는 메서드
	private void actionDoSendMail(Rq rq) {
		App app = Container.app;
		String clientEmail = rq.getParam("clientEmail", "");
		
		if(clientEmail.length() == 0) {
			rq.historyBack("clientEmail를 입력해주세요.");
			return;
		}
		
		String body = Ut.getTempPassword(4);
 		
		emailService.notify(clientEmail, "이메일 인증", "인증번호 :" + body);
		
		rq.setSessionAttr("bodyJson", Ut.toJson(body, ""));

	}

}
