package com.example.demo.factories;

import javafx.scene.control.Button;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.Pane;
import javafx.scene.text.Text;

/**
 * Factory class for creating and displaying the level end screen.
 */
public class LevelEndScreenFactory {

    private Pane root;
    private final Text messageText;
    private final Button playAgainButton;
    private final Button nextLevelButton;
    private final ImageView backgroundImage;

    private static final String BACKGROUND_IMAGE_PATH = "/com/example/demo/images/levelEnd.png";

    /**
     * Constructs a LevelEndScreenFactory.
     */
    public LevelEndScreenFactory() {
        messageText = new Text();
        messageText.setStyle("-fx-font-size: 50px; -fx-fill: white; -fx-font-weight: bold;");

        playAgainButton = new Button("Play Again");
        nextLevelButton = new Button("Next Level");

        // Load background image
        backgroundImage = new ImageView(new Image(getClass().getResource(BACKGROUND_IMAGE_PATH).toExternalForm()));
        backgroundImage.setFitWidth(1300);
        backgroundImage.setFitHeight(750);
    }

    /**
     * Initializes the level end screen with the specified root pane, screen width, and screen height.
     *
     * @param root the root pane to which the level end screen components will be added
     * @param screenWidth the width of the screen
     * @param screenHeight the height of the screen
     */
    public void initialize(Pane root, double screenWidth, double screenHeight) {
        this.root = root;

        messageText.setX(screenWidth / 2 - 150);
        messageText.setY(screenHeight / 2 - 100);

        playAgainButton.setLayoutX(screenWidth / 2 - 120);
        playAgainButton.setLayoutY(screenHeight / 2);

        nextLevelButton.setLayoutX(screenWidth / 2 + 20);
        nextLevelButton.setLayoutY(screenHeight / 2);
    }

    /**
     * Displays the win level screen and sets the actions for the play again and next level buttons.
     *
     * @param onPlayAgain the action to be performed when the play again button is clicked
     * @param onNextLevel the action to be performed when the next level button is clicked
     */
    public void showWinLevelScreen(Runnable onPlayAgain, Runnable onNextLevel) {
        if (root == null) {
            throw new IllegalStateException("LevelEndScreenFactory not initialized.");
        }
        messageText.setText("GOOD JOB");

        if (!root.getChildren().contains(backgroundImage)) {
            root.getChildren().add(backgroundImage);
        }
        if (!root.getChildren().contains(messageText)) {
            root.getChildren().addAll(messageText, playAgainButton, nextLevelButton);
        }
        playAgainButton.setOnAction(e -> onPlayAgain.run());
        nextLevelButton.setOnAction(e -> onNextLevel.run());
    }

    /**
     * Displays the lose level screen and sets the action for the play again button.
     *
     * @param onPlayAgain the action to be performed when the play again button is clicked
     */
    public void showLoseLevelScreen(Runnable onPlayAgain) {
        if (root == null) {
            throw new IllegalStateException("LevelEndScreenFactory not initialized.");
        }
        messageText.setText("BETTER LUCK NEXT TIME");

        if (!root.getChildren().contains(backgroundImage)) {
            root.getChildren().add(backgroundImage);
        }
        root.getChildren().remove(nextLevelButton);
        if (!root.getChildren().contains(messageText)) {
            root.getChildren().addAll(messageText, playAgainButton);
        }
        playAgainButton.setOnAction(e -> onPlayAgain.run());
    }
}