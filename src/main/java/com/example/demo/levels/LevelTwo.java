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

public class LevelTwo extends LevelParent {

	private static final String BACKGROUND_IMAGE_NAME = "/com/example/demo/images/background2.png";
	private static final int PLAYER_INITIAL_HEALTH = 5;
	private Boss boss;
	private static final BossPlaneFactory bossFactory = new BossPlaneFactory();
	private static final String NEXT_LEVEL = "LevelThree";

	public LevelTwo(double screenHeight, double screenWidth, Stage primaryStage) {
		super(BACKGROUND_IMAGE_NAME, screenHeight, screenWidth, PLAYER_INITIAL_HEALTH, primaryStage);
		boss = bossFactory.createEnemy(1000, 400); // No typecasting needed
	}

	@Override
	public void startGame() {
		showIntroScreen();
	}

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


	@Override
	protected void initializeFriendlyUnits() {
		getRoot().getChildren().add(getUser());
		//added
		getRoot().getChildren().add(boss.getShieldImage());
	}

	@Override
	protected void checkIfGameOver() {
		if (userIsDestroyed()) {
			loseGame();
		}
		else if (boss.isDestroyed()) {
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
			addEnemyUnit(boss);
		}
	}
	@Override
	protected LevelViewLevelOne instantiateLevelView() {
		if (boss == null) {
			boss = bossFactory.createEnemy(1000, 400); // Ensure boss is instantiated
		}
		//LevelViewLevelTwo levelView = new LevelViewLevelTwo(getRoot(), PLAYER_INITIAL_HEALTH);
		LevelViewLevelTwo levelView = new LevelViewLevelTwo(getRoot(), PLAYER_INITIAL_HEALTH, boss);
		levelView.showShield();
		boss.setLevelView(levelView); // Set the level view in boss to update health display
		return levelView;
	}

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



/*	@Override
	protected LevelView instantiateLevelView() {
		LevelViewLevelTwo levelView = new LevelViewLevelTwo(getRoot(), PLAYER_INITIAL_HEALTH);
		//added
		levelView.showShield();
		boss.setLevelView(levelView);
		return levelView;
	}
*/
}
