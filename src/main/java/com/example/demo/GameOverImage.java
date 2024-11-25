/*package com.example.demo;

import javafx.scene.image.Image;
import javafx.scene.image.ImageView;

public class GameOverImage extends ImageView {
	
	private static final String IMAGE_NAME = "/com/example/demo/images/gameover.png";


	public GameOverImage(double xPosition, double yPosition) {
		setImage(new Image(getClass().getResource(IMAGE_NAME).toExternalForm()) );
//		setImage(ImageSetUp.getImageList().get(ImageSetUp.getGameOver()));
		setLayoutX(xPosition);
		setLayoutY(yPosition);
	}

}
*/

package com.example.demo;

import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.stage.Screen;

public class GameOverImage extends ImageView {

	private static final String IMAGE_NAME = "/com/example/demo/images/gameover.png";
	private static final double DESIRED_HEIGHT = 200;
	private static final double DESIRED_WIDTH = 300;

	public GameOverImage() {
		// Set the image
		setImage(new Image(getClass().getResource(IMAGE_NAME).toExternalForm()));

		// Set the desired width and height of the image
		setFitHeight(DESIRED_HEIGHT);
		setFitWidth(DESIRED_WIDTH);

		// Calculate positions to center the image on the screen
		double screenWidth = Screen.getPrimary().getBounds().getWidth();
		double screenHeight = Screen.getPrimary().getBounds().getHeight();

		double xPosition = (screenWidth - DESIRED_WIDTH) / 2;
		double yPosition = (screenHeight - DESIRED_HEIGHT) / 2;

		// Set the position of the image to align it to the center of the screen
		setLayoutX(xPosition);
		setLayoutY(yPosition);
	}
}
