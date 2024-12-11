package com.example.demo.ui;

import com.example.demo.actors.Drone;
import javafx.scene.layout.Pane;
import javafx.scene.text.Text;

public class LevelViewLevelFour extends LevelViewLevelOne {

    private static final int SCREEN_WIDTH = 1300;
    private static final int SCREEN_HEIGHT = 750;

    private static final double KILL_COUNT_X_POSITION = SCREEN_WIDTH - 200;
    private static final double KILL_COUNT_Y_POSITION = 50;

    private static final double DRONE_HEALTH_X_POSITION = SCREEN_WIDTH - 300;
    private static final double DRONE_HEALTH_Y_POSITION = 100;

    private static final double TIMER_X_POSITION = SCREEN_WIDTH / 2 - 50;
    private static final double TIMER_Y_POSITION = 50;

    private final Text killCountText;
    private final Text droneHealthText;
    private final Text timerText;

    private final Drone drone;

    public LevelViewLevelFour(Pane root, int initialHearts, Drone drone) {
        super(root, initialHearts); // Call superclass constructor
        this.drone = drone;

        // Remove LevelView's kill count text
        root.getChildren().remove(getKillCountText());

        // Initialize kill count display
        this.killCountText = new Text("Kill Count: 0");
        this.killCountText.setStyle("-fx-font-size: 20px; -fx-fill: white; -fx-font-weight: bold;");
        this.killCountText.setX(KILL_COUNT_X_POSITION);
        this.killCountText.setY(KILL_COUNT_Y_POSITION);
        root.getChildren().add(killCountText);

        // Initialize drone health display
        this.droneHealthText = new Text("Drone Health: " + drone.getHealth());
        this.droneHealthText.setStyle("-fx-font-size: 20px; -fx-fill: red; -fx-font-weight: bold;");
        this.droneHealthText.setX(DRONE_HEALTH_X_POSITION);
        this.droneHealthText.setY(DRONE_HEALTH_Y_POSITION);
        root.getChildren().add(droneHealthText);

        // Initialize timer text (hidden initially)
        this.timerText = new Text();
        this.timerText.setStyle("-fx-font-size: 30px; -fx-fill: yellow; -fx-font-weight: bold;");
        this.timerText.setX(TIMER_X_POSITION);
        this.timerText.setY(TIMER_Y_POSITION);
        timerText.setVisible(false); // Hidden by default
        root.getChildren().add(timerText);
    }

    /**
     * Updates the displayed kill count
     */
    public void updateKillCount(int killCount) {
        killCountText.setText("Kill Count: " + killCount);
    }

    /**
     * Updates the displayed drone health
     */
    public void updateDroneHealth(int currentHealth) {
        droneHealthText.setText("Drone Health: " + currentHealth);
    }

    /**
     * Updates the heart display
     */
    public void addHeart() {
        heartDisplay.addHeart();
    }

    public void updateHearts(int currentHearts) {
        heartDisplay.updateHearts(currentHearts);
    }
}
