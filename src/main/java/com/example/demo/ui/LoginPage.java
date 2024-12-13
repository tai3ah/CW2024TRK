package com.example.demo.ui;

import com.example.demo.managers.LoginManager;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.StackPane;
import javafx.stage.Stage;

/**
 * Class for displaying the login page and handling user authentication.
 */
public class LoginPage {

    private final Stage primaryStage;
    private final LoginManager loginManager;

    /**
     * Constructs a LoginPage with the specified primary stage.
     *
     * @param primaryStage the primary stage of the application
     */
    public LoginPage(Stage primaryStage) {
        this.primaryStage = primaryStage;
        this.loginManager = new LoginManager();
    }

    /**
     * Displays the login screen.
     */
    public void showLoginScreen() {
        GridPane grid = new GridPane();
        grid.setAlignment(Pos.CENTER);
        grid.setHgap(10);
        grid.setVgap(10);

        Label usernameLabel = new Label("Username:");
        TextField usernameField = new TextField();

        Label passwordLabel = new Label("Password:");
        PasswordField passwordField = new PasswordField();

        Label messageLabel = new Label();

        Button loginButton = new Button("Login");
        loginButton.setOnAction(e -> {
            String username = usernameField.getText().trim();
            String password = passwordField.getText().trim();
            if (username.isEmpty() || password.isEmpty()) {
                messageLabel.setText("Please enter both username and password.");
            } else if (loginManager.login(username, password)) {
                messageLabel.setText("Login successful!");
                LevelSelectionPage levelSelectionPage = new LevelSelectionPage(primaryStage, loginManager, username);
                primaryStage.setScene(levelSelectionPage.createScene());
            } else {
                messageLabel.setText("Invalid username or password. Try again.");
            }
        });

        Button registerButton = new Button("Register");
        registerButton.setOnAction(e -> {
            String username = usernameField.getText().trim();
            String password = passwordField.getText().trim();
            if (username.isEmpty() || password.isEmpty()) {
                messageLabel.setText("Please enter both username and password to register.");
            } else if (loginManager.register(username, password)) {
                messageLabel.setText("Registration successful! You can now log in.");
            } else {
                messageLabel.setText("Username already exists. Please choose a different username.");
            }
        });

        Button backButton = new Button("Back to Main Menu");
        backButton.setOnAction(e -> {
            MainMenuPage mainMenuPage = new MainMenuPage();

            StackPane root = new StackPane();
            Scene mainMenuScene = new Scene(root, 1300, 750);
            primaryStage.setScene(mainMenuScene);

            mainMenuPage.start(primaryStage);
            primaryStage.sizeToScene();
            primaryStage.show();
        });

        grid.add(usernameLabel, 0, 0);
        grid.add(usernameField, 1, 0);
        grid.add(passwordLabel, 0, 1);
        grid.add(passwordField, 1, 1);
        grid.add(loginButton, 0, 2);
        grid.add(registerButton, 1, 2);
        grid.add(backButton, 0, 3);
        grid.add(messageLabel, 1, 3);

        Scene scene = new Scene(grid, 1300, 750);
        primaryStage.setScene(scene);
        primaryStage.show();
    }
}
