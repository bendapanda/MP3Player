module musicPlayerFX {
	requires javafx.controls;
	requires javafx.media;
	requires javafx.graphics;
	requires javafx.base;
	
	opens application to javafx.graphics, javafx.fxml;
}
