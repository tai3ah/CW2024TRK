package com.example.demo.actors;

/**
 * Represents the player's plane in the game.
 * The UserPlane can move vertically and horizontally, fire projectiles,
 * and track its health and number of kills.
 */
public class UserPlane extends FighterPlane {

	// Static constants for the plane's attributes
	private static final String IMAGE_NAME = "userplane.png";
	private static final double Y_UPPER_BOUND = 0;
	private static final double Y_LOWER_BOUND = 650.0;
	private static final double X_LEFT_BOUND = 0;
	private static final double X_RIGHT_BOUND = 1250.0;
	private static final double INITIAL_X_POSITION = 5.0;
	private static final double INITIAL_Y_POSITION = 300.0;
	private static final int IMAGE_HEIGHT = 50;
	private static final int VERTICAL_VELOCITY = 8;
	private static final int PROJECTILE_Y_POSITION_OFFSET = 20;

	// Controls movement and tracks kills
	private int velocityMultiplier;
	private int numberOfKills;

	/**
	 * Constructs a UserPlane with the specified initial health.
	 *
	 * @param initialHealth The initial health value for the player's plane.
	 */
	public UserPlane(int initialHealth) {
		super(IMAGE_NAME, IMAGE_HEIGHT, INITIAL_X_POSITION, INITIAL_Y_POSITION, initialHealth);
		velocityMultiplier = 0;
	}

	/**
	 * Updates the position of the UserPlane based on its movement state.
	 * Ensures the plane stays within allowed vertical boundaries.
	 */
	@Override
	public void updatePosition() {
		if (isMoving()) {
			double initialTranslateY = getTranslateY();
			this.moveVertically(VERTICAL_VELOCITY * velocityMultiplier);
			double newPosition = getLayoutY() + getTranslateY();

			// Check boundaries
			if (newPosition < Y_UPPER_BOUND || newPosition > Y_LOWER_BOUND) {
				this.setTranslateY(initialTranslateY); // Revert movement if out of bounds
			}
		}
	}

	/**
	 * Updates the state of the UserPlane.
	 * This includes movement logic.
	 */
	@Override
	public void updateActor() {
		updatePosition();
	}

	/**
	 * Fires a projectile from the UserPlane's current position.
	 *
	 * @return A new instance of {@link UserProjectile}.
	 */
	@Override
	public GameEntity fireProjectile() {
		return new UserProjectile(
				getTranslateX() + getBoundsInParent().getWidth() / 2,
				getProjectileYPosition(PROJECTILE_Y_POSITION_OFFSET)
		);
	}

	/**
	 * Checks whether the UserPlane is currently moving vertically.
	 *
	 * @return True if the plane is moving; otherwise, false.
	 */
	private boolean isMoving() {
		return velocityMultiplier != 0;
	}

	/**
	 * Moves the UserPlane upward by setting the vertical velocity to -1.
	 */
	public void moveUp() {
		velocityMultiplier = -1;
	}

	/**
	 * Moves the UserPlane downward by setting the vertical velocity to 1.
	 */
	public void moveDown() {
		velocityMultiplier = 1;
	}

	/**
	 * Moves the UserPlane leftward by decreasing its X-coordinate.
	 */
	public void moveLeft() {
		//setTranslateX(getTranslateX() - 10);
		double newXPosition = getTranslateX() - 10;
		if (newXPosition >= X_LEFT_BOUND) {
			setTranslateX(newXPosition);
		}
	}

	/**
	 * Moves the UserPlane rightward by increasing its X-coordinate.
	 */
	public void moveRight() {
		//setTranslateX(getTranslateX() + 10);
		double newXPosition = getTranslateX() + 10;
		if (newXPosition + getBoundsInParent().getWidth() <= X_RIGHT_BOUND) {
			setTranslateX(newXPosition);
		}
	}

	/**
	 * Stops the UserPlane's vertical movement by resetting the velocity multiplier.
	 */
	public void stop() {
		velocityMultiplier = 0;
	}

	/**
	 * Retrieves the number of kills the player has achieved.
	 *
	 * @return The current kill count.
	 */
	public int getNumberOfKills() {
		return numberOfKills;
	}

	/**
	 * Increments the player's kill count by one.
	 */
	public void incrementKillCount() {
		numberOfKills++;
	}

	/**
	 * Increases the player's health by a specified amount.
	 *
	 * @param amount The amount of health to add.
	 */
	public void increaseHealth(int amount) {
		this.health += amount;
		System.out.println("User health increased. Current health: " + this.health);
	}

	/**
	 * Retrieves the player's current health.
	 *
	 * @return The current health value.
	 */
	@Override
	public int getHealth() {
		return health;
	}
}
