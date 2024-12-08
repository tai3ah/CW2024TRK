package com.example.demo.factories;

import com.example.demo.actors.Drone;
import com.example.demo.actors.FighterPlane;

public class DroneFactory extends EnemyFactory {

    @Override
    public FighterPlane createEnemy(double initialXPos, double initialYPos) {
        return new Drone(initialXPos); // Create a Drone at the specified X position, fixed Y position
    }
}
