package com.example.demo.ui;

import javafx.application.Platform;
import javafx.scene.Scene;
import javafx.scene.control.Button;

import javafx.scene.layout.StackPane;
import javafx.stage.Stage;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;


import static org.junit.jupiter.api.Assertions.*;


class MainMenuPageTest {

    private MainMenuPage mainMenuPage;
    private Stage mockStage;

    @BeforeAll
    static void initToolkit() {
        Platform.startup(() -> {});
    }

    @BeforeEach
    void setUp() {
        Platform.runLater(() -> {
            mockStage = new Stage();
            mainMenuPage = new MainMenuPage();
        });
        waitForRunLater();
    }

    @Test
    void testStartMainMenu() {
        Platform.runLater(() -> {
            mainMenuPage.start(mockStage);
            assertTrue(mockStage.isShowing(), "Stage should be shown after starting the main menu.");
        });
        waitForRunLater();
    }

    @Test
    void testPlayButtonAction() {
        Platform.runLater(() -> {
            mainMenuPage.start(mockStage);
            Scene scene = mockStage.getScene();
            StackPane root = (StackPane) scene.getRoot();

            Button playButton = (Button) root.getChildren().stream()
                    .filter(node -> node instanceof Button && ((Button) node).getText().equals("Play"))
                    .findFirst()
                    .orElse(null);

            assertNotNull(playButton, "Play button should exist.");
            playButton.fire();  // Simulate button click
            assertTrue(mockStage.isShowing(), "Stage should remain showing after play button action.");
        });
        waitForRunLater();
    }

    @Test
    void testQuitButtonAction() {
        Platform.runLater(() -> {
            mainMenuPage.start(mockStage);
            Scene scene = mockStage.getScene();
            StackPane root = (StackPane) scene.getRoot();

            Button quitButton = (Button) root.getChildren().stream()
                    .filter(node -> node instanceof Button && ((Button) node).getText().equals("Quit"))
                    .findFirst()
                    .orElse(null);

            assertNotNull(quitButton, "Quit button should exist.");
            quitButton.fire();  // Simulate button click
            assertFalse(mockStage.isShowing(), "Stage should be closed after quit button action.");
        });
        waitForRunLater();
    }

    private void waitForRunLater() {
        try {
            Platform.runLater(() -> {});  // Dummy task to sync JavaFX thread
            Thread.sleep(100);
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
        }
    }
}
