package com.castleapi.model;

import java.sql.Date;

public class MemberEntity {
	private int id;

	private Date createDate;
	
	private String username;
	
	private byte[] hashedPassword;

	public MemberEntity(int id, Date createDate, String username, byte[] hashedPassword) {
		super();
		this.id = id;
		this.createDate = createDate;
		this.username = username;
		this.hashedPassword = hashedPassword;
	}

	public int getId() {
		return id;
	}

	public Date getCreateDate() {
		return createDate;
	}

	public String getUserId() {
		return username;
	}

	public String getUsername() {
		return username;
	}

	public byte[] getHashedPassword() {
		return hashedPassword;
	}
	
}

