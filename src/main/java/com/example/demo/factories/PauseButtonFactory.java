package com.example.demo.factories;

import javafx.scene.control.Button;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;

public class PauseButtonFactory extends GameButtonFactory {

    private static final String PAUSE_BUTTON_IMAGE_PATH = "/com/example/demo/images/pause.png";

    @Override
    public Button createButton() {
        ImageView pauseImage = new ImageView(new Image(getClass().getResource(PAUSE_BUTTON_IMAGE_PATH).toExternalForm()));
        pauseImage.setFitWidth(80);
        pauseImage.setFitHeight(60);

        Button pauseButton = new Button();
        pauseButton.setGraphic(pauseImage);
        pauseButton.setStyle("-fx-background-color: transparent;");
        return pauseButton;
    }
}
