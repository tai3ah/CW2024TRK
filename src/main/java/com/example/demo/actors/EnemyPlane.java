package com.example.demo.actors;

/**
 * Represents an enemy plane in the game. The enemy plane moves horizontally
 * and has a small probability of firing projectiles.
 */
public class EnemyPlane extends FighterPlane {

	// Static constants for the enemy plane's attributes
	private static final String IMAGE_NAME = "enemyplane.png";
	private static final int IMAGE_HEIGHT = 50;
	private static final int HORIZONTAL_VELOCITY = -6;
	private static final double PROJECTILE_X_POSITION_OFFSET = -100.0;
	private static final double PROJECTILE_Y_POSITION_OFFSET = 50.0;
	private static final int INITIAL_HEALTH = 1;
	private static final double FIRE_RATE = 0.01; // Probability of firing each frame

	/**
	 * Constructs an EnemyPlane with an initial position.
	 *
	 * @param initialXPos The initial X-coordinate of the enemy plane.
	 * @param initialYPos The initial Y-coordinate of the enemy plane.
	 */
	public EnemyPlane(double initialXPos, double initialYPos) {
		super(IMAGE_NAME, IMAGE_HEIGHT, initialXPos, initialYPos, INITIAL_HEALTH);
	}

	/**
	 * Updates the position of the enemy plane by moving it horizontally.
	 */
	@Override
	public void updatePosition() {
		moveHorizontally(HORIZONTAL_VELOCITY);
	}

	/**
	 * Fires a projectile from the enemy plane with a small probability.
	 *
	 * @return A new EnemyProjectile instance if the enemy fires; null otherwise.
	 */
	@Override
	public GameEntity fireProjectile() {
		if (Math.random() < FIRE_RATE) {
			double projectileXPosition = getProjectileXPosition(PROJECTILE_X_POSITION_OFFSET);
			double projectileYPosition = getProjectileYPosition(PROJECTILE_Y_POSITION_OFFSET);
			return new EnemyProjectile(projectileXPosition, projectileYPosition);
		}
		return null;
	}

	/**
	 * Updates the state of the enemy plane, including its position and actions.
	 */
	@Override
	public void updateActor() {
		updatePosition();
	}
}
