package com.example.demo.factories;

import javafx.scene.control.Button;

/**
 * The GameButtonFactory class is an abstract factory for creating game buttons.
 */
public abstract class GameButtonFactory {

    /**
     * Creates a game button.
     *
     * @return a new instance of a game button
     */
    public abstract Button createButton();
}