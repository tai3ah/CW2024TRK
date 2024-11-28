package com.example.demo.managers;

import com.example.demo.ui.MainMenuPage;
import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
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

    private static final String PAUSE_BUTTON = "/com/example/demo/images/pause.png";
    private static final String RESUME_BUTTON = "/com/example/demo/images/resume.png";
    private static final String QUIT_LEVEL_BUTTON = "/com/example/demo/images/quitLevel.png";


    public PauseManager(Pane root, Stage primaryStage, Timeline timeline, double screenWidth, double screenHeight, ImageView background) {
        this.root = root;
        this.primaryStage = primaryStage;
        this.timeline = timeline;
        this.screenWidth = screenWidth;
        this.screenHeight = screenHeight;
        this.background = background;
    }

    public void initializePauseButton() {
        Image pauseImage = new Image(getClass().getResourceAsStream(PAUSE_BUTTON));
        ImageView pauseImageView = new ImageView(pauseImage);
        pauseImageView.setFitWidth(80);
        pauseImageView.setFitHeight(60);

        Button pauseButton = new Button();
        pauseButton.setGraphic(pauseImageView);
        pauseButton.setLayoutX(screenWidth - 100);
        pauseButton.setLayoutY(20);
        pauseButton.setStyle("-fx-background-color: transparent;");
        pauseButton.setOnAction(event -> showPauseWindow(pauseButton));
        root.getChildren().add(pauseButton);
    }

    private void showPauseWindow(Button pauseButton) {
        timeline.pause();

        StackPane pauseOverlay = new StackPane();
        pauseOverlay.setPrefSize(screenWidth, screenHeight);
        pauseOverlay.setStyle("-fx-background-color: rgba(0, 0, 0, 0.5);");

        Label pauseLabel = new Label("GAME PAUSED");
        pauseLabel.setStyle("-fx-font-family: 'Arial'; -fx-font-size: 30px; -fx-text-fill: #FFD700;");

        Image resumeImage = new Image(getClass().getResourceAsStream(RESUME_BUTTON));
        ImageView resumeImageView = new ImageView(resumeImage);
        resumeImageView.setFitWidth(100);
        resumeImageView.setFitHeight(60);

        Button resumeButton = new Button();
        resumeButton.setGraphic(resumeImageView);
        resumeButton.setStyle("-fx-background-color: transparent;");
        resumeButton.setOnAction(event -> {
            root.getChildren().remove(pauseOverlay);
            pauseButton.setDisable(true);
            resumeGameWithCountdown(pauseButton);
        });

        Image quitLevelImage = new Image(getClass().getResourceAsStream(QUIT_LEVEL_BUTTON));
        ImageView quitLevelImageView = new ImageView(quitLevelImage);
        quitLevelImageView.setFitWidth(100);
        quitLevelImageView.setFitHeight(60);

        Button quitLevelButton = new Button();
        quitLevelButton.setGraphic(quitLevelImageView);
        quitLevelButton.setStyle("-fx-background-color: transparent;");
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

                    // Regain focus for input handling
                    background.requestFocus(); // Ensure the background has focus after resuming
                })
        );
        countdownTimeline.setCycleCount(1);
        countdownTimeline.play();
    }
    /*private void resumeGameWithCountdown(Button pauseButton) {
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
                })
        );
        countdownTimeline.setCycleCount(1);
        countdownTimeline.play();
    }*/

    private void goToMainMenu() {
        timeline.stop();

        MainMenuPage mainMenuPage = new MainMenuPage();

        StackPane root = new StackPane();
        Scene mainMenuScene = new Scene(root, 1300, 750);
        primaryStage.setScene(mainMenuScene);

        mainMenuPage.start(primaryStage);
        primaryStage.sizeToScene();
        primaryStage.show();
    }
}