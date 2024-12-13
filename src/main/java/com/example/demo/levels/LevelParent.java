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
import javafx.application.Platform;
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

/**
 * The LevelParent class serves as a base class for all game levels.
 * It handles common functionalities such as initializing the scene, managing game entities,
 * handling collisions, and updating the game state.
 */
public abstract class LevelParent {

	/**
	 * The adjustment value for the screen height.
	 */
	private static final double SCREEN_HEIGHT_ADJUSTMENT = 150;

	/**
	 * The delay in milliseconds for the game loop.
	 */
	private static final int MILLISECOND_DELAY = 50;

	/**
	 * The height of the screen.
	 */
	private final double screenHeight;

	/**
	 * The width of the screen.
	 */
	private final double screenWidth;

	/**
	 * The maximum Y position for enemies.
	 */
	private final double enemyMaximumYPosition;

	/**
	 * The root pane for the scene.
	 */
	private final Pane root;

	/**
	 * The sound manager for playing sounds.
	 */
	private final SoundManager soundManager = SoundManager.getInstance();

	/**
	 * The timeline for the game loop.
	 */
	private final Timeline timeline;

	/**
	 * The user plane controlled by the player.
	 */
	private final UserPlane user;

	/**
	 * The scene for the level.
	 */
	private final Scene scene;

	/**
	 * The background image for the level.
	 */
	private final ImageView background;

	/**
	 * The login manager for handling user logins.
	 */
	private final LoginManager loginManager = new LoginManager();

	/**
	 * The currently logged-in user.
	 */
	private String loggedInUser;

	/**
	 * The list of friendly units in the level.
	 */
	private final List<GameEntity> friendlyUnits;

	/**
	 * The list of enemy units in the level.
	 */
	private final List<GameEntity> enemyUnits;

	/**
	 * The list of projectiles fired by the user.
	 */
	private final List<GameEntity> userProjectiles;

	/**
	 * The list of projectiles fired by enemies.
	 */
	private final List<GameEntity> enemyProjectiles;

	/**
	 * The current number of enemies in the level.
	 */
	private int currentNumberOfEnemies;

	/**
	 * The view for the level.
	 */
	private final LevelViewLevelOne levelView;

	/**
	 * The primary stage for the application.
	 */
	private final Stage primaryStage;

	/**
	 * The list of listeners for level changes.
	 */
	private final List<Consumer<String>> levelChangeListeners = new ArrayList<>();

	/**
	 * The pause manager for handling game pauses.
	 */
	private final PauseManager pauseManager;

	/**
	 * The collision manager for handling collisions between game entities.
	 */
	private final CollisionManager collisionManager;

	/**
	 * The input handler for handling user inputs.
	 */
	private final InputHandler inputHandler;

	/**
	 * The factory for creating level end screens.
	 */
	private final LevelEndScreenFactory endScreenFactory;

	/**
	 * Indicates whether the game is paused.
	 */
	private boolean gamePaused;

	/**
	 * Constructs a LevelParent instance with the specified parameters.
	 *
	 * @param backgroundImageName the name of the background image
	 * @param screenHeight        the height of the screen
	 * @param screenWidth         the width of the screen
	 * @param playerInitialHealth the initial health of the player
	 * @param primaryStage        the primary stage for the application
	 */
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

	/**
	 * Initializes friendly units for the level.
	 */
	protected abstract void initializeFriendlyUnits();

	/**
	 * Checks if the game is over.
	 */
	protected abstract void checkIfGameOver();

	/**
	 * Spawns enemy units for the level.
	 */
	protected abstract void spawnEnemyUnits();

	/**
	 * Instantiates the level view for the level.
	 *
	 * @return the level view for the level
	 */
	protected abstract LevelViewLevelOne instantiateLevelView();

	/**
	 * Initializes the scene for the level.
	 *
	 * @return the initialized scene
	 */
	public Scene initializeScene() {
		initializeBackground();
		initializeFriendlyUnits();
		levelView.showHeartDisplay();
		return scene;
	}

	/**
	 * Starts the game by playing the background sound and starting the timeline.
	 */
	public void startGame() {
		soundManager.playBackgroundSound();
		background.requestFocus();
		timeline.play();
	}

	/**
	 * Adds a listener for level changes.
	 *
	 * @param listener the listener to add
	 */
	public void addLevelChangeListener(Consumer<String> listener) {
		levelChangeListeners.add(listener);
	}

