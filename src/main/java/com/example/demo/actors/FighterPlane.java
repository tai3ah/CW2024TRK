package com.example.demo.actors;

/**
 * Represents a generic fighter plane in the game.
 * This is an abstract class providing basic functionality such as firing projectiles,
 * taking damage, and handling health status.
 */
public abstract class FighterPlane extends GameEntity {

	/**
	 * The current health of the fighter plane.
	 */
	public int health;

	/**
	 * Constructs a FighterPlane instance with specified parameters.
	 *
	 * @param imageName   The file name of the plane's image.
	 * @param imageHeight The height of the plane's image.
	 * @param initialXPos The initial X-coordinate of the plane.
	 * @param initialYPos The initial Y-coordinate of the plane.
	 * @param health      The initial health of the plane.
	 */
	public FighterPlane(String imageName, int imageHeight, double initialXPos, double initialYPos, int health) {
		super(imageName, imageHeight, initialXPos, initialYPos);
		this.health = health;
	}

	/**
	 * Fires a projectile from the fighter plane.
	 * This method is abstract and must be implemented by subclasses.
	 *
	 * @return A projectile fired from the plane.
	 */
	public abstract GameEntity fireProjectile();

	/**
	 * Reduces the plane's health when it takes damage.
	 * If health reaches zero, the plane is destroyed.
	 */
	@Override
	public void takeDamage() {
		health--;
		if (healthAtZero()) {
			this.destroy();
		}
	}

	/**
	 * Calculates the X-coordinate where a projectile should be created.
	 *
	 * @param xPositionOffset The offset from the plane's current X-coordinate.
	 * @return The calculated X-coordinate for the projectile.
	 */
	protected double getProjectileXPosition(double xPositionOffset) {
		return getLayoutX() + getTranslateX() + xPositionOffset;
	}

	/**
	 * Calculates the Y-coordinate where a projectile should be created.
	 *
	 * @param yPositionOffset The offset from the plane's current Y-coordinate.
	 * @return The calculated Y-coordinate for the projectile.
	 */
	protected double getProjectileYPosition(double yPositionOffset) {
		return getLayoutY() + getTranslateY() + yPositionOffset;
	}

	/**
	 * Checks if the fighter plane's health has reached zero.
	 *
	 * @return True if health is zero, otherwise false.
	 */
	private boolean healthAtZero() {
		return health == 0;
	}

	/**
	 * Retrieves the current health of the fighter plane.
	 *
	 * @return The current health value.
	 */
	public int getHealth() {
		return health;
	}
}
