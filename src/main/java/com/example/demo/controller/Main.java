package com.example.demo.controller;

import javafx.application.Application;
import javafx.stage.Stage;
import com.example.demo.MainMenuPage;

public class Main extends Application {

	private static final int SCREEN_WIDTH = 1300;
	private static final int SCREEN_HEIGHT = 750;
	private static final String TITLE = "Sky Battle";
	//private Controller myController;

	@Override
	//removed unused exception throws
	public void start(Stage stage) throws  SecurityException, IllegalArgumentException {
		stage.setTitle(TITLE);
		//was false making true to test
		stage.setResizable(false);
		stage.setHeight(SCREEN_HEIGHT);
		stage.setWidth(SCREEN_WIDTH);

		// Launch the main menu page
		MainMenuPage mainMenuPage = new MainMenuPage();
		mainMenuPage.start(stage);


	}


	public static void main(String[] args) {
		launch();
	}
}