package com.example.demo;

import javafx.scene.image.Image;
import javafx.scene.image.ImageView;

public class ShieldImage extends ImageView {
	
	//private static final String IMAGE_NAME = "/images/shield.png";


	//added
	private boolean shieldStatus = false;

	private static final int SHIELD_SIZE = 200;
	
	public ShieldImage(double xPosition, double yPosition) {
		this.setLayoutX(xPosition);
		this.setLayoutY(yPosition);
		//this.setImage(new Image(IMAGE_NAME));
		this.setImage(new Image(getClass().getResource("/com/example/demo/images/shield.png").toExternalForm()));
		//this.setImage(new Image(getClass().getResource(IMAGE_NAME).toExternalForm()));
		this.setVisible(true);
		this.setFitHeight(SHIELD_SIZE);
		this.setFitWidth(SHIELD_SIZE);
	}

	public void showShield() {
	//added
		if (!shieldStatus){
			shieldStatus=true;
			this.setVisible(true);
			System.out.println("shield is here. shield image");
		}
		//this.setVisible(true);
	}
	
	public void hideShield() {
		this.setVisible(false);
		System.out.println("shield is not here");
	}

	//added
	public void updatePosition(double xPosition, double yPosition) {
		setLayoutX(xPosition);
		setLayoutY(yPosition);
		System.out.println("Shield Position Updated(Shield Image class): " + getLayoutX() + ", " + getLayoutY());


	}

}
