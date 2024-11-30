package com.example.demo.factories;

import com.example.demo.actors.Boss;
import com.example.demo.actors.FighterPlane;

public class BossPlaneFactory extends EnemyFactory {
    @Override
    public Boss createEnemy(double initialXPos, double initialYPos) {
        return new Boss(); // Adjust the Boss constructor if needed to accept initialXPos and initialYPos
    }
}
