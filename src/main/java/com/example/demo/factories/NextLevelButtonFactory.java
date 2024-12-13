package com.example.demo.factories;

import javafx.scene.control.Button;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;

/**
 * Factory class for creating a next level button with an image.
 */
class NextLevelButtonFactory extends GameButtonFactory {

    private static final String NEXT_LEVEL_BUTTON_IMAGE_PATH = "/com/example/demo/images/nextLevel.png";

    /**
     * Creates a next level button with an image.
     *
     * @return the created next level button
     */
    @Override
    public Button createButton() {
        ImageView nextLevelImage = new ImageView(new Image(getClass().getResource(NEXT_LEVEL_BUTTON_IMAGE_PATH).toExternalForm()));
        nextLevelImage.setFitWidth(300);
        nextLevelImage.setPreserveRatio(true);

        Button nextLevelButton = new Button();
        nextLevelButton.setGraphic(nextLevelImage);
        nextLevelButton.setStyle("-fx-background-color: transparent;");
        nextLevelButton.setOnAction(event -> {
            // Trigger next level action
            System.out.println("Next Level button clicked!");
        });

        return nextLevelButton;
    }
}