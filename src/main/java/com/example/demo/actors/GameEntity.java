package com.example.demo.actors;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;

public abstract class GameEntity extends ImageView {

	private static final String IMAGE_LOCATION = "/com/example/demo/images/";
	private boolean isDestroyed;

	public GameEntity(String imageName, int imageHeight, double initialXPos, double initialYPos) {
		this.setImage(new Image(getClass().getResource(IMAGE_LOCATION + imageName).toExternalForm()));
		this.setLayoutX(initialXPos);
		this.setLayoutY(initialYPos);
		this.setFitHeight(imageHeight);
		this.setPreserveRatio(true);
		this.isDestroyed = false;
	}

	public abstract void updatePosition();
	public abstract void updateActor();

	public void takeDamage() {
		// Implementation for taking damage.
	}

	public void destroy() {
		setDestroyed(true);
	}

	protected void setDestroyed(boolean isDestroyed) {
		this.isDestroyed = isDestroyed;
	}

	public boolean isDestroyed() {
		return isDestroyed;
	}

	protected void moveHorizontally(double horizontalMove) {
		this.setTranslateX(getTranslateX() + horizontalMove);
	}

	protected void moveVertically(double verticalMove) {
		this.setTranslateY(getTranslateY() + verticalMove);
	}
}
