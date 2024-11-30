package com.example.demo.factories;

import javafx.scene.control.Button;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;

public class QuitButtonFactory extends GameButtonFactory {

    private static final String QUIT_BUTTON_IMAGE_PATH = "/com/example/demo/images/quit.png";

    @Override
    public Button createButton() {
        ImageView quitImage = new ImageView(new Image(getClass().getResource(QUIT_BUTTON_IMAGE_PATH).toExternalForm()));
        quitImage.setFitWidth(300);
        quitImage.setPreserveRatio(true);

        Button quitButton = new Button();
        quitButton.setGraphic(quitImage);
        quitButton.setStyle("-fx-background-color: transparent;");
        quitButton.setOnAction(event -> {
            System.out.println("Quit button clicked! Closing game...");
            System.exit(0);
        });

        return quitButton;
    }
}
