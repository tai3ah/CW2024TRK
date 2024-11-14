package com.example.demo.builder;

import com.example.demo.LevelParent;
import com.example.demo.LevelOne;
import com.example.demo.LevelTwo;

public class LevelBuilder {

    public static LevelParent createLevel(String levelName, double height, double width){
        switch (levelName){
            case "LevelOne" :
                return new LevelOne(height, width);
            case "LevelTwo" :
                return new LevelTwo(height, width);
            default :
                throw new IllegalArgumentException("Invalid Level Name: " + levelName);
        }
    }
}
