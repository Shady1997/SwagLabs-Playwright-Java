package database;

import java.sql.*;

public class DatabaseConnection {

	// define db properties
	static String dbName;
	static String port;
	static ResultSet rs;
	static String userName;
	static String Password;
	static Connection con;
	static Statement stmt;

	// declare constructor
	public DatabaseConnection(String dbName, String port, String userName, String Password) {
		DatabaseConnection.dbName = dbName;
		DatabaseConnection.userName = userName;
		DatabaseConnection.Password = Password;
		DatabaseConnection.port = port;
	}

	// start connect to db
	public static void createDBConnection() throws SQLException {
		Connection con = DriverManager.getConnection("jdbc:mysql://localhost:" + port + "/" + dbName, userName,
				Password);
		DatabaseConnection.con = con;
	}

	// create statement
	public static void createStatement() throws SQLException {
		Statement stmt = con.createStatement();
		DatabaseConnection.stmt = stmt;
	}

	// get result set from database
	public static ResultSet dbResultSet(String query) throws SQLException {

		rs = stmt.executeQuery(query);
		return rs;
	}

	// close db connection
	public static void closedbConnection() throws SQLException {
		con.close();
	}
}
