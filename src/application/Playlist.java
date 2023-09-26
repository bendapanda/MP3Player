package application;

import java.util.ArrayList;

/** Class that allows for the collection of multiple songs in a specific place and order
 * 
 * TODO: error handling for empty playlist - throw errors
 * TODO: shuffle and storage functionality for playlist
 */

public class Playlist {
	private boolean playlistIsEmpty;
	private ArrayList<Song> songs;
	private ArrayList<Song> songsToPlay;
	private int currentIndex;
	
	public Playlist() {
		this.songs = new ArrayList<Song>();
		playlistIsEmpty = true;
	}
	
	public Playlist(ArrayList<Song> songs) {
		this.songs = songs;
		this.currentIndex = 0;
		playlistIsEmpty = false;
	}
	
	
	/**
	 * 
	 * @return The number of songs in the playlist
	 */
	public int getLength() {
		return songs.size();
	}
	/**Returns the song currently selected in the playlist
	 * 
	 * @return the current song
	 * 
	 * @exception 
	 * TODO: error handling for if currentIndex is out of bounds
	 */
	public Song getCurrentSong() {
		return songs.get(currentIndex);
	}
	
	/** Changes to the next song, and keeps track of what has been played
	 * 
	 */
	public void nextSong() {
		songsToPlay.remove(getCurrentSong());
		currentIndex ++;
		currentIndex %= songs.size();
	}
	
	/** Adds a <code>Song</code> object to the playlist
	 * 
	 * @param song song to add
	 */
	public void addSong(Song song) {
		songs.add(song);
	}
}
