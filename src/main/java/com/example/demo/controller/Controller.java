package com.example.demo.controller;


import java.util.Observable;
import java.util.Observer;

import javafx.scene.Scene;

import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import javafx.stage.Stage;
import com.example.demo.LevelParent;
import com.example.demo.builder.LevelBuilder;


public class Controller implements Observer {



	private LevelParent currentLevel;

	private static final String LEVEL_ONE_CLASS_NAME = "LevelOne";


	private final Stage stage;

	public Controller(Stage stage) {
		this.stage = stage;

	}

	public void launchGame()   {


		stage.show();
		goToLevel(LEVEL_ONE_CLASS_NAME);

	}



	/*private void goToLevel(String levelName)  {
			try{
				LevelParent myLevel = LevelBuilder.createLevel(levelName, stage.getHeight(), stage.getWidth());
				myLevel.addObserver(this);
				Scene scene = myLevel.initializeScene();
				stage.setScene(scene);
				myLevel.startGame();
			} catch (IllegalArgumentException e){
				Alert alert = new Alert(AlertType.ERROR);
				alert.setContentText(e.getMessage());
				alert.show();
			}
	}*/

	private void goToLevel(String levelName) {
		try {
			// Stop any ongoing game activity from the current level
			if (currentLevel != null) {
				currentLevel.stopGame();
			}

			// Clean up any leftover objects from the previous level
			cleanupCurrentLevel();

			// Create the new level using LevelBuilder
			//LevelParent myLevel = LevelBuilder.createLevel(levelName, stage.getHeight(), stage.getWidth());
			LevelParent myLevel = LevelBuilder.createLevel(levelName, stage.getHeight(), stage.getWidth(), stage);
			myLevel.addObserver(this);
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


	private void cleanupCurrentLevel() {
		if (currentLevel != null) {
			// Stop the timeline to stop any ongoing animations
			currentLevel.getTimeline().stop();

			// Clear all children from the root to remove the current level's UI elements
			currentLevel.getRoot().getChildren().clear();
		}
	}



	@Override
	public void update(Observable observable, Object levelName) {
		//goToLevel((String) levelName);
		try {
			if (levelName instanceof String) {
				goToLevel((String) levelName);
			} else {
				System.out.println("Invalid level update received: " + levelName);
			}
		} catch (IllegalArgumentException e) {
			System.out.println("Error transitioning to level: " + e.getMessage());
		}
	}

}
