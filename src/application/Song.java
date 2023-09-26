package application;

import java.io.File;
import java.util.ArrayList;

import javafx.scene.media.Media;

public class Song {
	private String name;
	private String filePath;
	private Media media;
	private ArrayList<String> tags;
	
	public Song(String name, String filePath) {
		this.name = name;
		this.filePath = filePath;
		this.tags = new ArrayList<String>();
		
		this.setMedia(new Media( new File(this.filePath).toURI().toString()) );		
	}
	
	public Song(String name, String filePath, ArrayList<String> tags) {
		this.name = name;
		this.filePath = filePath;
		this.tags = tags;
		
		this.setMedia(new Media( new File(this.filePath).toURI().toString()) );		
	}
	
	/**
	 * 
	 * @return name of the song
	 */
	public String getName() {
		return name;
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
		this.setMedia(new Media( new File(this.filePath).toURI().toString() ));
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
	 */
	private void setMedia(Media m) {
		this.media = m;
	}

	/** Returns all of the songs tags
	 * 
	 * @return an <code>ArrayList</code> of all of the song's tags
	 */
	public ArrayList<String> getTags() {
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
		if(!tags.contains(tagName)) {
			tags.add(tagName);
		}
	}
	
	
}
