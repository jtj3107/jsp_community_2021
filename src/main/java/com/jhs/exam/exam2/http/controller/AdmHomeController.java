package com.jhs.exam.exam2.http.controller;

import com.jhs.exam.exam2.http.Rq;
import com.jhs.exam.exam2.http.controller.Controller;
import com.jhs.exam.exam2.util.Ut;

public class AdmHomeController extends Controller {
	public void init() {
		
	}
	
	@Override
	public void performAction(Rq rq) {
		switch (rq.getActionMethodName()) {
		case "main":
			actionShowMain(rq);
			break;
		default:
			rq.println("존재하지 않는 페이지 입니다.");
			break;
		}
	}

	// 관리자 페이지로 이동하는 메서드
	private void actionShowMain(Rq rq) {
		rq.print("관리자 페이지 입니다.");
	}

}

