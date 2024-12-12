package com.example.demo.factories;

import javafx.scene.control.Button;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;

/**
 * Factory class for creating a quit level button with an image.
 */
public class QuitLevelButtonFactory extends GameButtonFactory {

    private static final String QUIT_LEVEL_BUTTON_IMAGE_PATH = "/com/example/demo/images/quitLevel.png";

    /**
     * Creates a quit level button with an image.
     *
     * @return the created quit level button
     */
    @Override
    public Button createButton() {
        ImageView quitLevelImage = new ImageView(new Image(getClass().getResource(QUIT_LEVEL_BUTTON_IMAGE_PATH).toExternalForm()));
        quitLevelImage.setFitWidth(100);
        quitLevelImage.setFitHeight(60);

        Button quitLevelButton = new Button();
        quitLevelButton.setGraphic(quitLevelImage);
        quitLevelButton.setStyle("-fx-background-color: transparent;");
        return quitLevelButton;
    }
}