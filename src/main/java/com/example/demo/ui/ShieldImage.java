package com.example.demo.ui;

import javafx.scene.image.Image;
import javafx.scene.image.ImageView;

/**
 * Class for displaying a shield image, extending the functionality of ImageView.
 */
public class ShieldImage extends ImageView {

	private static final int SHIELD_SIZE = 200;

	/**
	 * Constructs a ShieldImage with the specified position.
	 *
	 * @param xPosition the x-coordinate of the shield image
	 * @param yPosition the y-coordinate of the shield image
	 */
	public ShieldImage(double xPosition, double yPosition) {
		this.setLayoutX(xPosition);
		this.setLayoutY(yPosition);
		this.setImage(new Image(getClass().getResource("/com/example/demo/images/shield.png").toExternalForm()));
		this.setFitHeight(SHIELD_SIZE);
		this.setFitWidth(SHIELD_SIZE);
		this.setVisible(false);
	}

	/**
	 * Shows the shield image.
	 */
	public void showShield() {
		this.setVisible(true);
		System.out.println("Shield is activated (ShieldImage class)");
	}

	/**
	 * Hides the shield image.
	 */
	public void hideShield() {
		this.setVisible(false);
		System.out.println("Shield is deactivated (ShieldImage class)");
	}

	/**
	 * Updates the position of the shield image.
	 *
	 * @param xPosition the new x-coordinate of the shield image
	 * @param yPosition the new y-coordinate of the shield image
	 */
	public void updatePosition(double xPosition, double yPosition) {
		setTranslateX(xPosition);
		setTranslateY(yPosition);
	}
}