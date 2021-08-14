package com.jhs.exam.exam2.service;

import com.jhs.exam.exam2.container.Container;
import com.jhs.exam.exam2.container.ContainerComponent;
import com.jhs.exam.exam2.dto.IsLike;
import com.jhs.exam.exam2.dto.Member;
import com.jhs.exam.exam2.dto.ResultData;
import com.jhs.exam.exam2.repository.ArticleRepository;
import com.jhs.exam.exam2.repository.LikeRepository;

public class LikeService implements ContainerComponent{

	private ArticleRepository articleRepository;
	private LikeRepository likeRepository;

	@Override
	public void init() {
		articleRepository = Container.articleRepository;
		likeRepository = Container.likeRepository;
	}

	public ResultData likeUpDown(int articleId, Member actor) {
		int memberId = actor.getId();
		
		IsLike like = likeRepository.getLikeByArticleIdAndMemberId(articleId, memberId);
	
		if(like != null) {
			likeDown(articleId, memberId);
			return ResultData.from("S-1", "좋아요 취소");
		}
		
		likeRepository.likeInsert(articleId, memberId);	
		articleRepository.likeup(articleId);
		
		return ResultData.from("S-2", "좋아요");
	}

	private void likeDown(int articleId, int memberId) {
		likeRepository.likeDelete(articleId, memberId);
		articleRepository.likeDown(articleId);
	}

	public ResultData disLikeUpDown(int articleId, Member actor) {
		int memberId = actor.getId();
		
		IsLike disLike = likeRepository.getDisLikeByArticleIdAndMemberId(articleId, memberId);
	
		if(disLike != null) {
			disLikeDown(articleId, memberId);
			return ResultData.from("S-1", "싫어요 취소");
		}
		
		likeRepository.disLikeInsert(articleId, memberId);	
		articleRepository.disLikeup(articleId);
		
		return ResultData.from("S-2", "싫어요");
	}

	private void disLikeDown(int articleId, int memberId) {
		likeRepository.disLikeDelete(articleId, memberId);
		articleRepository.disLikeDown(articleId);
	}

}
