package com.jhs.exam.exam2.service;

import com.jhs.exam.exam2.container.Container;
import com.jhs.exam.exam2.container.ContainerComponent;
import com.jhs.exam.exam2.dto.IsLike;
import com.jhs.exam.exam2.dto.Member;
import com.jhs.exam.exam2.dto.ResultData;
import com.jhs.exam.exam2.repository.LikeRepository;

public class LikeService implements ContainerComponent{

	private LikeRepository likeRepository;

	@Override
	public void init() {
		likeRepository = Container.likeRepository;
	}

	public ResultData likeUpDown(String relTypeCode, int relId, int actorId) {		
		IsLike like = likeRepository.getLikeByArticleIdAndMemberId(relTypeCode, relId, actorId);
	
		if(like != null) {
			likeDown(relTypeCode, relId, actorId);
			return ResultData.from("S-1", "좋아요 취소");
		}
		
		likeRepository.likeInsert(relTypeCode, relId, actorId);	
		
		return ResultData.from("S-2", "좋아요");
	}

	private void likeDown(String relTypeCode, int relId, int actorId) {
		likeRepository.likeDelete(relTypeCode, relId, actorId);
	}

	public ResultData disLikeUpDown(int articleId, Member actor) {
		int memberId = actor.getId();
		
		IsLike disLike = likeRepository.getDisLikeByArticleIdAndMemberId(articleId, memberId);
	
		if(disLike != null) {
			disLikeDown(articleId, memberId);
			return ResultData.from("S-1", "싫어요 취소");
		}
		
		likeRepository.disLikeInsert(articleId, memberId);	
		
		return ResultData.from("S-2", "싫어요");
	}

	private void disLikeDown(int articleId, int memberId) {
		likeRepository.disLikeDelete(articleId, memberId);
	}

}
