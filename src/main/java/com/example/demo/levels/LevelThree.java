package com.example.demo.levels;

import com.example.demo.actors.FinalBoss;
import com.example.demo.ui.LevelViewLevelOne;
import com.example.demo.ui.LevelViewLevelThree;
import com.example.demo.factories.FinalBossFactory;
import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.StackPane;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.scene.text.Text;
import javafx.stage.Stage;
import javafx.util.Duration;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

public class LevelThree extends LevelParent {

    private static final String BACKGROUND_IMAGE_NAME = "/com/example/demo/images/background4.jpg";
    private static final String TIME_POWERUP_IMAGE = "/com/example/demo/images/timePowerUp.png";
    private static final int PLAYER_INITIAL_HEALTH = 5;
    private static final String NEXT_LEVEL = "LevelFour";
    private static final int LEVEL_TIME_LIMIT = 25;
    private static final int TIME_POWERUP_BONUS = 5;
    private static final int TIME_POWERUP_LIFETIME = 7000;
    private static final double TIME_POWERUP_SPAWN_PROBABILITY = 0.01;

    private FinalBoss finalBoss;
    private static final FinalBossFactory finalBossFactory = new FinalBossFactory();
    private LevelViewLevelThree levelView;

    private int remainingTime;
    private final Timeline timerTimeline;
    private final List<ImageView> timePowerUps = new ArrayList<>();
    private final List<Long> timePowerUpSpawnTimes = new ArrayList<>();

    public LevelThree(double screenHeight, double screenWidth, Stage primaryStage) {
        super(BACKGROUND_IMAGE_NAME, screenHeight, screenWidth, PLAYER_INITIAL_HEALTH, primaryStage);
        finalBoss = finalBossFactory.createEnemy(1000, 400);
        remainingTime = LEVEL_TIME_LIMIT;

        timerTimeline = new Timeline(new KeyFrame(Duration.seconds(1), event -> {
            if (!isGamePaused()) {
                remainingTime--;
                updateTimerDisplay();
                removeExpiredTimePowerUps();
                checkTimePowerUpCollisions();
                if (remainingTime <= 0) {
                    stopTimer();
                    loseGame();
                }
            }
        }));
        timerTimeline.setCycleCount(Timeline.INDEFINITE);
    }

    private void showIntroScreen() {
        StackPane introOverlay = new StackPane();
        introOverlay.setPrefSize(getScreenWidth(), getScreenHeight());
        introOverlay.setStyle("-fx-background-color: black;");

        Text introText = new Text(
                "KILL THE FINAL BOSS BEFORE YOUR TIME IS UP TO BEAT LEVEL THREE!\n\n" +
                        "WARNING: THE FINAL BOSS IS FASTER AND MOVES IN ALL DIRECTIONS!\n\n" +
                        "HINT: COLLECT THE TIME POWER-UPS THAT APPEAR ON THE SCREEN TO INCREASE TIME BY 5 SECONDS!"
        );
        introText.setFill(Color.WHITE);
        introText.setFont(Font.font("Arial", 25));
        introText.setTextAlignment(javafx.scene.text.TextAlignment.CENTER);
        introOverlay.getChildren().add(introText);


        getRoot().getChildren().add(introOverlay);

        Timeline timeline = new Timeline(
                new KeyFrame(Duration.seconds(7), e -> {
                    getRoot().getChildren().remove(introOverlay);
                    super.startGame();
                    timerTimeline.play();
                })
        );
        timeline.setCycleCount(1);
        timeline.play();
    }


    private void updateTimerDisplay() {
        if (getLevelView() instanceof LevelViewLevelThree) {
            ((LevelViewLevelThree) getLevelView()).updateTimerDisplay(remainingTime);
        }
    }

    private void stopTimer() {
        timerTimeline.stop();
    }

    @Override
    protected void initializeFriendlyUnits() {
        getRoot().getChildren().add(getUser());
    }

    @Override
    protected void checkIfGameOver() {
        if (userIsDestroyed()) {
            stopTimer();
            loseGame();
        } else if (finalBoss.isDestroyed()) {
            stopTimer();
            winGame();
        }
    }

    @Override
    protected void goToNextLevel() {
        LevelParent nextLevel = LevelBuilder.createLevel(NEXT_LEVEL, getScreenHeight(), getScreenWidth(), getPrimaryStage());
        getPrimaryStage().setScene(nextLevel.initializeScene());
        nextLevel.startGame();
    }

    @Override
    protected void spawnEnemyUnits() {
        if (getCurrentNumberOfEnemies() == 0) {
            addEnemyUnit(finalBoss);
        }
        spawnTimePowerUp();
    }

    @Override
    protected LevelViewLevelOne instantiateLevelView() {
        if (finalBoss == null) {
            finalBoss = finalBossFactory.createEnemy(1000, 400);
        }
        levelView = new LevelViewLevelThree(getRoot(), PLAYER_INITIAL_HEALTH, finalBoss);
        finalBoss.setLevelView(levelView);
        return levelView;
    }

    @Override
    protected void updateScene() {
        super.updateScene();

        if (finalBoss != null) {
            javafx.application.Platform.runLater(() -> {
                if (getLevelView() instanceof LevelViewLevelThree) {
                    ((LevelViewLevelThree) getLevelView()).updateFinalBossHealth(finalBoss.getHealth());
                }
            });
        }
    }

    @Override
    public void startGame() {
       // super.startGame();
        //timerTimeline.play();
        showIntroScreen();
    }

    private void spawnTimePowerUp() {
        if (timePowerUps.size() >= 2 || Math.random() > TIME_POWERUP_SPAWN_PROBABILITY) {
            return;
        }
        double xPosition = Math.random() * getScreenWidth();
        double yPosition = Math.random() * getScreenHeight();

        ImageView timePowerUp = new ImageView(new Image(getClass().getResource(TIME_POWERUP_IMAGE).toExternalForm()));
        timePowerUp.setFitHeight(40);
        timePowerUp.setPreserveRatio(true);
        timePowerUp.setX(xPosition);
        timePowerUp.setY(yPosition);

        timePowerUps.add(timePowerUp);
        timePowerUpSpawnTimes.add(System.currentTimeMillis());
        getRoot().getChildren().add(timePowerUp);
    }

    private void checkTimePowerUpCollisions() {
        Iterator<ImageView> powerUpIterator = timePowerUps.iterator();
        Iterator<Long> timeIterator = timePowerUpSpawnTimes.iterator();

        while (powerUpIterator.hasNext() && timeIterator.hasNext()) {
            ImageView powerUp = powerUpIterator.next();
            Long spawnTime = timeIterator.next();

            if (powerUp.getBoundsInParent().intersects(getUser().getBoundsInParent())) {
                remainingTime += TIME_POWERUP_BONUS;
                updateTimerDisplay();

                getRoot().getChildren().remove(powerUp);
                powerUpIterator.remove();
                timeIterator.remove();
            }
        }
    }

    private void removeExpiredTimePowerUps() {
        long currentTime = System.currentTimeMillis();
        Iterator<ImageView> powerUpIterator = timePowerUps.iterator();
        Iterator<Long> timeIterator = timePowerUpSpawnTimes.iterator();

        while (powerUpIterator.hasNext() && timeIterator.hasNext()) {
            ImageView powerUp = powerUpIterator.next();
            Long spawnTime = timeIterator.next();

            if (currentTime - spawnTime > TIME_POWERUP_LIFETIME) {
                getRoot().getChildren().remove(powerUp);
                powerUpIterator.remove();
                timeIterator.remove();
            }
        }
    }
}
