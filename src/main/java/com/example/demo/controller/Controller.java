package com.example.demo.controller;

//import java.lang.reflect.Constructor;
//import java.lang.reflect.InvocationTargetException;
import java.util.Observable;
import java.util.Observer;

import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import javafx.stage.Stage;
import com.example.demo.LevelParent;
import com.example.demo.builder.LevelBuilder;

public class Controller implements Observer {

	private static final String LEVEL_ONE_CLASS_NAME = "LevelOne";
	private final Stage stage;

	public Controller(Stage stage) {
		this.stage = stage;
	}

	public void launchGame()   {

			stage.show();
			goToLevel(LEVEL_ONE_CLASS_NAME);
	}

	private void goToLevel(String levelName)  {
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
	}

	@Override
	public void update(Observable obervable, Object levelName) {
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
