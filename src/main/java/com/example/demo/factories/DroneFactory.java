package com.example.demo.factories;

import com.example.demo.actors.Drone;
import com.example.demo.actors.FighterPlane;

/**
 * The DroneFactory class is responsible for creating instances of the Drone enemy.
 */
public class DroneFactory extends EnemyFactory {

    /**
     * Creates a Drone enemy with the specified initial position.
     *
     * @param initialXPos the initial X position of the Drone
     * @param initialYPos the initial Y position of the Drone
     * @return a new instance of the Drone enemy
     */
    @Override
    public FighterPlane createEnemy(double initialXPos, double initialYPos) {
        return new Drone(initialXPos);
    }
}