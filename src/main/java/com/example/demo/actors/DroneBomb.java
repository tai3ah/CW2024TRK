package com.example.demo.actors;

/**
 * Represents a bomb dropped by the Drone in the game.
 * The bomb moves vertically downward at a constant velocity.
 */
class DroneBomb extends Projectile {

    // Static constants for the bomb's attributes
    private static final String IMAGE_NAME = "bomb.png";
    private static final int IMAGE_HEIGHT = 30;
    private static final double VERTICAL_VELOCITY = 10;

    /**
     * Constructs a DroneBomb instance with the given initial position.
     *
     * @param initialXPos The initial X-coordinate of the bomb.
     * @param initialYPos The initial Y-coordinate of the bomb.
     */
    public DroneBomb(double initialXPos, double initialYPos) {
        super(IMAGE_NAME, IMAGE_HEIGHT, initialXPos, initialYPos);
    }

    /**
     * Updates the position of the bomb by moving it vertically downward.
     */
    @Override
    public void updatePosition() {
        moveVertically(VERTICAL_VELOCITY);
    }

    /**
     * Updates the state of the bomb by updating its position.
     */
    @Override
    public void updateActor() {
        updatePosition();
    }
}
