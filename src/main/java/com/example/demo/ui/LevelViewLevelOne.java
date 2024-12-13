package com.example.demo.ui;

import javafx.scene.text.Text;
import javafx.scene.layout.Pane;

/**
 * Class for displaying the view for Level One, including heart display and kill count.
 */
public class LevelViewLevelOne {

	private static final double HEART_DISPLAY_X_POSITION = 5;
	private static final double HEART_DISPLAY_Y_POSITION = 25;
	private static final int WIN_IMAGE_X_POSITION = 355;
	private static final int WIN_IMAGE_Y_POSITION = 175;

	private static final double KILL_COUNT_MARGIN = 20;
	private static final int SCREEN_WIDTH = 1300;
	private static final int SCREEN_HEIGHT = 750;

	private final Pane root;
	private final HeartDisplay heartDisplay;
	private final Text killCountText;

	/**
	 * Constructs a LevelViewLevelOne with the specified root pane and initial number of hearts to display.
	 *
	 * @param root the root pane for the level view
	 * @param heartsToDisplay the initial number of hearts to display
	 */
	public LevelViewLevelOne(Pane root, int heartsToDisplay) {
		this.root = root;
		this.heartDisplay = new HeartDisplay(HEART_DISPLAY_X_POSITION, HEART_DISPLAY_Y_POSITION, heartsToDisplay);

		// Initialize kill count display
		this.killCountText = new Text("Kill Count: 0");
		killCountText.setStyle("-fx-font-size: 20px; -fx-fill: white; -fx-font-weight: bold;");
		root.getChildren().add(killCountText);

		updateKillCountPosition();
	}

	/**
	 * Updates the position of the kill count text to ensure it stays within the screen.
	 */
	private void updateKillCountPosition() {
		killCountText.applyCss();
		double textWidth = killCountText.getBoundsInLocal().getWidth();
		double xPosition = SCREEN_WIDTH - textWidth - (4 * KILL_COUNT_MARGIN);

		if (xPosition < KILL_COUNT_MARGIN) {
			xPosition = KILL_COUNT_MARGIN;
		}

		killCountText.setX(xPosition);
		killCountText.setY(KILL_COUNT_MARGIN);
	}

	/**
	 * Displays the heart display on the screen.
	 */
	public void showHeartDisplay() {
		if (!root.getChildren().contains(heartDisplay.getContainer())) {
			root.getChildren().add(heartDisplay.getContainer());
		}
	}

	/**
	 * Removes hearts from the heart display to match the specified number of remaining hearts.
	 *
	 * @param heartsRemaining the number of hearts remaining
	 */
	public void removeHearts(int heartsRemaining) {
		int currentNumberOfHearts = heartDisplay.getContainer().getChildren().size();
		for (int i = 0; i < currentNumberOfHearts - heartsRemaining; i++) {
			heartDisplay.removeHeart();
		}
	}

	/**
	 * Adds hearts to the heart display.
	 *
	 * @param heartsToAdd the number of hearts to add
	 */
	public void addHearts(int heartsToAdd) {
		for (int i = 0; i < heartsToAdd; i++) {
			heartDisplay.addHeart();
		}
	}

	/**
	 * Updates the kill count display with the specified kill count.
	 *
	 * @param killCount the current kill count to display
	 */
	public void updateKillCount(int killCount) {
		killCountText.setText("Kill Count: " + killCount);
		killCountText.toFront();
	}

	/**
	 * Returns the kill count text.
	 *
	 * @return the kill count text
	 */
	public Text getKillCountText() {
		return killCountText;
	}
}
