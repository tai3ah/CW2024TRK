package com.example.demo.levels;

import com.example.demo.actors.FinalBoss;
import com.example.demo.ui.LevelViewLevelOne;
import com.example.demo.ui.LevelViewLevelThree;
import com.example.demo.factories.FinalBossFactory;
import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.stage.Stage;
import javafx.util.Duration;
import javafx.scene.text.Text;

public class LevelThree extends LevelParent {

    private static final String BACKGROUND_IMAGE_NAME = "/com/example/demo/images/background4.jpg";
    private static final int PLAYER_INITIAL_HEALTH = 5;
    private static final String NEXT_LEVEL = "LevelFour";
    private static final int LEVEL_TIME_LIMIT = 25; // Time limit in seconds

    private FinalBoss finalBoss;
    private static final FinalBossFactory finalBossFactory = new FinalBossFactory();
    private LevelViewLevelThree levelView;

    private Timeline levelTimer;
    private int remainingTime;
    private final Text timerDisplay = new Text();

    public LevelThree(double screenHeight, double screenWidth, Stage primaryStage) {
        super(BACKGROUND_IMAGE_NAME, screenHeight, screenWidth, PLAYER_INITIAL_HEALTH, primaryStage);
        finalBoss = finalBossFactory.createEnemy(1000, 400); // Create FinalBoss
        remainingTime = LEVEL_TIME_LIMIT;

    }



    private void startLevelTimer() {
        levelTimer = new Timeline(
                new KeyFrame(Duration.seconds(1), event -> {
                    remainingTime--;
                    updateTimerDisplay();

                    if (remainingTime <= 0) {
                        loseGame(); // Timer runs out, player loses
                        levelTimer.stop();
                    }
                })
        );
        levelTimer.setCycleCount(Timeline.INDEFINITE);
        levelTimer.play();
    }

    private void updateTimerDisplay() {
        if (getLevelView() instanceof LevelViewLevelThree) {
            ((LevelViewLevelThree) getLevelView()).updateTimerDisplay(remainingTime);
        }
    }


    @Override
    protected void initializeFriendlyUnits() {
        getRoot().getChildren().add(getUser());
    }

    @Override
    protected void checkIfGameOver() {
        if (userIsDestroyed()) {
            loseGame(); // Player dies
            levelTimer.stop();
        } else if (finalBoss.isDestroyed()) {
            winGame(); // Player wins
            levelTimer.stop();
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
    }

    @Override
    protected LevelViewLevelOne instantiateLevelView() {
        if (finalBoss == null) {
            finalBoss = finalBossFactory.createEnemy(1000, 400); // Ensure FinalBoss is created
        }
        levelView = new LevelViewLevelThree(getRoot(), PLAYER_INITIAL_HEALTH, finalBoss);
        finalBoss.setLevelView(levelView); // Sync level view with FinalBoss for health updates
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
        super.startGame();
        startLevelTimer(); // Start the timer when the level begins
    }
}
