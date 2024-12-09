package com.example.demo.factories;

import com.example.demo.actors.FinalBoss;
import com.example.demo.actors.FighterPlane;

public class FinalBossFactory extends EnemyFactory {

    @Override
    public FinalBoss createEnemy(double initialXPos, double initialYPos) {
        return new FinalBoss(); // Create a FinalBoss instance
    }
}

