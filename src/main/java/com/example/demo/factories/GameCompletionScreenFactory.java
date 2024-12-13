package com.example.demo.factories;

import javafx.scene.control.Button;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.Pane;
import javafx.scene.text.Text;

/**
 * Factory class for creating and displaying the game completion screen.
 */
public class GameCompletionScreenFactory {

    private final Pane root;
    private final Text messageText;
    private final Button mainMenuButton;
    private final ImageView backgroundImage;

    private final MainMenuButtonFactory mainMenuButtonFactory = new MainMenuButtonFactory();
    private static final String BACKGROUND_IMAGE_PATH = "/com/example/demo/images/victory.png";

    /**
     * Constructs a GameCompletionScreenFactory with the specified root pane, screen width, and screen height.
     *
     * @param root the root pane to which the game completion screen components will be added
     * @param screenWidth the width of the screen
     * @param screenHeight the height of the screen
     */
    public GameCompletionScreenFactory(Pane root, double screenWidth, double screenHeight) {
        this.root = root;

        // Load background image
        backgroundImage = new ImageView(new Image(getClass().getResource(BACKGROUND_IMAGE_PATH).toExternalForm()));
        backgroundImage.setFitWidth(screenWidth);
        backgroundImage.setFitHeight(screenHeight);

        // Setup message text
        messageText = new Text("Congrats! You WIN");
        messageText.setStyle("-fx-font-size: 60px; -fx-fill: white; -fx-font-weight: bold;");
        messageText.setX(screenWidth / 2 - 200);
        messageText.setY(screenHeight / 2 - 100);

        // Setup main menu button
        mainMenuButton = mainMenuButtonFactory.createButton();
        mainMenuButton.setLayoutX(screenWidth / 2 - 80);
        mainMenuButton.setLayoutY(screenHeight / 2 + 50);
    }

    /**
     * Displays the game completion screen and sets the action for the main menu button.
     *
     * @param onMainMenu the action to be performed when the main menu button is clicked
     */
    public void showGameCompletionScreen(Runnable onMainMenu) {
        if (!root.getChildren().contains(backgroundImage)) {
            root.getChildren().add(backgroundImage);
        }
        if (!root.getChildren().contains(messageText)) {
            root.getChildren().addAll(messageText, mainMenuButton);
        }
        mainMenuButton.setOnAction(e -> onMainMenu.run());
    }
}