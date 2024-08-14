package com.example.dracoworld.domain;

import lombok.Getter;

@Getter
public enum Category {
	ALL("전체"),
	DAILY_LOOK("데일리룩 게시판"),
	FREE("자유 게시판"),
	INTERVIEW("인터뷰 게시판"),
	LOOKBOOK("룩북 게시판"),
	TRADE("거래 게시판");

	private final String displayName;

	Category(String displayName) {
		this.displayName = displayName;
	}


}
