package com.castleapi.model;

import java.io.IOException;
import java.sql.Connection;
import java.sql.Date;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class CommentDataAccess {
	private DatabaseConnector dbConnector;
	MemberDataAccess memberDataAccess;

	public CommentDataAccess() throws IOException {
		dbConnector = new DatabaseConnector();
		memberDataAccess = new MemberDataAccess();
	}

	public List<CommentEntity> findByArticleId(int articleId) throws SQLException, ClassNotFoundException {
		List<CommentEntity> comments = new ArrayList<CommentEntity>();

		try (Connection conn = dbConnector.connect();
				PreparedStatement statement = createPreparedStatement(conn, "SELECT * FROM comment WHERE articleId = ?",
						articleId);
				ResultSet rs = statement.executeQuery()) {
			while (rs.next()) {
				int id = rs.getInt("id");
				int memberId = rs.getInt("memberId");
				String username = memberDataAccess.findById(memberId).getUsername();
				String contents = rs.getString("contents");
				Date createDate = rs.getDate("createDate");
				comments.add(new CommentEntity(id, articleId, memberId, username, contents, createDate));
			}
		}

		return comments;
	}

	public void insert(int articleId, String username, String contents) throws SQLException, ClassNotFoundException {
		int memberId = memberDataAccess.findByUsername(username).getId();

		try (Connection conn = dbConnector.connect();
				PreparedStatement statement = createPreparedStatement(conn,
						"INSERT INTO comment (articleId, memberId, contents ,createDate) VALUES  (?, ?, ?,NOW())",
						articleId, memberId, contents)) {
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
