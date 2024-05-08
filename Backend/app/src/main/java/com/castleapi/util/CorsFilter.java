package com.castleapi.util;

import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;

import jakarta.servlet.Filter;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.ServletRequest;
import jakarta.servlet.ServletResponse;
import jakarta.servlet.annotation.WebFilter;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

@WebFilter("/*")
public class CorsFilter implements Filter {

	@Override
	public void doFilter(ServletRequest req, ServletResponse res, FilterChain chain)
			throws IOException, ServletException {
		Properties properties = new Properties();

		try (InputStream input = getClass().getClassLoader().getResourceAsStream("application.properties")) {
			if (input == null) {
				throw new IOException("Unable to find application.properties");
			}
			properties.load(input);
		}

		String corsAllowedOrigin = properties.getProperty("cors.allowed.origin");

		HttpServletRequest request = (HttpServletRequest) req;
		HttpServletResponse response = (HttpServletResponse) res;

		response.setHeader("Access-Control-Allow-Origin", corsAllowedOrigin);
		response.setHeader("Access-Control-Allow-Methods", "GET, POST, DELETE, OPTIONS");
		response.setHeader("Access-Control-Allow-Headers", "Content-Type, X-Requested-With");
		response.setHeader("Access-Control-Allow-Credentials", "true");
		response.setHeader("X-Content-Type-Options", "nosniff"); 
		String requestedWithHeader = request.getHeader("X-Requested-With");
		
		if (!request.getMethod().equals("OPTIONS") && (requestedWithHeader == null || !requestedWithHeader.equals("XMLHttpRequest"))) {
			response.setStatus(HttpServletResponse.SC_FORBIDDEN);
			return;
		}
		
		chain.doFilter(req, res);
	}
}