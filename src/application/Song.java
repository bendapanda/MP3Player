package application;

import java.io.File;
import java.nio.InvalidMarkException;
import java.nio.file.InvalidPathException;
import java.util.ArrayList;
import java.util.HashSet;

import javafx.scene.media.Media;
import javafx.scene.media.MediaException;

public class Song {
	private String name;
	private String filePath;
	private Media media;
	private HashSet<String> tags = new HashSet<String>();
	private String author = "Unknown Artist";
	
	//TODO: Use a builder pattern to allow for songs to be created easier?
	
	/** Initialises a song object
	 * 
	 * @param name : name of the song
	 * @param filePath : location of the song's mp3 file
	 */
	public Song(String name, String filePath) {
		this.setName(name);
		this.setFilePath(filePath);	
	}
	
	/**
	 * 
	 * @return name of the song
	 */
	public String getName() {
		return name;
	}
	
	public void setName(String name) {
		this.name = name;
	}
	
	/** Returns the filepath of the song playing.
	 * 
	 * @return filepath of the song
	 */
	public String getFilePath() {
		return filePath;
	}
	/** Sets the filepath of the song, and changes the song's media to match
	 * 
	 * @param filePath the location of the song's mp3 file
	 */
	public void setFilePath(String filePath) {
		this.filePath = filePath;
				
		try { // ensure the filepath is valid, else throw an error
			this.setMedia(new Media( new File(this.filePath).toURI().toString()));	
		} catch(MediaException e) {
			throw new InvalidPathException(this.filePath, e.getMessage());
		}	
	}

	/** Returns the song's media.
	 * 
	 * @return the <code>Media</code> object that contains the audio file for the song
	 */
	public Media getMedia() {
		return media;
	}
	
	/** Set's the song's <code>Media</code> private, as user should not
	 * be able to change the media of the song, only file path
	 * 
	 * @param m : the media to be set
	 */
	private void setMedia(Media m) {
		this.media = m;
	}

	/** Returns all of the songs tags
	 * 
	 * @return an <code>ArrayList</code> of all of the song's tags
	 */
	public HashSet<String> getTags() {
		return tags;
	}
	
	/** Returns true if the song has the specified tag
	 * 
	 * @param tagName the name of the tag to be checked
	 * @return true if the song has the tage <code>tagName</code>
	 */
	public boolean hasTag(String tagName) {
		return tags.contains(tagName);
	}
	
	/**Adds the given tag to the song's list of tags, if it is not already in the list.
	 * 
	 * @param tagName name of tag to be added
	 */
	public void addTag(String tagName) {
		tags.add(tagName);
	}
	
	/**
	 * 
	 * @param tagsToSet
	 */
	public void setTags(HashSet<String> tagsToSet) {
		this.tags = tagsToSet;
	}

	public String getAuthor() {
		return author;
	}

	public void setAuthor(String author) {
		this.author = author;
	}
	
}
