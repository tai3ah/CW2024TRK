package com.example.demo.levels;

import com.example.demo.actors.*;
import com.example.demo.factories.DroneFactory;
import com.example.demo.factories.EnemyPlaneFactory;
import com.example.demo.factories.GameCompletionScreenFactory;
import com.example.demo.ui.LevelViewLevelFour;
import com.example.demo.ui.LevelViewLevelOne;
import com.example.demo.ui.MainMenuPage;
import javafx.animation.*;
import javafx.scene.Scene;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.StackPane;
import javafx.stage.Stage;
import javafx.util.Duration;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

public class LevelFour extends LevelParent {

    private static final String BACKGROUND_IMAGE_NAME = "/com/example/demo/images/background3.png";
    private static final int PLAYER_INITIAL_HEALTH = 5;
    private static final DroneFactory droneFactory = new DroneFactory();
    private static final EnemyPlaneFactory enemyFactory = new EnemyPlaneFactory();
    private static final int KILLS_TO_ADVANCE = 20;
    private static final double ENEMY_SPAWN_PROBABILITY = .20;
    private static final int TOTAL_ENEMIES = 3;
    private static final double HEART_SPAWN_PROBABILITY = 0.002;
    private static final int HEART_LIFETIME = 7000;
    private static final int DRONE_RESPAWN_DELAY = 10;

    private Drone drone;
    private final List<ImageView> hearts = new ArrayList<>();
    private final List<Long> heartSpawnTimes = new ArrayList<>();
    private LevelViewLevelFour levelView;
    private final double screenHeight;
    private Timeline droneRespawnTimer;

    public LevelFour(double screenHeight, double screenWidth, Stage primaryStage) {
        super(BACKGROUND_IMAGE_NAME, screenHeight, screenWidth, PLAYER_INITIAL_HEALTH, primaryStage);
        this.screenHeight = screenHeight;
        initializeDrone();
        startHeartSpawnTimer();
        startGameLoop();
    }

    private void initializeDrone() {
        if (drone == null) {
            drone = (Drone) droneFactory.createEnemy(0, 0);
        }
    }

    @Override
    protected void initializeFriendlyUnits() {
        getRoot().getChildren().add(getUser());
        initializeDrone();
        levelView = new LevelViewLevelFour(getRoot(), PLAYER_INITIAL_HEALTH, drone);
    }

    @Override
    protected void checkIfGameOver() {
        if (userIsDestroyed()) {
            loseGame();
        } else if (drone != null && drone.isDestroyed()) {
            if (userHasReachedKillTarget()) {
                winGame();
            } else {
                startDroneRespawnTimer(); // Start timer if drone is destroyed but kill target not reached
            }
        }
    }

    private void startDroneRespawnTimer() {
        if (droneRespawnTimer != null && droneRespawnTimer.getStatus() == Animation.Status.RUNNING) {
            return;
        }
        droneRespawnTimer = new Timeline(
                new KeyFrame(Duration.seconds(DRONE_RESPAWN_DELAY), event -> {
                    if (!userHasReachedKillTarget()) {
                        respawnDrone();
                    }
                })
        );
        droneRespawnTimer.setCycleCount(1);
        droneRespawnTimer.play();
    }

    private void respawnDrone() {
        drone = (Drone) droneFactory.createEnemy(0, 0);
        addEnemyUnit(drone);
        levelView.updateDroneHealth(drone.getHealth());
    }

    @Override
    protected void goToNextLevel() {
        GameCompletionScreenFactory completionScreen = new GameCompletionScreenFactory(getRoot(), getScreenWidth(), getScreenHeight());
        completionScreen.showGameCompletionScreen(() -> {
            MainMenuPage mainMenuPage = new MainMenuPage();

            StackPane root = new StackPane();
            Scene mainMenuScene = new Scene(root, 1300, 750);
            getPrimaryStage().setScene(mainMenuScene);

            mainMenuPage.start(getPrimaryStage());
            getPrimaryStage().sizeToScene();
            getPrimaryStage().show();
        });
    }

