package com.example.demo.controller;

import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import javafx.stage.Stage;
import com.example.demo.levels.LevelParent;
import com.example.demo.levels.LevelBuilder;

/**
 * The Controller class manages the game flow, including launching the game,
 * transitioning between levels, and cleaning up resources.
 */
public class Controller {

	/**
	 * The current level being played.
	 */
	private LevelParent currentLevel;

	/**
	 * The class name of the first level to be loaded.
	 */
	private static final String LEVEL_ONE_CLASS_NAME = "LevelOne";

	/**
	 * The primary stage for displaying the game.
	 */
	private final Stage stage;

	/**
	 * Constructs a Controller with the specified stage.
	 *
	 * @param stage the primary stage for displaying the game
	 */
	public Controller(Stage stage) {
		this.stage = stage;
	}

	/**
	 * Launches the game by showing the stage and loading the first level.
	 */
	public void launchGame() {
		stage.show();
		goToLevel(LEVEL_ONE_CLASS_NAME);
	}

	/**
	 * Transitions to the specified level.
	 *
	 * @param levelName the name of the level to load
	 */
	private void goToLevel(String levelName) {
		try {
			// Stop any ongoing game activity from the current level
			if (currentLevel != null) {
				currentLevel.stopGame();
			}

			// Clean up any leftover objects from the previous level
			cleanupCurrentLevel();

			// Create the new level using LevelBuilder
			LevelParent myLevel = LevelBuilder.createLevel(levelName, stage.getHeight(), stage.getWidth(), stage);

			// Add a level change listener to go to the next level
			myLevel.addLevelChangeListener(this::goToLevel);

			Scene scene = myLevel.initializeScene();
			stage.setScene(scene);
			myLevel.startGame();

			// Update the current level reference
			currentLevel = myLevel;
		} catch (IllegalArgumentException e) {
			Alert alert = new Alert(AlertType.ERROR);
			alert.setContentText(e.getMessage());
			alert.show();
		}
	}

	/**
	 * Cleans up resources from the current level.
	 */
	private void cleanupCurrentLevel() {
		if (currentLevel != null) {
			// Stop the timeline to stop any ongoing animations
			currentLevel.getTimeline().stop();

			// Clear all children from the root to remove the current level's UI elements
			currentLevel.getRoot().getChildren().clear();
		}
	}
}