package com.example.demo.levels;

import com.example.demo.actors.*;
import com.example.demo.factories.DroneFactory;
import com.example.demo.factories.EnemyPlaneFactory;
import com.example.demo.factories.GameCompletionScreenFactory;
import com.example.demo.managers.SoundManager;
import com.example.demo.ui.LevelViewLevelFour;
import com.example.demo.ui.LevelViewLevelOne;
import com.example.demo.ui.MainMenuPage;
import javafx.animation.*;
import javafx.scene.Scene;
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
 * The LevelFour class represents the fourth level of the game.
 * It handles the initialization, gameplay, and transition to the next level.
 */
public class LevelFour extends LevelParent {
    /**
     * Indicates if the game has been completed to prevent duplicate actions.
     */
    private boolean gameCompleted = false;

    /**
     * The background image for the level.
     */
    private static final String BACKGROUND_IMAGE_NAME = "/com/example/demo/images/background3.png";

    /**
     * The initial health of the player.
     */
    private static final int PLAYER_INITIAL_HEALTH = 5;

    /**
     * The factory for creating drones.
     */
    private static final DroneFactory droneFactory = new DroneFactory();

    /**
     * The factory for creating enemy planes.
     */
    private static final EnemyPlaneFactory enemyFactory = new EnemyPlaneFactory();

    /**
     * The number of kills required to advance to the next level.
     */
    private static final int KILLS_TO_ADVANCE = 20;

    /**
     * The probability of spawning an enemy.
     */
    private static final double ENEMY_SPAWN_PROBABILITY = 0.20;

    /**
     * The total number of enemies to spawn.
     */
    private static final int TOTAL_ENEMIES = 3;

    /**
     * The probability of spawning a heart power-up.
     */
    private static final double HEART_SPAWN_PROBABILITY = 0.001;

    /**
     * The lifetime of a heart power-up in milliseconds.
     */
    private static final int HEART_LIFETIME = 7000;

    /**
     * The delay in seconds before a new drone respawns.
     */
    private static final int DRONE_RESPAWN_DELAY = 10;

    /**
     * The drone enemy for this level.
     */
    private Drone drone;

    /**
     * The list of active heart power-ups.
     */
    private final List<ImageView> hearts = new ArrayList<>();

    /**
     * The list of spawn times for the active heart power-ups.
     */
    private final List<Long> heartSpawnTimes = new ArrayList<>();

    /**
     * The view for this level.
     */
    private LevelViewLevelFour levelView;

    /**
     * The height of the screen.
     */
    private final double screenHeight;

    /**
     * The timeline for respawning the drone.
     */
    private Timeline droneRespawnTimer;


    /**
     * The upper boundary for heart spawning along the Y-axis.
     * This ensures hearts spawn within the flyable area of the user plane.
     */
    private static final double Y_UPPER_BOUND = 0;

    /**
     * The lower boundary for heart spawning along the Y-axis.
     * Hearts will not appear below this boundary, keeping them within the playable area.
     */
    private static final double Y_LOWER_BOUND = 650.0;

    /**
     * The left boundary for heart spawning along the X-axis.
     * This ensures hearts spawn within the screen's visible area.
     */
    private static final double X_LEFT_BOUND = 0;

    /**
     * The right boundary for heart spawning along the X-axis.
     * Hearts will not appear beyond this point, keeping them within the playable area.
     */
    private static final double X_RIGHT_BOUND = 1250.0;


    /**
     * Constructs a LevelFour instance with the specified screen dimensions and primary stage.
     *
     * @param screenHeight the height of the screen
     * @param screenWidth  the width of the screen
     * @param primaryStage the primary stage for the level
     */
    public LevelFour(double screenHeight, double screenWidth, Stage primaryStage) {
        super(BACKGROUND_IMAGE_NAME, screenHeight, screenWidth, PLAYER_INITIAL_HEALTH, primaryStage);
        this.screenHeight = screenHeight;
        initializeDrone();
        startHeartSpawnTimer();
        startGameLoop();
    }

    /**
     * Starts the game by showing the intro screen.
     */
    @Override
    public void startGame() {
        showIntroScreen();
    }

    /**
     * Shows the intro screen with instructions for the level.
     */
    private void showIntroScreen() {
        StackPane introOverlay = new StackPane();
        introOverlay.setPrefSize(getScreenWidth(), getScreenHeight());
        introOverlay.setStyle("-fx-background-color: black;");

        Text introText = new Text(
                "KILL 20 ENEMY PLANES AND THE DRONE TO WIN LEVEL FOUR!\n\n" +
                        "WARNING: A NEW DRONE WILL APPEAR 10 SECONDS AFTER YOU KILL THE FIRST DRONE, \nIF YOU HAVEN'T REACHED THE KILL TARGET!\n\n" +
                        "HINT: COLLECT HEARTS THAT APPEAR ON THE SCREEN TO GAIN EXTRA LIVES!");
        introText.setFill(Color.WHITE);
        introText.setFont(Font.font("Arial", 40));
        introOverlay.getChildren().add(introText);

        getRoot().getChildren().add(introOverlay);

        Timeline timeline = new Timeline(
                new KeyFrame(Duration.seconds(5), e -> {
                    getRoot().getChildren().remove(introOverlay);
                    super.startGame();
                })
        );
        timeline.setCycleCount(1);
        timeline.play();
    }

