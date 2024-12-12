package com.example.demo.factories;

import javafx.scene.control.Button;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;

/**
 * Factory class for creating a play button with an image.
 */
public class PlayButtonFactory extends GameButtonFactory {

    private static final String PLAY_BUTTON_IMAGE_PATH = "/com/example/demo/images/play.png";

    /**
     * Creates a play button with an image.
     *
     * @return the created play button
     */
    @Override
    public Button createButton() {
        ImageView playImage = new ImageView(new Image(getClass().getResource(PLAY_BUTTON_IMAGE_PATH).toExternalForm()));
        playImage.setFitWidth(300);
        playImage.setPreserveRatio(true);

        Button playButton = new Button();
        playButton.setGraphic(playImage);
        playButton.setStyle("-fx-background-color: transparent;");
        playButton.setOnAction(event -> {
            // Launch level one
            System.out.println("Play button clicked! Launching game...");
        });

        return playButton;
    }
}