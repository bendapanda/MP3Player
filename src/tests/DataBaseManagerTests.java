package tests;

import static org.junit.jupiter.api.Assertions.*;

import java.sql.Connection;
import java.sql.DatabaseMetaData;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashSet;

import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import application.DataBaseManager;
import application.Playlist;
import application.Song;

class DataBaseManagerTests {
	static String testDatabaseName = "db/testDatabase.db";
	
	private String crabRave = "src/resources/crab-rave.mp3";

	@BeforeAll
	static void setUpBeforeClass() throws Exception {
		DataBaseManager.initaliseDatabase(testDatabaseName);
	}

	@AfterAll
	static void tearDownAfterClass() throws Exception {
	}

	@BeforeEach
	void setUp() throws Exception {
	}

	@AfterEach
	void tearDown() throws Exception {
	}

	/**
	 * Tests connecting to a brand new database, and making a new song table
	 * 
	 * Input: new database name
	 * 
	 * Output: query the database to make sure the table exists
	 */
	@Test
	void testCreateSongTableOnNewDataBase() {
		try {
			DataBaseManager.createSongTable("db/initialisationTest.db");
		} catch (SQLException e) {
			System.out.println(e.getMessage());
			fail();
		}
		// ensure the table exists
		try {
			String url = "jdbc:sqlite:C:/sqlite/db/initialisationTest.db";
			Connection conn = DriverManager.getConnection(url);
			DatabaseMetaData meta = conn.getMetaData();
			ResultSet rs = meta.getTables(null, null, "songs", new String[] {"TABLE"});
			
			if(!rs.next()) {
				System.out.println("no item");
				fail();
			}
			conn.close();
		} catch (SQLException e) {
			System.out.println(e.getMessage());
			fail();
		}
	}
	
	/**
	 * Tests connecting to a brand new database, and making a new playlist table
	 * 
	 * Input: new database name
	 * 
	 * Output: query the database to make sure the table exists
	 */
	@Test
	void testCreatePlaylistTableOnNewDataBase() {
		try {
			DataBaseManager.createPlaylistTable("db/initialisationTest.db");
		} catch (SQLException e) {
			System.out.println(e.getMessage());
			fail();
		}
		// ensure the table exists
		try {
			String url = "jdbc:sqlite:C:/sqlite/db/initialisationTest.db";
			Connection conn = DriverManager.getConnection(url);
			DatabaseMetaData meta = conn.getMetaData();
			ResultSet rs = meta.getTables(null, null, "playlists", new String[] {"TABLE"});
			
			if(!rs.next()) {
				System.out.println("no playlist table");
				fail();
			}
			conn.close();
		} catch (SQLException e) {
			System.out.println(e.getMessage());
			fail();
		}
	}
	
	/**
	 * Tests connecting to a brand new database, and making a new tags table
	 * Input: new database name
	 * 
	 * Output: query the database to make sure the table exists
	 */
	@Test
	void testCreateTagTableOnNewDataBase() {
		try {
			DataBaseManager.createTagsTable("db/initialisationTest.db");
		} catch (SQLException e) {
			System.out.println(e.getMessage());
			fail();
		}
		// ensure the table exists
		try {
			String url = "jdbc:sqlite:C:/sqlite/db/initialisationTest.db";
			Connection conn = DriverManager.getConnection(url);
			DatabaseMetaData meta = conn.getMetaData();
			ResultSet rs = meta.getTables(null, null, "tags", new String[] {"TABLE"});
			
			if(!rs.next()) {
				System.out.println("no tags table");
				fail();
			}
			conn.close();
		} catch (SQLException e) {
			System.out.println(e.getMessage());
			fail();
		}
		
	}
	
	/**
	 * Tests connecting to a brand new database, and making a new playlists songs table
	 * Input: new database name
	 * 
	 * Output: query the database to make sure the table exists
	 */
	@Test
	void testCreatePlaylistSongsTableOnNewDataBase() {
		try {
			DataBaseManager.createPlaylistSongTable("db/initialisationTest.db");
		} catch (SQLException e) {
			System.out.println(e.getMessage());
			fail();
		}
		// ensure the table exists
		try {
			String url = "jdbc:sqlite:C:/sqlite/db/initialisationTest.db";
			Connection conn = DriverManager.getConnection(url);
			DatabaseMetaData meta = conn.getMetaData();
			ResultSet rs = meta.getTables(null, null, "playlist_songs", new String[] {"TABLE"});
			
			if(!rs.next()) {
				System.out.println("no playlists_songs tab;e");
				fail();
			}
			conn.close();
		} catch (SQLException e) {
			System.out.println(e.getMessage());
			fail();
		}
		
	}
	
	/**
	 * Tests connecting to a brand new database, and making a new tags songs table
	 * Input: new database name
	 * 
	 * Output: query the database to make sure the table exists
	 */
	@Test
	void testCreateSongsTagsTableOnNewDataBase() {
		try {
			DataBaseManager.createSongTagsTable("db/initialisationTest.db");
		} catch (SQLException e) {
			System.out.println(e.getMessage());
			fail();
		}
		// ensure the table exists
		try {
			String url = "jdbc:sqlite:C:/sqlite/db/initialisationTest.db";
			Connection conn = DriverManager.getConnection(url);
			DatabaseMetaData meta = conn.getMetaData();
			ResultSet rs = meta.getTables(null, null, "songs_tags", new String[] {"TABLE"});
			
			if(!rs.next()) {
				System.out.println("no songs tags tab;e");
				fail();
			}
			conn.close();
		} catch (SQLException e) {
			System.out.println(e.getMessage());
			fail();
		}
	}
	
