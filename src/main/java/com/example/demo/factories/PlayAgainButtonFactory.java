package com.example.demo.factories;

import javafx.scene.control.Button;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;


/**
 * Factory class for creating a play again button with an image.
 */
class PlayAgainButtonFactory extends GameButtonFactory {

    private static final String PLAY_AGAIN_BUTTON_IMAGE_PATH = "/com/example/demo/images/playAgain.png";

    /**
     * Creates a play again button with an image.
     *
     * @return the created play again button
     */
    @Override
    public Button createButton() {
        ImageView playAgainImage = new ImageView(new Image(getClass().getResource(PLAY_AGAIN_BUTTON_IMAGE_PATH).toExternalForm()));
        playAgainImage.setFitWidth(300);
        playAgainImage.setPreserveRatio(true);

        Button playAgainButton = new Button();
        playAgainButton.setGraphic(playAgainImage);
        playAgainButton.setStyle("-fx-background-color: transparent;");
        playAgainButton.setOnAction(event -> {
            // Trigger play again action
            System.out.println("Play Again button clicked!");
        });

        return playAgainButton;
    }
}