package com.example.demo.ui;

import com.example.demo.actors.Boss;
import javafx.scene.layout.Pane;
import javafx.scene.text.Text;

/**
 * Class for displaying the view for Level Two, extending the functionality of Level One.
 */
public class LevelViewLevelTwo extends LevelViewLevelOne {
	private Boss boss;

	private static final int SHIELD_X_POSITION = 1150;
	private static final int SHIELD_Y_POSITION = 500;

	private static final double BOSS_HEALTH_X_POSITION = 1000;
	private static final double BOSS_HEALTH_Y_POSITION = 60;

	private final Pane root;
	private final ShieldImage shieldImage;
	private final Text bossHealthText;

	/**
	 * Constructs a LevelViewLevelTwo with the specified root pane, initial hearts, and boss.
	 *
	 * @param root the root pane for the level view
	 * @param heartsToDisplay the initial number of hearts to display
	 * @param boss the boss object to display health for
	 */
	public LevelViewLevelTwo(Pane root, int heartsToDisplay, Boss boss) {
		super(root, heartsToDisplay);
		this.boss = boss;
		this.root = root;
		this.shieldImage = new ShieldImage(SHIELD_X_POSITION, SHIELD_Y_POSITION);
		this.bossHealthText = new Text("Boss Health: " + boss.getHealth());
		bossHealthText.setStyle("-fx-font-size: 20px; -fx-fill: red; -fx-font-weight: bold;");
		root.getChildren().remove(getKillCountText());
		root.getChildren().add(bossHealthText);
		addImagesToRoot();
		addHealthTextToRoot();
	}

	/**
	 * Adds images to the root pane.
	 */
	private void addImagesToRoot() {
		root.getChildren().addAll(shieldImage);
	}

	/**
	 * Adds the boss health text to the root pane.
	 */
	private void addHealthTextToRoot() {
		bossHealthText.setX(BOSS_HEALTH_X_POSITION);
		bossHealthText.setY(BOSS_HEALTH_Y_POSITION);
	}

	/**
	 * Shows the shield image.
	 */
	public void showShield() {
		shieldImage.showShield();
	}

	/**
	 * Hides the shield image.
	 */
	public void hideShield() {
		shieldImage.hideShield();
	}

	/**
	 * Updates the displayed boss health.
	 *
	 * @param currentHealth the current health of the boss to display
	 */
	public void updateBossHealth(int currentHealth) {
		System.out.println("Updating Boss Health: " + currentHealth);
		bossHealthText.setText("Boss Health: " + currentHealth);
		bossHealthText.toFront();
		root.layout(); // Force a layout update
	}
}