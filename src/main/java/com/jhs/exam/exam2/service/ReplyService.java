package com.jhs.exam.exam2.service;

import com.jhs.exam.exam2.container.Container;
import com.jhs.exam.exam2.container.ContainerComponent;
import com.jhs.exam.exam2.dto.Member;
import com.jhs.exam.exam2.dto.ResultData;
import com.jhs.exam.exam2.repository.ReplyRepository;
import com.jhs.exam.exam2.util.Ut;

public class ReplyService implements ContainerComponent {

	private ReplyRepository replyRepository;

	@Override
	public void init() {
		replyRepository = Container.replyRepository;
	}

	public ResultData write(Member actor, int articleId, String reply) {
		int memberId = actor.getId();
		
		int id = replyRepository.write(memberId, articleId, reply);
		
		return ResultData.from("S-1", "등록되었습니다.", "id", id);
	}

}
