package com.example.demo.levels;

import javafx.stage.Stage;

/**
 * The LevelBuilder class is responsible for creating instances of different game levels.
 */
public class LevelBuilder {

    /**
     * Creates a level based on the specified level name.
     *
     * @param levelName the name of the level to create
     * @param height the height of the level
     * @param width the width of the level
     * @param primaryStage the primary stage for the level
     * @return an instance of the specified level
     * @throws IllegalArgumentException if the level name is invalid
     */
    public static LevelParent createLevel(String levelName, double height, double width, Stage primaryStage) {
        return switch (levelName) {
            case "LevelOne" -> new LevelOne(height, width, primaryStage);
            case "LevelTwo" -> new LevelTwo(height, width, primaryStage);
            case "LevelThree" -> new LevelThree(height, width, primaryStage);
            case "LevelFour" -> new LevelFour(height, width, primaryStage);
            default -> throw new IllegalArgumentException("Invalid Level Name: " + levelName);
        };
    }
}