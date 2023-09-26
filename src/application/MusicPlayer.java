package application;

import javafx.scene.media.MediaPlayer;

/** Class that controls all features of music playing
 * 
 */

public class MusicPlayer {
	private boolean isPlaying;
	private Playlist playlist;
	private MediaPlayer media;
	
	public MusicPlayer(Playlist p, boolean playImmediately) {
		playlist = p;
		
		media = new MediaPlayer( playlist.getCurrentSong().getMedia() );
		isPlaying = playImmediately;
		if(playImmediately) {
			media.play();
		} else {
			media.pause();
		}
	}
	/** Changes the playlist
	 * 
	 * @param playlist the new playlist
	 */
	public void setPlaylist(Playlist playlist) {
		this.playlist = playlist;
	}
	/**
	 * Plays the song
	 */
	public void play() {
		media.play();
		isPlaying = true;
	}
	
	/**
	 * Pauses the song
	 */
	public void pause() {
		media.pause();
		isPlaying = false;
	}
	/** Changes the song from playing to paused and vice versa.
	 * 
	 */
	public void togglePlayPause() {
		if(isPlaying) {
			isPlaying = false;
			media.pause();
		} else {
			isPlaying = true;
			media.play();
		}
	}
	
	/** changes the song to the next one in the playlist.
	 * If the current song is paused, the new one will be too.
	 * 
	 */
	public void nextSong() {
		media.stop();
		playlist.nextSong();
		media = new MediaPlayer( playlist.getCurrentSong().getMedia() );
		if (isPlaying) {
			media.play();
		} else {
			media.pause();
		}
	}
}
