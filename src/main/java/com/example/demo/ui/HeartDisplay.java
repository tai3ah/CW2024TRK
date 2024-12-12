package com.example.demo.ui;

import javafx.application.Platform;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.HBox;

/**
 * Class for displaying a series of heart images, typically used to represent lives or health in a game.
 */
public class HeartDisplay {

	public static final String HEART_IMAGE_NAME = "/com/example/demo/images/heart.png";
	public static final int HEART_HEIGHT = 50;

	private HBox container;
	private final double containerXPosition;
	private final double containerYPosition;
	private int numberOfHeartsToDisplay;

	/**
	 * Constructs a HeartDisplay with the specified position and number of hearts to display.
	 *
	 * @param xPosition the x-coordinate of the heart display container
	 * @param yPosition the y-coordinate of the heart display container
	 * @param heartsToDisplay the initial number of hearts to display
	 */
	public HeartDisplay(double xPosition, double yPosition, int heartsToDisplay) {
		this.containerXPosition = xPosition;
		this.containerYPosition = yPosition;
		this.numberOfHeartsToDisplay = heartsToDisplay;
		initializeContainer();
		initializeHearts();
	}

	/**
	 * Initializes the container for the heart images.
	 */
	private void initializeContainer() {
		container = new HBox();
		container.setLayoutX(containerXPosition);
		container.setLayoutY(containerYPosition);
	}

	/**
	 * Initializes the heart images in the container.
	 */
	private void initializeHearts() {
		container.getChildren().clear();
		for (int i = 0; i < numberOfHeartsToDisplay; i++) {
			container.getChildren().add(createHeartImageView());
		}
	}

	/**
	 * Creates an ImageView for a heart image.
	 *
	 * @return the created ImageView for the heart image
	 */
	private ImageView createHeartImageView() {
		Image heartImage = new Image(getClass().getResource(HEART_IMAGE_NAME).toExternalForm());
		ImageView heartImageView = new ImageView(heartImage);
		heartImageView.setFitHeight(HEART_HEIGHT);
		heartImageView.setPreserveRatio(true);
		return heartImageView;
	}

	/**
	 * Adds a heart to the display.
	 */
	public void addHeart() {
		Platform.runLater(() -> {
			numberOfHeartsToDisplay++;
			initializeHearts(); // Refresh the display with the new number of hearts
		});
	}

	/**
	 * Removes a heart from the display.
	 */
	public void removeHeart() {
		Platform.runLater(() -> {
			if (numberOfHeartsToDisplay > 0) {
				numberOfHeartsToDisplay--;
				initializeHearts(); // Refresh the display with the updated number of hearts
			}
		});
	}

	/**
	 * Updates the display to show the specified number of hearts.
	 *
	 * @param currentHearts the number of hearts to display
	 */
	public void updateHearts(int currentHearts) {
		Platform.runLater(() -> {
			container.getChildren().clear(); // Clear existing hearts
			for (int i = 0; i < currentHearts; i++) {
				container.getChildren().add(createHeartImageView());
			}
		});
	}

	/**
	 * Returns the container holding the heart images.
	 *
	 * @return the HBox container
	 */
	public HBox getContainer() {
		return container;
	}
}