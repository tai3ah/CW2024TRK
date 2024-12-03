package com.example.demo.levels;

import com.example.demo.actors.*;
//import com.example.demo.factories.BombFactory;
import com.example.demo.factories.BossPlaneFactory;
import com.example.demo.factories.EnemyPlaneFactory;
import com.example.demo.ui.LevelView;
import com.example.demo.ui.LevelViewLevelTwo;
//import javafx.animation.AnimationTimer;
import javafx.stage.Stage;

//import java.util.ArrayList;
//import java.util.List;

public class LevelThree extends LevelParent {

    private static final String BACKGROUND_IMAGE_NAME = "/com/example/demo/images/background3.png";
    private static final int PLAYER_INITIAL_HEALTH = 5;
    private static final BossPlaneFactory bossFactory = new BossPlaneFactory();
    private static final EnemyPlaneFactory enemyFactory = new EnemyPlaneFactory();
    private Boss boss;
    private static final int KILLS_TO_ADVANCE = 10;
    private static final double ENEMY_SPAWN_PROBABILITY = .20;
    private static final int TOTAL_ENEMIES = 3;
    //  private static final BombFactory bombFactory = new BombFactory();
    private static final int MAX_BOMBS = 3;
    private static final double BOMB_SPAWN_PROBABILITY = 0.01;
    //  private List<Bomb> bombs = new ArrayList<>();

    public LevelThree(double screenHeight, double screenWidth, Stage primaryStage) {
        super(BACKGROUND_IMAGE_NAME, screenHeight, screenWidth, PLAYER_INITIAL_HEALTH, primaryStage);
        initializeBoss();
        //  startBombAnimation();
    }

    private void initializeBoss() {
        if (boss == null) {
            boss = bossFactory.createEnemy(1000, 400); // Create boss using factory
        }
    }

   /* private void startBombAnimation() {
        AnimationTimer bombMovementTimer = new AnimationTimer() {
            @Override
            public void handle(long now) {
                for (Bomb bomb : bombs) {
                    bomb.updatePosition(); // Move each bomb downwards
                }
            }
        };
        bombMovementTimer.start();
    }
*/

    @Override
    protected void initializeFriendlyUnits() {
        getRoot().getChildren().add(getUser());
        initializeBoss();
        if (boss != null) {
            getRoot().getChildren().add(boss.getShieldImage());
        }
    }

    @Override
    protected void checkIfGameOver() {
        if (userIsDestroyed()) {
            loseGame();
        } else if (boss != null && boss.isDestroyed() && userHasReachedKillTarget()) {
            winGame();
        }
    }

    @Override
    protected void spawnEnemyUnits() {
        if (boss != null && getCurrentNumberOfEnemies() == 0) {
            addEnemyUnit(boss);
        }
        //spawnBombs();
        int currentNumberOfEnemies = getCurrentNumberOfEnemies();
        for (int i = 0; i < TOTAL_ENEMIES - currentNumberOfEnemies; i++) {
            if (Math.random() < ENEMY_SPAWN_PROBABILITY) {
                double newEnemyInitialYPosition = Math.random() * getEnemyMaximumYPosition();
                GameEntity newEnemy = enemyFactory.createEnemy(getScreenWidth(), newEnemyInitialYPosition);
                addEnemyUnit(newEnemy);
            }
        }
    }
/*
    private void spawnBombs() {
        if (Math.random() < BOMB_SPAWN_PROBABILITY) {
            double initialX = Math.random() * getScreenWidth();
            Bomb newBomb = bombFactory.createBomb(initialX, 0); // Spawn at the top of the screen
            bombs.add(newBomb);
            getRoot().getChildren().add(newBomb); // Directly add the Bomb as it extends ImageView
        }
    } */

    @Override
    protected LevelView instantiateLevelView() {
        initializeBoss();
        LevelViewLevelTwo levelView = new LevelViewLevelTwo(getRoot(), PLAYER_INITIAL_HEALTH, boss);
        if (boss != null) {
            levelView.showShield();
            boss.setLevelView(levelView);
        }
        return levelView;
    }

    private boolean userHasReachedKillTarget() {
        return getUser().getNumberOfKills() >= KILLS_TO_ADVANCE;
    }
}
