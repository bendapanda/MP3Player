package application;
	
import java.io.File;
import java.sql.SQLException;
import java.util.ArrayList;

import javafx.application.Application;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.geometry.Pos;
import javafx.stage.Stage;
import javafx.scene.Group;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.input.KeyEvent;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.scene.media.Media;
import javafx.scene.media.MediaPlayer;
/**
 * Basic UI for the overall application. Needs some extensive work as-is, just a prototype for now
 */

public class Main extends Application {
	private static String dataBaseName = "db/prototype.db";
	@Override
	public void start(Stage primaryStage) {
		try {
			
			
			Playlist playlist = DataBaseManager.getPlaylistByName("myPlaylist");
			
			
			
			MusicPlayer player = new MusicPlayer(playlist, true);
		    
			// now display the scene
			VBox root = new VBox();
		    Scene scene = new Scene(root, 600, 300);
		    
			
		    HBox buttonBar = new HBox();
		    addPlayPauseButtons(buttonBar, player);
		    
		    HBox searchBar = new HBox();
		    TextField searcher = new TextField();
		    Label searchLabel = new Label("Search: ");
		    
		    
		    EventHandler<KeyEvent> onSearched = new EventHandler<KeyEvent>() {
		    	@Override
		    	public void handle(KeyEvent arg0) {
		    		String seracherInput = searcher.getText();
		    		ArrayList<Song> result = new ArrayList<Song>();
		    		try {
						result = DataBaseManager.getSongsByName(seracherInput);
					} catch (SQLException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
		    		
		    		for(Song s: result) {
		    			System.out.println(s.getName());
		    		}
		    		
		    	}
		    };
		    
		    
		    
		    searcher.setOnKeyTyped(onSearched);
		    searchBar.getChildren().addAll(searchLabel, searcher);
		    searchBar.setSpacing(10);
		    
		    
		    Label songTitle = new Label("song name here");
		    root.getChildren().add(searchBar);
		    root.getChildren().add(songTitle);
		    root.getChildren().add(buttonBar);
		    root.setAlignment(Pos.CENTER);
		    
		    primaryStage.setTitle("Music Player");
		    primaryStage.setScene(scene);
		    
		    primaryStage.show();

		    
		} catch(Exception e) {
			e.printStackTrace();
		}
		
	}
	/**
	 * Function that adds all of the play pause skip buttons to the scene
	 */
	private static void addPlayPauseButtons(HBox buttonBar, MusicPlayer player) {
		Button playButton = new Button("Play");
	    Button pauseButton = new Button("Pause");
	    Button skipButton = new Button("Skip");
	    Button backButton = new Button("Back");
	    
	    EventHandler<ActionEvent> playClicked = new EventHandler<ActionEvent>() {
	    	public void handle(ActionEvent e) {
	    		player.play();
	    	}
	    };
	    EventHandler<ActionEvent> pauseClicked = new EventHandler<ActionEvent>() {
	    	public void handle(ActionEvent e) {
	    		player.pause();
	    	}
	    };
	    
	    EventHandler<ActionEvent> skipClicked = new EventHandler<ActionEvent>() {
	    	public void handle(ActionEvent e) {
	    		player.nextSong();
	    	}
	    };
	    
	    playButton.setOnAction(playClicked);
	    pauseButton.setOnAction(pauseClicked);
	    skipButton.setOnAction(skipClicked);
	    
	    buttonBar.getChildren().add(backButton);
	    buttonBar.getChildren().add(playButton);
	    buttonBar.getChildren().add(pauseButton);
	    buttonBar.getChildren().add(skipButton);
	    buttonBar.setAlignment(Pos.CENTER);
	}
	
	public static void main(String[] args) {
		try {
			DataBaseManager.initaliseDatabase(dataBaseName);
			launch(args);
			DataBaseManager.closeConnection();
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
	}
}
