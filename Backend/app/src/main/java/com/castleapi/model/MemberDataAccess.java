package com.castleapi.model;

import java.io.IOException;
import java.sql.Connection;
import java.sql.Date;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class MemberDataAccess {

	private DatabaseConnector dbConnector;

	public MemberDataAccess() throws IOException {
		dbConnector = new DatabaseConnector();
	}

	public MemberEntity findByUsername(String username) throws SQLException, ClassNotFoundException {
		MemberEntity member = null;

		try (Connection conn = dbConnector.connect();
				PreparedStatement statement = createPreparedStatement(conn, "SELECT * FROM member WHERE username = ?",
						username);
				ResultSet rs = statement.executeQuery()) {
			while (rs.next()) {
				int id = rs.getInt("id");
				Date createDate = rs.getDate("createDate");
				String usernameFromDatabase = rs.getString("username");
				byte[] hashedPassword = rs.getBytes("hashedPassword");

				member = new MemberEntity(id, createDate, usernameFromDatabase, hashedPassword);
			}
		}

		return member;
	}

	public void insert(String username, byte[] hashedpassword) throws SQLException, ClassNotFoundException {
		try (Connection conn = dbConnector.connect();
				PreparedStatement statement = createPreparedStatement(conn,
						"INSERT INTO member (username, hashedpassword,createDate) VALUES (?, ?,NOW())", username,
						hashedpassword)) {
			statement.executeUpdate();
		}
	}

	public void deleteByUsername(String username) throws SQLException, ClassNotFoundException {
		try (Connection conn = dbConnector.connect();
				PreparedStatement statement = createPreparedStatement(conn, "DELETE FROM member WHERE username = ?",
						username)) {
			statement.executeUpdate();
		}
	}

	private PreparedStatement createPreparedStatement(Connection conn, String query, Object... params)
			throws SQLException {
		PreparedStatement statement = conn.prepareStatement(query);

		for (int i = 0; i < params.length; i++) {
			statement.setObject(i + 1, params[i]);
		}

		return statement;
	}
}
