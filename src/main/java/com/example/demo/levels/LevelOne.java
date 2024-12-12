package com.example.demo.levels;

import com.example.demo.actors.GameEntity;
import com.example.demo.ui.LevelViewLevelOne;
import com.example.demo.factories.EnemyPlaneFactory;
import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.scene.layout.StackPane;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.scene.text.Text;
import javafx.stage.Stage;
import javafx.util.Duration;

/**
 * The LevelOne class represents the first level of the game.
 * It handles the initialization, gameplay, and transition to the next level.
 */
public class LevelOne extends LevelParent {

	/**
	 * The background image for the level.
	 */
	private static final String BACKGROUND_IMAGE_NAME = "/com/example/demo/images/background1.png";

	/**
	 * The name of the next level to transition to.
	 */
	private static final String NEXT_LEVEL = "LevelTwo";

	/**
	 * The total number of enemies to spawn.
	 */
	private static final int TOTAL_ENEMIES = 5;

	/**
	 * The number of kills required to advance to the next level.
	 */
	private static final int KILLS_TO_ADVANCE = 10;

	/**
	 * The probability of spawning an enemy.
	 */
	private static final double ENEMY_SPAWN_PROBABILITY = 0.20;

	/**
	 * The initial health of the player.
	 */
	private static final int PLAYER_INITIAL_HEALTH = 5;

	/**
	 * The factory for creating enemy planes.
	 */
	private final EnemyPlaneFactory enemyFactory = new EnemyPlaneFactory();

	/**
	 * Constructs a LevelOne instance with the specified screen dimensions and primary stage.
	 *
	 * @param screenHeight the height of the screen
	 * @param screenWidth the width of the screen
	 * @param primaryStage the primary stage for the level
	 */
	public LevelOne(double screenHeight, double screenWidth, Stage primaryStage) {
		super(BACKGROUND_IMAGE_NAME, screenHeight, screenWidth, PLAYER_INITIAL_HEALTH, primaryStage);
	}

	/**
	 * Starts the game by showing the intro screen.
	 */
	@Override
	public void startGame() {
		showIntroScreen();
	}

	/**
	 * Displays the intro screen with instructions for the level.
	 */
	private void showIntroScreen() {
		StackPane introOverlay = new StackPane();
		introOverlay.setPrefSize(getScreenWidth(), getScreenHeight());
		introOverlay.setStyle("-fx-background-color: black;");

		Text introText = new Text("KILL " + KILLS_TO_ADVANCE + " ENEMY PLANES TO WIN LEVEL ONE!");
		introText.setFill(Color.WHITE);
		introText.setFont(Font.font("Arial", 40));
		introOverlay.getChildren().add(introText);

		getRoot().getChildren().add(introOverlay);

		Timeline timeline = new Timeline(
				new KeyFrame(Duration.seconds(4), e -> {
					getRoot().getChildren().remove(introOverlay);
					super.startGame();
				})
		);
		timeline.setCycleCount(1);
		timeline.play();
	}

	/**
	 * Checks if the game is over by evaluating the player's status.
	 */
	@Override
	protected void checkIfGameOver() {
		if (userIsDestroyed()) {
			loseGame();
		} else if (userHasReachedKillTarget()) {
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
	 * Initializes friendly units for the level.
	 */
	@Override
	protected void initializeFriendlyUnits() {
		getRoot().getChildren().add(getUser());
	}

	/**
	 * Spawns enemy units based on the spawn probability.
	 */
	@Override
	protected void spawnEnemyUnits() {
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
	 * Instantiates the level view for Level One.
	 *
	 * @return the level view for Level One
	 */
	@Override
	protected LevelViewLevelOne instantiateLevelView() {
		return new LevelViewLevelOne(getRoot(), PLAYER_INITIAL_HEALTH);
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