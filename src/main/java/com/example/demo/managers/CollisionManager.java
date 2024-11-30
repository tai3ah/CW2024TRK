package com.example.demo.managers;

import com.example.demo.actors.GameEntity;
import com.example.demo.actors.Boss;
import com.example.demo.actors.UserPlane;
import javafx.scene.layout.Pane;

import java.util.List;

public class CollisionManager {
    private final Pane root;
    private final UserPlane user;
    private final List<GameEntity> friendlyUnits;
    private final List<GameEntity> enemyUnits;
    private final List<GameEntity> userProjectiles;
    private final List<GameEntity> enemyProjectiles;

    public CollisionManager(Pane root, UserPlane user, List<GameEntity> enemyUnits, List<GameEntity> friendlyUnits,
                            List<GameEntity> userProjectiles, List<GameEntity> enemyProjectiles) {
        this.root = root;
        this.user = user;
        this.enemyUnits = enemyUnits;
        this.friendlyUnits = friendlyUnits;
        this.userProjectiles = userProjectiles;
        this.enemyProjectiles = enemyProjectiles;
    }

    public void handleCollisions() {
        handleUserProjectileCollisions();
        handleEnemyProjectileCollisions();
        handlePlaneCollisions();
        handleEnemyPenetration();
    }

    private void handleUserProjectileCollisions() {
        for (GameEntity projectile : userProjectiles)
            for (GameEntity enemy : enemyUnits) {
                if (!enemy.isDestroyed() && projectile.getBoundsInParent().intersects(enemy.getBoundsInParent())) {
                    if (enemy instanceof Boss) {
                        Boss boss = (Boss) enemy;
                        if (!boss.isShielded()) {
                            boss.takeDamage();
                            System.out.println("Boss hit! Health remaining: " + boss.getHealth());

                        } else {
                            System.out.println("Projectile hit the shield. Boss is protected.");
                        }
                    } else {
                        enemy.takeDamage();
                        if (enemy.isDestroyed()) {
                            user.incrementKillCount(); // Increment kill count when enemy is destroyed
                            System.out.println("Kill count: " + user.getNumberOfKills());
                        }
                    }
                    projectile.takeDamage();
                    root.getChildren().remove(projectile);
                }
            }
    }

    private void handleEnemyProjectileCollisions() {
        handleCollisions(enemyProjectiles, friendlyUnits);
    }

    private void handlePlaneCollisions() {
        handleCollisions(friendlyUnits, enemyUnits);
    }

    private void handleEnemyPenetration() {
        for (GameEntity enemy : enemyUnits) {
            if (enemyHasPenetratedDefenses(enemy)) {
                enemy.takeDamage();
                enemy.destroy();
            }
        }
    }

    private void handleCollisions(List<GameEntity> actors1, List<GameEntity> actors2) {
        for (GameEntity actor : actors2) {
            for (GameEntity otherActor : actors1) {
                if (actor.getBoundsInParent().intersects(otherActor.getBoundsInParent())) {
                    actor.takeDamage();
                    otherActor.takeDamage();
                }
            }
        }
    }

    private boolean enemyHasPenetratedDefenses(GameEntity enemy) {
        return Math.abs(enemy.getTranslateX()) > root.getWidth();
    }
}