package com.example.demo.actors;

/**
 * Represents a projectile fired by the player's plane in the game.
 * The projectile moves horizontally to the right at a constant speed.
 */
public class UserProjectile extends Projectile {

	// Static constants for projectile attributes
	private static final String IMAGE_NAME = "userfire.png";
	private static final int IMAGE_HEIGHT = 10;
	private static final int HORIZONTAL_VELOCITY = 15;

	/**
	 * Constructs a UserProjectile with the specified initial position.
	 *
	 * @param initialXPos The initial X-coordinate of the projectile.
	 * @param initialYPos The initial Y-coordinate of the projectile.
	 */
	public UserProjectile(double initialXPos, double initialYPos) {
		super(IMAGE_NAME, IMAGE_HEIGHT, initialXPos, initialYPos);
	}

	/**
	 * Updates the projectile's position by moving it horizontally to the right.
	 */
	@Override
	public void updatePosition() {
		moveHorizontally(HORIZONTAL_VELOCITY);
	}

	/**
	 * Updates the projectile's state, including its position.
	 */
	@Override
	public void updateActor() {
		updatePosition();
	}
}

