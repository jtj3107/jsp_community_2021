package com.jhs.exam.exam2.dto;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

import lombok.Data;

@JsonIgnoreProperties(ignoreUnknown = true)
@Data
public class reply {
	private int id;
	private String regDate;
	private String updateDate;
	private String body;
	private int memberId;
	private int articleId;
	
	private String extra__writerName;
	private Boolean extra__actorCanModify;
	private Boolean extra__actorCanDelete;

	public String getBodySummaryForPrint() {
		return body;
	}
}
