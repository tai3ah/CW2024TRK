package com.example.demo.actors;

/**
 * Represents a Drone enemy in the game. The Drone moves horizontally back and forth
 * across the screen and has a probability of dropping bombs while moving.
 */
public class Drone extends FighterPlane {

    // Static constants for the drone's attributes
    private static final String IMAGE_NAME = "drone.png";
    private static final double INITIAL_Y_POSITION = 50;
    private static final double HORIZONTAL_VELOCITY = 5;
    private static final int IMAGE_HEIGHT = 50;
    private static final int HEALTH = 10;
    private static final double FIRE_RATE = 0.02; // Probability of dropping a bomb each frame

    private boolean movingRight;

    /**
     * Constructs a Drone instance with the given initial X position.
     *
     * @param initialXPos The initial X-coordinate of the drone.
     */
    public Drone(double initialXPos) {
        super(IMAGE_NAME, IMAGE_HEIGHT, initialXPos, INITIAL_Y_POSITION, HEALTH);
        this.movingRight = true; // Starts moving to the right by default
    }

    /**
     * Updates the position of the Drone, making it move horizontally.
     * The Drone reverses direction when it reaches the screen boundaries.
     */
    @Override
    public void updatePosition() {
        if (movingRight) {
            moveHorizontally(HORIZONTAL_VELOCITY);
            if (getLayoutX() + getTranslateX() > 1200) { // Example screen boundary width
                movingRight = false; // Change direction to left
            }
        } else {
            moveHorizontally(-HORIZONTAL_VELOCITY);
            if (getLayoutX() + getTranslateX() < 0) { // Screen boundary (left)
                movingRight = true; // Change direction to right
            }
        }
    }

    /**
     * Updates the Drone's state, including its position.
     */
    @Override
    public void updateActor() {
        updatePosition();
    }

    /**
     * Fires a bomb from the Drone with a certain probability.
     *
     * @return A new DroneBomb instance if the Drone decides to drop a bomb; null otherwise.
     */
    @Override
    public GameEntity fireProjectile() {
        return shouldDropBomb()
                ? new DroneBomb(getLayoutX() + getTranslateX() + getFitWidth() / 2,
                getLayoutY() + getTranslateY() + getFitHeight())
                : null;
    }

    /**
     * Determines whether the Drone should drop a bomb during this frame.
     *
     * @return True if the Drone should drop a bomb; false otherwise.
     */
    private boolean shouldDropBomb() {
        return Math.random() < FIRE_RATE;
    }
}
