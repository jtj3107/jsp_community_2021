package com.jhs.exam.exam2.dto;

import lombok.Data;

@Data
public class IsLike {
	private int id;
	private String regDate;
	private String updateDate;
	private int articleId;
	private int memberId;
	private int islike;
}