    @Override
    protected void spawnEnemyUnits() {
        if (drone != null && getCurrentNumberOfEnemies() == 0) {
            addEnemyUnit(drone);
        }
        int currentNumberOfEnemies = getCurrentNumberOfEnemies();
        for (int i = 0; i < TOTAL_ENEMIES - currentNumberOfEnemies; i++) {
            if (Math.random() < ENEMY_SPAWN_PROBABILITY) {
                double newEnemyInitialYPosition = Math.random() * getEnemyMaximumYPosition();
                GameEntity newEnemy = enemyFactory.createEnemy(getScreenWidth(), newEnemyInitialYPosition);
                addEnemyUnit(newEnemy);
            }
        }
    }

    protected void updateUI() {
        levelView.updateKillCount(getUser().getNumberOfKills());
        if (drone != null) {
            levelView.updateDroneHealth(drone.getHealth());
        }
    }

    @Override
    protected LevelViewLevelOne instantiateLevelView() {
        initializeDrone();
        levelView = new LevelViewLevelFour(getRoot(), PLAYER_INITIAL_HEALTH, drone);
        return levelView;
    }

    private void startHeartSpawnTimer() {
        AnimationTimer heartTimer = new AnimationTimer() {
            @Override
            public void handle(long now) {
                spawnHearts();
                checkHeartCollisions();
                removeExpiredHearts();
            }
        };
        heartTimer.start();
    }

    private void startGameLoop() {
        AnimationTimer gameLoop = new AnimationTimer() {
            @Override
            public void handle(long now) {
                checkIfGameOver();
                spawnEnemyUnits();
                updateUI();
            }
        };
        gameLoop.start();
    }

    private void spawnHearts() {
        if (getTimeline() != null && getTimeline().getStatus() != Animation.Status.RUNNING) {
            return;
        }

        if (Math.random() < HEART_SPAWN_PROBABILITY) {
            double xPosition = Math.random() * getScreenWidth();
            double yPosition = Math.random() * this.screenHeight;
            Image heartImage = new Image(getClass().getResource("/com/example/demo/images/heart.png").toExternalForm());
            ImageView heart = new ImageView(heartImage);
            heart.setFitHeight(40);
            heart.setPreserveRatio(true);
            heart.setX(xPosition);
            heart.setY(yPosition);

            hearts.add(heart);
            heartSpawnTimes.add(System.currentTimeMillis());
            getRoot().getChildren().add(heart);
        }
    }

    private void checkHeartCollisions() {
        Iterator<ImageView> heartIterator = hearts.iterator();
        Iterator<Long> timeIterator = heartSpawnTimes.iterator();

        while (heartIterator.hasNext() && timeIterator.hasNext()) {
            ImageView heart = heartIterator.next();
            Long spawnTime = timeIterator.next();

            if (heart.getBoundsInParent().intersects(getUser().getBoundsInParent())) {
                getUser().increaseHealth(1);
                levelView.addHearts(1);
                getRoot().getChildren().remove(heart);
                heartIterator.remove();
                timeIterator.remove();
            }
        }
    }

    private void removeExpiredHearts() {
        long currentTime = System.currentTimeMillis();
        Iterator<ImageView> heartIterator = hearts.iterator();
        Iterator<Long> timeIterator = heartSpawnTimes.iterator();

        while (heartIterator.hasNext() && timeIterator.hasNext()) {
            ImageView heart = heartIterator.next();
            Long spawnTime = timeIterator.next();

            if (currentTime - spawnTime > HEART_LIFETIME) {
                getRoot().getChildren().remove(heart);
                heartIterator.remove();
                timeIterator.remove();
            }
        }
    }

    private boolean userHasReachedKillTarget() {
        return getUser().getNumberOfKills() >= KILLS_TO_ADVANCE;
    }
}
