package com.jhs.exam.exam2.http.controller;

import com.jhs.exam.exam2.container.Container;
import com.jhs.exam.exam2.dto.ResultData;
import com.jhs.exam.exam2.http.Rq;
import com.jhs.exam.exam2.service.ReplyService;

public class UsrReplyController extends Controller{
	private ReplyService replyService;

	public void init() {
		replyService = Container.replyService;
	}

	@Override
	public void performAction(Rq rq) {
		switch (rq.getActionMethodName()) {
		case "doWrite":
			actionDoWrite(rq);
			break;
		default:
			rq.println("존재하지 않는 페이지 입니다.");
			break;
		}
	}

	private void actionDoWrite(Rq rq) {
		int articleId = rq.getIntParam("articleId", 0);
		String reply = rq.getParam("reply", "");
		
		if(articleId == 0) {
			rq.historyBack("articleId를 입력해주세요.");
			return;
		}
		
		if(reply.length() == 0) {
			rq.historyBack("reply를 입력해주세요.");
			return;
		}
		
		ResultData writeRd = replyService.write(rq.getLoginedMember(), articleId, reply);

		if (writeRd.isFail()) {
			rq.historyBack(writeRd.getMsg());
			return;
		}

	}

}
