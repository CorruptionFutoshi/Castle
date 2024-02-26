package com.castleapi.Controller;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.StringReader;
import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.sql.SQLException;
import java.util.Arrays;
import java.util.UUID;

import com.castleapi.model.MemberDataAccess;
import com.castleapi.model.MemberEntity;

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

@WebServlet("/member/*")
public class MemberController extends HttpServlet {
	MemberDataAccess memberDataAccess;

	public MemberController() {
		try {
			memberDataAccess = new MemberDataAccess();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	@Override
	protected void doPost(HttpServletRequest req, HttpServletResponse res) {
		String path = req.getRequestURI().substring("/app/member/".length());

		if (path.equals("signup/")) {
			handleSignupRequest(req, res);
		} else if (path.equals("signin/")) {
			handleSigninRequest(req, res);
		}
	}

	@Override
	protected void doDelete(HttpServletRequest req, HttpServletResponse res) {
		String path = req.getRequestURI().substring("/app/member/".length());

		if (path.equals("signout/")) {
			handleSignoutRequest(req, res);
		} else if (path.equals("cancelmember/")) {
			handleCancelMemberRequest(req, res);
		}
	}

	private void handleSignupRequest(HttpServletRequest req, HttpServletResponse res) {
		try {
			JsonObject jsonObject = createJsonObject(req);
			String username = jsonObject.getString("username");
			String password = jsonObject.getString("password");

			if (memberDataAccess.findByUsername(username) != null) {
				res.setStatus(HttpServletResponse.SC_CONFLICT);
				res.getWriter().write("Username already taken");
				return;
			}

			byte[] hashedPassword = hashPassword(password);
			memberDataAccess.insert(username, hashedPassword);
			res.setStatus(HttpServletResponse.SC_OK);
			res.getWriter().write("Signup successful");
		} catch (ClassNotFoundException | SQLException | IOException e) {
			e.printStackTrace();
		}
	}

	private void handleSigninRequest(HttpServletRequest req, HttpServletResponse res) {
		String username = null;
		String password = null;
		MemberEntity member = null;

		try {
			JsonObject jsonObject = createJsonObject(req);
			username = jsonObject.getString("username");
			password = jsonObject.getString("password");

			member = memberDataAccess.findByUsername(username);
		} catch (ClassNotFoundException | SQLException | IOException e) {
			e.printStackTrace();
		}

		if (member != null && Arrays.equals(hashPassword(password), member.getHashedPassword())) {
			String sessionId = UUID.randomUUID().toString();
			Cookie sessionCookie = new Cookie("session", sessionId);
			sessionCookie.setMaxAge(60 * 60);
			sessionCookie.setPath("/");
			res.addCookie(sessionCookie);

			try (JedisPool pool = new JedisPool("localhost", 6379);
					Jedis jedis = pool.getResource();) {
				jedis.setex(sessionId, 60 * 60, username);
			}

			res.setStatus(HttpServletResponse.SC_OK);

			try {
				res.getWriter().write("Signin successful");
			} catch (IOException e) {
				e.printStackTrace();
			}

		} else {
			res.setStatus(HttpServletResponse.SC_UNAUTHORIZED);

			try {
				res.getWriter().write("Invalid credential");
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
	}

	private void handleSignoutRequest(HttpServletRequest req, HttpServletResponse res) {
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

		if (sessionId == null) {
			res.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
			try {
				res.getWriter().write("Not signed in");
			} catch (IOException e) {
				e.printStackTrace();
			}
			return;
		}

		try (JedisPool pool = new JedisPool("localhost", 6379);
				Jedis jedis = pool.getResource();) {
			jedis.del(sessionId);
		}

		Cookie sessionCookie = new Cookie("session", "");
		sessionCookie.setMaxAge(0);
		sessionCookie.setPath("/");
		res.addCookie(sessionCookie);

		try {
			res.getWriter().write("Signout successful");
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	private void handleCancelMemberRequest(HttpServletRequest req, HttpServletResponse res) {
		Cookie[] cookies = req.getCookies();
		String sessionId = null;
		String username = null;

		if (cookies != null) {
			for (Cookie cookie : cookies) {
				if (cookie.getName().equals("session")) {
					sessionId = cookie.getValue();
					break;
				}
			}
		}

		if (sessionId == null) {
			res.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
			try {
				res.getWriter().write("Not signed in");
			} catch (IOException e) {
				e.printStackTrace();
			}
			return;
		}

		try (JedisPool pool = new JedisPool("localhost", 6379);
				Jedis jedis = pool.getResource();) {
			username = jedis.get(sessionId);
			jedis.del(sessionId);
		}

		Cookie sessionCookie = new Cookie("session", "");
		sessionCookie.setMaxAge(0);
		sessionCookie.setPath("/");
		res.addCookie(sessionCookie);

		try {
			memberDataAccess.deleteByUsername(username);
			res.setStatus(HttpServletResponse.SC_OK);
			res.getWriter().write("Member cancellation successful");
		} catch (ClassNotFoundException | SQLException | IOException e) {
			e.printStackTrace();
		}
	}

	private byte[] hashPassword(String password) {
		try {
			MessageDigest messageDigest = MessageDigest.getInstance("SHA-512");
			messageDigest.update(password.getBytes(StandardCharsets.UTF_8));
			return messageDigest.digest();
		} catch (NoSuchAlgorithmException e) {
			throw new RuntimeException(e);
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
