package com.example.demo;

import java.util.*;
import java.util.stream.Collectors;

import javafx.animation.*;
import javafx.event.EventHandler;
import javafx.scene.Scene;
import javafx.scene.image.*;
import javafx.scene.input.*;
import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;
import javafx.util.Duration;
import javafx.scene.layout.Pane;

import javafx.stage.Stage;

import javafx.scene.control.Button;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;
import javafx.scene.control.Label;
import javafx.geometry.Pos;


public abstract class LevelParent extends Observable {

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

	private final List<ActiveActorDestructible> friendlyUnits;
	private final List<ActiveActorDestructible> enemyUnits;
	private final List<ActiveActorDestructible> userProjectiles;
	private final List<ActiveActorDestructible> enemyProjectiles;
	
	private int currentNumberOfEnemies;
	private final LevelView levelView;

	private final Stage primaryStage;

	private static final String PAUSE_BUTTON = "/com/example/demo/images/pause.png";
	private static final String RESUME_BUTTON = "/com/example/demo/images/resume.png";
	private static final String QUIT_LEVEL_BUTTON= "/com/example/demo/images/quitLevel.png";

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

		this.background = new ImageView(new Image(getClass().getResource(backgroundImageName).toExternalForm()));
		this.screenHeight = screenHeight;
		this.screenWidth = screenWidth;
		this.enemyMaximumYPosition = screenHeight - SCREEN_HEIGHT_ADJUSTMENT;
		this.levelView = instantiateLevelView();
		this.currentNumberOfEnemies = 0;
		initializeTimeline();
		friendlyUnits.add(user);
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

	public void goToNextLevel(String levelName) {
		setChanged();
		notifyObservers(levelName);
	}

	private void updateScene() {
		spawnEnemyUnits();
		updateActors();
		generateEnemyFire();
		updateNumberOfEnemies();
		handleEnemyPenetration();
		handleUserProjectileCollisions();
		handleEnemyProjectileCollisions();
		handlePlaneCollisions();
		removeAllDestroyedActors();
		updateKillCount();
		updateLevelView();
		checkIfGameOver();
	}

	private void initializeTimeline() {
		timeline.setCycleCount(Timeline.INDEFINITE);
		KeyFrame gameLoop = new KeyFrame(Duration.millis(MILLISECOND_DELAY), e -> updateScene());
		timeline.getKeyFrames().add(gameLoop);
	}

	private void initializeBackground() {
		background.setFocusTraversable(true);
		background.setFitHeight(screenHeight);
		background.setFitWidth(screenWidth);
		background.setOnKeyPressed(new EventHandler<KeyEvent>() {
			public void handle(KeyEvent e) {
				KeyCode kc = e.getCode();
				if (kc == KeyCode.UP) user.moveUp();
				if (kc == KeyCode.DOWN) user.moveDown();
				if (kc == KeyCode.SPACE) fireProjectile();
			}
		});
		background.setOnKeyReleased(new EventHandler<KeyEvent>() {
			public void handle(KeyEvent e) {
				KeyCode kc = e.getCode();
				if (kc == KeyCode.UP || kc == KeyCode.DOWN) user.stop();
			}
		});
		root.getChildren().add(background);

		// Add Pause Button to the root
		Image pauseImage = new Image(getClass().getResourceAsStream(PAUSE_BUTTON)); // Load the pause image from the specified path
		ImageView pauseImageView = new ImageView(pauseImage);
		pauseImageView.setFitWidth(80);
		pauseImageView.setFitHeight(60);

		Button pauseButton = new Button();
		pauseButton.setGraphic(pauseImageView);
		pauseButton.setLayoutX(screenWidth - 100);
		pauseButton.setLayoutY(20);
		pauseButton.setStyle("-fx-background-color: transparent;");
		pauseButton.setOnAction(event -> {
			showPauseWindow(pauseButton);

		});
		root.getChildren().add(pauseButton);
	}


//added this method
	private void showPauseWindow(Button pauseButton) {
		// Pause the game
		timeline.pause();


		// Create a pause overlay
		StackPane pauseOverlay = new StackPane();
		pauseOverlay.setPrefSize(screenWidth, screenHeight);
		pauseOverlay.setStyle("-fx-background-color: rgba(0, 0, 0, 0.5);");


		// Pause message
		Label pauseLabel = new Label("GAME PAUSED");
		pauseLabel.setStyle("-fx-font-family: 'Arial'; -fx-font-size: 30px; -fx-text-fill: #FFD700;");

		// resume game and Quit level buttons
		Image resumeImage = new Image(getClass().getResourceAsStream(RESUME_BUTTON));
		ImageView resumeImageView = new ImageView(resumeImage);
		resumeImageView.setFitWidth(100);
		resumeImageView.setFitHeight(60);

		Button resumeButton = new Button();
		resumeButton.setGraphic(resumeImageView);
		resumeButton.setStyle("-fx-background-color: transparent;");
		resumeButton.setOnAction(event ->  {
			root.getChildren().remove(pauseOverlay);
			pauseButton.setDisable(true);
			resumeGameWithCountdown(pauseButton);
		});

		Image quitLevelImage = new Image(getClass().getResourceAsStream(QUIT_LEVEL_BUTTON));
		ImageView quitLevelImageView = new ImageView(quitLevelImage);
		quitLevelImageView.setFitWidth(100);
		quitLevelImageView.setFitHeight(60);

		Button quitLevelButton = new Button();
		quitLevelButton.setGraphic(quitLevelImageView);
		quitLevelButton.setStyle("-fx-background-color: transparent;");
		quitLevelButton.setOnAction(event -> goToMainMenu(primaryStage));

		VBox buttonsLayout = new VBox(20, pauseLabel, resumeButton, quitLevelButton);
		buttonsLayout.setAlignment(Pos.CENTER);

		pauseOverlay.getChildren().add(buttonsLayout);
		root.getChildren().add(pauseOverlay);
	}

//added this method
	private void resumeGameWithCountdown(Button pauseButton) {
		Label countdownLabel = new Label("3");
		countdownLabel.setStyle("-fx-font-size: 50px; -fx-text-fill: white;");
		countdownLabel.setLayoutX(screenWidth / 2 - 25);
		countdownLabel.setLayoutY(screenHeight / 2 - 25);
		root.getChildren().add(countdownLabel);

		Timeline countdownTimeline = new Timeline(
				new KeyFrame(Duration.seconds(1), e -> countdownLabel.setText("2")),
				new KeyFrame(Duration.seconds(2), e -> countdownLabel.setText("1")),
				new KeyFrame(Duration.seconds(3), e -> {
					root.getChildren().remove(countdownLabel);
					pauseButton.setDisable(false);
					timeline.play(); // Resume the game
				})
		);
		countdownTimeline.setCycleCount(1);
		countdownTimeline.play();
	}

//added this method

