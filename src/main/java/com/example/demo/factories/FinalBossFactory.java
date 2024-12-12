package com.example.demo.factories;

import com.example.demo.actors.FinalBoss;

/**
 * The FinalBossFactory class is responsible for creating instances of the FinalBoss enemy.
 */
public class FinalBossFactory extends EnemyFactory {

    /**
     * Creates a FinalBoss enemy with the specified initial position.
     *
     * @param initialXPos the initial X position of the FinalBoss
     * @param initialYPos the initial Y position of the FinalBoss
     * @return a new instance of the FinalBoss enemy
     */
    @Override
    public FinalBoss createEnemy(double initialXPos, double initialYPos) {
        return new FinalBoss(); // Create a FinalBoss instance
    }
}
