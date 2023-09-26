package application;
	
import java.io.File;

import javafx.application.Application;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.geometry.Pos;
import javafx.stage.Stage;
import javafx.scene.Group;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.scene.media.Media;
import javafx.scene.media.MediaPlayer;


public class Main extends Application {
	@Override
	public void start(Stage primaryStage) {
		try {
			
			String rockDJFilePath = "musicPlayerFX/src/resources/rock-dj.mp3";
			
			Song rockdj = new Song("Rock DJ", "src/resources/rock-dj.mp3");
			Song crabRave = new Song("Crab Rave", "src/resources/crab-rave.mp3");
			
			
			Playlist playlist = new Playlist();
			playlist.addSong(rockdj);
			playlist.addSong(crabRave);
			
			MusicPlayer player = new MusicPlayer(playlist, true);
		    
			VBox root = new VBox();
			
		    HBox buttonBar = new HBox();
		    Scene scene = new Scene(root, 600, 300);
		    
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
		    
		    Label songTitle = new Label("song name here");
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
	
	public static void main(String[] args) {
		launch(args);
	}
}
