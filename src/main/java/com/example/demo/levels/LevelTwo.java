package com.example.demo.levels;

import com.example.demo.actors.Boss;
import com.example.demo.ui.LevelViewLevelOne;
import com.example.demo.ui.LevelViewLevelTwo;
import com.example.demo.factories.BossPlaneFactory;
import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.scene.layout.StackPane;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.scene.text.Text;
import javafx.stage.Stage;
import javafx.util.Duration;

/**
 * The LevelTwo class represents the second level of the game.
 * It handles the initialization, gameplay, and transition to the next level.
 */
public class LevelTwo extends LevelParent {

	/**
	 * The background image for the level.
	 */
	private static final String BACKGROUND_IMAGE_NAME = "/com/example/demo/images/background2.png";

	/**
	 * The initial health of the player.
	 */
	private static final int PLAYER_INITIAL_HEALTH = 5;

	/**
	 * The boss enemy for this level.
	 */
	private Boss boss;

	/**
	 * The factory for creating boss planes.
	 */
	private static final BossPlaneFactory bossFactory = new BossPlaneFactory();

	/**
	 * The name of the next level to transition to.
	 */
	private static final String NEXT_LEVEL = "LevelThree";

	/**
	 * Constructs a LevelTwo instance with the specified screen dimensions and primary stage.
	 *
	 * @param screenHeight the height of the screen
	 * @param screenWidth the width of the screen
	 * @param primaryStage the primary stage for the level
	 */
	public LevelTwo(double screenHeight, double screenWidth, Stage primaryStage) {
		super(BACKGROUND_IMAGE_NAME, screenHeight, screenWidth, PLAYER_INITIAL_HEALTH, primaryStage);
		boss = bossFactory.createEnemy(1000, 400); // No typecasting needed
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

		Text introText = new Text("SHOOT THE BOSS TO REDUCE ITS HEALTH TO ZERO AND WIN LEVEL TWO!");
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
	 * Initializes friendly units for the level.
	 */
	@Override
	protected void initializeFriendlyUnits() {
		getRoot().getChildren().add(getUser());
		getRoot().getChildren().add(boss.getShieldImage());
	}

	/**
	 * Checks if the game is over by evaluating the player's and boss's status.
	 */
	@Override
	protected void checkIfGameOver() {
		if (userIsDestroyed()) {
			loseGame();
		} else if (boss.isDestroyed()) {
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
	 * Spawns enemy units, specifically the boss, if no enemies are present.
	 */
	@Override
	protected void spawnEnemyUnits() {
		if (getCurrentNumberOfEnemies() == 0) {
			addEnemyUnit(boss);
		}
	}

	/**
	 * Instantiates the level view for Level Two.
	 *
	 * @return the level view for Level Two
	 */
	@Override
	protected LevelViewLevelOne instantiateLevelView() {
		if (boss == null) {
			boss = bossFactory.createEnemy(1000, 400); // Ensure boss is instantiated
		}
		LevelViewLevelTwo levelView = new LevelViewLevelTwo(getRoot(), PLAYER_INITIAL_HEALTH, boss);
		boss.setLevelView(levelView); // Set the level view in boss to update health display
		return levelView;
	}

	/**
	 * Updates the scene, including the boss's health display.
	 */
	@Override
	protected void updateScene() {
		super.updateScene();

		// Update boss health if the boss is present
		if (boss != null) {
			javafx.application.Platform.runLater(() -> {
				if (getLevelView() instanceof LevelViewLevelTwo) {
					((LevelViewLevelTwo) getLevelView()).updateBossHealth(boss.getHealth());
				}
			});
		}
	}
}
