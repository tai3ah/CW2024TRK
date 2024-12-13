package com.example.demo.factories;
import javafx.scene.control.Button;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
/**
 * Factory class for creating a main menu button with an image.
 */
class MainMenuButtonFactory extends GameButtonFactory {

    private static final String MAIN_MENU_BUTTON_IMAGE_PATH = "/com/example/demo/images/mainMenuButton.png";

    /**
     * Creates a main menu button with an image.
     *
     * @return the created main menu button
     */
    @Override
    public Button createButton() {
        ImageView mainMenuImage = new ImageView(new Image(getClass().getResource(MAIN_MENU_BUTTON_IMAGE_PATH).toExternalForm()));
        mainMenuImage.setFitWidth(300);
        mainMenuImage.setPreserveRatio(true);

        Button mainMenuButton = new Button();
        mainMenuButton.setGraphic(mainMenuImage);
        mainMenuButton.setStyle("-fx-background-color: transparent;");
        mainMenuButton.setOnAction(event -> {
            // Trigger main menu action
            System.out.println("Main Menu button clicked!");
        });

        return mainMenuButton;
    }
}
