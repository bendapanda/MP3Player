package application;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;

/**
 *TODO: Error handling for the class. Fine to print them for now, but should they be thrown in the future?
 *Maybe it is the best idea to let the class using the database catch the exeptions
 *
 *TODO: put all create functions into one private function that takes a sql string as an argument.
 * class that manages the database for the music player.
 * Begin by calling the init function.
 */
public class DataBaseManager {
	
	public static String dataBaseName = "MP3Database.db";
	private static Connection conn;
	
	/** Connects to an existing database, or will create a new one
	 * if the database does not exist. Returns an instance of a connection
	 * that needs to be closed later on, hence this method is private
	 * 
	 * @param fileName : the name of the database file
	 * @return Connection conn: An instance of a database connection
	 */
	
	private static void connect(String fileName) throws SQLException{
		String url = "jdbc:sqlite:C:/sqlite/" + fileName;
		
		DataBaseManager.conn = DriverManager.getConnection(url); 

	}
	
	/** Creates the table for songs
	 * 
	 * @param fileName : name of the database
	 * @throws SQLException
	 */
	public static void createSongTable(String fileName) throws SQLException{
		DataBaseManager.connect(fileName);

		String sqlCommand = "CREATE TABLE IF NOT EXISTS songs (\n"
				+ "id integer PRIMARY KEY,\n"
				+ "name text NOT NULL,\n"
				+ "filepath text NOT NULL,\n"
				+ "author text"
				+ ");";
		Statement stmt = conn.createStatement();
		stmt.execute(sqlCommand);
		
		DataBaseManager.closeConnection();
	}
	
	/**
	 * Creates a table to store all the tags used in the system.
	 * @param fileName : name of the database
	 * @throws SQLException
	 */
	public static void createTagsTable(String fileName) throws SQLException{
		DataBaseManager.connect(fileName);

		String sqlCommand = "CREATE TABLE IF NOT EXISTS tags (\n"
				+ "id integer PRIMARY KEY,\n"
				+ "name text NOT NULL"
				+ ");";
		Statement stmt = conn.createStatement();
		stmt.execute(sqlCommand);
		
		DataBaseManager.closeConnection();
	}
	
	/**
	 * Creates a table in the database for storing playlists
	 * @param fileName : filename of the database
	 * @throws SQLException
	 */
	public static void createPlaylistTable(String fileName) throws SQLException{
		DataBaseManager.connect(fileName);

		String sqlCommand = "CREATE TABLE IF NOT EXISTS playlists (\n"
				+ "id integer PRIMARY KEY,\n"
				+ "name text NOT NULL"
				+ ");";
		Statement stmt = conn.createStatement();
		stmt.execute(sqlCommand);
		
		DataBaseManager.closeConnection();
	}
	/**
	 * Creates join table for songs and playlists
	 * @param fileName
	 * @throws SQLException
	 */
	public static void createPlaylistSongTable(String fileName) throws SQLException{
		DataBaseManager.connect(fileName);

		String sqlCommand = "CREATE TABLE IF NOT EXISTS playlist_songs (\n"
				+ "id integer PRIMARY KEY,\n"
				+ "playlist_id INTEGER,\n"
				+ "song_id INTEGER,\n"
				+ "FOREIGN KEY(playlist_id) REFERENCES playlists(id),\n"
				+ "FOREIGN KEY(song_id) REFERENCES songs(id)"
				+ ");";
		Statement stmt = conn.createStatement();
		stmt.execute(sqlCommand);
		
		DataBaseManager.closeConnection();
	}
	
