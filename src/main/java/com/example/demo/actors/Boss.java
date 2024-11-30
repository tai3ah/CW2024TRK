package com.example.demo.actors;

import com.example.demo.ui.LevelViewLevelTwo;
import com.example.demo.ui.ShieldImage;
import java.util.*;
//import javafx.application.Platform;

public class Boss extends FighterPlane {

	protected double getProjectileInitialPosition() {
		return getLayoutY() + getTranslateY() + PROJECTILE_Y_POSITION_OFFSET;
	}

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
	private static final int Y_POSITION_UPPER_BOUND = 0;//-100;
	private static final int Y_POSITION_LOWER_BOUND = 475;
	private static final int MAX_FRAMES_WITH_SHIELD = 500;

	private final List<Integer> movePattern;
	private boolean isShielded;
	private int consecutiveMovesInSameDirection;
	private int indexOfCurrentMove;
	private int framesWithShieldActivated;
	private ShieldImage shieldImage;
	private LevelViewLevelTwo levelView;

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

	public ShieldImage getShieldImage() {
		return shieldImage;
	}

	// Setter method to set the level view reference
	public void setLevelView(LevelViewLevelTwo levelView) {
		this.levelView = levelView;
	}

	@Override
	public void updatePosition() {
		double initialTranslateY = getTranslateY();
		moveVertically(getNextMove());
		double currentPosition = getLayoutY() + getTranslateY();

		if (currentPosition < Y_POSITION_UPPER_BOUND || currentPosition > Y_POSITION_LOWER_BOUND) {
			setTranslateY(initialTranslateY);
		} else {
			// Update the shield's position based on the boss's position
			shieldImage.updatePosition(getTranslateX(), getTranslateY());
		}
	}

	@Override
	public void updateActor() {
		updatePosition();
		updateShield();
	}

	@Override
	public GameEntity fireProjectile() {
		return bossFiresInCurrentFrame() ? new BossProjectile(getProjectileInitialPosition()) : null;
	}

	/*@Override
	public void takeDamage() {
		if (!isShielded) {
			super.takeDamage();
			System.out.println("Boss Health: " + getHealth());
		}
	} */

	@Override
	public void takeDamage() {
		if (!isShielded) {
			super.takeDamage();
			int currentHealth = getHealth();
			System.out.println("Boss took damage. Current Health: " + currentHealth); // Debug line

			if (levelView != null) {
				// Pass the current health value to ensure synchronization
				javafx.application.Platform.runLater(() -> levelView.updateBossHealth(currentHealth));
			}
		}
	}


	/*@Override
	public void takeDamage() {
		if (!isShielded) {
			super.takeDamage();
			System.out.println("Boss Health: " + getHealth());
			if (levelView != null) {
				Platform.runLater(() -> levelView.updateBossHealth(getHealth())); // Ensure UI update happens on JavaFX thread
			}
		}
	} */

	private void initializeMovePattern() {
		for (int i = 0; i < MOVE_FREQUENCY_PER_CYCLE; i++) {
			movePattern.add(VERTICAL_VELOCITY);
			movePattern.add(-VERTICAL_VELOCITY);
			movePattern.add(ZERO);
		}
		Collections.shuffle(movePattern);
	}

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

	private boolean bossFiresInCurrentFrame() {
		return Math.random() < BOSS_FIRE_RATE;
	}

	private boolean shieldShouldBeActivated() {
		return Math.random() < BOSS_SHIELD_PROBABILITY;
	}

	private boolean shieldExhausted() {
		return framesWithShieldActivated == MAX_FRAMES_WITH_SHIELD;
	}

	private void activateShield() {
		isShielded = true;
		shieldImage.showShield();
	}

	private void deactivateShield() {
		isShielded = false;
		framesWithShieldActivated = 0;
		shieldImage.hideShield();
	}

	public boolean isShielded() {
		return isShielded;
	}
}
