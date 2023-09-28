package application;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

/**
 * class that manages the database for the music player.
 * Begin by calling the init function.
 */
public class DataBaseManager {
	
	/** Connects to an existing database, or will create a new one
	 * if the database does not exist. Returns an instance of a connection
	 * that needs to be closed later on, hence this method is private
	 * 
	 * @param fileName : the name of the database file
	 * @return Connection conn: An instance of a database connection
	 */
	private static Connection connect(String fileName) {
		String url = "jdbc:sqlite:C:/sqlite/" + fileName;
		
		Connection conn = null;
		try {
			conn = DriverManager.getConnection(url); 
		} catch (SQLException e) {
			System.out.println(e.getMessage());
		}
		
		return conn;
	}
	public static void createSongTable() {
		
	}
	
}
