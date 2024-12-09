package com.example.cs262;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.stage.Stage;
import javafx.stage.StageStyle;

import java.io.IOException;


/**
 * Entry point for the application. Responsible for initializing and displaying the primary stage.
 */

public class HelloApplication extends Application {

    /**
     * Starts the application and sets up the primary stage.
     *
     * @param stage the primary stage for this application
     * @throws IOException if the FXML resource is not found
     */
    @Override
    public void start(Stage stage) throws IOException {
        // Load the FXML file for the application
        FXMLLoader fxmlLoader = new FXMLLoader(HelloApplication.class.getResource("/com/example/cs262/FXML.fxml"));

        // Verify that the FXML resource is correctly located
        if (fxmlLoader.getLocation() == null) {
            throw new IOException("FXML resource not found");
        }

        // Create and set up the scene using the loaded FXML
        Scene scene = new Scene(fxmlLoader.load());

        stage.initStyle(StageStyle.UNDECORATED);
        stage.setScene(scene);
        stage.show();
    }

    /**
     * The main method, serving as the entry point for the application.
     *
     * @param args command-line arguments
     */
    public static void main(String[] args) {

        launch(); // Launch the JavaFX application
    }
}
