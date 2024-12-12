package com.example.demo.actors;

/**
 * Represents a projectile fired by the Boss in the game.
 * The projectile moves horizontally with a fixed velocity.
 */
public class BossProjectile extends Projectile {

	// Static constants for the projectile's attributes
	private static final String IMAGE_NAME = "fireball.png";
	private static final int IMAGE_HEIGHT = 75;
	private static final int HORIZONTAL_VELOCITY = -15;
	private static final int INITIAL_X_POSITION = 950;

	/**
	 * Constructs a BossProjectile with an initial Y-position.
	 *
	 * @param initialYPos The initial Y-coordinate of the projectile.
	 */
	public BossProjectile(double initialYPos) {
		super(IMAGE_NAME, IMAGE_HEIGHT, INITIAL_X_POSITION, initialYPos);
	}

	/**
	 * Updates the position of the projectile by moving it horizontally.
	 */
	@Override
	public void updatePosition() {
		moveHorizontally(HORIZONTAL_VELOCITY);
	}

	/**
	 * Updates the state of the projectile by calling its position update method.
	 */
	@Override
	public void updateActor() {
		updatePosition();
	}
}
