package com.jhs.exam.exam2.http.controller;

import com.jhs.exam.exam2.container.Container;
import com.jhs.exam.exam2.dto.Article;
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
			actionDoLike(rq);
			break;
		case "doDisLike":
			actionDoDisLike(rq);
			break;
		default:
			rq.println("존재하지 않는 페이지 입니다.");
			break;
		}
	}

	private void actionDoDisLike(Rq rq) {
		int articleId = rq.getIntParam("articleId", 0);
		String redirectUrl = rq.getParam("redirectUrl", "usr/article/detail?id=" + articleId);
		
		if(articleId == 0) {
			rq.historyBack("id를 입력해주세요.");
			return;
		}
		
		ResultData disLikeUpRd = likeService.disLikeUpDown(articleId, rq.getLoginedMember());
		
		rq.replace(disLikeUpRd.getMsg(), redirectUrl);
		
	}

	private void actionDoLike(Rq rq) {
		int articleId = rq.getIntParam("articleId", 0);
		String redirectUrl = rq.getParam("redirectUrl", "usr/article/detail?id=" + articleId);
		
		if(articleId == 0) {
			rq.historyBack("id를 입력해주세요.");
			return;
		}
		
		ResultData likeUpRd = likeService.likeUpDown(articleId, rq.getLoginedMember());
		
		rq.replace(likeUpRd.getMsg(), redirectUrl);
		
	}

}