	private void goToMainMenu(Stage primaryStage) {
		stopGame(); // Stop the timeline and game logic

		// Create a new Main Menu Page and set it on the primaryStage
		MainMenuPage mainMenuPage = new MainMenuPage();

		// Reset the scene for main menu
		StackPane root = new StackPane();
		Scene mainMenuScene = new Scene(root, 1300, 750); // Set your preferred width and height
		primaryStage.setScene(mainMenuScene);

		// Set up the main menu layout and content
		mainMenuPage.start(primaryStage);
		primaryStage.sizeToScene();
		primaryStage.show();

	}



	private void fireProjectile() {
		ActiveActorDestructible projectile = user.fireProjectile();
		root.getChildren().add(projectile);
		userProjectiles.add(projectile);
	}

	private void generateEnemyFire() {
		enemyUnits.forEach(enemy -> spawnEnemyProjectile(((FighterPlane) enemy).fireProjectile()));
	}

	private void spawnEnemyProjectile(ActiveActorDestructible projectile) {
		if (projectile != null) {
			root.getChildren().add(projectile);
			enemyProjectiles.add(projectile);
		}
	}

	private void updateActors() {
		friendlyUnits.forEach(plane -> plane.updateActor());
		enemyUnits.forEach(enemy -> enemy.updateActor());
		userProjectiles.forEach(projectile -> projectile.updateActor());
		enemyProjectiles.forEach(projectile -> projectile.updateActor());
	}

	private void removeAllDestroyedActors() {
		removeDestroyedActors(friendlyUnits);
		removeDestroyedActors(enemyUnits);
		removeDestroyedActors(userProjectiles);
		removeDestroyedActors(enemyProjectiles);
	}

	private void removeDestroyedActors(List<ActiveActorDestructible> actors) {
		List<ActiveActorDestructible> destroyedActors = actors.stream().filter(actor -> actor.isDestroyed())
				.collect(Collectors.toList());
		root.getChildren().removeAll(destroyedActors);
		actors.removeAll(destroyedActors);
	}

	private void handlePlaneCollisions() {
		handleCollisions(friendlyUnits, enemyUnits);
	}


	private void handleUserProjectileCollisions() {
		for (ActiveActorDestructible projectile : userProjectiles) {
			for (ActiveActorDestructible enemy : enemyUnits) {
				if (!enemy.isDestroyed() && projectile.getBoundsInParent().intersects(enemy.getBoundsInParent())) {
					if (enemy instanceof Boss) {
						Boss boss = (Boss) enemy;
						if (!boss.isShielded()) {
							boss.takeDamage();
							System.out.println("Boss hit! Health remaining: " + boss.getHealth());
						} else {
							System.out.println("Projectile hit the shield. Boss is protected.");
						}
					} else {
						enemy.takeDamage();
						if (enemy.isDestroyed()) {
							user.incrementKillCount(); // Increment kill count when enemy is destroyed
							System.out.println("Kill count: " + user.getNumberOfKills());
						}
					}
					projectile.takeDamage();
					root.getChildren().remove(projectile);
				}
			}
		}
	}


	private void handleEnemyProjectileCollisions() {
		handleCollisions(enemyProjectiles, friendlyUnits);


	}

	private void handleCollisions(List<ActiveActorDestructible> actors1,
			List<ActiveActorDestructible> actors2) {
		for (ActiveActorDestructible actor : actors2) {
			for (ActiveActorDestructible otherActor : actors1) {
				if (actor.getBoundsInParent().intersects(otherActor.getBoundsInParent())) {
					actor.takeDamage();
					otherActor.takeDamage();

				}
			}
		}

	}

	private void handleEnemyPenetration() {
		for (ActiveActorDestructible enemy : enemyUnits) {
			if (enemyHasPenetratedDefenses(enemy)) {
				enemy.takeDamage();
				enemy.destroy();
			}
		}
	}

	private void updateLevelView() {
		levelView.removeHearts(user.getHealth());
	}

	private void updateKillCount() {
		levelView.updateKillCount(user.getNumberOfKills());
	}

	private boolean enemyHasPenetratedDefenses(ActiveActorDestructible enemy) {
		return Math.abs(enemy.getTranslateX()) > screenWidth;
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

	//changed from protected to public
	public Pane getRoot() {
		return root;
	}

	protected int getCurrentNumberOfEnemies() {
		return enemyUnits.size();
	}

	protected void addEnemyUnit(ActiveActorDestructible enemy) {
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
