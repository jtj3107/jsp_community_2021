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

	// 댓글을 등록하는 메서드
	private void actionDoWrite(Rq rq) {
		// 댓글등록 페이제에서 해당 변수값을 찾아 저장
		int relId = rq.getIntParam("relId", 0);
		String reply = rq.getParam("reply", "");
		
		// 비정상적으로 접근시 메세지 출력후 뒤로가기
		if(relId == 0) {
			rq.historyBack("articleId를 입력해주세요.");
			return;
		}
		
		if(reply.length() == 0) {
			rq.historyBack("reply를 입력해주세요.");
			return;
		}
		
		// 댓글을 등록하고 리턴값 저장
		ResultData writeRd = replyService.write(rq.getLoginedMember(), relId, reply);

		// writeRd값이 F-로 시작시 메세지 출력후 리턴
		if (writeRd.isFail()) {
			rq.historyBack(writeRd.getMsg());
			return;
		}

	}

}
