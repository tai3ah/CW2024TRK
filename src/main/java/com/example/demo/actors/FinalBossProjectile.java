package com.example.demo.actors;

/**
 * Represents a projectile fired by the Final Boss in the game.
 * The projectile moves horizontally to the left at a fixed speed.
 */
public class FinalBossProjectile extends Projectile {

    // Static constants defining the projectile's appearance and movement
    private static final String IMAGE_NAME = "finalbossprojectile.png";
    private static final int IMAGE_HEIGHT = 10;
    private static final double PROJECTILE_SPEED = -15;  // Moves leftward

    /**
     * Constructs a FinalBossProjectile with the specified initial position.
     *
     * @param initialXPos The initial X-coordinate of the projectile.
     * @param initialYPos The initial Y-coordinate of the projectile.
     */
    public FinalBossProjectile(double initialXPos, double initialYPos) {
        super(IMAGE_NAME, IMAGE_HEIGHT, initialXPos, initialYPos);
    }

    /**
     * Updates the projectile's position by moving it horizontally to the left.
     */
    @Override
    public void updatePosition() {
        moveHorizontally(PROJECTILE_SPEED);
    }

    /**
     * Updates the state of the projectile, including its position.
     */
    @Override
    public void updateActor() {
        updatePosition();
    }
}
