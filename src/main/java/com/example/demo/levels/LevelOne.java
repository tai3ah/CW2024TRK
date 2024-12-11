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

public class LevelOne extends LevelParent {

	private static final String BACKGROUND_IMAGE_NAME = "/com/example/demo/images/background1.png";
	private static final String NEXT_LEVEL = "LevelTwo";

	private static final int TOTAL_ENEMIES = 5;
	private static final int KILLS_TO_ADVANCE = 10;
	private static final double ENEMY_SPAWN_PROBABILITY = 0.20;
	private static final int PLAYER_INITIAL_HEALTH = 5;

	private final EnemyPlaneFactory enemyFactory = new EnemyPlaneFactory();

	public LevelOne(double screenHeight, double screenWidth, Stage primaryStage) {
		super(BACKGROUND_IMAGE_NAME, screenHeight, screenWidth, PLAYER_INITIAL_HEALTH, primaryStage);
	}

	@Override
	public void startGame() {
		showIntroScreen();
	}

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

	@Override
	protected void checkIfGameOver() {
		if (userIsDestroyed()) {
			loseGame();
		} else if (userHasReachedKillTarget()) {
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
	protected void initializeFriendlyUnits() {
		getRoot().getChildren().add(getUser());
	}

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

	@Override
	protected LevelViewLevelOne instantiateLevelView() {
		return new LevelViewLevelOne(getRoot(), PLAYER_INITIAL_HEALTH);
	}

	private boolean userHasReachedKillTarget() {
		return getUser().getNumberOfKills() >= KILLS_TO_ADVANCE;
	}
}
