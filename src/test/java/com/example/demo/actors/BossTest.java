package com.example.demo.actors;

import com.example.demo.ui.LevelViewLevelTwo;
import javafx.application.Platform;
import javafx.scene.layout.Pane;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

class BossTest {

    private Boss boss;
    private LevelViewLevelTwo mockLevelView;

    @BeforeAll
    static void initToolkit() {
        Platform.startup(() -> {});
    }

    @BeforeEach
    void setUp() {
        boss = new Boss();
        mockLevelView = new LevelViewLevelTwo(new Pane(), 5, boss);
        boss.setLevelView(mockLevelView);
    }

    @Test
    void testBossInitialization() {
        assertNotNull(boss, "Boss should be initialized.");
        assertEquals(20, boss.getHealth(), "Boss should have initial health of 20.");
    }

    @Test
    void testBossTakesDamage() {
        Platform.runLater(() -> {
            boss.takeDamage();
            assertEquals(19, boss.getHealth(), "Boss health should decrease when taking damage.");
        });
        waitForRunLater();
    }

    @Test
    void testBossFiresProjectile() {
        Platform.runLater(() -> {
            boolean fired = boss.fireProjectile() != null;
            assertTrue(fired, "Boss should fire a projectile based on its fire rate.");
        });
        waitForRunLater();
    }




    @Test
    void testBossPositionUpdate() {
        Platform.runLater(() -> {
            double initialY = boss.getTranslateY();
            boss.updatePosition();
            double newY = boss.getTranslateY();
            assertNotEquals(initialY, newY, "Boss should change position when updated.");
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
