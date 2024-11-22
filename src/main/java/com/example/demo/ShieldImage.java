package com.example.demo;

import javafx.scene.image.Image;
import javafx.scene.image.ImageView;

public class ShieldImage extends ImageView {

	private static final int SHIELD_SIZE = 200;

	public ShieldImage(double xPosition, double yPosition) {
		this.setLayoutX(xPosition);
		this.setLayoutY(yPosition);
		this.setImage(new Image(getClass().getResource("/com/example/demo/images/shield.png").toExternalForm()));
		this.setFitHeight(SHIELD_SIZE);
		this.setFitWidth(SHIELD_SIZE);
		this.setVisible(false); // By default, the shield is not visible
	}

	public void showShield() {
		this.setVisible(true);
		System.out.println("Shield is activated (ShieldImage class)");
	}

	public void hideShield() {
		this.setVisible(false);
		System.out.println("Shield is deactivated (ShieldImage class)");
	}

	public void updatePosition(double xPosition, double yPosition) {
		setTranslateX(xPosition);
		setTranslateY(yPosition);
		//System.out.println("Shield Position Updated (ShieldImage class): " + getTranslateX() + ", " + getTranslateY());
	}
}
