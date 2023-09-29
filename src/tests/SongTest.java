/**
 * 
 */
package tests;

import static org.junit.jupiter.api.Assertions.*;

import java.io.File;
import java.nio.file.InvalidPathException;
import java.util.Collection;
import java.util.HashSet;

import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import application.Song;
import javafx.scene.media.Media;

/**
 * 
 */
class SongTest {
	
	private String testSongName = "testSong";
	private String testSongURL = "src/resources/rock-dj.mp3";
	
	private String secondTestSongURL = "src/resources/crab-rave.mp3";
	
	private Song testSong;

	/**
	 * @throws java.lang.Exception
	 */
	@BeforeAll
	static void setUpBeforeClass() throws Exception {
	}

	/**
	 * @throws java.lang.Exception
	 */
	@AfterAll
	static void tearDownAfterClass() throws Exception {
	}

	/**
	 * @throws java.lang.Exception
	 */
	@BeforeEach
	void setUp() throws Exception {
		this.testSong  = new Song(this.testSongName, this.testSongURL);
	}

	/**
	 * @throws java.lang.Exception
	 */
	@AfterEach
	void tearDown() throws Exception {
		
	}
	
	/**
	 * Test to ensure we get an exception after providing
	 * and invalid URL
	 */
	@Test
	void testCreatingClassInvalidFilePath() {
		try {
			Song testSong = new Song("testSong", "invalid URL");
			fail();
		} catch(InvalidPathException e) {
			
		}
	}
	
	/** 
	 * Test to ensure given a valid URL the song actually runs
	 */
	@Test
	void testCreatingClassValidFilePath() {
		Song testSong = new Song(this.testSongName, this.testSongURL);
	}
	
	/**
	 * test functionality of getName method
	 */
	@Test
	void testGetName() {
		assertEquals(this.testSongName, this.testSong.getName());
	}
	
	/**
	 * Test funcitonality of getFilepath method
	 */
	@Test
	void testGetFilepath() {
		assertEquals(this.testSongURL, this.testSong.getFilePath());
	}
	
	
	/**
	 * Test funcitonality of setFilePath method,
	 * ensuring we can change the path to something valid,
	 * and not change it to something stupid
	 */
	@Test
	void testGetSetFilepath() {
		Media testMedia = new Media( new File(this.secondTestSongURL).toURI().toString() );
		this.testSong.setFilePath(this.secondTestSongURL);
		assertEquals(this.secondTestSongURL, this.testSong.getFilePath());
		
		try {
			this.testSong.setFilePath("something stupid");
			fail();
		} catch(InvalidPathException e) {
			
		}
	}
	
	/**TODO:
	 * Test ability to get and set tags
	 */
	@Test
	void testGetSetTags() {
		HashSet<String> tags = new HashSet<String>();
		tags.add("Tag1");
		tags.add("Tag2");
		tags.add("Tag3");
		
		this.testSong.setTags(tags);
		
	}
	
}
