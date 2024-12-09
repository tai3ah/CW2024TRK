package com.example.demo.levels;

import com.example.demo.actors.*;
import com.example.demo.factories.DroneFactory;
import com.example.demo.factories.EnemyPlaneFactory;
import com.example.demo.ui.LevelViewLevelFour;
import com.example.demo.ui.LevelView;
import javafx.animation.AnimationTimer;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.stage.Stage;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

public class LevelFour extends LevelParent {

    private static final String BACKGROUND_IMAGE_NAME = "/com/example/demo/images/background3.png";
    private static final int PLAYER_INITIAL_HEALTH = 5;
    private static final DroneFactory droneFactory = new DroneFactory();
    private static final EnemyPlaneFactory enemyFactory = new EnemyPlaneFactory();
    private static final int KILLS_TO_ADVANCE = 10;
    private static final double ENEMY_SPAWN_PROBABILITY = .20;
    private static final int TOTAL_ENEMIES = 3;
    private static final double HEART_SPAWN_PROBABILITY = 0.01; // Probability of spawning a heart each frame
    private static final int HEART_LIFETIME = 7000; // Heart lifetime in milliseconds

    private Drone drone;
    private final List<ImageView> hearts = new ArrayList<>();
    private final List<Long> heartSpawnTimes = new ArrayList<>();
    private LevelViewLevelFour levelView;
    private final double screenHeight;

    public LevelFour(double screenHeight, double screenWidth, Stage primaryStage) {
        super(BACKGROUND_IMAGE_NAME, screenHeight, screenWidth, PLAYER_INITIAL_HEALTH, primaryStage);
        this.screenHeight = screenHeight;
        initializeDrone();
        startHeartSpawnTimer();
        startGameLoop();
    }

    private void initializeDrone() {
        if (drone == null) {
            drone = (Drone) droneFactory.createEnemy(0, 0); // Create drone using factory
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
        } else if (drone != null && drone.isDestroyed() && userHasReachedKillTarget()) {
            winGame();
        }
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
        levelView.updateKillCount(getUser().getNumberOfKills()); // Reflect user kills
        if (drone != null) {
            levelView.updateDroneHealth(drone.getHealth()); // Reflect drone health
        }
    }

    @Override
    protected LevelView instantiateLevelView() {
        initializeDrone();
        levelView = new LevelViewLevelFour(getRoot(), PLAYER_INITIAL_HEALTH, drone);
        return levelView; // Return as LevelView
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
                updateUI(); // Call updateUI in the game loop
            }
        };
        gameLoop.start();
    }



    private void spawnHearts() {
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

  /*  private void checkHeartCollisions() {
        Iterator<ImageView> heartIterator = hearts.iterator();
        Iterator<Long> timeIterator = heartSpawnTimes.iterator();

        while (heartIterator.hasNext() && timeIterator.hasNext()) {
            ImageView heart = heartIterator.next();
            Long spawnTime = timeIterator.next();

            if (heart.getBoundsInParent().intersects(getUser().getBoundsInParent())) {
                // Increase user health by 1
                getUser().increaseHealth(1);

                // Update heart display
                levelView.updateKillCount(getUser().getHealth());

                // Remove the collected heart from the game screen
                getRoot().getChildren().remove(heart);
                heartIterator.remove();
                timeIterator.remove();
            }
        }
    }
*/

    private void checkHeartCollisions() {
        Iterator<ImageView> heartIterator = hearts.iterator();
        Iterator<Long> timeIterator = heartSpawnTimes.iterator();

        while (heartIterator.hasNext() && timeIterator.hasNext()) {
            ImageView heart = heartIterator.next();
            Long spawnTime = timeIterator.next();

            if (heart.getBoundsInParent().intersects(getUser().getBoundsInParent())) {
                // Increase user health by 1
                getUser().increaseHealth(1);



                // Update heart display
              //  levelView.updateHearts(getUser().getHealth());
                levelView.addHearts(1);


                // Remove the collected heart from the game screen
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



