package com.example.demo.actors;

/**
 * Represents a generic projectile in the game.
 * Projectiles are entities fired by game characters such as bosses and enemies.
 * This class provides a default implementation for handling damage.
 */
public abstract class Projectile extends GameEntity {

	/**
	 * Constructs a Projectile with the specified image and position.
	 *
	 * @param imageName   The file name of the projectile's image.
	 * @param imageHeight The height of the projectile's image.
	 * @param initialXPos The initial X-coordinate of the projectile.
	 * @param initialYPos The initial Y-coordinate of the projectile.
	 */
	public Projectile(String imageName, int imageHeight, double initialXPos, double initialYPos) {
		super(imageName, imageHeight, initialXPos, initialYPos);
	}

	/**
	 * Handles the logic when the projectile takes damage.
	 * Since projectiles are usually destroyed upon impact, the default behavior
	 * is to mark the projectile as destroyed.
	 */
	@Override
	public void takeDamage() {
		this.destroy();
	}

	/**
	 * Updates the position of the projectile.
	 * Subclasses must implement this method to define specific movement behavior.
	 */
	@Override
	public abstract void updatePosition();
}
