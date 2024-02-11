package com.castleapi.model;

import java.sql.Date;

public class ArticleLightEntity {
	private int id;

	private Date createDate;

	private String title;

	private String summary;

	private String tag;

	public ArticleLightEntity(int id, Date createDate, String title, String summary, String tag) {
		super();
		this.id = id;
		this.createDate = createDate;
		this.title = title;
		this.summary = summary;
		this.tag = tag;
	}

	public int getId() {
		return id;
	}

	public Date getCreateDate() {
		return createDate;
	}

	public String getTitle() {
		return title;
	}

	public String getSummary() {
		return summary;
	}

	public String getTag() {
		return tag;
	}
}
