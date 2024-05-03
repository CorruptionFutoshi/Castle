package com.castleapi.Controller;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.StringReader;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import com.castleapi.model.CommentDataAccess;
import com.castleapi.model.CommentEntity;
import com.castleapi.util.ObjectToJsonConverter;

import jakarta.json.Json;
import jakarta.json.JsonObject;
import jakarta.json.JsonReader;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import redis.clients.jedis.Jedis;
import redis.clients.jedis.JedisPool;

@WebServlet("/comment/*")
public class CommentController extends HttpServlet {
	CommentDataAccess commentDataAccess;

	@Override
	protected void doGet(HttpServletRequest req, HttpServletResponse res) {
		res.setContentType("application/json");

		try {
			commentDataAccess = new CommentDataAccess();
			String path = req.getRequestURI().substring("/app/comment/".length());

			int articleId = -1;
			List<CommentEntity> comments = new ArrayList<CommentEntity>();

			try {
				articleId = Integer.parseInt(path.replace("/", ""));
			} catch (NumberFormatException e) {
				e.printStackTrace();
			}

			try {
				comments = commentDataAccess.findByArticleId(articleId);
				String json = ObjectToJsonConverter.toJson(comments);
				res.getWriter().write(json);
			} catch (SQLException | ClassNotFoundException | IOException e) {
				e.printStackTrace();
			}
		} catch (IOException e) {
			e.printStackTrace();
			return;
		} finally {
			commentDataAccess.dispose();
		}
	}

	@Override
	protected void doPost(HttpServletRequest req, HttpServletResponse res) {
		res.setContentType("application/json");

		if (req.getContentType() == null || !req.getContentType().equals("application/json")) {
			res.setStatus(HttpServletResponse.SC_BAD_REQUEST);

			try {
				res.getWriter().write("{\"message\": \"Request Invalid\"}");
			} catch (IOException e) {
				e.printStackTrace();
			}

			return;
		}

		try {
			commentDataAccess = new CommentDataAccess();
			String path = req.getRequestURI().substring("/app/comment/".length());
			int articleId = -1;
			String username = null;
			String contents = null;

			Cookie[] cookies = req.getCookies();
			String sessionId = null;

			if (cookies != null) {
				for (Cookie cookie : cookies) {
					if (cookie.getName().equals("session")) {
						sessionId = cookie.getValue();
						break;
					}
				}
			}

			try {
				articleId = Integer.parseInt(path.replace("/", ""));
				JsonObject jsonObject = createJsonObject(req);

				try (JedisPool pool = new JedisPool("localhost", 6379);
						Jedis jedis = pool.getResource()) {
					username = jedis.get(sessionId);

					if (username == null) {
						try {
							res.getWriter().write("{\"message\":\"Not signed in\"}");
						} catch (IOException e) {
							e.printStackTrace();
						}
						return;
					}
				}

				contents = jsonObject.getString("contents");
			} catch (IOException | NumberFormatException e) {
				e.printStackTrace();
			}

			try {
				commentDataAccess.insert(articleId, username, contents);
				String json = ObjectToJsonConverter.toJson(commentDataAccess.findByArticleId(articleId));
				res.getWriter().write(json);
			} catch (ClassNotFoundException | SQLException | IOException e) {
				e.printStackTrace();
			}
		} catch (IOException e) {
			e.printStackTrace();
			return;
		} finally {
			commentDataAccess.dispose();
		}
	}

	private JsonObject createJsonObject(HttpServletRequest req) throws IOException {
		StringBuilder stringBuilder = new StringBuilder();
		String line;
		BufferedReader reader = req.getReader();

		while ((line = reader.readLine()) != null) {
			stringBuilder.append(line);
		}

		String jsonString = stringBuilder.toString();
		JsonReader jsonReader = Json.createReader(new StringReader(jsonString));

		return jsonReader.readObject();
	}
}