	/**
	 * Updates the scene by spawning enemies, updating actors, handling collisions, and checking game over conditions.
	 */
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

	/**
	 * Sets the game paused state.
	 *
	 * @param paused true to pause the game, false to resume
	 */
	public void setGamePaused(boolean paused) {
		this.gamePaused = paused;
	}

	/**
	 * Checks if the game is paused.
	 *
	 * @return true if the game is paused, false otherwise
	 */
	public boolean isGamePaused() {
		return gamePaused;
	}

	/**
	 * Gets the level view for the level.
	 *
	 * @return the level view
	 */
	protected LevelViewLevelOne getLevelView() {
		return levelView;
	}

	/**
	 * Initializes the timeline for the game loop.
	 */
	private void initializeTimeline() {
		timeline.setCycleCount(Timeline.INDEFINITE);
		KeyFrame gameLoop = new KeyFrame(Duration.millis(MILLISECOND_DELAY), e -> updateScene());
		timeline.getKeyFrames().add(gameLoop);
	}

	/**
	 * Initializes the background for the level.
	 */
	private void initializeBackground() {
		background.setFitHeight(screenHeight);
		background.setFitWidth(screenWidth);
		root.getChildren().add(background);
		pauseManager.initializePauseButton();
		inputHandler.initializeInputHandlers();
	}

	/**
	 * Fires a projectile from the user plane.
	 */
	private void fireProjectile() {
		GameEntity projectile = user.fireProjectile();
		root.getChildren().add(projectile);
		userProjectiles.add(projectile);
	}

	/**
	 * Generates enemy fire by spawning projectiles from enemy units.
	 */
	private void generateEnemyFire() {
		enemyUnits.forEach(enemy -> spawnEnemyProjectile(((FighterPlane) enemy).fireProjectile()));
	}

	/**
	 * Spawns a projectile fired by an enemy unit.
	 *
	 * @param projectile the projectile to spawn
	 */
	private void spawnEnemyProjectile(GameEntity projectile) {
		if (projectile != null) {
			root.getChildren().add(projectile);
			enemyProjectiles.add(projectile);
		}
	}

	/**
	 * Updates the state of all actors in the level.
	 */
	private void updateActors() {
		friendlyUnits.forEach(GameEntity::updateActor);
		enemyUnits.forEach(GameEntity::updateActor);
		userProjectiles.forEach(GameEntity::updateActor);
		enemyProjectiles.forEach(GameEntity::updateActor);
	}

	/**
	 * Removes all destroyed actors from the level.
	 */
	private void removeAllDestroyedActors() {
		removeDestroyedActors(friendlyUnits);
		removeDestroyedActors(enemyUnits);
		removeDestroyedActors(userProjectiles);
		removeDestroyedActors(enemyProjectiles);
	}

	/**
	 * Removes destroyed actors from the specified list of actors.
	 *
	 * @param actors the list of actors to check for destruction
	 */
	private void removeDestroyedActors(List<GameEntity> actors) {
		List<GameEntity> destroyedActors = actors.stream().filter(GameEntity::isDestroyed).toList();
		root.getChildren().removeAll(destroyedActors);
		actors.removeAll(destroyedActors);
	}

	/**
	 * Updates the level view to reflect the current health of the user.
	 */
	private void updateLevelView() {
		levelView.removeHearts(user.getHealth());
	}

	/**
	 * Updates the kill count in the level view.
	 */
	private void updateKillCount() {
		levelView.updateKillCount(user.getNumberOfKills());
	}

	/**
	 * Handles the actions to be taken when the player wins the game.
	 */
	protected void winGame() {
		stopGame();
		soundManager.stopBackgroundSound();
		soundManager.playWinSound();

		getTimeline().stop();

		// Save progress
		int currentLevelNumber = getLevelNumber();
		if (currentLevelNumber < 4) { // Ensure only levels 1-3 save progress
			saveProgress(currentLevelNumber + 1); // Unlock the next level
		}

		//endScreenFactory.initialize(getRoot(), getScreenWidth(), getScreenHeight());
		//endScreenFactory.showWinLevelScreen(this::restartLevel, this::goToNextLevel);
		// Initialize the level end screen
		endScreenFactory.initialize(getRoot(), getScreenWidth(), getScreenHeight());
		endScreenFactory.showWinLevelScreen(
				this::restartLevel,      // Action for "Play Again"
				this::goToNextLevel,     // Action for "Next Level"
				getPrimaryStage()        // Pass the primary stage for "Main Menu"
		);

	}

