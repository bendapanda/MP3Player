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
import javafx.embed.swing.JFXPanel;

class DataBaseManagerTests {
	static String testDatabaseName = "db/testDatabase.db";
	
	private String crabRave = "src/resources/crab-rave.mp3";
	private String rockDJ = "src/resources/rock-dj.mp3";

	@BeforeAll
	static void setUpBeforeClass() throws Exception {
		JFXPanel fxPanel = new JFXPanel();
	}

	@AfterAll
	static void tearDownAfterClass() throws Exception {
	}

	@BeforeEach
	void setUp() throws Exception {
		DataBaseManager.initaliseDatabase(testDatabaseName);
	}

	@AfterEach
	void tearDown() throws Exception {
		DataBaseManager.closeConnection();
	}

	
	/**
	 * Tests adding tags to database. Ensures the test is successful 
	 * by querying that tag.
	 */
	@Test
	void testAddTagToDatabase() {
		try {//first, add the tag
			DataBaseManager.addTagToDataBase("testTag");
		} catch (SQLException e) {
			fail();
		}
		
		try {
			HashSet<String> res = DataBaseManager.getTagsLike("testTag");
			assertTrue(res.contains("testTag"));
		} catch (SQLException e) {
			System.out.println("error with getTagsLike");
			fail();
		}
		
		// now try add the tag again, and check the result only contains 1 thing
		try {//first, add the tag
			DataBaseManager.addTagToDataBase("testTag");
		} catch (SQLException e) {
			System.out.println("2nd pass");
			fail();
		}
		
		try {
			HashSet<String> res = DataBaseManager.getTagsLike("testTag");
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
		songToAdd.setAuthor("red red");
		
		try {
			DataBaseManager.addSongToDataBase(songToAdd);
		} catch (SQLException e) {
			System.out.println("failed adding song the first time");
			fail();
		}
		
		try {
			Song result = DataBaseManager.getSongsByName("test song").get(0);
			assertTrue(result.getAuthor() == "red red");
		} catch(SQLException e) {
			System.out.println("something wrong wiht the query");
			fail();
		}
		
		try {// Now try to add the song again (should get an error)
			DataBaseManager.addSongToDataBase(songToAdd);
			System.out.println("Something went wrong adding for the second time");
			fail();
		} catch (SQLException e) {
			
		}
		
		
	}
	
	@Test
	void testAddSongToDatabaseWithTags() {
		Song songToAdd = new Song("test song2", rockDJ);
		songToAdd.setAuthor("red ted");
		songToAdd.addTag("test tag");
		songToAdd.addTag("my tag");
		
		try {
			DataBaseManager.addSongToDataBase(songToAdd);
		} catch (SQLException e) {
			System.out.println("add to database" + e.getMessage());
			fail();
		}
		
		try {
			Song result = DataBaseManager.getSongsByName("test song2").get(0);
			HashSet<String> tags = DataBaseManager.getTagsLike("test");
			
			assertTrue(result.getTags().contains("test tag"));
			assertTrue(tags.contains("test tag"));
			
		} catch(SQLException e) {
			fail();
			System.out.println("something wrong with query");
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
			DataBaseManager.addPlaylistToDataBase(playlist);
		} catch(SQLException e) {
			System.out.println("failed test adding playlist to database for the first time");
			fail();
		}
		
		try {
			Playlist p = DataBaseManager.getPlaylistByName("test playlist");
			String name = p.getName();
			assertTrue(name.equals("test playlist"));
		} catch(SQLException e) {
			System.out.println("something is wrong with the query");
			System.out.println(e.getMessage());
			fail();
		}
		
		try {
			DataBaseManager.addPlaylistToDataBase(playlist);
		} catch(SQLException e) {
			System.out.println("failed test adding playlist to database for the second time");
			fail();
		}
		
	}
	/**
	 * test getting a song from the database
	 */
	@Test
	void testGetSongByName() {
		
		try {
			Song songToGet = new Song("test song", crabRave);
			songToGet.addTag("my tag");
			DataBaseManager.addSongToDataBase(songToGet);
			ArrayList<Song> songs = DataBaseManager.getSongsByName("test son");
			assertEquals(songs.get(0).getName(), "test song");
			assertTrue(songs.get(0).getTags().contains("my tag"));
		} catch(SQLException e) {
			System.out.println("exception" + e.getMessage());
			fail();
		}
	}
	
	/*
	 * Test getting a song's tags from the database
	 */
	@Test
	void testGetTagsBySong() {
		try {
			ArrayList<Song> songs = DataBaseManager.getSongsByTag("my tag");
			assertTrue(songs.get(0).getTags().contains("my tag"));
		} catch (SQLException e) {
			System.out.println(e.getMessage());
			fail();
		}
	}
	
	/*
	 * Test getting tags like a certian tag
	 */
	@Test
	void testGettingTagsLike() {
		try {
			DataBaseManager.addTagToDataBase( "testtag1");
			DataBaseManager.addTagToDataBase("testtag2");
		} catch (SQLException e) {
			fail();
		}
		
		try {
			HashSet<String> tagSet = DataBaseManager.getTagsLike("test");
			assertTrue(tagSet.contains("testtag1"));
			assertTrue(tagSet.contains("testtag2"));
			
			tagSet = DataBaseManager.getTagsLike("testtag1");
			assertTrue(tagSet.contains("testtag1"));
			assertFalse(tagSet.contains("testtag2"));
		} catch (SQLException e) {
			fail();
		}
	}
	
}
