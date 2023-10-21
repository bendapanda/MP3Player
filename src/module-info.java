module musicPlayerFX {
	requires javafx.controls;
	requires javafx.media;
	requires javafx.graphics;
	requires javafx.base;
	requires java.sql;
	requires org.junit.jupiter.api;
	requires javafx.swing;
	
	opens application to javafx.graphics, javafx.fxml;
}
