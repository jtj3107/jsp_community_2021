package com.jhs.exam.exam2.service;

import java.util.List;

import com.jhs.exam.exam2.container.Container;
import com.jhs.exam.exam2.container.ContainerComponent;
import com.jhs.exam.exam2.dto.Member;
import com.jhs.exam.exam2.dto.Reply;
import com.jhs.exam.exam2.dto.ResultData;
import com.jhs.exam.exam2.repository.ReplyRepository;
import com.jhs.exam.exam2.util.Ut;

public class ReplyService implements ContainerComponent {

	private ReplyRepository replyRepository;

	@Override
	public void init() {
		replyRepository = Container.replyRepository;
	}

	// 댓글을 등록하는 메서드
	public ResultData write(Member actor, int articleId, String reply) {
		// 로그인한 회원의 id값을 구함
		int memberId = actor.getId();
		
		// 댓글을 등록하는 함수
		int newReplyId = replyRepository.write(memberId, articleId, reply);
		
		// S-1, 메세지 저장후 리턴
		return ResultData.from("S-1", "등록되었습니다.", "newReplyId", newReplyId);
	}

	// 댓글 리스트를 리턴하는 함수
	public List<Reply> getForPrintReplies(int id) {
		return replyRepository.getForPrintReplies(id);
	}

}
