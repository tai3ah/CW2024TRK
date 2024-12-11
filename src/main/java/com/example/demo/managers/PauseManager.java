package com.example.demo.managers;

import com.example.demo.factories.PauseButtonFactory;
import com.example.demo.factories.ResumeButtonFactory;
import com.example.demo.factories.QuitLevelButtonFactory;
import com.example.demo.ui.MainMenuPage;
import com.example.demo.levels.LevelParent;
import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.image.ImageView;
import javafx.scene.layout.Pane;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;
import javafx.util.Duration;

public class PauseManager {
    private final Pane root;
    private final Stage primaryStage;
    private final Timeline timeline;
    private final double screenWidth;
    private final double screenHeight;
    private final ImageView background;
    private final LevelParent levelParent;

    private final PauseButtonFactory pauseButtonFactory = new PauseButtonFactory();
    private final ResumeButtonFactory resumeButtonFactory = new ResumeButtonFactory();
    private final QuitLevelButtonFactory quitLevelButtonFactory = new QuitLevelButtonFactory();

    public PauseManager(Pane root, Stage primaryStage, Timeline timeline, double screenWidth, double screenHeight, ImageView background, LevelParent levelParent) {
        this.root = root;
        this.primaryStage = primaryStage;
        this.timeline = timeline;
        this.screenWidth = screenWidth;
        this.screenHeight = screenHeight;
        this.background = background;
        this.levelParent = levelParent;
    }

    public void initializePauseButton() {
        Button pauseButton = pauseButtonFactory.createButton();
        pauseButton.setLayoutX(screenWidth - 100);
        pauseButton.setLayoutY(20);
        pauseButton.setOnAction(event -> showPauseWindow(pauseButton));
        root.getChildren().add(pauseButton);
    }

    private void showPauseWindow(Button pauseButton) {
        timeline.pause();
        levelParent.setGamePaused(true);

        StackPane pauseOverlay = new StackPane();
        pauseOverlay.setPrefSize(screenWidth, screenHeight);
        pauseOverlay.setStyle("-fx-background-color: rgba(0, 0, 0, 0.5);");

        Label pauseLabel = new Label("GAME PAUSED");
        pauseLabel.setStyle("-fx-font-family: 'Arial'; -fx-font-size: 30px; -fx-text-fill: #FFD700;");

        Button resumeButton = resumeButtonFactory.createButton();
        resumeButton.setOnAction(event -> {
            root.getChildren().remove(pauseOverlay);
            pauseButton.setDisable(true);
            resumeGameWithCountdown(pauseButton);
        });

        Button quitLevelButton = quitLevelButtonFactory.createButton();
        quitLevelButton.setOnAction(event -> goToMainMenu());

        VBox buttonsLayout = new VBox(20, pauseLabel, resumeButton, quitLevelButton);
        buttonsLayout.setAlignment(Pos.CENTER);

        pauseOverlay.getChildren().add(buttonsLayout);
        root.getChildren().add(pauseOverlay);
    }

    private void resumeGameWithCountdown(Button pauseButton) {
        Label countdownLabel = new Label("3");
        countdownLabel.setStyle("-fx-font-size: 50px; -fx-text-fill: white;");
        countdownLabel.setLayoutX(screenWidth / 2 - 25);
        countdownLabel.setLayoutY(screenHeight / 2 - 25);
        root.getChildren().add(countdownLabel);

        Timeline countdownTimeline = new Timeline(
                new KeyFrame(Duration.seconds(1), e -> countdownLabel.setText("2")),
                new KeyFrame(Duration.seconds(2), e -> countdownLabel.setText("1")),
                new KeyFrame(Duration.seconds(3), e -> {
                    root.getChildren().remove(countdownLabel);
                    pauseButton.setDisable(false);
                    timeline.play();
                    levelParent.setGamePaused(false);
                    background.requestFocus();
                })
        );
        countdownTimeline.setCycleCount(1);
        countdownTimeline.play();
    }

    private void goToMainMenu() {
        timeline.stop();
        levelParent.setGamePaused(true);
        MainMenuPage mainMenuPage = new MainMenuPage();

        StackPane root = new StackPane();
        Scene mainMenuScene = new Scene(root, 1300, 750);
        primaryStage.setScene(mainMenuScene);

        mainMenuPage.start(primaryStage);
        primaryStage.sizeToScene();
        primaryStage.show();
    }
}