	/**
	 * Gets the current level number based on the class name.
	 *
	 * @return the current level number
	 */
	private int getLevelNumber() {
		return switch (getClass().getSimpleName()) {
			case "LevelOne" -> 1;
			case "LevelTwo" -> 2;
			case "LevelThree" -> 3;
			case "LevelFour" -> 4;
			default -> throw new IllegalArgumentException("Unknown level: " + getClass().getSimpleName());
		};
	}

	/**
	 * Handles the actions to be taken when the player loses the game.
	 */
	protected void loseGame() {
		stopGame();
		soundManager.stopBackgroundSound();
		soundManager.playLoseSound();
		// Initialize the level end screen
		endScreenFactory.initialize(getRoot(), getScreenWidth(), getScreenHeight());
		endScreenFactory.showLoseLevelScreen(
				this::restartLevel,     // Action for "Play Again"
				getPrimaryStage()       // Pass the primary stage for "Main Menu"
		);

	}

	/**
	 * Restarts the current level.
	 */
	private void restartLevel() {
		LevelParent currentLevel = LevelBuilder.createLevel(getClass().getSimpleName(), screenHeight, screenWidth, primaryStage);
		primaryStage.setScene(currentLevel.initializeScene());
		currentLevel.startGame();
	}

	/**
	 * Transitions to the next level.
	 * This method should be implemented by subclasses to define the specific behavior for moving to the next level.
	 */
	protected abstract void goToNextLevel();

	/**
	 * Gets the user plane controlled by the player.
	 *
	 * @return the user plane
	 */
	protected UserPlane getUser() {
		return user;
	}

	/**
	 * Gets the primary stage for the application.
	 *
	 * @return the primary stage
	 */
	public Stage getPrimaryStage() {
		return primaryStage;
	}

	/**
	 * Gets the height of the screen.
	 *
	 * @return the screen height
	 */
	public double getScreenHeight() {
		return screenHeight;
	}

	/**
	 * Gets the root pane for the scene.
	 *
	 * @return the root pane
	 */
	public Pane getRoot() {
		return root;
	}

	/**
	 * Gets the current number of enemies in the level.
	 *
	 * @return the current number of enemies
	 */
	protected int getCurrentNumberOfEnemies() {
		return enemyUnits.size();
	}

	/**
	 * Adds an enemy unit to the level if it is not already present.
	 *
	 * @param enemy the enemy unit to add
	 */
	protected void addEnemyUnit(GameEntity enemy) {
		if (!root.getChildren().contains(enemy)) {
			enemyUnits.add(enemy);
			root.getChildren().add(enemy);
		}
	}


	/**
	 * Gets the maximum Y position for enemies.
	 *
	 * @return the maximum Y position for enemies
	 */
	protected double getEnemyMaximumYPosition() {
		return enemyMaximumYPosition;
	}

	/**
	 * Gets the width of the screen.
	 *
	 * @return the screen width
	 */
	protected double getScreenWidth() {
		return screenWidth;
	}

	/**
	 * Checks if the user plane is destroyed.
	 *
	 * @return true if the user plane is destroyed, false otherwise
	 */
	protected boolean userIsDestroyed() {
		return user.isDestroyed();
	}

	/**
	 * Updates the current number of enemies in the level.
	 */
	private void updateNumberOfEnemies() {
		currentNumberOfEnemies = enemyUnits.size();
	}

	/**
	 * Gets the timeline for the game loop.
	 *
	 * @return the timeline
	 */
	public Timeline getTimeline() {
		return timeline;
	}

	/**
	 * Stops the game and the background sound.
	 */
	public void stopGame() {
		if (timeline != null) {
			timeline.stop();
		}
		soundManager.stopBackgroundSound();
	}

	/**
	 * Sets the logged-in user and initializes their progress if necessary.
	 *
	 * @param username the username of the logged-in user
	 */
	public void setLoggedInUser(String username) {
		this.loggedInUser = username;

		// Optionally, ensure the initial progress is loaded or saved here
		int progress = loginManager.getUserProgress(username);
		if (progress == 0) {
			loginManager.saveProgress(username, 1); // Initialize progress
		}
	}

	/**
	 * Saves the progress of the logged-in user.
	 *
	 * @param unlockedLevel the level to unlock
	 */
	protected void saveProgress(int unlockedLevel) {
		if (loggedInUser != null && !loggedInUser.isEmpty()) {
			loginManager.saveProgress(loggedInUser, unlockedLevel);
		}
	}
}