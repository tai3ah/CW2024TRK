package com.example.demo.actors;

import javafx.scene.image.Image;
import javafx.scene.image.ImageView;

/**
 * Represents a game entity in the game, extending JavaFX's ImageView for graphical display.
 * This class provides foundational properties and behaviors like movement, destruction,
 * and graphical representation.
 */
public abstract class GameEntity extends ImageView {

	// Static constant for the image file location path
	private static final String IMAGE_LOCATION = "/com/example/demo/images/";

	// Tracks whether the entity has been destroyed
	private boolean isDestroyed;

	/**
	 * Constructs a GameEntity with the specified properties.
	 *
	 * @param imageName   The file name of the entity's image.
	 * @param imageHeight The height of the entity's image.
	 * @param initialXPos The initial X-coordinate of the entity.
	 * @param initialYPos The initial Y-coordinate of the entity.
	 */
	public GameEntity(String imageName, int imageHeight, double initialXPos, double initialYPos) {
		this.setImage(new Image(getClass().getResource(IMAGE_LOCATION + imageName).toExternalForm()));
		this.setLayoutX(initialXPos);
		this.setLayoutY(initialYPos);
		this.setFitHeight(imageHeight);
		this.setPreserveRatio(true);
		this.isDestroyed = false;
	}

	/**
	 * Abstract method to update the entity's position.
	 * Subclasses must implement this method to define their movement logic.
	 */
	public abstract void updatePosition();

	/**
	 * Abstract method to update the entity's state.
	 * Subclasses must implement this method to define how the entity behaves.
	 */
	public abstract void updateActor();

	/**
	 * Handles the logic when the entity takes damage.
	 * This method can be overridden by subclasses for custom damage logic.
	 */
	public void takeDamage() {
		// Implementation for taking damage (default is no action).
	}

	/**
	 * Marks the entity as destroyed.
	 * Calls the {@link #setDestroyed(boolean)} method with true.
	 */
	public void destroy() {
		setDestroyed(true);
	}

	/**
	 * Sets whether the entity is destroyed.
	 *
	 * @param isDestroyed True if the entity is destroyed; otherwise, false.
	 */
	protected void setDestroyed(boolean isDestroyed) {
		this.isDestroyed = isDestroyed;
	}

	/**
	 * Checks whether the entity is destroyed.
	 *
	 * @return True if the entity is destroyed; otherwise, false.
	 */
	public boolean isDestroyed() {
		return isDestroyed;
	}

	/**
	 * Moves the entity horizontally by a specified amount.
	 *
	 * @param horizontalMove The amount to move along the X-axis.
	 */
	protected void moveHorizontally(double horizontalMove) {
		this.setTranslateX(getTranslateX() + horizontalMove);
	}

	/**
	 * Moves the entity vertically by a specified amount.
	 *
	 * @param verticalMove The amount to move along the Y-axis.
	 */
	protected void moveVertically(double verticalMove) {
		this.setTranslateY(getTranslateY() + verticalMove);
	}
}
