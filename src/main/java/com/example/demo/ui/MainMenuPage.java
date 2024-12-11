package com.example.demo.ui;

import com.example.demo.factories.PlayButtonFactory;
import com.example.demo.factories.QuitButtonFactory;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;

public class MainMenuPage {

    private static final String BACKGROUND_IMAGE = "/com/example/demo/images/mainMenuBG.png";
    private final PlayButtonFactory playButtonFactory = new PlayButtonFactory();
    private final QuitButtonFactory quitButtonFactory = new QuitButtonFactory();

    public void start(Stage primaryStage) {
        // Load background image
        ImageView background = loadBackground();

        // Create Play button
        Button playButton = playButtonFactory.createButton();
        playButton.setOnAction(event -> {
            com.example.demo.controller.Controller controller = new com.example.demo.controller.Controller(primaryStage);
            controller.launchGame();
        });

        // Create Login and Play button
        Button loginButton = new Button("Login and Play");
        loginButton.setOnAction(event -> {
            LoginPage loginPage = new LoginPage(primaryStage);
            loginPage.showLoginScreen();
        });

        // Create Quit button
        Button quitButton = quitButtonFactory.createButton();
        quitButton.setOnAction(event -> primaryStage.close());

        // Layout for buttons
        VBox buttonsLayout = new VBox(15);
        buttonsLayout.getChildren().addAll(playButton, loginButton, quitButton);
        buttonsLayout.setAlignment(Pos.CENTER);
        buttonsLayout.setTranslateY(100);

        // Create root layout
        StackPane root = new StackPane();
        if (background != null) {
            root.getChildren().add(background);
        }
        root.getChildren().add(buttonsLayout);

        // Set the scene and stage
        Scene scene = new Scene(root, 1300, 750);
        primaryStage.setTitle("Sky Battle - Main Menu");
        primaryStage.setScene(scene);
        primaryStage.show();
    }

    private ImageView loadBackground() {
        try {
            ImageView background = new ImageView(new Image(getClass().getResource(BACKGROUND_IMAGE).toExternalForm()));
            background.setFitWidth(1300);
            background.setFitHeight(750);
            background.setPreserveRatio(false);
            return background;
        } catch (NullPointerException e) {
            System.err.println("Error: Unable to load background image at path: " + BACKGROUND_IMAGE);
            e.printStackTrace();
            return null;
        }
    }
}
