package com.example.demo.managers;

import com.example.demo.actors.ActiveActorDestructible;
import com.example.demo.actors.Boss;
import com.example.demo.actors.UserPlane;
import javafx.scene.layout.Pane;

import java.util.List;

public class CollisionManager {
    private final Pane root;
    private final UserPlane user;
    private final List<ActiveActorDestructible> friendlyUnits;
    private final List<ActiveActorDestructible> enemyUnits;
    private final List<ActiveActorDestructible> userProjectiles;
    private final List<ActiveActorDestructible> enemyProjectiles;

    public CollisionManager(Pane root, UserPlane user, List<ActiveActorDestructible> enemyUnits, List<ActiveActorDestructible> friendlyUnits,
                            List<ActiveActorDestructible> userProjectiles, List<ActiveActorDestructible> enemyProjectiles) {
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
        for (ActiveActorDestructible projectile : userProjectiles)
            for (ActiveActorDestructible enemy : enemyUnits) {
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
        for (ActiveActorDestructible enemy : enemyUnits) {
            if (enemyHasPenetratedDefenses(enemy)) {
                enemy.takeDamage();
                enemy.destroy();
            }
        }
    }

    private void handleCollisions(List<ActiveActorDestructible> actors1, List<ActiveActorDestructible> actors2) {
        for (ActiveActorDestructible actor : actors2) {
            for (ActiveActorDestructible otherActor : actors1) {
                if (actor.getBoundsInParent().intersects(otherActor.getBoundsInParent())) {
                    actor.takeDamage();
                    otherActor.takeDamage();
                }
            }
        }
    }

    private boolean enemyHasPenetratedDefenses(ActiveActorDestructible enemy) {
        return Math.abs(enemy.getTranslateX()) > root.getWidth();
    }
}