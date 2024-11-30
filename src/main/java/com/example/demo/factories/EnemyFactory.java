package com.example.demo.factories;

import com.example.demo.actors.FighterPlane;

public abstract class EnemyFactory {
    // Abstract method to create an enemy plane
    public abstract FighterPlane createEnemy(double initialXPos, double initialYPos);
}
