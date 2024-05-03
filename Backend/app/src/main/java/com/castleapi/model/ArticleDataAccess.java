package com.castleapi.model;

import java.io.IOException;
import java.sql.Connection;
import java.sql.Date;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class ArticleDataAccess {

	private DatabaseConnector dbConnector;

	public ArticleDataAccess() throws IOException {
		dbConnector = new DatabaseConnector();
	}

	public List<ArticleLightEntity> findAll() throws SQLException, ClassNotFoundException {
		return findByQuery("SELECT * FROM article");
	}

	public List<ArticleLightEntity> findByTag(String tag) throws SQLException, ClassNotFoundException {
		return findByQuery("SELECT * FROM article WHERE tag = ?", tag);
	}

	public List<ArticleLightEntity> findByKeyword(String keyword) throws SQLException, ClassNotFoundException {
		String query = "SELECT * FROM article WHERE title LIKE ? OR summary LIKE ? OR contents LIKE ?";
		return findByQuery(query, "%" + keyword + "%", "%" + keyword + "%", "%" + keyword + "%");
	}

	public List<ArticleLightEntity> findByQuery(String query, Object... params)
			throws SQLException, ClassNotFoundException {
		List<ArticleLightEntity> articleList = new ArrayList<>();

		try (Connection conn = dbConnector.connect();
				PreparedStatement statement = createPreparedStatement(conn, query, params);
				ResultSet rs = statement.executeQuery()) {
			while (rs.next()) {
				int id = rs.getInt("id");
				Date createDate = rs.getDate("createDate");
				String title = rs.getString("title");
				String summary = rs.getString("summary");
				String tag = rs.getString("tag");

				articleList.add(new ArticleLightEntity(id, createDate, title, summary, tag));
			}
		}

		return articleList;
	}

	public ArticleEntity findById(int searchId) throws SQLException, ClassNotFoundException {
		ArticleEntity article = null;

		try (Connection conn = dbConnector.connect();
				PreparedStatement statement = createPreparedStatement(conn, "SELECT * FROM article WHERE id = ?",
						searchId);
				ResultSet rs = statement.executeQuery()) {
			while (rs.next()) {
				Date createDate = rs.getDate("createDate");
				String title = rs.getString("title");
				String summary = rs.getString("summary");
				String tag = rs.getString("tag");
				String contents = rs.getString("contents");

				article = new ArticleEntity(searchId, createDate, title, summary, tag, contents);
			}
		}

		return article;
	}

	private PreparedStatement createPreparedStatement(Connection conn, String query, Object... params)
			throws SQLException {
		PreparedStatement statement = conn.prepareStatement(query);

		for (int i = 0; i < params.length; i++) {
			statement.setObject(i + 1, params[i]);
		}

		return statement;
	}

	public void dispose() {
		try {
			dbConnector.close();
		} catch (SQLException e) {
			e.printStackTrace();
		}
	}
}
