package com.example.demo.factories;

import com.example.demo.actors.FighterPlane;

/**
 * The EnemyFactory class is an abstract factory for creating enemy planes.
 */
public abstract class EnemyFactory {

    /**
     * Creates an enemy plane with the specified initial position.
     *
     * @param initialXPos the initial X position of the enemy plane
     * @param initialYPos the initial Y position of the enemy plane
     * @return a new instance of an enemy plane
     */
    public abstract FighterPlane createEnemy(double initialXPos, double initialYPos);
}