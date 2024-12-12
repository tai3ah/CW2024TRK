package com.example.demo.managers;
import com.example.demo.managers.CollisionManager;
import com.example.demo.actors.GameEntity;
import com.example.demo.actors.UserPlane;
import javafx.application.Platform;
import javafx.scene.layout.Pane;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertTrue;

class CollisionManagerTest {

    static class TestGameEntity extends GameEntity {
        boolean damaged = false;

        public TestGameEntity(String imageName, int imageHeight, double initialX, double initialY) {
            super("", imageHeight, initialX, initialY); // Bypass image loading
        }

        @Override
        public void updatePosition() {

        }

        @Override
        public void updateActor() {

        }

        @Override
        public void takeDamage() {
            damaged = true;
        }

        @Override
        public boolean isDestroyed() {
            return damaged;
        }
    }

    private CollisionManager collisionManager;
    private Pane root;
    private UserPlane userPlane;
    private List<GameEntity> enemyUnits;
    private List<GameEntity> friendlyUnits;
    private List<GameEntity> userProjectiles;
    private List<GameEntity> enemyProjectiles;

    @BeforeAll
    static void initToolkit() {
        Platform.startup(() -> {});
    }

    @BeforeEach
    void setUp() {
        root = new Pane();
        userPlane = new UserPlane(5);
        enemyUnits = new ArrayList<>();
        friendlyUnits = new ArrayList<>();
        userProjectiles = new ArrayList<>();
        enemyProjectiles = new ArrayList<>();

        collisionManager = new CollisionManager(root, userPlane, enemyUnits, friendlyUnits, userProjectiles, enemyProjectiles);
    }

    @Test
    void testUserProjectileHitsEnemy() {
        TestGameEntity enemy = new TestGameEntity("", 50, 100, 100);
        TestGameEntity projectile = new TestGameEntity("", 20, 100, 100);

        root.getChildren().addAll(enemy, projectile);
        enemyUnits.add(enemy);
        userProjectiles.add(projectile);

        collisionManager.handleCollisions();

        assertTrue(enemy.damaged, "Enemy should be damaged.");
        assertTrue(projectile.damaged, "Projectile should be damaged.");
    }

    @Test
    void testEnemyProjectileHitsFriendly() {
        TestGameEntity friendly = new TestGameEntity("", 50, 200, 200);
        TestGameEntity projectile = new TestGameEntity("", 20, 200, 200);

        root.getChildren().addAll(friendly, projectile);
        friendlyUnits.add(friendly);
        enemyProjectiles.add(projectile);

        collisionManager.handleCollisions();

        assertTrue(friendly.damaged, "Friendly unit should be damaged.");
        assertTrue(projectile.damaged, "Projectile should be damaged.");
    }

    private void fireEvent(Pane target, Runnable action) {
        Platform.runLater(action);
        waitForRunLater();
    }

    private void waitForRunLater() {
        try {
            Platform.runLater(() -> {});  // Dummy action
            Thread.sleep(100);
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
        }
    }
}
