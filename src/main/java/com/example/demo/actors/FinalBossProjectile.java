/*package com.example.demo.actors;

public class FinalBossProjectile extends Projectile {

    private static final String IMAGE_NAME = "finalbossprojectile.png";
    private static final int IMAGE_HEIGHT = 10;
    private static final int HORIZONTAL_VELOCITY = -15;
    private static final int INITIAL_X_POSITION = 950;

    public FinalBossProjectile(double initialYPos) {
        super(IMAGE_NAME, IMAGE_HEIGHT, INITIAL_X_POSITION, initialYPos);
    }

    @Override
    public void updatePosition() {
        moveHorizontally(HORIZONTAL_VELOCITY);
    }

    @Override
    public void updateActor() {
        updatePosition();
    }
}
*/


package com.example.demo.actors;

public class FinalBossProjectile extends Projectile {

    private static final String IMAGE_NAME = "finalbossprojectile.png";
    private static final int IMAGE_HEIGHT = 10;
    private static final double PROJECTILE_SPEED = -15;  // Moving leftward

    // Updated constructor: Accepts X and Y positions
    public FinalBossProjectile(double initialXPos, double initialYPos) {
        super(IMAGE_NAME, IMAGE_HEIGHT, initialXPos, initialYPos);
    }

    @Override
    public void updatePosition() {
        moveHorizontally(PROJECTILE_SPEED);
    }

    @Override
    public void updateActor() {
        updatePosition();
    }
}
