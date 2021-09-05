package com.jhs.exam.exam2.http.controller;

import com.jhs.exam.exam2.container.Container;
import com.jhs.exam.exam2.dto.ResultData;
import com.jhs.exam.exam2.http.Rq;
import com.jhs.exam.exam2.service.LikeService;

public class UsrLikeController extends Controller {
	private LikeService likeService;

	public void init() {
		likeService = Container.likeService;
	}

	@Override
	public void performAction(Rq rq) {
		switch (rq.getActionMethodName()) {
		case "doLike":
			getDoLike(rq);
			break;
		case "doDislike":
			getDoDisLike(rq);
			break;
		default:
			rq.println("존재하지 않는 페이지 입니다.");
			break;
		}
	}

	private void getDoDisLike(Rq rq) {
		String relTypeCode = rq.getParam("relTypeCode", "");

		if (relTypeCode.length() == 0) {
			return;
		}

		int relId = rq.getIntParam("relId", 0);

		if (relId == 0) {
			return;
		}

		int actorId = rq.getLoginedMemberId();

		ResultData likeUpDownRd = likeService.disLikeUpDown(relTypeCode, relId, actorId);

		rq.replace(likeUpDownRd.getMsg(), rq.getParam("redirectUrl", "usr/home/main"));
	}

	private void getDoLike(Rq rq) {
		String relTypeCode = rq.getParam("relTypeCode", "");

		if (relTypeCode.length() == 0) {
			return;
		}

		int relId = rq.getIntParam("relId", 0);

		if (relId == 0) {
			return;
		}

		int actorId = rq.getLoginedMemberId();

		ResultData likeUpDownRd = likeService.likeUpDown(relTypeCode, relId, actorId);

		rq.replace(likeUpDownRd.getMsg(), rq.getParam("redirectUrl", "usr/home/main"));
	}
}
