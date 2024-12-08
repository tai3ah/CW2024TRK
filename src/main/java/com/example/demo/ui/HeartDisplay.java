/*package com.example.demo.ui;

import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.HBox;

public class HeartDisplay {
	
	public static final String HEART_IMAGE_NAME = "/com/example/demo/images/heart.png";
	public static final int HEART_HEIGHT = 50;
	private static final int INDEX_OF_FIRST_ITEM = 0;
	private HBox container;
	private double containerXPosition;
	private double containerYPosition;
	private int numberOfHeartsToDisplay;
	
	public HeartDisplay(double xPosition, double yPosition, int heartsToDisplay) {
		this.containerXPosition = xPosition;
		this.containerYPosition = yPosition;
		this.numberOfHeartsToDisplay = heartsToDisplay;
		initializeContainer();
		initializeHearts();
	}
	
	private void initializeContainer() {
		container = new HBox();
		container.setLayoutX(containerXPosition);
		container.setLayoutY(containerYPosition);		
	}
	
	/*private void initializeHearts() {
		for (int i = 0; i < numberOfHeartsToDisplay; i++) {
			ImageView heart = new ImageView(new Image(getClass().getResource(HEART_IMAGE_NAME).toExternalForm()));

			heart.setFitHeight(HEART_HEIGHT);
			heart.setPreserveRatio(true);
			container.getChildren().add(heart);
		}
	}

	 */

	/*private void initializeHearts() {
		for (int i = 0; i < numberOfHeartsToDisplay; i++) {
			container.getChildren().add(createHeartImageView());
		}
	}

	// Method to create a heart ImageView
	private ImageView createHeartImageView() {
		Image heartImage = new Image(getClass().getResource(HEART_IMAGE_NAME).toExternalForm());
		ImageView heartImageView = new ImageView(heartImage);
		heartImageView.setFitHeight(HEART_HEIGHT);
		heartImageView.setPreserveRatio(true);
		return heartImageView;
	}

	// Method to add a heart visually
	public void addHeart() {
		javafx.application.Platform.runLater(() -> {
			System.out.println("Adding a heart to HeartDisplay...");
			container.getChildren().clear();  // Clear the existing hearts
			numberOfHeartsToDisplay++;
			initializeHearts();  // Reinitialize the hearts with the updated number of hearts
			container.requestLayout();  // Explicitly request a layout update
		});
	}




	public void removeHeart() {
		if (!container.getChildren().isEmpty())
			container.getChildren().remove(INDEX_OF_FIRST_ITEM);
	}
	
	public HBox getContainer() {
		return container;
	}

}
*/

package com.example.demo.ui;

import javafx.application.Platform;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.HBox;

public class HeartDisplay {

	public static final String HEART_IMAGE_NAME = "/com/example/demo/images/heart.png";
	public static final int HEART_HEIGHT = 50;

	private HBox container;
	private final double containerXPosition;
	private final double containerYPosition;
	private int numberOfHeartsToDisplay;

	public HeartDisplay(double xPosition, double yPosition, int heartsToDisplay) {
		this.containerXPosition = xPosition;
		this.containerYPosition = yPosition;
		this.numberOfHeartsToDisplay = heartsToDisplay;
		initializeContainer();
		initializeHearts();
	}

	private void initializeContainer() {
		container = new HBox();
		container.setLayoutX(containerXPosition);
		container.setLayoutY(containerYPosition);
	}

	private void initializeHearts() {
		container.getChildren().clear();
		for (int i = 0; i < numberOfHeartsToDisplay; i++) {
			container.getChildren().add(createHeartImageView());
		}
	}

	private ImageView createHeartImageView() {
		Image heartImage = new Image(getClass().getResource(HEART_IMAGE_NAME).toExternalForm());
		ImageView heartImageView = new ImageView(heartImage);
		heartImageView.setFitHeight(HEART_HEIGHT);
		heartImageView.setPreserveRatio(true);
		return heartImageView;
	}

	public void addHeart() {
		Platform.runLater(() -> {
			numberOfHeartsToDisplay++;
			initializeHearts(); // Refresh the display with the new number of hearts
		});
	}

	public void removeHeart() {
		Platform.runLater(() -> {
			if (numberOfHeartsToDisplay > 0) {
				numberOfHeartsToDisplay--;
				initializeHearts(); // Refresh the display with the updated number of hearts
			}
		});
	}

	public void updateHearts(int currentHearts) {
		Platform.runLater(() -> {
			container.getChildren().clear(); // Clear existing hearts
			for (int i = 0; i < currentHearts; i++) {
				container.getChildren().add(createHeartImageView());
			}
		});
	}


	public HBox getContainer() {
		return container;
	}
}
