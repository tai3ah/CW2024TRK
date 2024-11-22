package com.example.demo.builder;

import com.example.demo.LevelParent;
import com.example.demo.LevelOne;
import com.example.demo.LevelTwo;
import com.example.demo.MainMenuPage;
import javafx.stage.Stage;

public class LevelBuilder {

    //public static Object createLevel(String levelName, double height, double width){
    public static LevelParent createLevel(String levelName, double height, double width, Stage primaryStage){
        return switch (levelName) {
            case "LevelOne" -> new LevelOne(height, width, primaryStage);
            case "LevelTwo" -> new LevelTwo(height, width, primaryStage);
            default -> throw new IllegalArgumentException("Invalid Level Name: " + levelName);
        };
    }
}
