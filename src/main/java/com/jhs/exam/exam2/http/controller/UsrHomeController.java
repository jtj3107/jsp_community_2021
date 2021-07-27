package com.jhs.exam.exam2.http.controller;

import com.jhs.exam.exam2.http.Rq;
import com.jhs.exam.exam2.util.Ut;

public class UsrHomeController extends Controller {
	@Override
	public void performAction(Rq rq) {
		// ActionMethodName가 아래 case와 같을시 해당 함수로 이동
		switch (rq.getActionMethodName()) {
		case "main":
			actionShowMain(rq);
			break;
		case "doSendMail":
			actionDoSendMail(rq);
			break;
		// 해당 함수가 없을시 오류메세지 출력후 break;
		default:
			rq.println("존재하지 않는 페이지 입니다.");
			break;
		}
	}

	// doSendMail로 들어올시 이메일을 보내는 함수
	private void actionDoSendMail(Rq rq) {
		Ut.sendMail("jtj3926@gmail.com", "khnvjxnxfqxotpnv", "no-reply@lemon-cm.com", "레몬 커뮤니티 알림", "jtj3926@gmail.com", "제목ㅋㅋㅋ", "내용 ㅋㅋㅋ");	
	}

	// 해당 페이지로 이동하는 함수
	private void actionShowMain(Rq rq) {
		rq.jsp("usr/home/main");
	}

}
