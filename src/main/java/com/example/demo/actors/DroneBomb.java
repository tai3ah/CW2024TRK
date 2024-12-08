package com.example.demo.actors;

class DroneBomb extends Projectile {

    private static final String IMAGE_NAME = "bomb.png";
    private static final int IMAGE_HEIGHT = 30;
    private static final double VERTICAL_VELOCITY = 10;

    public DroneBomb(double initialXPos, double initialYPos) {
        super(IMAGE_NAME, IMAGE_HEIGHT, initialXPos, initialYPos);
    }

    @Override
    public void updatePosition() {
        moveVertically(VERTICAL_VELOCITY);
    }

    @Override
    public void updateActor() {
        updatePosition();
    }
}
