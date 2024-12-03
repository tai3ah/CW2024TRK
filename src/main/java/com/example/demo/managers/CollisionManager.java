/*package com.example.demo.managers;

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

 */

package com.example.demo.managers;


import com.example.demo.actors.GameEntity;
import com.example.demo.actors.Boss;
import com.example.demo.actors.UserPlane;
import javafx.scene.layout.Pane;

import java.util.ArrayList;
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
        root.layout();

        handleUserProjectileCollisions();
        handleEnemyProjectileCollisions();
        handlePlaneCollisions();
        handleEnemyPenetration();
        handleProjectileCollisions();
    }

    private void handleUserProjectileCollisions() {
        List<GameEntity> projectilesToRemove = new ArrayList<>();
        List<GameEntity> enemiesToRemove = new ArrayList<>();

        for (GameEntity projectile : userProjectiles) {
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
                            user.incrementKillCount();
                            System.out.println("Kill count: " + user.getNumberOfKills());
                            enemiesToRemove.add(enemy);
                        }
                    }
                    projectile.takeDamage();
                    projectilesToRemove.add(projectile);
                    break;
                }
            }
        }

        // Remove destroyed entities after collision checks
        removeEntities(projectilesToRemove, userProjectiles);
        removeEntities(enemiesToRemove, enemyUnits);
    }

    private void handleEnemyProjectileCollisions() {
        handleCollisions(enemyProjectiles, friendlyUnits);
    }

    private void handlePlaneCollisions() {
        handleCollisions(friendlyUnits, enemyUnits);
    }

    private void handleEnemyPenetration() {
        List<GameEntity> enemiesToRemove = new ArrayList<>();

        for (GameEntity enemy : enemyUnits) {
            if (enemyHasPenetratedDefenses(enemy)) {
                System.out.println("Enemy penetrated defenses and is being destroyed.");
                enemy.takeDamage();
                enemy.destroy();
                enemiesToRemove.add(enemy);
            }
        }

        // Remove enemies that have penetrated defenses
        removeEntities(enemiesToRemove, enemyUnits);
    }

    private void handleProjectileCollisions() {
        List<GameEntity> userProjectilesToRemove = new ArrayList<>();
        List<GameEntity> enemyProjectilesToRemove = new ArrayList<>();

        for (GameEntity userProjectile : userProjectiles) {
            for (GameEntity enemyProjectile : enemyProjectiles) {
                if (userProjectile.getBoundsInParent().intersects(enemyProjectile.getBoundsInParent())) {
                    System.out.println("Collision detected: User projectile hit an enemy projectile!");
                    userProjectilesToRemove.add(userProjectile);
                    enemyProjectilesToRemove.add(enemyProjectile);
                    break; // Stop checking other enemy projectiles for this user projectile
                }
            }
        }

        // Remove colliding projectiles after checks
        removeEntities(userProjectilesToRemove, userProjectiles);
        removeEntities(enemyProjectilesToRemove, enemyProjectiles);
    }

    private void handleCollisions(List<GameEntity> actors1, List<GameEntity> actors2) {
        List<GameEntity> actors1ToRemove = new ArrayList<>();
        List<GameEntity> actors2ToRemove = new ArrayList<>();

        for (GameEntity actor1 : actors1) {
            for (GameEntity actor2 : actors2) {
                if (actor1.getBoundsInParent().intersects(actor2.getBoundsInParent())) {
                    System.out.println("Collision detected between actors.");
                    actor1.takeDamage();
                    actor2.takeDamage();

                    if (actor1.isDestroyed()) {
                        actors1ToRemove.add(actor1);
                    }
                    if (actor2.isDestroyed()) {
                        actors2ToRemove.add(actor2);
                    }
                }
            }
        }

        // Remove destroyed entities after collision checks
        removeEntities(actors1ToRemove, actors1);
        removeEntities(actors2ToRemove, actors2);
    }

    private boolean enemyHasPenetratedDefenses(GameEntity enemy) {
        return Math.abs(enemy.getTranslateX()) > root.getWidth();
    }

    // Helper method to remove entities from both the list and the scene
    private <T extends GameEntity> void removeEntities(List<T> entitiesToRemove, List<T> entities) {
        for (T entity : entitiesToRemove) {
            entities.remove(entity);
            root.getChildren().remove(entity);
        }
    }
}
