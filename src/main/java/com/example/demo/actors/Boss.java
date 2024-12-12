package com.example.demo.actors;

import com.example.demo.ui.LevelViewLevelTwo;
import com.example.demo.ui.ShieldImage;
import java.util.*;

/**
 * Represents the Boss enemy in the game. The Boss is a type of FighterPlane
 * with specific behaviors, such as firing projectiles, moving vertically,
 * and activating a shield for protection.
 */
public class Boss extends FighterPlane {

	/**
	 * Gets the initial Y-position of the boss's projectile.
	 *
	 * @return The Y-coordinate where the projectile starts.
	 */
	protected double getProjectileInitialPosition() {
		return getLayoutY() + getTranslateY() + PROJECTILE_Y_POSITION_OFFSET;
	}

	// Static constants for the boss's attributes
	private static final String IMAGE_NAME = "bossplane.png";
	private static final double INITIAL_X_POSITION = 1000.0;
	private static final double INITIAL_Y_POSITION = 400;
	private static final double PROJECTILE_Y_POSITION_OFFSET = 75.0;
	private static final double BOSS_FIRE_RATE = 0.04;
	private static final double BOSS_SHIELD_PROBABILITY = 0.002;
	private static final int IMAGE_HEIGHT = 75;
	private static final int VERTICAL_VELOCITY = 8;
	private static final int HEALTH = 20;
	private static final int MOVE_FREQUENCY_PER_CYCLE = 5;
	private static final int ZERO = 0;
	private static final int MAX_FRAMES_WITH_SAME_MOVE = 10;
	private static final int Y_POSITION_UPPER_BOUND = 0; // -100;
	private static final int Y_POSITION_LOWER_BOUND = 475;
	private static final int MAX_FRAMES_WITH_SHIELD = 500;

	private final List<Integer> movePattern;
	private boolean isShielded;
	private int consecutiveMovesInSameDirection;
	private int indexOfCurrentMove;
	private int framesWithShieldActivated;
	private ShieldImage shieldImage;
	private LevelViewLevelTwo levelView;

	/**
	 * Constructs a Boss instance with its initial settings.
	 */
	public Boss() {
		super(IMAGE_NAME, IMAGE_HEIGHT, INITIAL_X_POSITION, INITIAL_Y_POSITION, HEALTH);
		movePattern = new ArrayList<>();
		consecutiveMovesInSameDirection = 0;
		indexOfCurrentMove = 0;
		framesWithShieldActivated = 0;
		isShielded = false;

		shieldImage = new ShieldImage(INITIAL_X_POSITION, INITIAL_Y_POSITION);
		initializeMovePattern();
	}

	/**
	 * Gets the ShieldImage associated with the Boss.
	 *
	 * @return The ShieldImage object representing the shield.
	 */
	public ShieldImage getShieldImage() {
		return shieldImage;
	}

	/**
	 * Sets the reference to the LevelViewLevelTwo instance to allow updating the boss's health in the UI.
	 *
	 * @param levelView The LevelViewLevelTwo instance managing the level.
	 */
	public void setLevelView(LevelViewLevelTwo levelView) {
		this.levelView = levelView;
	}

	/**
	 * Updates the position of the Boss, ensuring it remains within bounds.
	 * Also updates the shield's position to follow the Boss.
	 */
	@Override
	public void updatePosition() {
		double initialTranslateY = getTranslateY();
		moveVertically(getNextMove());
		double currentPosition = getLayoutY() + getTranslateY();

		if (currentPosition < Y_POSITION_UPPER_BOUND || currentPosition > Y_POSITION_LOWER_BOUND) {
			setTranslateY(initialTranslateY);
		} else {
			shieldImage.updatePosition(getTranslateX(), getTranslateY());
		}
	}

	/**
	 * Updates the Boss, including its position and shield status.
	 */
	@Override
	public void updateActor() {
		updatePosition();
		updateShield();
	}

	/**
	 * Fires a projectile from the Boss, based on a probability.
	 *
	 * @return A new BossProjectile instance if the Boss fires; null otherwise.
	 */
	@Override
	public GameEntity fireProjectile() {
		return bossFiresInCurrentFrame() ? new BossProjectile(getProjectileInitialPosition()) : null;
	}

	/**
	 * Handles the Boss taking damage. If the shield is active, damage is not taken.
	 * Updates the boss's health in the UI if linked to a LevelView.
	 */
	@Override
	public void takeDamage() {
		if (!isShielded) {
			super.takeDamage();
			int currentHealth = getHealth();
			System.out.println("Boss took damage. Current Health: " + currentHealth);

			if (levelView != null) {
				javafx.application.Platform.runLater(() -> levelView.updateBossHealth(currentHealth));
			}
		}
	}

	/**
	 * Initializes the move pattern for the Boss.
	 * The pattern is a shuffled combination of upward, downward, and stationary movements.
	 */
	private void initializeMovePattern() {
		for (int i = 0; i < MOVE_FREQUENCY_PER_CYCLE; i++) {
			movePattern.add(VERTICAL_VELOCITY);
			movePattern.add(-VERTICAL_VELOCITY);
			movePattern.add(ZERO);
		}
		Collections.shuffle(movePattern);
	}

	/**
	 * Updates the shield's status. Activates or deactivates the shield based on conditions.
	 */
	private void updateShield() {
		if (isShielded) {
			framesWithShieldActivated++;
		} else if (shieldShouldBeActivated()) {
			activateShield();
		}
		if (shieldExhausted()) {
			deactivateShield();
		}
	}

	/**
	 * Retrieves the next movement for the Boss from the move pattern.
	 *
	 * @return The next move (velocity) for the Boss.
	 */
	private int getNextMove() {
		int currentMove = movePattern.get(indexOfCurrentMove);
		consecutiveMovesInSameDirection++;
		if (consecutiveMovesInSameDirection == MAX_FRAMES_WITH_SAME_MOVE) {
			Collections.shuffle(movePattern);
			consecutiveMovesInSameDirection = 0;
			indexOfCurrentMove++;
		}
		if (indexOfCurrentMove == movePattern.size()) {
			indexOfCurrentMove = 0;
		}
		return currentMove;
	}

	/**
	 * Determines if the Boss should fire a projectile in the current frame.
	 *
	 * @return True if the Boss fires; false otherwise.
	 */
	private boolean bossFiresInCurrentFrame() {
		return Math.random() < BOSS_FIRE_RATE;
	}

	/**
	 * Determines if the shield should be activated based on probability.
	 *
	 * @return True if the shield should be activated; false otherwise.
	 */
	private boolean shieldShouldBeActivated() {
		return Math.random() < BOSS_SHIELD_PROBABILITY;
	}

	/**
	 * Determines if the shield has been active for the maximum allowed frames.
	 *
	 * @return True if the shield is exhausted; false otherwise.
	 */
	private boolean shieldExhausted() {
		return framesWithShieldActivated == MAX_FRAMES_WITH_SHIELD;
	}

	/**
	 * Activates the Boss's shield and displays it.
	 */
	private void activateShield() {
		isShielded = true;
		shieldImage.showShield();
	}

	/**
	 * Deactivates the Boss's shield and hides it.
	 */
	private void deactivateShield() {
		isShielded = false;
		framesWithShieldActivated = 0;
		shieldImage.hideShield();
	}

	/**
	 * Checks if the Boss's shield is currently active.
	 *
	 * @return True if the shield is active; false otherwise.
	 */
	public boolean isShielded() {
		return isShielded;
	}
}
