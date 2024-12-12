package com.example.demo.ui;

import com.example.demo.levels.LevelBuilder;
import com.example.demo.levels.LevelParent;
import com.example.demo.managers.LoginManager;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;

/**
 * Class for creating and managing the level selection page in the application.
 */
public class LevelSelectionPage {

    private final Stage primaryStage;
    private final LoginManager loginManager;
    private final String username;
    private final int unlockedLevel;

    /**
     * Constructs a LevelSelectionPage with the specified primary stage, login manager, and username.
     *
     * @param primaryStage the primary stage of the application
     * @param loginManager the login manager to handle user authentication and progress
     * @param username the username of the logged-in user
     */
    public LevelSelectionPage(Stage primaryStage, LoginManager loginManager, String username) {
        this.primaryStage = primaryStage;
        this.loginManager = loginManager;
        this.username = username;
        this.unlockedLevel = loginManager.getUserProgress(username);
    }

    /**
     * Creates the scene for the level selection page.
     *
     * @return the created Scene for the level selection page
     */
    public Scene createScene() {
        StackPane root = new StackPane();

        Label welcomeLabel = new Label("Welcome, " + username + "!");
        welcomeLabel.setStyle("-fx-font-size: 30px; -fx-text-fill: white; -fx-font-weight: bold;");

        VBox levelButtonsLayout = new VBox(15);
        levelButtonsLayout.setAlignment(Pos.CENTER);

        // Generate level buttons based on progress
        for (int i = 1; i <= 4; i++) {
            Button levelButton = new Button("Level " + i);
            if (i <= unlockedLevel) {
                levelButton.setDisable(false); // Unlocked level
                String levelName = "Level" + convertNumberToName(i);
                levelButton.setOnAction(e -> launchLevel(levelName));
            } else {
                levelButton.setDisable(true); // Locked level
            }
            levelButtonsLayout.getChildren().add(levelButton);
        }

        Button backButton = new Button("Logout and Return to Main Menu");
        backButton.setOnAction(event -> {
            MainMenuPage mainMenu = new MainMenuPage();
            mainMenu.start(primaryStage);
        });

        levelButtonsLayout.getChildren().add(backButton);

        GridPane content = new GridPane();
        content.setAlignment(Pos.CENTER);
        content.setVgap(20);
        content.add(welcomeLabel, 0, 0);
        content.add(levelButtonsLayout, 0, 1);

        root.getChildren().add(content);
        Scene scene = new Scene(root, 1300, 750);
        root.setStyle("-fx-background-color: black;"); // Set a dark background
        return scene;
    }

    /**
     * Launches the specified level.
     *
     * @param levelName the name of the level to launch
     */
    private void launchLevel(String levelName) {
        LevelParent selectedLevel = LevelBuilder.createLevel(levelName, getPrimaryStage().getHeight(), getPrimaryStage().getWidth(), primaryStage);
        selectedLevel.setLoggedInUser(username);
        primaryStage.setScene(selectedLevel.initializeScene());
        selectedLevel.startGame();
    }

    /**
     * Converts a level number to its corresponding name.
     *
     * @param levelNumber the level number to convert
     * @return the name of the level
     */
    private String convertNumberToName(int levelNumber) {
        return switch (levelNumber) {
            case 1 -> "One";
            case 2 -> "Two";
            case 3 -> "Three";
            case 4 -> "Four";
            default -> throw new IllegalArgumentException("Invalid level number: " + levelNumber);
        };
    }

    /**
     * Returns the primary stage of the application.
     *
     * @return the primary stage
     */
    private Stage getPrimaryStage() {
        return primaryStage;
    }
}