	/**
	 * Creates a join table for representing the many-to-many relation ship between
	 * tags and songs
	 * @param fileName : name of the file the table is being put into.
	 * @throws SQLException
	 */
	public static void createSongTagsTable(String fileName) throws SQLException{
		DataBaseManager.connect(fileName);

		String sqlCommand = "CREATE TABLE IF NOT EXISTS songs_tags (\n"
				+ "id integer PRIMARY KEY,\n"
				+ "playlist_id INTEGER,\n"
				+ "song_id INTEGER,\n"
				+ "FOREIGN KEY(playlist_id) REFERENCES playlists(id),\n"
				+ "FOREIGN KEY(song_id) REFERENCES songs(id)"
				+ ");";
		Statement stmt = conn.createStatement();
		stmt.execute(sqlCommand);
		
		DataBaseManager.closeConnection();
	}
	
	/** Adds song to songs table in database. If the table does not exist, creates it.
	 * TODO: also add the song's tags to the database
	 * @param filename : filename of the database
	 * @param song : <code>Song</code> object to be added
	 * @throws SQLException
	 */
	
	public static void addSongToDataBase(String filename, Song song) throws SQLException {
		DataBaseManager.connect(filename);
		try {
			String sqlCommand = "INSERT INTO songs(name, filepath, author) VALUES ( ?, ?, ?);";
			
			PreparedStatement stmt = DataBaseManager.conn.prepareStatement(sqlCommand);
			stmt.setString(1, song.getName());
			stmt.setString(2, song.getFilePath());
			stmt.setString(3, song.getAuthor());
			
			stmt.executeUpdate();	
		} catch(SQLException e) {
			DataBaseManager.createSongTable(filename);
			DataBaseManager.addSongToDataBase(filename, song);
		}
		
		DataBaseManager.closeConnection();
	}
	
	/** returns a song queried from a database
	 * 
	 * @param filename : filename of database
	 * @param songName : name of song to be queried
	 * @throws SQLException
	 */
	public static ArrayList<Song> getSongsByName(String filename, String songName) throws SQLException {
		DataBaseManager.connect(filename);
		
		String sqlCommand = "SELECT id, name, filepath, author "
				+ "FROM songs WHERE name == ?;";
		
		
		PreparedStatement stmt = DataBaseManager.conn.prepareStatement(sqlCommand);
		stmt.setString(1, songName);
		
		ResultSet rs = stmt.executeQuery();
		
		ArrayList<Song> songs = new ArrayList<Song>();
		while(rs.next()) {
			Song songToAdd = new Song(rs.getString("name"), rs.getString("filepath"));
			songToAdd.setId(rs.getInt("id"));
			songToAdd.setAuthor(rs.getString("author"));
			//Set the songs' tags
		}
		
		DataBaseManager.closeConnection();
		
		return songs;
		
	}
	
	/**
	 * Given a song, queries the database for all tags that the song has
	 * @param filename : name of database to query
	 * @param song : <code>Song</code> object we wish to query the tags for
	 * @return 
	 * @throws SQLException
	 */
	public static ArrayList<String> getTagsBySong(String filename, Song song) throws SQLException {
		DataBaseManager.connect(filename);
		
		String sqlCommand  = "SELECT id, name "
				+ "FROM tags WHERE id IN ("
				+ "SELECT tag_id FROM songs_tags WHERE song_id = ?);";
		
		PreparedStatement stmt = DataBaseManager.conn.prepareStatement(sqlCommand);
		stmt.setInt(1, song.getId());
		
		ResultSet rs = stmt.executeQuery();
		ArrayList<String> result = new ArrayList<String>();
		while(rs.next()) {
			result.add(rs.getString("name"));
		}
		
		DataBaseManager.closeConnection();
		return result;
	}
	
	private static void closeConnection() throws SQLException {
		if(DataBaseManager.conn != null) {
			conn.close();
		}
	}
	/**
	 * Performs all the creation queries for the database. If the database already exists,
	 * practically does nothing.
	 * @param filename : name of database.
	 */
	public static void initaliseDatabase(String filename) throws SQLException{
		DataBaseManager.createSongTable(filename);
		DataBaseManager.createTagsTable(filename);
		DataBaseManager.createPlaylistTable(filename);
		DataBaseManager.createSongTagsTable(filename);
		DataBaseManager.createPlaylistSongTable(filename);
	}
	
}
