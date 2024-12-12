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

/**
 * The LevelThree class represents the third level of the game.
 * It handles the initialization, gameplay, and transition to the next level.
 */
public class LevelThree extends LevelParent {

    /**
     * The background image for the level.
     */
    private static final String BACKGROUND_IMAGE_NAME = "/com/example/demo/images/background4.jpg";

    /**
     * The image for the time power-up.
     */
    private static final String TIME_POWERUP_IMAGE = "/com/example/demo/images/timePowerUp.png";

    /**
     * The initial health of the player.
     */
    private static final int PLAYER_INITIAL_HEALTH = 5;

    /**
     * The name of the next level to transition to.
     */
    private static final String NEXT_LEVEL = "LevelFour";

    /**
     * The time limit for the level in seconds.
     */
    private static final int LEVEL_TIME_LIMIT = 25;

    /**
     * The bonus time added when a time power-up is collected.
     */
    private static final int TIME_POWERUP_BONUS = 5;

    /**
     * The lifetime of a time power-up in milliseconds.
     */
    private static final int TIME_POWERUP_LIFETIME = 7000;

    /**
     * The probability of spawning a time power-up.
     */
    private static final double TIME_POWERUP_SPAWN_PROBABILITY = 0.01;

    /**
     * The final boss for this level.
     */
    private FinalBoss finalBoss;

    /**
     * The factory for creating final bosses.
     */
    private static final FinalBossFactory finalBossFactory = new FinalBossFactory();

    /**
     * The view for this level.
     */
    private LevelViewLevelThree levelView;

    /**
     * The remaining time for the level.
     */
    private int remainingTime;

    /**
     * The timeline for the level timer.
     */
    private final Timeline timerTimeline;

    /**
     * The list of active time power-ups.
     */
    private final List<ImageView> timePowerUps = new ArrayList<>();

    /**
     * The list of spawn times for the active time power-ups.
     */
    private final List<Long> timePowerUpSpawnTimes = new ArrayList<>();

    /**
     * Constructs a LevelThree instance with the specified screen dimensions and primary stage.
     *
     * @param screenHeight the height of the screen
     * @param screenWidth  the width of the screen
     * @param primaryStage the primary stage for the level
     */
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

    /**
     * Shows the intro screen with instructions for the level.
     */
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

    /**
     * Updates the timer display in the level view.
     */
    private void updateTimerDisplay() {
        if (getLevelView() instanceof LevelViewLevelThree) {
            ((LevelViewLevelThree) getLevelView()).updateTimerDisplay(remainingTime);
        }
    }

    /**
     * Stops the level timer.
     */
    private void stopTimer() {
        timerTimeline.stop();
    }

    /**
     * Initializes friendly units for the level.
     */
    @Override
    protected void initializeFriendlyUnits() {
        getRoot().getChildren().add(getUser());
    }

    /**
     * Checks if the game is over by evaluating the player's and final boss's status.
     */
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

    /**
     * Transitions to the next level.
     */
    @Override
    protected void goToNextLevel() {
        LevelParent nextLevel = LevelBuilder.createLevel(NEXT_LEVEL, getScreenHeight(), getScreenWidth(), getPrimaryStage());
        getPrimaryStage().setScene(nextLevel.initializeScene());
        nextLevel.startGame();
    }

    /**
     * Spawns enemy units, specifically the final boss, if no enemies are present.
     * Also spawns time power-ups.
     */
    @Override
    protected void spawnEnemyUnits() {
        if (getCurrentNumberOfEnemies() == 0) {
            addEnemyUnit(finalBoss);
        }
        spawnTimePowerUp();
    }

    /**
     * Instantiates the level view for Level Three.
     *
     * @return the level view for Level Three
     */
    @Override
    protected LevelViewLevelOne instantiateLevelView() {
        if (finalBoss == null) {
            finalBoss = finalBossFactory.createEnemy(1000, 400);
        }
        levelView = new LevelViewLevelThree(getRoot(), PLAYER_INITIAL_HEALTH, finalBoss);
        finalBoss.setLevelView(levelView);
        return levelView;
    }

    /**
     * Updates the scene, including the final boss's health display.
     */
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

    /**
     * Starts the game by showing the intro screen and starting the timer.
     */
    @Override
    public void startGame() {
        showIntroScreen();
    }

    /**
     * Spawns a time power-up at a random position on the screen.
     */
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

    /**
     * Checks for collisions between the player and time power-ups.
     * If a collision is detected, the time power-up is collected and the remaining time is increased.
     */
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

    /**
     * Removes expired time power-ups from the screen.
     */
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