    /**
     * Initializes the drone for the level.
     */
    private void initializeDrone() {
        if (drone == null) {
            drone = (Drone) droneFactory.createEnemy(0, 0);
        }
    }

    /**
     * Initializes friendly units for the level.
     */
    @Override
    protected void initializeFriendlyUnits() {
        getRoot().getChildren().add(getUser());
        initializeDrone();
        levelView = new LevelViewLevelFour(getRoot(), PLAYER_INITIAL_HEALTH, drone);
    }

    /**
     * Checks if the game is over by evaluating the player's and drone's status.
     */
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

    /**
     * Starts the timer for respawning the drone.
     */
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

    /**
     * Respawns the drone and updates its health display.
     */
    private void respawnDrone() {
        drone = (Drone) droneFactory.createEnemy(0, 0);
        addEnemyUnit(drone);
        levelView.updateDroneHealth(drone.getHealth());
    }

    /**
     * Transitions to the next level by showing the game completion screen.
     */
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

    /**
     * Spawns enemy units, including the drone and other enemies, based on spawn probabilities.
     */
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

    /**
     * Updates the user interface, including the kill count and drone health.
     */
    protected void updateUI() {
        levelView.updateKillCount(getUser().getNumberOfKills());
        if (drone != null) {
            levelView.updateDroneHealth(drone.getHealth());
        }
    }

    /**
     * Instantiates the level view for Level Four.
     *
     * @return the level view for Level Four
     */
    @Override
    protected LevelViewLevelOne instantiateLevelView() {
        initializeDrone();
        levelView = new LevelViewLevelFour(getRoot(), PLAYER_INITIAL_HEALTH, drone);
        return levelView;
    }

    /**
     * Starts the timer for spawning heart power-ups.
     */
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

    /**
     * Starts the main game loop, which checks for game over conditions, spawns enemies, and updates the UI.
     */
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

    /**
     * Spawns heart power-ups at random positions on the screen based on spawn probability.
     */
    private void spawnHearts() {
        if (getTimeline() != null && getTimeline().getStatus() != Animation.Status.RUNNING) {
            return;
        }

        if (Math.random() < HEART_SPAWN_PROBABILITY) {
            // Generate random X and Y positions within user plane's flyable bounds
            double xPosition = X_LEFT_BOUND + Math.random() * (X_RIGHT_BOUND - X_LEFT_BOUND);
            double yPosition = Y_UPPER_BOUND + Math.random() * (Y_LOWER_BOUND - Y_UPPER_BOUND);

            // Create heart image
            Image heartImage = new Image(getClass().getResource("/com/example/demo/images/heart.png").toExternalForm());
            ImageView heart = new ImageView(heartImage);
            heart.setFitHeight(40);
            heart.setPreserveRatio(true);
            heart.setX(xPosition);
            heart.setY(yPosition);

            // Track spawned hearts and add them to the root
            hearts.add(heart);
            heartSpawnTimes.add(System.currentTimeMillis());
            getRoot().getChildren().add(heart);
        }
    }


    /**
     * Checks for collisions between the player and heart power-ups.
     * If a collision is detected, the player's health is increased and the heart is removed.
     */
    private void checkHeartCollisions() {
        Iterator<ImageView> heartIterator = hearts.iterator();
        Iterator<Long> timeIterator = heartSpawnTimes.iterator();

        while (heartIterator.hasNext() && timeIterator.hasNext()) {
            ImageView heart = heartIterator.next();
            Long spawnTime = timeIterator.next();

            if (heart.getBoundsInParent().intersects(getUser().getBoundsInParent())) {
                getUser().increaseHealth(1);
                // Corrected: Update heart display in the UI
                if (getLevelView() instanceof LevelViewLevelOne) {
                    ((LevelViewLevelOne) getLevelView()).addHearts(1);
                }
                getRoot().getChildren().remove(heart);
                heartIterator.remove();
                timeIterator.remove();
            }
        }
    }

    /**
     * Removes expired heart power-ups from the screen.
     */
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

    /**
     * Checks if the player has reached the kill target to advance to the next level.
     *
     * @return true if the player has reached the kill target, false otherwise
     */
    private boolean userHasReachedKillTarget() {
        return getUser().getNumberOfKills() >= KILLS_TO_ADVANCE;
    }
}