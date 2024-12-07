package com.example.demo.ui;

import com.example.demo.factories.PlayButtonFactory;
import com.example.demo.factories.QuitButtonFactory;
import javafx.scene.Scene;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.StackPane;
import javafx.stage.Stage;
import javafx.scene.control.Button;
import javafx.scene.layout.VBox;
import javafx.geometry.Pos;

public class MainMenuPage {


    private static final String BACKGROUND_IMAGE = "/com/example/demo/images/mainMenuBG.png";
    private final PlayButtonFactory playButtonFactory = new PlayButtonFactory();
    private final QuitButtonFactory quitButtonFactory = new QuitButtonFactory();



    public void start(Stage primaryStage) {
        // Load the background image
        ImageView background = null;
        try {
            background = new ImageView(new Image(getClass().getResource(BACKGROUND_IMAGE).toExternalForm()));
            background.setFitWidth(1300); // Set the background size as per your window dimensions
            background.setFitHeight(750);
            background.setPreserveRatio(false); // Stretch the image to fit
        } catch (NullPointerException e) {
            System.err.println("Error: Unable to load background image at path: " + BACKGROUND_IMAGE);
            e.printStackTrace();
        }


        Button playButton = playButtonFactory.createButton();
        // Event handler for Play button
        playButton.setOnAction(event -> {
            com.example.demo.controller.Controller controller = new com.example.demo.controller.Controller(primaryStage);
            controller.launchGame();
        });

        Button quitButton = quitButtonFactory.createButton();
        // Event handler for Quit button
        quitButton.setOnAction(event -> primaryStage.close());

        // Layout for buttons
        VBox buttonsLayout = new VBox(20); // Add some spacing between buttons
        buttonsLayout.getChildren().addAll(playButton, quitButton);
        buttonsLayout.setAlignment(Pos.CENTER); // Center the buttons horizontally and vertically
        buttonsLayout.setTranslateY(100); // Adjust vertical position if needed

        // Create root layout
        StackPane root = new StackPane();
        if (background != null) {
            root.getChildren().add(background); // Add background first to keep it in the back
        }
        root.getChildren().add(buttonsLayout); // Add buttons on top

        // Set the scene and stage
        Scene scene = new Scene(root, 1300, 750);
        primaryStage.setTitle("Sky Battle - Main Menu");
        primaryStage.setScene(scene);
        primaryStage.show();
    }
}