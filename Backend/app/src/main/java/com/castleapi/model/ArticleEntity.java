package com.castleapi.model;

import java.sql.Date;

public class ArticleEntity {
	private int id;

	private Date createDate;

	private String title;

	private String summary;

	private String tag;

	private String contents;

	public ArticleEntity(int id, Date createDate, String title, String summary, String tag, String contents) {
		super();
		this.id = id;
		this.createDate = createDate;
		this.title = title;
		this.summary = summary;
		this.tag = tag;
		this.contents = contents;
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

	public String getContents() {
		return contents;
	}
}
