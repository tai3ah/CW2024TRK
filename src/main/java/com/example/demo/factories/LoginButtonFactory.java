package com.example.demo.factories;

import javafx.scene.control.Button;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;

/**
 * Factory class for creating a login button with an image.
 */
public class LoginButtonFactory extends GameButtonFactory {

    private static final String LOGIN_BUTTON_IMAGE_PATH = "/com/example/demo/images/login.png";

    /**
     * Creates a login button with an image.
     *
     * @return the created login button
     */
    @Override
    public Button createButton() {
        ImageView loginImage = new ImageView(new Image(getClass().getResource(LOGIN_BUTTON_IMAGE_PATH).toExternalForm()));
        loginImage.setFitWidth(300);
        loginImage.setPreserveRatio(true);

        Button loginButton = new Button();
        loginButton.setGraphic(loginImage);
        loginButton.setStyle("-fx-background-color: transparent;");
        loginButton.setOnAction(event -> {
            // Trigger login action
            System.out.println("Login button clicked! Proceeding with login...");
        });

        return loginButton;
    }
}
