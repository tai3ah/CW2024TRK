package com.example.demo.factories;

import com.example.demo.ui.MainMenuPage;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.Pane;
import javafx.scene.layout.StackPane;
import javafx.scene.text.Text;
import javafx.stage.Stage;

/**
 * Factory class for creating and displaying the level end screen.
 */
public class LevelEndScreenFactory {

    private Pane root;
    private final Text messageText;
    private final Button playAgainButton;
    private final Button nextLevelButton;
    private final Button backToMainMenuButton;
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
        backToMainMenuButton = new Button("Main Menu");

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

        backToMainMenuButton.setLayoutX(screenWidth / 2 - 50);
        backToMainMenuButton.setLayoutY(screenHeight / 2 + 80);
    }

    /**
     * Displays the win level screen and sets actions for buttons.
     *
     * @param onPlayAgain the action to be performed when the play again button is clicked
     * @param onNextLevel the action to be performed when the next level button is clicked
     * @param primaryStage the main application stage
     */
    public void showWinLevelScreen(Runnable onPlayAgain, Runnable onNextLevel, Stage primaryStage) {
        if (root == null) {
            throw new IllegalStateException("LevelEndScreenFactory not initialized.");
        }
        messageText.setText("GOOD JOB");

        if (!root.getChildren().contains(backgroundImage)) {
            root.getChildren().add(backgroundImage);
        }
        if (!root.getChildren().contains(messageText)) {
            root.getChildren().addAll(messageText, playAgainButton, nextLevelButton, backToMainMenuButton);
        }

        playAgainButton.setOnAction(e -> onPlayAgain.run());
        nextLevelButton.setOnAction(e -> onNextLevel.run());
        backToMainMenuButton.setOnAction(e -> navigateToMainMenu(primaryStage));
    }

    /**
     * Displays the lose level screen and sets actions for buttons.
     *
     * @param onPlayAgain the action to be performed when the play again button is clicked
     * @param primaryStage the main application stage
     */
    public void showLoseLevelScreen(Runnable onPlayAgain, Stage primaryStage) {
        if (root == null) {
            throw new IllegalStateException("LevelEndScreenFactory not initialized.");
        }
        messageText.setText("BETTER LUCK NEXT TIME");

        if (!root.getChildren().contains(backgroundImage)) {
            root.getChildren().add(backgroundImage);
        }
        root.getChildren().remove(nextLevelButton);
        if (!root.getChildren().contains(messageText)) {
            root.getChildren().addAll(messageText, playAgainButton, backToMainMenuButton);
        }

        playAgainButton.setOnAction(e -> onPlayAgain.run());
        backToMainMenuButton.setOnAction(e -> navigateToMainMenu(primaryStage));
    }

    /**
     * Navigates to the main menu using the provided primary stage.
     *
     * @param primaryStage The main application stage.
     */
    private void navigateToMainMenu(Stage primaryStage) {
        MainMenuPage mainMenuPage = new MainMenuPage();
        StackPane root = new StackPane();
        Scene mainMenuScene = new Scene(root, 1300, 750);
        primaryStage.setScene(mainMenuScene);

        mainMenuPage.start(primaryStage);
        primaryStage.sizeToScene();
        primaryStage.show();
    }
}
