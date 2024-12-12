package com.example.demo.controller;

import javafx.application.Application;
import javafx.stage.Stage;
import com.example.demo.ui.MainMenuPage;

/**
 * The Main class serves as the entry point for the application.
 * It sets up the primary stage and launches the main menu page.
 */
public class Main extends Application {

	/**
	 * The width of the application window.
	 */
	private static final int SCREEN_WIDTH = 1300;

	/**
	 * The height of the application window.
	 */
	private static final int SCREEN_HEIGHT = 750;

	/**
	 * The title of the application window.
	 */
	private static final String TITLE = "Sky Battle";

	/**
	 * Starts the application by setting up the stage and launching the main menu page.
	 *
	 * @param stage the primary stage for this application
	 * @throws SecurityException if a security manager exists and its checkPermission method denies access
	 * @throws IllegalArgumentException if the specified stage is invalid
	 */
	@Override
	public void start(Stage stage) throws SecurityException, IllegalArgumentException {
		stage.setTitle(TITLE);
		stage.setResizable(false);
		stage.setHeight(SCREEN_HEIGHT);
		stage.setWidth(SCREEN_WIDTH);

		// Launch the main menu page
		MainMenuPage mainMenuPage = new MainMenuPage();
		mainMenuPage.start(stage);
	}

	/**
	 * The main method that launches the application.
	 *
	 * @param args the command line arguments
	 */
	public static void main(String[] args) {
		launch();
	}
}