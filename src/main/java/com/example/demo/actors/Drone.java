package com.example.demo.actors;


public class Drone extends FighterPlane {

    private static final String IMAGE_NAME = "drone.png";
    private static final double INITIAL_Y_POSITION = 50;
    private static final double HORIZONTAL_VELOCITY = 5;
    private static final int IMAGE_HEIGHT = 50;
    private static final int HEALTH = 10;
    private static final double FIRE_RATE = 0.02; // Probability of dropping a bomb each frame

    private boolean movingRight;

    public Drone(double initialXPos) {
        super(IMAGE_NAME, IMAGE_HEIGHT, initialXPos, INITIAL_Y_POSITION, HEALTH);
        this.movingRight = true; // Starts moving to the right
    }

    @Override
    public void updatePosition() {
        if (movingRight) {
            moveHorizontally(HORIZONTAL_VELOCITY);
            if (getLayoutX() + getTranslateX() > 1200) { // Screen boundary (example width)
                movingRight = false;
            }
        } else {
            moveHorizontally(-HORIZONTAL_VELOCITY);
            if (getLayoutX() + getTranslateX() < 0) { // Screen boundary
                movingRight = true;
            }
        }
    }

    @Override
    public void updateActor() {
        updatePosition();
    }

    @Override
    public GameEntity fireProjectile() {
        return shouldDropBomb() ? new DroneBomb(getLayoutX() + getTranslateX() + getFitWidth() / 2, getLayoutY() + getTranslateY() + getFitHeight()) : null;
    }

    private boolean shouldDropBomb() {
        return Math.random() < FIRE_RATE;
    }
}
