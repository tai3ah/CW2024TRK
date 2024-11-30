package com.example.demo.factories;

import com.example.demo.actors.EnemyPlane;
import com.example.demo.actors.FighterPlane;

public class EnemyPlaneFactory extends EnemyFactory {
    @Override
    public FighterPlane createEnemy(double initialXPos, double initialYPos) {
        return new EnemyPlane(initialXPos, initialYPos);
    }
}

