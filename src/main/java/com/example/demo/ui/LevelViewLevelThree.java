package com.example.demo.ui;

import com.example.demo.actors.FinalBoss;
import javafx.scene.layout.Pane;
import javafx.scene.text.Text;

/**
 * Class for displaying the view for Level Three, extending the functionality of Level One.
 */
public class LevelViewLevelThree extends LevelViewLevelOne {

    private static final int SCREEN_WIDTH = 1300;
    private static final int SCREEN_HEIGHT = 750;

    private static final double HEART_DISPLAY_X_POSITION = 20;
    private static final double HEART_DISPLAY_Y_POSITION = 20;

    private static final double KILL_COUNT_X_POSITION = SCREEN_WIDTH - 200;
    private static final double KILL_COUNT_Y_POSITION = 50;

    private static final double FINAL_BOSS_HEALTH_X_POSITION = SCREEN_WIDTH - 300;
    private static final double FINAL_BOSS_HEALTH_Y_POSITION = 100;

    private static final double TIMER_DISPLAY_X_POSITION = SCREEN_WIDTH - 150;
    private static final double TIMER_DISPLAY_Y_POSITION = 150;

    private final Text killCountText;
    private final Text finalBossHealthText;
    private final Text timerDisplay;
    private final FinalBoss finalBoss;

    /**
     * Constructs a LevelViewLevelThree with the specified root pane, initial hearts, and final boss.
     *
     * @param root the root pane for the level view
     * @param initialHearts the initial number of hearts to display
     * @param finalBoss the final boss object to display health for
     */
    public LevelViewLevelThree(Pane root, int initialHearts, FinalBoss finalBoss) {
        super(root, initialHearts); // Call the superclass constructor
        this.finalBoss = finalBoss;

        // Remove the generic LevelView kill count text
        root.getChildren().remove(getKillCountText());

        // Initialize kill count display
        this.killCountText = new Text("Kill Count: 0");
        this.killCountText.setStyle("-fx-font-size: 20px; -fx-fill: white; -fx-font-weight: bold;");
        this.killCountText.setX(KILL_COUNT_X_POSITION);
        this.killCountText.setY(KILL_COUNT_Y_POSITION);
        root.getChildren().add(killCountText);

        // Initialize final boss health display
        this.finalBossHealthText = new Text("Final Boss Health: " + finalBoss.getHealth());
        this.finalBossHealthText.setStyle("-fx-font-size: 20px; -fx-fill: red; -fx-font-weight: bold;");
        this.finalBossHealthText.setX(FINAL_BOSS_HEALTH_X_POSITION);
        this.finalBossHealthText.setY(FINAL_BOSS_HEALTH_Y_POSITION);
        root.getChildren().add(finalBossHealthText);

        // Initialize timer display
        this.timerDisplay = new Text("Time Left: 25s");
        this.timerDisplay.setStyle("-fx-font-size: 20px; -fx-fill: yellow; -fx-font-weight: bold;");
        this.timerDisplay.setX(TIMER_DISPLAY_X_POSITION);
        this.timerDisplay.setY(TIMER_DISPLAY_Y_POSITION);
        root.getChildren().add(timerDisplay);
    }

    /**
     * Updates the displayed kill count.
     *
     * @param killCount the current kill count to display
     */
    public void updateKillCount(int killCount) {
        killCountText.setText("Kill Count: " + killCount);
    }

    /**
     * Updates the displayed final boss health.
     *
     * @param currentHealth the current health of the final boss to display
     */
    public void updateFinalBossHealth(int currentHealth) {
        finalBossHealthText.setText("Final Boss Health: " + currentHealth);
        finalBossHealthText.toFront();
    }

    /**
     * Updates the timer display with the remaining time.
     *
     * @param remainingTime the remaining time to display
     */
    public void updateTimerDisplay(int remainingTime) {
        timerDisplay.setText("Time Left: " + remainingTime + "s");
        timerDisplay.toFront();
    }
}