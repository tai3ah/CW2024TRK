package com.example.demo.levels;

import com.example.demo.actors.GameEntity;
import com.example.demo.actors.FighterPlane;
import com.example.demo.actors.UserPlane;
import com.example.demo.factories.LevelEndScreenFactory;
import com.example.demo.managers.CollisionManager;
import com.example.demo.managers.InputHandler;
import com.example.demo.managers.PauseManager;
import com.example.demo.managers.SoundManager;
import com.example.demo.managers.LoginManager;
import com.example.demo.ui.LevelViewLevelOne;
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

	private final SoundManager soundManager = SoundManager.getInstance();
	private final Timeline timeline;
	private final UserPlane user;
	private final Scene scene;
	private final ImageView background;

	private final LoginManager loginManager = new LoginManager(); // Declare in class
	private String loggedInUser; // Track current user if logged in

	private final List<GameEntity> friendlyUnits;
	private final List<GameEntity> enemyUnits;
	private final List<GameEntity> userProjectiles;
	private final List<GameEntity> enemyProjectiles;

	private int currentNumberOfEnemies;
	private final LevelViewLevelOne levelView;

	private final Stage primaryStage;
	private final List<Consumer<String>> levelChangeListeners = new ArrayList<>();

	private final PauseManager pauseManager;
	private final CollisionManager collisionManager;
	private final InputHandler inputHandler; // New
	private final LevelEndScreenFactory endScreenFactory;

	private boolean gamePaused;

	public LevelParent(String backgroundImageName, double screenHeight, double screenWidth, int playerInitialHealth, Stage primaryStage) {
		this.endScreenFactory = new LevelEndScreenFactory();
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
		this.gamePaused = false;
		initializeTimeline();
		friendlyUnits.add(user);

		this.pauseManager = new PauseManager(root, primaryStage, timeline, screenWidth, screenHeight, background, this);
		this.collisionManager = new CollisionManager(root, user, enemyUnits, friendlyUnits, userProjectiles, enemyProjectiles);

		// Create and initialize InputHandler
		this.inputHandler = new InputHandler(user, background);
		this.inputHandler.setOnFire(this::fireProjectile);
	}

	protected abstract void initializeFriendlyUnits();

	protected abstract void checkIfGameOver();

	protected abstract void spawnEnemyUnits();

	protected abstract LevelViewLevelOne instantiateLevelView();

	public Scene initializeScene() {
		initializeBackground();
		initializeFriendlyUnits();
		levelView.showHeartDisplay();
		return scene;
	}

	public void startGame() {
		soundManager.playBackgroundSound();
		background.requestFocus();
		timeline.play();
	}

	public void addLevelChangeListener(Consumer<String> listener) {
		levelChangeListeners.add(listener);
	}

	//protected void goToNextLevel(String levelName) {
	//	levelChangeListeners.forEach(listener -> listener.accept(levelName));
	//}

	protected void updateScene() {
		if (!gamePaused) {
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
	}

	public void setGamePaused(boolean paused) {
		this.gamePaused = paused;
	}

	public boolean isGamePaused() {
		return gamePaused;
	}

	// Add an accessor for levelView
	protected LevelViewLevelOne getLevelView() {
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

/*	protected void winGame() {
		stopGame();
		soundManager.stopBackgroundSound();
		soundManager.playWinSound();
		endScreenFactory.initialize(getRoot(), getScreenWidth(), getScreenHeight());
		endScreenFactory.showWinLevelScreen(this::restartLevel, this::goToNextLevel);
	}
*/

	protected void winGame() {
		stopGame();
		soundManager.stopBackgroundSound();
		soundManager.playWinSound();

		// Save progress
		int currentLevelNumber = getLevelNumber();
		if (currentLevelNumber < 4) { // Ensure only levels 1-3 save progress
			saveProgress(currentLevelNumber + 1); // Unlock the next level
		}

		endScreenFactory.initialize(getRoot(), getScreenWidth(), getScreenHeight());
		endScreenFactory.showWinLevelScreen(this::restartLevel, this::goToNextLevel);
	}




	private int getLevelNumber() {
		return switch (getClass().getSimpleName()) {
			case "LevelOne" -> 1;
			case "LevelTwo" -> 2;
			case "LevelThree" -> 3;
			case "LevelFour" -> 4;
			default -> throw new IllegalArgumentException("Unknown level: " + getClass().getSimpleName());
		};
	}




	protected void loseGame() {
		stopGame();
		soundManager.stopBackgroundSound();
		soundManager.playLoseSound();
		endScreenFactory.initialize(getRoot(), getScreenWidth(), getScreenHeight());
		endScreenFactory.showLoseLevelScreen(this::restartLevel);
	}


	private void restartLevel() {
		LevelParent currentLevel = LevelBuilder.createLevel(getClass().getSimpleName(), screenHeight, screenWidth, primaryStage);
		primaryStage.setScene(currentLevel.initializeScene());
		currentLevel.startGame();
	}

	protected abstract void goToNextLevel();
	protected UserPlane getUser() {
		return user;
	}

	public Stage getPrimaryStage() {
		return primaryStage;
	}

	public double getScreenHeight() {
		return screenHeight;
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
		soundManager.stopBackgroundSound();
	}

	public void setLoggedInUser(String username) {
		this.loggedInUser = username;

		// Optionally, ensure the initial progress is loaded or saved here
		int progress = loginManager.getUserProgress(username);
		if (progress == 0) {
			loginManager.saveProgress(username, 1); // Initialize progress
		}
	}


	protected void saveProgress(int unlockedLevel) {
		if (loggedInUser != null && !loggedInUser.isEmpty()) {
			loginManager.saveProgress(loggedInUser, unlockedLevel);
		}
	}

}
