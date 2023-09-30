package tests;

import static org.junit.jupiter.api.Assertions.*;

import java.sql.Connection;
import java.sql.SQLException;

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
	 * Tests inserting into a database that does not have a valid table
	 */
	@Test
	void testCreatingDatabase() {
		try {
			DataBaseManager.initaliseDatabase(DataBaseManagerTests.testDatabaseName);
		} catch(SQLException e) {
			System.out.println(e.getMessage());
			fail();
		}
		
	}
	
	/**
	 * tests adding a song to the database (TODO at the moment tests are done by hand,
	 * in SQLite itself. In the future these will be queried with other functions i write)
	 */
	@Test
	void testAddSongToDataBase() {
		try {
			Song songToAdd = new Song("test_song", "src/resources/crab-rave.mp3");
			songToAdd.addTag("Hype");
			songToAdd.addTag("Sick Music");
			DataBaseManager.addSongToDataBase(testDatabaseName, songToAdd);
		} catch (SQLException e) {
			System.out.println(e.getMessage());
			fail();
		}
	}
	
	/**
	 * tests adding a playlist to the database
	 */
	@Test
	void testAddPlaylistToDataBase() {
		try {
			Playlist playlistToAdd = new Playlist("myPlaylist");
			DataBaseManager.addPlaylistToDataBase(testDatabaseName, playlistToAdd);	
		} catch (SQLException e) {
			System.out.println(e.getMessage());
			fail();
		}
	}
	
	

}
