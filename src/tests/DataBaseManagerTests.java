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

}
