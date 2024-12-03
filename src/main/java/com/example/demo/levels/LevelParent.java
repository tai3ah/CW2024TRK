package com.example.demo.levels;

import com.example.demo.actors.GameEntity;
import com.example.demo.actors.FighterPlane;
import com.example.demo.actors.UserPlane;
import com.example.demo.managers.CollisionManager;
import com.example.demo.managers.InputHandler;
import com.example.demo.managers.PauseManager;
import com.example.demo.ui.LevelView;
import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.scene.Scene;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.Pane;
import javafx.stage.Stage;
import javafx.util.Duration;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.function.Consumer;

public abstract class LevelParent {

	private static final double SCREEN_HEIGHT_ADJUSTMENT = 150;
	private static final int MILLISECOND_DELAY = 50;
	private final double screenHeight;
	private final double screenWidth;
	private final double enemyMaximumYPosition;

	private final Pane root;

	private final Timeline timeline;
	private final UserPlane user;
	private final Scene scene;
	private final ImageView background;

	private final List<GameEntity> friendlyUnits;
	private final List<GameEntity> enemyUnits;
	private final List<GameEntity> userProjectiles;
	private final List<GameEntity> enemyProjectiles;

	private int currentNumberOfEnemies;
	private final LevelView levelView;

	private final Stage primaryStage;
	private final List<Consumer<String>> levelChangeListeners = new ArrayList<>();

	private final PauseManager pauseManager;
	private final CollisionManager collisionManager;
	private final InputHandler inputHandler; // New

	public LevelParent(String backgroundImageName, double screenHeight, double screenWidth, int playerInitialHealth, Stage primaryStage) {
		this.primaryStage = primaryStage;
		this.root = new Pane();
		this.scene = new Scene(root, screenWidth, screenHeight);
		this.timeline = new Timeline();
		this.user = new UserPlane(playerInitialHealth);
		this.friendlyUnits = new ArrayList<>();
		this.enemyUnits = new ArrayList<>();
		this.userProjectiles = new ArrayList<>();
		this.enemyProjectiles = new ArrayList<>();

		this.background = new ImageView(new Image(Objects.requireNonNull(getClass().getResource(backgroundImageName)).toExternalForm()));
		this.screenHeight = screenHeight;
		this.screenWidth = screenWidth;
		this.enemyMaximumYPosition = screenHeight - SCREEN_HEIGHT_ADJUSTMENT;
		this.levelView = instantiateLevelView();
		this.currentNumberOfEnemies = 0;
		initializeTimeline();
		friendlyUnits.add(user);

		this.pauseManager = new PauseManager(root, primaryStage, timeline, screenWidth, screenHeight, background);
		this.collisionManager = new CollisionManager(root, user, enemyUnits, friendlyUnits, userProjectiles, enemyProjectiles);

		// Create and initialize InputHandler
		this.inputHandler = new InputHandler(user, background);
		this.inputHandler.setOnFire(this::fireProjectile);
	}

	protected abstract void initializeFriendlyUnits();

	protected abstract void checkIfGameOver();

	protected abstract void spawnEnemyUnits();

	protected abstract LevelView instantiateLevelView();

	public Scene initializeScene() {
		initializeBackground();
		initializeFriendlyUnits();
		levelView.showHeartDisplay();
		return scene;
	}

	public void startGame() {
		background.requestFocus();
		timeline.play();
	}

	public void addLevelChangeListener(Consumer<String> listener) {
		levelChangeListeners.add(listener);
	}

	protected void goToNextLevel(String levelName) {
		levelChangeListeners.forEach(listener -> listener.accept(levelName));
	}

	protected void updateScene() {
		spawnEnemyUnits();
		updateActors();
		generateEnemyFire();
		updateNumberOfEnemies();
		collisionManager.handleCollisions();
		removeAllDestroyedActors();
		updateKillCount();
		updateLevelView();
		checkIfGameOver();
	}

	// Add an accessor for levelView
	protected LevelView getLevelView() {
		return levelView;
	}

	private void initializeTimeline() {
		timeline.setCycleCount(Timeline.INDEFINITE);
		KeyFrame gameLoop = new KeyFrame(Duration.millis(MILLISECOND_DELAY), e -> updateScene());
		timeline.getKeyFrames().add(gameLoop);
	}

	private void initializeBackground() {
		background.setFitHeight(screenHeight);
		background.setFitWidth(screenWidth);
		root.getChildren().add(background);
		pauseManager.initializePauseButton();
		inputHandler.initializeInputHandlers();
	}

	private void fireProjectile() {
		GameEntity projectile = user.fireProjectile();
		root.getChildren().add(projectile);
		userProjectiles.add(projectile);
	}

	private void generateEnemyFire() {
		enemyUnits.forEach(enemy -> spawnEnemyProjectile(((FighterPlane) enemy).fireProjectile()));
	}

	private void spawnEnemyProjectile(GameEntity projectile) {
		if (projectile != null) {
			root.getChildren().add(projectile);
			enemyProjectiles.add(projectile);
		}
	}

	private void updateActors() {
		friendlyUnits.forEach(GameEntity::updateActor);
		enemyUnits.forEach(GameEntity::updateActor);
		userProjectiles.forEach(GameEntity::updateActor);
		enemyProjectiles.forEach(GameEntity::updateActor);
	}

	private void removeAllDestroyedActors() {
		removeDestroyedActors(friendlyUnits);
		removeDestroyedActors(enemyUnits);
		removeDestroyedActors(userProjectiles);
		removeDestroyedActors(enemyProjectiles);
	}

	private void removeDestroyedActors(List<GameEntity> actors) {
		List<GameEntity> destroyedActors = actors.stream().filter(GameEntity::isDestroyed)
				.toList();
		root.getChildren().removeAll(destroyedActors);
		actors.removeAll(destroyedActors);
	}

	private void updateLevelView() {
		levelView.removeHearts(user.getHealth());
	}

	private void updateKillCount() {
		levelView.updateKillCount(user.getNumberOfKills());
	}

	protected void winGame() {
		timeline.stop();
		levelView.showWinImage();
	}

	protected void loseGame() {
		timeline.stop();
		levelView.showGameOverImage();
	}

	protected UserPlane getUser() {
		return user;
	}

	public Pane getRoot() {
		return root;
	}

	protected int getCurrentNumberOfEnemies() {
		return enemyUnits.size();
	}

	protected void addEnemyUnit(GameEntity enemy) {
		enemyUnits.add(enemy);
		root.getChildren().add(enemy);
	}

	protected double getEnemyMaximumYPosition() {
		return enemyMaximumYPosition;
	}

	protected double getScreenWidth() {
		return screenWidth;
	}

	protected boolean userIsDestroyed() {
		return user.isDestroyed();
	}

	private void updateNumberOfEnemies() {
		currentNumberOfEnemies = enemyUnits.size();
	}

	public Timeline getTimeline() {
		return timeline;
	}

	public void stopGame() {
		if (timeline != null) {
			timeline.stop();
		}
	}
}
