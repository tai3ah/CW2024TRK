package com.example.demo.factories;

import com.example.demo.actors.Boss;

/**
 * The BossPlaneFactory class is responsible for creating instances of the Boss enemy.
 */
public class BossPlaneFactory extends EnemyFactory {

    /**
     * Creates a Boss enemy with the specified initial position.
     *
     * @param initialXPos the initial X position of the Boss
     * @param initialYPos the initial Y position of the Boss
     * @return a new instance of the Boss enemy
     */
    @Override
    public Boss createEnemy(double initialXPos, double initialYPos) {
        return new Boss(); // Adjust the Boss constructor if needed to accept initialXPos and initialYPos
    }
}