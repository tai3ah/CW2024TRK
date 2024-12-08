package com.example.demo.ui;

import javafx.scene.text.Text;
import javafx.scene.layout.Pane;

public class LevelViewLevelOne {

	private static final double HEART_DISPLAY_X_POSITION = 5;
	private static final double HEART_DISPLAY_Y_POSITION = 25;
	private static final int WIN_IMAGE_X_POSITION = 355;
	private static final int WIN_IMAGE_Y_POSITION = 175;
	//private static final int LOSS_SCREEN_X_POSITION = -160;
	//private static final int LOSS_SCREEN_Y_POSITION = -375;

	private static final double KILL_COUNT_MARGIN = 20; // Margin from the edge
	//private static final double KILL_COUNT_X_POSITION = 10;
	//private static final double KILL_COUNT_Y_POSITION = 60;
	// Fixed screen dimensions
	private static final int SCREEN_WIDTH = 1300;
	private static final int SCREEN_HEIGHT = 750;

	private final Pane root;
	private final WinImage winImage;
	private final GameOverImage gameOverImage;
	final HeartDisplay heartDisplay;
	private final Text killCountText; // Kill count display

	public LevelViewLevelOne(Pane root, int heartsToDisplay) {
		this.root = root;
		this.heartDisplay = new HeartDisplay(HEART_DISPLAY_X_POSITION, HEART_DISPLAY_Y_POSITION, heartsToDisplay);
		this.winImage = new WinImage(WIN_IMAGE_X_POSITION, WIN_IMAGE_Y_POSITION);
		//this.gameOverImage = new GameOverImage(LOSS_SCREEN_X_POSITION, LOSS_SCREEN_Y_POSITION);
		this.gameOverImage = new GameOverImage();

		// Initialize kill count display
		this.killCountText = new Text("Kill Count: 0");
		killCountText.setStyle("-fx-font-size: 20px; -fx-fill: white; -fx-font-weight: bold;");
		root.getChildren().add(killCountText); // Add kill count text to the root

		updateKillCountPosition();

	}

	//added
	private void updateKillCountPosition() {
		// First set the text to make sure the bounds are updated
		killCountText.applyCss(); // Apply CSS to make sure we get an accurate size
		double textWidth = killCountText.getBoundsInLocal().getWidth();

		// Calculate the X position to ensure it stays within the screen
		double xPosition = SCREEN_WIDTH - textWidth - (4 * KILL_COUNT_MARGIN); // Increased margin for safety

		// Ensure the X position is not too close to the left edge
		if (xPosition < KILL_COUNT_MARGIN) {
			xPosition = KILL_COUNT_MARGIN;
		}

		// Set positions explicitly
		killCountText.setX(xPosition);
		killCountText.setY(KILL_COUNT_MARGIN); // A fixed margin from the top
	}

	public void showHeartDisplay() {
		root.getChildren().add(heartDisplay.getContainer());
	}

	public void showWinImage() {
		if (!root.getChildren().contains(winImage)) { // Check if winImage is already added
			root.getChildren().add(winImage);        // Add only if it's not already present
		}
		winImage.showWinImage();
	}

	public void showGameOverImage() {
		if (!root.getChildren().contains(gameOverImage)) { // Ensure no duplicate addition
			root.getChildren().add(gameOverImage);
		}
		gameOverImage.toFront(); // Bring the game over image to the front
	}


	public void removeHearts(int heartsRemaining) {
		int currentNumberOfHearts = heartDisplay.getContainer().getChildren().size();
		for (int i = 0; i < currentNumberOfHearts - heartsRemaining; i++) {
			heartDisplay.removeHeart();
		}
	}


	/*public void addHearts(int heartsToAdd) {
		for (int i = 0; i < heartsToAdd; i++) {
			heartDisplay.addHeart();
		}
	}
*/
	public void updateHearts(int currentHearts) {
		heartDisplay.updateHearts(currentHearts);
	}

	public void addHearts(int heartsToAdd) {
		for (int i = 0; i < heartsToAdd; i++) {
			heartDisplay.addHeart();
		}
	}



	// Method to update the kill count display
	public void updateKillCount(int killCount) {
		killCountText.setText("Kill Count: " + killCount);
		killCountText.toFront();
	}

	public Text getKillCountText() {
		return killCountText;
	}

}