	/**
	 * Tests adding tags to database. Ensures the test is successful 
	 * by querying that tag.
	 */
	@Test
	void testAddTagToDatabase() {
		try {//first, add the tag
			DataBaseManager.addTagToDataBase(testDatabaseName, "testTag");
		} catch (SQLException e) {
			fail();
		}
		
		try {
			HashSet<String> res = DataBaseManager.getTagsLike(testDatabaseName, "testTag");
			assertTrue(res.contains("testTag"));
		} catch (SQLException e) {
			System.out.println("error with getTagsLike");
			fail();
		}
		
		// now try add the tag again, and check the result only contains 1 thing
		try {//first, add the tag
			DataBaseManager.addTagToDataBase(testDatabaseName, "testTag");
		} catch (SQLException e) {
			System.out.println("2nd pass");
			fail();
		}
		
		try {
			HashSet<String> res = DataBaseManager.getTagsLike(testDatabaseName, "testTag");
			assertTrue(res.contains("testTag"));
			assertTrue(res.size() == 1);
		} catch (SQLException e) {
			System.out.println("error with getTagsLike");
			fail();
		}		
	}
	
	/*
	 * Tests adding a song to the database, and then queries to see if it is there
	 * Input: a new song
	 * 
	 * Expected output: query the song from the database
	 * 
	 */
	@Test
	void testAddSongNoTagsToDatabase() {
		Song songToAdd = new Song("test song", crabRave);
		songToAdd.setAuthor("red ted");
		
		try {
			DataBaseManager.addSongToDataBase(testDatabaseName, songToAdd);
		} catch (SQLException e) {
			fail();
		}
		
		try {
			String url = "jdbc:sqlite:C:/sqlite/db/initialisationTest.db";
			Connection conn = DriverManager.getConnection(url);
			
			String sqlCommand = "SELECT name FROM songs WHERE filepath = ?;";
			PreparedStatement stmt = conn.prepareStatement(sqlCommand);
			stmt.setString(1, crabRave);
			ResultSet rs = stmt.executeQuery();
			
			assertTrue(rs.next());
			conn.close();
		} catch(SQLException e) {
			fail();
		}
		
		try {// Now try to add the song again (should get an error)
			DataBaseManager.addSongToDataBase(testDatabaseName, songToAdd);
			fail();
		} catch (SQLException e) {
			
		}
		
		
	}
	
	@Test
	void testAddSongToDatabaseWithTags() {
		Song songToAdd = new Song("test song", crabRave);
		songToAdd.setAuthor("red ted");
		songToAdd.addTag("test tag");
		songToAdd.addTag("my tag");
		
		try {
			DataBaseManager.addSongToDataBase(testDatabaseName, songToAdd);
		} catch (SQLException e) {
			fail();
		}
		
		try {
			String url = "jdbc:sqlite:C:/sqlite/db/initialisationTest.db";
			Connection conn = DriverManager.getConnection(url);
			
			String sqlCommand = "SELECT name FROM songs WHERE filepath = ?;";
			PreparedStatement stmt = conn.prepareStatement(sqlCommand);
			stmt.setString(1, crabRave);
			ResultSet rs = stmt.executeQuery();
			
			assertTrue(rs.next());
			
			sqlCommand = "SELECT id, name FROM tags WHERE name = ?;";
			stmt = conn.prepareStatement(sqlCommand);
			stmt.setString(1, "my tag");
			rs = stmt.executeQuery();
			
			assertTrue(rs.next());
			
			int tagId = rs.getInt("id");
			sqlCommand = "SELECT id FROM songs_tags WHERE tag_id = ?;";
			stmt = conn.prepareStatement(sqlCommand);
			stmt.setInt(1, tagId);
			rs = stmt.executeQuery();
			
			assertTrue(rs.next());
		} catch(SQLException e) {
			fail();
		}
	}
	
	/**
	 * tests adding playlist to database, also test no error after adding two
	 */
	@Test
	void testAddingPlaylistToDatabase() {
		Playlist playlist = new Playlist("test playlist");
		playlist.addSong(new Song("test song", crabRave));
		
		try {
			DataBaseManager.addPlaylistToDataBase(testDatabaseName, playlist);
		} catch(SQLException e) {
			fail();
		}
		
		try {
			String url = "jdbc:sqlite:C:/sqlite/db/initialisationTest.db";
			Connection conn = DriverManager.getConnection(url);
			
			String sqlCommand = "SELECT name FROM playlists WHERE name = ";
			PreparedStatement stmt = conn.prepareStatement(sqlCommand);
			stmt.setString(1, playlist.getName());
			ResultSet rs = stmt.executeQuery();
			
			assertTrue(rs.next());
			conn.close();
		} catch(SQLException e) {
			fail();
		}
		
		try {
			DataBaseManager.addPlaylistToDataBase(testDatabaseName, playlist);
		} catch(SQLException e) {
			fail();
		}
		
	}
	/**
	 * test getting a song from the database
	 */
	@Test
	void testGetSongByName() {
		try {
			ArrayList<Song> songs = DataBaseManager.getSongsByName(testDatabaseName, "test son");
			assertEquals(songs.get(0).getName(), "test song");
			assertTrue(songs.get(0).getTags().contains("my tag"));
		} catch(SQLException e) {
			fail();
		}
	}
	
	@Test
	void testGetTagsBySong() {
		try {
			ArrayList<Song> songs = DataBaseManager.getSongsByTag(testDatabaseName, "my tag");
			assertTrue(songs.get(0).getTags().contains("my tag"));
		} catch (SQLException e) {
			fail();
		}
	}
	
}
