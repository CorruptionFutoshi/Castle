package com.webapplication.actual.blogBackend;

import java.util.Date;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.Data;

@Data
@Entity
@Table(name = "article")
public class Article {
	@Id
	private int id;

	@Column(name = "createdate")
	private Date createDate;

	@Column
	private String title;

	@Column
	private String summary;

	@Column
	private String tag;

	@Column
	private String contents;
}
