package com.example.demo.actors;

import com.example.demo.ui.LevelViewLevelThree;
import java.util.*;

/**
 * Represents the Final Boss enemy in the game.
 * The Final Boss can move in multiple directions and fire projectiles at the player.
 * Its health is synchronized with the level's UI.
 */
public class FinalBoss extends FighterPlane {

    // Static constants for the final boss's attributes
    private static final String IMAGE_NAME = "finalboss.png";
    private static final double INITIAL_X_POSITION = 1000.0;
    private static final double INITIAL_Y_POSITION = 400;
    private static final double PROJECTILE_Y_POSITION_OFFSET = 75.0;
    private static final double FINAL_BOSS_FIRE_RATE = 0.04;
    private static final int IMAGE_HEIGHT = 75;
    private static final int MOVE_SPEED = 8;
    private static final int HEALTH = 20;
    private static final int MAX_FRAMES_WITH_SAME_MOVE = 10;

    private static final double X_LEFT_BOUND = 0;
    private static final double X_RIGHT_BOUND = 1250;
    private static final double Y_UPPER_BOUND = 0;
    private static final double Y_LOWER_BOUND = 650;

    private final List<int[]> movePattern;
    private int consecutiveMovesInSameDirection;
    private int indexOfCurrentMove;

    // Reference to the level's UI for syncing health
    private LevelViewLevelThree levelView;

    /**
     * Constructs a FinalBoss instance with its initial settings.
     */
    public FinalBoss() {
        super(IMAGE_NAME, IMAGE_HEIGHT, INITIAL_X_POSITION, INITIAL_Y_POSITION, HEALTH);
        movePattern = new ArrayList<>();
        consecutiveMovesInSameDirection = 0;
        indexOfCurrentMove = 0;
        initializeMovePattern();
    }

    /**
     * Sets the reference to the level's view for updating health on the UI.
     *
     * @param levelView The instance of LevelViewLevelThree managing the level's UI.
     */
    public void setLevelView(LevelViewLevelThree levelView) {
        this.levelView = levelView;
    }

    /**
     * Updates the position of the Final Boss based on its movement pattern.
     * Ensures the boss stays within screen boundaries.
     */
    @Override
    public void updatePosition() {
        double initialX = getTranslateX();
        double initialY = getTranslateY();

        int[] currentMove = getNextMove();
        moveHorizontally(currentMove[0]);
        moveVertically(currentMove[1]);

        double currentX = getLayoutX() + getTranslateX();
        double currentY = getLayoutY() + getTranslateY();

        if (currentX < X_LEFT_BOUND || currentX > X_RIGHT_BOUND) {
            setTranslateX(initialX); // Revert horizontal movement if out of bounds
        }
        if (currentY < Y_UPPER_BOUND || currentY > Y_LOWER_BOUND) {
            setTranslateY(initialY); // Revert vertical movement if out of bounds
        }
    }

    /**
     * Updates the state of the Final Boss, including position and health display.
     */
    @Override
    public void updateActor() {
        updatePosition();
        if (levelView != null) {
            levelView.updateFinalBossHealth(getHealth());
        }
    }

    /**
     * Fires a projectile from the Final Boss if the firing condition is met.
     *
     * @return A new instance of FinalBossProjectile if fired; otherwise, null.
     */
    @Override
    public GameEntity fireProjectile() {
        if (bossFiresInCurrentFrame()) {
            double projectileX = getLayoutX() + getTranslateX();
            double projectileY = getLayoutY() + getTranslateY() + PROJECTILE_Y_POSITION_OFFSET;
            return new FinalBossProjectile(projectileX, projectileY);
        }
        return null;
    }

    /**
     * Determines whether the Final Boss should fire a projectile this frame.
     *
     * @return True if the boss fires; otherwise, false.
     */
    private boolean bossFiresInCurrentFrame() {
        return Math.random() < FINAL_BOSS_FIRE_RATE;
    }

    /**
     * Retrieves the next movement direction for the Final Boss.
     * The move pattern is reshuffled after a specific number of frames.
     *
     * @return The next move direction as an array of X and Y velocities.
     */
    private int[] getNextMove() {
        int[] currentMove = movePattern.get(indexOfCurrentMove);
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
     * Initializes the movement pattern for the Final Boss.
     * The pattern includes diagonal, horizontal, and vertical movements.
     */
    private void initializeMovePattern() {
        int[][] directions = {
                {MOVE_SPEED, 0}, {-MOVE_SPEED, 0},      // Horizontal
                {0, MOVE_SPEED}, {0, -MOVE_SPEED},      // Vertical
                {MOVE_SPEED, MOVE_SPEED}, {-MOVE_SPEED, -MOVE_SPEED}, // Diagonal
                {-MOVE_SPEED, MOVE_SPEED}, {MOVE_SPEED, -MOVE_SPEED}
        };
        movePattern.addAll(Arrays.asList(directions));
        Collections.shuffle(movePattern);
    }
}
