package application;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.HashSet;

/**
 *TODO: Error handling for the class. Fine to print them for now, but should they be thrown in the future?
 *Maybe it is the best idea to let the class using the database catch the exeptions
 *
 *TODO: put all create functions into one private function that takes a sql string as an argument.
 * class that manages the database for the music player.
 * Begin by calling the init function.
 * 
 * 
 * Things this class needs to do
 * 
 * x initalise the database
 * x add a song to the database, with its tags
 * x add a tag to the database
 * x add a playlist to the database, along with all its songs 
 * - query a song(s) from the database, by author
 * x query a tag from the database
 * x query a playlist from the database by name
 * x query songs from the database by tag (s)
 * x query tags from the database by playlist
 * 
 * - editing functionality for the playlist (TODO)
 * - deleting functoinality for the database (TODO
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
				+ "filepath text NOT NULL UNIQUE,\n"
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
				+ "name text NOT NULL UNIQUE"
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
				+ "name text NOT NULL UNIQUE"
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
				+ "tag_id INTEGER,\n"
				+ "song_id INTEGER,\n"
				+ "FOREIGN KEY(tag_id) REFERENCES tags(id),\n"
				+ "FOREIGN KEY(song_id) REFERENCES songs(id),\n"
				+ "UNIQUE(tag_id, song_id)"
				+ ");";
		Statement stmt = conn.createStatement();
		stmt.execute(sqlCommand);
		
		DataBaseManager.closeConnection();
	}
	
	/**
	 * Adds tag to tags database, if the tag is not already in the database.
	 * @param filename : filename of the database
	 * @param tag : tag to add to the database
	 */
	public static void addTagToDataBase(String filename, String tag) throws SQLException{
		DataBaseManager.connect(filename);
		
		String sqlCommand = "INSERT INTO tags (name) VALUES (?);";
		
		PreparedStatement stmt = DataBaseManager.conn.prepareStatement(sqlCommand);
		stmt.setString(1, tag);
		
		try {
			stmt.executeUpdate();
		} catch (SQLException e) {
			System.out.println(e.getMessage());
		}
		
		DataBaseManager.closeConnection();
	}
	/**
	 * TODO: This is definitately not the best way to do this, but will do for now
	 * Given a <code>Song</code> object and a tag (both which should already be in the database),
	 * creates a connection between them
	 * @param filename
	 * @param song
	 * @param tag
	 */
	private static void addSongTagConnectionToDataBase(String filename, Song song, String tag) throws SQLException{
		DataBaseManager.connect(filename);
		
		String sqlCommand = "INSERT INTO songs_tags (song_id, tag_id) VALUES (?, ?);";
		
		String getSongIDSqlCommand = "SELECT id FROM songs WHERE filepath = ?;";
		PreparedStatement songIDStmt = DataBaseManager.conn.prepareStatement(getSongIDSqlCommand);
		songIDStmt.setString(1, song.getFilePath());
		ResultSet songIDRs = songIDStmt.executeQuery();
		int songID = songIDRs.getInt("id");
		
		String getTagIDSqlCommand = "SELECT id FROM tags WHERE name = ?;";
		PreparedStatement tagIDStmt = DataBaseManager.conn.prepareStatement(getTagIDSqlCommand);
		tagIDStmt.setString(1, tag);
		ResultSet tagIDRs = tagIDStmt.executeQuery();
		int tagID = tagIDRs.getInt("id");
		
		PreparedStatement stmt = DataBaseManager.conn.prepareStatement(sqlCommand);
		stmt.setInt(1, songID);
		stmt.setInt(2, tagID);
		
		try {// if the tag is already assigned to the song, throws an error
			stmt.executeUpdate();
		} catch (SQLException e) {
			System.out.println(e.getMessage());
		}
		
		
		DataBaseManager.closeConnection();
	}
	
	/** Adds song to songs table in database. If the table does not exist, creates it.
	 * Also add the song's tags to the database, as well as connecting them through a join table.
	 * @param filename : filename of the database
	 * @param song : <code>Song</code> object to be added
	 * @throws SQLException
	 */
	public static void addSongToDataBase(String filename, Song song) throws SQLException {
		DataBaseManager.connect(filename);
		
		String sqlCommand = "INSERT INTO songs(name, filepath, author) VALUES ( ?, ?, ?);";
		
		PreparedStatement stmt = DataBaseManager.conn.prepareStatement(sqlCommand);
		stmt.setString(1, song.getName());
		stmt.setString(2, song.getFilePath());
		stmt.setString(3, song.getAuthor());
		
		try {// attempt to add the song to the database. If not unique, return
			stmt.executeUpdate();	
		} catch (SQLException e) {
			System.out.println(e.getMessage());
			throw new SQLException("Filepath must be unique");
		}
		
		
		for(String tag : song.getTags()) {
			DataBaseManager.addTagToDataBase(filename, tag);
			DataBaseManager.addSongTagConnectionToDataBase(filename, song, tag);
		}

		
		DataBaseManager.closeConnection();
	}
	
	/**
	 * Adds a playlist to the playlist table if it does not already exist.
	 * 
	 * @param filename : filename of the database
	 * @param playlist : Playlist object to add
	 */
	public static void addPlaylistToDataBase(String filename, Playlist playlist) throws SQLException{
		DataBaseManager.connect(filename);
		
		String sqlCommand = "INSERT INTO playlists(name) VALUES (?);";
		PreparedStatement stmt = DataBaseManager.conn.prepareStatement(sqlCommand);
		stmt.setString(1, playlist.getName());
		
		try {
			stmt.executeUpdate();
		} catch(SQLException e) {
			System.out.println(e.getMessage());
		}
		
		DataBaseManager.closeConnection();
	}
	
	/**
	 * Adds a song to a playlist in the database by updating the playlists_songs table
	 * @param filename
	 * @param song
	 * @param playlist
	 */
	public static void addSongToPlaylist(String filename, Song song, Playlist playlist) throws SQLException{
		DataBaseManager.connect(filename);
		
		String sqlCommand = "INSERT INTO playlists_songs (playlist_id, song_id) VALUES (?, ?);";
		
		String getSongIDSqlCommand = "SELECT id FROM songs WHERE filepath = ?;";
		PreparedStatement songIDStmt = DataBaseManager.conn.prepareStatement(getSongIDSqlCommand);
		songIDStmt.setString(1, song.getFilePath());
		ResultSet songIDRs = songIDStmt.executeQuery();
		int songID = songIDRs.getInt("id");
		
		String getPlaylistIDSqlCommand = "SELECT id FROM playlists WHERE name = ?;";
		PreparedStatement playlistIDStmt = DataBaseManager.conn.prepareStatement(getPlaylistIDSqlCommand);
		playlistIDStmt.setString(1, playlist.getName());
		ResultSet playlistIDRs = playlistIDStmt.executeQuery();
		int playlistID = playlistIDRs.getInt("id");
		
		PreparedStatement stmt = DataBaseManager.conn.prepareStatement(sqlCommand);
		stmt.setInt(1, playlistID);
		stmt.setInt(2, songID);
		stmt.executeUpdate();
		
		DataBaseManager.closeConnection();
	}
	
	/**
	 * Takes in a song name, and returns all songs starting with that string. 
	 * These are in the form of song objects.
	 * @param filename : filename of database
	 * @param name : name, or partial name of song to query
	 * @return
	 */
	public static ArrayList<Song> getSongsByName(String filename, String name) throws SQLException{
		DataBaseManager.connect(filename);
		
		String sqlCommand = "SELECT (id, name, filepath, author) FROM songs WHERE name LIKE ?;";
		PreparedStatement stmt = DataBaseManager.conn.prepareStatement(sqlCommand);
		stmt.setString(1, name + "%");
		ResultSet rs = stmt.executeQuery();
		
		ArrayList<Song> results = new ArrayList<Song>();
		while(rs.next()) {
			Song nextSong = new Song(rs.getString("name"), rs.getString("filepath"));
			nextSong.setAuthor(rs.getString("author"));
			nextSong.setId(rs.getInt("id"));
			HashSet<String> tags = DataBaseManager.getTagsBySong(filename, nextSong);
			for(String tag: tags) {
				nextSong.addTag(tag);
			}
			
			results.add(nextSong);
		}
		
		DataBaseManager.closeConnection();
		return results;
	}
	
	/**
	 * returns a complete playlist, together with all of its songs, based on a complete
	 * playlist name.
	 * @param filename : filename of the database
	 * @param name : name of the playlist
	 * @return a complete Playlist object
	 * @throws SQLException
	 */
	public static Playlist getPlaylistByName(String filename, String name) throws SQLException {
		DataBaseManager.connect(filename);
		
		String sqlCommand = "SELECT id, name FROM playlists WHERE name = ?;";
		PreparedStatement stmt = DataBaseManager.conn.prepareStatement(sqlCommand);
		stmt.setString(1, name);
		ResultSet rs = stmt.executeQuery();
		
		Playlist playlist = new Playlist(rs.getString("name"));
		int playlistID = rs.getInt("id");
		
		for(Song song : DataBaseManager.getSongsByPlaylist(filename, playlist)) {
			playlist.addSong(song);
		}
		
		DataBaseManager.closeConnection();
		return playlist;
	}
	
	/**
	 * returns the songs in a given playlist
	 * @param database : name of the database
	 * @param name : name of the playlist
	 * @return
	 */
	public static ArrayList<Song> getSongsByPlaylist(String filename, Playlist playlist) throws SQLException{
		DataBaseManager.connect(filename);
		
		String sqlCommand = "SELECT songs.id, songs.name, songs.filepath, songs.author FROM \n"
				+ "playlists INNER JOIN playlists_songs \n"
				+ " ON playlists.id = playlists_songs.playlist_id \n"
				+ "INNER JOIN songs ON playlists_songs.song_id = songs.id \n"
				+ "WHERE playlists.name = ?;";
		
		PreparedStatement stmt = DataBaseManager.conn.prepareStatement(sqlCommand);
		stmt.setString(1, playlist.getName());
		ResultSet rs = stmt.executeQuery();
		
		ArrayList<Song> result = new ArrayList<Song>(); 
		while(rs.next()) {
			Song nextSong = new Song(rs.getString("songs.name"), rs.getString("songs.filepath"));
			nextSong.setAuthor(rs.getString("songs.author"));
			for(String tag : DataBaseManager.getTagsBySong(filename, nextSong)) {
				nextSong.addTag(tag);
			}
			result.add(nextSong);
		}
		
		DataBaseManager.closeConnection();
		return result;
	}
			
	
	/**
	 * Given a song (assuming we do not know the tags), returns a HashSet
	 * of tags
	 * @param filename : filename of the database
	 * @param song : a song object
	 * @return A Hashset of all the tags the song has in the database
	 */
	private static HashSet<String> getTagsBySong(String filename, Song song) throws SQLException{
		DataBaseManager.connect(filename);
		if(song.getId() == 0) { //if the song has an id, use it
			String songIDCommand = "SELECT id FROM songs where filepath = ?;";
			PreparedStatement songIDstmt = DataBaseManager.conn.prepareStatement(songIDCommand);
			songIDstmt.setString(1, song.getFilePath());
			
			ResultSet songRs = songIDstmt.executeQuery();
			song.setId(songRs.getInt("id"));
		}
		String sqlCommand = "SELECT tags.name FROM songs_tags \n"
				+ "INNER JOIN tags ON songs_tags.tag_id = tags.id \n"
				+ "WHERE songs_tags.song_id = ?";
		PreparedStatement stmt = DataBaseManager.conn.prepareStatement(sqlCommand);
		stmt.setInt(1, song.getId());
		
		ResultSet rs = stmt.executeQuery();
		
		HashSet<String> result = new HashSet<String>();
		while(rs.next()) {
			result.add(rs.getString("tags.name"));
		}
		
		DataBaseManager.closeConnection();
		return result;
	}
	/**
	 * returns a hashset of all the tags in the database that have a name
	 * like the given string
	 * @param filename : filename of the database
	 * @param tag : name of the tag to be queried
	 * @return
	 */
	public static HashSet<String> getTagsLike(String filename, String tag) throws SQLException {
		DataBaseManager.connect(filename);
		
		String sqlCommand = "SELECT name FROM tags WHERE name like ?;";
		PreparedStatement stmt = DataBaseManager.conn.prepareStatement(sqlCommand);
		stmt.setString(1, tag + "%");
		
		ResultSet rs = stmt.executeQuery();
		HashSet<String> result = new HashSet<String>();
		while(rs.next()) {
			result.add(rs.getString("name"));
		}
		
		DataBaseManager.closeConnection();
		return result;
	}
	
	/**
	 * Given a tag, returns all song objects in the database that have that tag
	 * @param filename : name of database
	 * @param tag : tag to be queried against
	 * @throws SQLException
	 * @returns a list of all the songs with that particular tag
	 */
	public static ArrayList<Song> getSongsByTag(String filename, String tag) throws SQLException {
		DataBaseManager.connect(filename);
		
		String sqlCommand = "SELECT songs.id, songs.name, songs.filepath, songs.author \n"
				+ "FROM songs INNER JOIN songs_tags ON songs.id = songs_tags.song_id \n"
				+ "INNER JOIN tags ON songs_tags.tag_id = tags.id \n"
				+ "WHERE tags.name = ?;";
		
		PreparedStatement stmt = DataBaseManager.conn.prepareStatement(sqlCommand);
		stmt.setString(1, tag);
		ResultSet rs = stmt.executeQuery();
		
		ArrayList<Song> result = new ArrayList<Song>();
		while(rs.next()) {
			Song nextSong = new Song(rs.getString("songs.name"),
					rs.getString(rs.getString("songs.filepath")));
			nextSong.setAuthor(rs.getString("songs.author"));
			nextSong.setId(rs.getInt("songs.id"));
			for(String songTag : DataBaseManager.getTagsBySong(filename, nextSong)) {
				nextSong.addTag(songTag);
			}
			result.add(nextSong);
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
