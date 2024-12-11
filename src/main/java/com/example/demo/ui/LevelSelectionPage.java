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

public class LevelSelectionPage {

    private final Stage primaryStage;
    private final LoginManager loginManager;
    private final String username;
    private final int unlockedLevel;

    public LevelSelectionPage(Stage primaryStage, LoginManager loginManager, String username) {
        this.primaryStage = primaryStage;
        this.loginManager = loginManager;
        this.username = username;
        this.unlockedLevel = loginManager.getUserProgress(username);
    }

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

    private void launchLevel(String levelName) {
        LevelParent selectedLevel = LevelBuilder.createLevel(levelName, getPrimaryStage().getHeight(), getPrimaryStage().getWidth(), primaryStage);
        selectedLevel.setLoggedInUser(username);
        primaryStage.setScene(selectedLevel.initializeScene());
        selectedLevel.startGame();
    }

    private String convertNumberToName(int levelNumber) {
        return switch (levelNumber) {
            case 1 -> "One";
            case 2 -> "Two";
            case 3 -> "Three";
            case 4 -> "Four";
            default -> throw new IllegalArgumentException("Invalid level number: " + levelNumber);
        };
    }

    private Stage getPrimaryStage() {
        return primaryStage;
    }
}
