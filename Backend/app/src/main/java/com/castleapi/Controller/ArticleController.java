package com.castleapi.Controller;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.nio.charset.StandardCharsets;
import java.sql.SQLException;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import com.castleapi.model.ArticleDataAccess;
import com.castleapi.model.ArticleLightEntity;
import com.castleapi.util.ObjectToJsonConverter;

import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

@WebServlet("/article/*")
public class ArticleController extends HttpServlet {
	ArticleDataAccess articleDataAccess;

	@Override
	protected void doGet(HttpServletRequest req, HttpServletResponse res) {
		try {
			articleDataAccess = new ArticleDataAccess();

			String path = req.getRequestURI().substring("/app/article/".length());

			if (path.startsWith("articles/")) {
				handleArticlesRequest(req, res, path.substring("articles/".length()));
			} else if (path.equals("tags/")) {
				handleTagsRequest(req, res);
			} else if (path.startsWith("search/")) {
				handleSearchRequest(req, res, path.substring("search/".length()));
			}
		} catch (IOException e) {
			e.printStackTrace();
			return;
		} finally {
			articleDataAccess.dispose();
		}
	}

	private void handleArticlesRequest(HttpServletRequest req, HttpServletResponse res, String idOrTag) {
		int id = -1;
		String tag = null;
		Object foundArticle;
		res.setContentType("application/json");

		try {
			id = Integer.parseInt(idOrTag.replace("/", ""));
		} catch (NumberFormatException e) {
			try {
				idOrTag = java.net.URLDecoder.decode(idOrTag, StandardCharsets.UTF_8.name());
				tag = idOrTag.replace("/", "");
			} catch (UnsupportedEncodingException ex) {
				ex.printStackTrace();
			}
		}

		try {
			if (idOrTag == "") {
				foundArticle = articleDataAccess.findAll();
			} else if (tag == null) {
				foundArticle = articleDataAccess.findById(id);
			} else {
				foundArticle = articleDataAccess.findByTag(tag);
			}

			String json = ObjectToJsonConverter.toJson(foundArticle);
			res.getWriter().write(json);
		} catch (SQLException | IOException | ClassNotFoundException e) {
			e.printStackTrace();
		}
	}

	private void handleTagsRequest(HttpServletRequest req, HttpServletResponse res) {
		res.setContentType("application/json");

		try {
			List<ArticleLightEntity> articles = articleDataAccess.findAll();
			Set<String> uniqueTags = new HashSet<>();

			for (ArticleLightEntity article : articles) {
				uniqueTags.add(article.getTag());
			}

			String json = ObjectToJsonConverter.toJson(uniqueTags);
			res.getWriter().write(json);
		} catch (SQLException | IOException | ClassNotFoundException e) {
			e.printStackTrace();
		}
	}

	private void handleSearchRequest(HttpServletRequest req, HttpServletResponse res, String keyword) {
		try {
			var plainKeyword = java.net.URLDecoder.decode(keyword, StandardCharsets.UTF_8.name());
			List<ArticleLightEntity> articles = articleDataAccess.findByKeyword(plainKeyword.replace("/", ""));
			String json = ObjectToJsonConverter.toJson(articles);
			res.setContentType("application/json");
			res.getWriter().write(json);
		} catch (SQLException | IOException | ClassNotFoundException e) {
			e.printStackTrace();
		}
	}
}
