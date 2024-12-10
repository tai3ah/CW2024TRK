package com.example.demo.ui;
import com.example.demo.actors.Boss;
import javafx.scene.layout.Pane;
import javafx.scene.text.Text;


public class LevelViewLevelTwo extends LevelViewLevelOne {
	private Boss boss;

	private static final int SHIELD_X_POSITION = 1150;
	private static final int SHIELD_Y_POSITION = 500;

	private static final double BOSS_HEALTH_X_POSITION = 1000;
	private static final double BOSS_HEALTH_Y_POSITION = 60;

	private final Pane root;
	private final ShieldImage shieldImage;
	private final Text bossHealthText;

	public LevelViewLevelTwo(Pane root, int heartsToDisplay, Boss boss) {
		super(root, heartsToDisplay);
		this.boss = boss;
		//super(root, heartsToDisplay);
		this.root = root;
		this.shieldImage = new ShieldImage(SHIELD_X_POSITION, SHIELD_Y_POSITION);
		//this.bossHealthText = new Text("Boss Health: 100");
		this.bossHealthText = new Text("Boss Health: " + boss.getHealth());
		bossHealthText.setStyle("-fx-font-size: 20px; -fx-fill: red; -fx-font-weight: bold;");
		root.getChildren().remove(getKillCountText());
		root.getChildren().add(bossHealthText);
		addImagesToRoot();
		addHealthTextToRoot();
		//addImagesToRoot();

	}

	private void addImagesToRoot() {
		root.getChildren().addAll(shieldImage);
	}

	private void addHealthTextToRoot() {
		//updateBossHealth(boss.getHealth());
		bossHealthText.setX(BOSS_HEALTH_X_POSITION);
		bossHealthText.setY(BOSS_HEALTH_Y_POSITION);
		//root.getChildren().add(bossHealthText);
	}

	public void showShield() {
		shieldImage.showShield();
	}

	public void hideShield() {
		shieldImage.hideShield();
	}

	public void updateBossHealth(int currentHealth) {
		System.out.println("Updating Boss Health: " + currentHealth);
		bossHealthText.setText("Boss Health: " + currentHealth);
		bossHealthText.toFront();
		root.layout(); // Force a layout update
	}



}
