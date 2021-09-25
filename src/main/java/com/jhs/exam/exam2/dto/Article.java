package com.jhs.exam.exam2.dto;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

import lombok.Data;

@JsonIgnoreProperties(ignoreUnknown = true)
@Data
public class Article {
	private int id;
	private String regDate;
	private String updateDate;
	private int boardId;
	private int memberId;
	private String title;
	private String body;
	private int dislikeCount;
	private int likeCount;
	private int hitCount;

	private String extra__writerName;
	private String extra_boardName;
	private Boolean extra__actorCanModify;
	private Boolean extra__actorCanDelete;

	private Boolean extra__actorCanLike;
	private Boolean extra__actorCanCancelLike;
	private Boolean extra__actorCanDisLike;
	private Boolean extra__actorCanCancelDisLike;

	private int extra__likePoint;
	private int extra__likeOnlyPoint;
	private int extra__dislikeOnlyPoint;

	public String getTitleForPrint() {
		return title;
	}

	public String getBodySummaryForPrint() {
		return body;
	}
	
	public String getBodySummaryForPrintNlToBr() {
		return body.replaceAll("\n", "<br>");
	}

	public String getWriterProfileImgUri() {
		return "https://i.pravatar.cc/200?img=" + (memberId % 1000 + 1);
	}
}
