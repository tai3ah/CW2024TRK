package com.example.demo.actors;

import com.example.demo.ui.LevelViewLevelThree;
import java.util.*;

public class FinalBoss extends FighterPlane {

    private static final String IMAGE_NAME = "finalboss.png";
    private static final double INITIAL_X_POSITION = 1000.0;
    private static final double INITIAL_Y_POSITION = 400;
    private static final double PROJECTILE_Y_POSITION_OFFSET = 75.0;
    private static final double FINAL_BOSS_FIRE_RATE = 0.04;
    private static final int IMAGE_HEIGHT = 75;
    private static final int MOVE_SPEED = 8;
    private static final int HEALTH = 20;
    private static final int MAX_FRAMES_WITH_SAME_MOVE = 10;

    private final List<int[]> movePattern;
    private int consecutiveMovesInSameDirection;
    private int indexOfCurrentMove;

    private LevelViewLevelThree levelView;  // New field for syncing health

    public FinalBoss() {
        super(IMAGE_NAME, IMAGE_HEIGHT, INITIAL_X_POSITION, INITIAL_Y_POSITION, HEALTH);
        movePattern = new ArrayList<>();
        consecutiveMovesInSameDirection = 0;
        indexOfCurrentMove = 0;
        initializeMovePattern();
    }

    // New setter for levelView
    public void setLevelView(LevelViewLevelThree levelView) {
        this.levelView = levelView;
    }

    @Override
    public void updatePosition() {
        double initialX = getTranslateX();
        double initialY = getTranslateY();

        int[] currentMove = getNextMove();
        moveHorizontally(currentMove[0]);
        moveVertically(currentMove[1]);

        double currentX = getLayoutX() + getTranslateX();
        double currentY = getLayoutY() + getTranslateY();

        if (currentX < 0 || currentX > 1300 - IMAGE_HEIGHT) {
            setTranslateX(initialX);
        }
        if (currentY < 0 || currentY > 750 - IMAGE_HEIGHT) {
            setTranslateY(initialY);
        }
    }

    @Override
    public void updateActor() {
        updatePosition();
        if (levelView != null) {
            levelView.updateFinalBossHealth(getHealth());
        }
    }

    @Override
    public GameEntity fireProjectile() {
        if (bossFiresInCurrentFrame()) {
            double projectileX = getLayoutX() + getTranslateX();
            double projectileY = getLayoutY() + getTranslateY() + PROJECTILE_Y_POSITION_OFFSET;
            return new FinalBossProjectile(projectileX, projectileY);
        }
        return null;
    }

    private boolean bossFiresInCurrentFrame() {
        return Math.random() < FINAL_BOSS_FIRE_RATE;
    }

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

    private void initializeMovePattern() {
        int[][] directions = {
                {MOVE_SPEED, 0}, {-MOVE_SPEED, 0},    // Horizontal
                {0, MOVE_SPEED}, {0, -MOVE_SPEED},    // Vertical
                {MOVE_SPEED, MOVE_SPEED}, {-MOVE_SPEED, -MOVE_SPEED},
                {-MOVE_SPEED, MOVE_SPEED}, {MOVE_SPEED, -MOVE_SPEED}
        };
        movePattern.addAll(Arrays.asList(directions));
        Collections.shuffle(movePattern);
    }
}
