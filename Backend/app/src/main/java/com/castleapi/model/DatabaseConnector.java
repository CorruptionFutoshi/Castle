package com.castleapi.model;

import java.io.IOException;
import java.io.InputStream;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.Properties;

public class DatabaseConnector {
	private String url;
	private String user;
	private String password;
	private Connection connection;

	public DatabaseConnector() throws IOException {
		Properties properties = new Properties();

		try (InputStream input = getClass().getClassLoader().getResourceAsStream("database.properties")) {
			if (input == null) {
				throw new IOException("Unable to find database.properties");
			}
			properties.load(input);
		}

		url = properties.getProperty("db.url");
		user = properties.getProperty("db.user");
		password = properties.getProperty("db.password");
	}

	public Connection connect() throws SQLException, ClassNotFoundException {
		if (connection == null || connection.isClosed()) {
			Class.forName("org.mariadb.jdbc.Driver");
			connection = DriverManager.getConnection(url, user, password);
		}
		
		return connection;
	}

	public void close() throws SQLException {
		if (connection != null && !connection.isClosed()) {
			connection.close();
		}
	}
}
