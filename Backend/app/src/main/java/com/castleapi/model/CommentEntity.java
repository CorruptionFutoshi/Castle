package com.castleapi.model;

import java.sql.Date;

public class CommentEntity {
	private int id;

	private int articleId;

	private int memberId;

	private String username;

	private String contents;

	private Date createDate;

	public CommentEntity(int id, int articleId, int memberId, String username, String contents, Date createDate) {
		super();
		this.id = id;
		this.articleId = articleId;
		this.memberId = memberId;
		this.username = username;
		this.contents = contents;
		this.createDate = createDate;
	}

	public int getId() {
		return id;
	}

	public int getArticleId() {
		return articleId;
	}

	public int getMemberId() {
		return memberId;
	}

	public String getUsername() {
		return username;
	}

	public String getContents() {
		return contents;
	}

	public Date getCreateDate() {
		return createDate;
	}
}