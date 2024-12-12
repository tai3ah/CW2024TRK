package com.example.demo.factories;

import com.example.demo.actors.EnemyPlane;
import com.example.demo.actors.FighterPlane;

/**
 * The EnemyPlaneFactory class is responsible for creating instances of the EnemyPlane.
 */
public class EnemyPlaneFactory extends EnemyFactory {

    /**
     * Creates an EnemyPlane with the specified initial position.
     *
     * @param initialXPos the initial X position of the EnemyPlane
     * @param initialYPos the initial Y position of the EnemyPlane
     * @return a new instance of the EnemyPlane
     */
    @Override
    public FighterPlane createEnemy(double initialXPos, double initialYPos) {
        return new EnemyPlane(initialXPos, initialYPos);
    }
}
