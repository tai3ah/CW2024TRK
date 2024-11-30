package com.example.demo.factories;

import javafx.scene.control.Button;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;

public class ResumeButtonFactory extends GameButtonFactory {

    private static final String RESUME_BUTTON_IMAGE_PATH = "/com/example/demo/images/resume.png";

    @Override
    public Button createButton() {
        ImageView resumeImage = new ImageView(new Image(getClass().getResource(RESUME_BUTTON_IMAGE_PATH).toExternalForm()));
        resumeImage.setFitWidth(100);
        resumeImage.setFitHeight(60);

        Button resumeButton = new Button();
        resumeButton.setGraphic(resumeImage);
        resumeButton.setStyle("-fx-background-color: transparent;");
        return resumeButton;
    }
}
