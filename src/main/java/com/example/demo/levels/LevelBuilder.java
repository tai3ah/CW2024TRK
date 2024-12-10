package com.example.demo.levels;

import com.example.demo.ui.MainMenuPage;
import javafx.stage.Stage;

public class LevelBuilder {

    public static LevelParent createLevel(String levelName, double height, double width, Stage primaryStage){
        return switch (levelName) {
            case "LevelOne" -> new LevelOne(height, width, primaryStage);
            case "LevelTwo" -> new LevelTwo(height, width, primaryStage);
            case "LevelThree" -> new LevelThree(height, width, primaryStage);
            case "LevelFour" -> new LevelFour(height, width, primaryStage);
            default -> throw new IllegalArgumentException("Invalid Level Name: " + levelName);
        };
    }
}
