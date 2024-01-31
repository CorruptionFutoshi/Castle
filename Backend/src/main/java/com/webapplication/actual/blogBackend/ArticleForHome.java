package com.webapplication.actual.blogBackend;

import java.util.Date;

import lombok.Getter;

@Getter
public class ArticleForHome {
	private int id;

	private Date createDate;

	private String title;

	private String summary;

	private String tag;

	public ArticleForHome(int id, Date createDate, String title, String summary, String tag) {
		super();
		this.id = id;
		this.createDate = createDate;
		this.title = title;
		this.summary = summary;
		this.tag = tag;
	}
}
