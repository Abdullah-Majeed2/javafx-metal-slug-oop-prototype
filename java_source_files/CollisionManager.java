import javafx.geometry.Rectangle2D;
import java.util.ArrayList;

public class CollisionManager {

    public static void checkBulletCollisions(Player player, ArrayList<Enemy> enemies, double cameraX, double sceneWidth) {
        // Check player bullets hitting enemiezz

        ArrayList<Bullet> playerBullets = player.getBullets();
        for (int i = playerBullets.size() - 1; i >= 0; i--) {
            Bullet bullet = playerBullets.get(i);
            if (!bullet.isActive()) continue;

            Rectangle2D bulletBounds = bullet.getBounds();

            for (Enemy enemy : enemies) {
                if (enemy.isDead() || !enemy.isActive()) continue;

                Rectangle2D enemyBounds = enemy.getBounds();

                if (bulletBounds.intersects(enemyBounds)) {
                    // Check if enemy can be damaged
                    if (enemy instanceof ShieldedEnemy) {
                        ShieldedEnemy shielded = (ShieldedEnemy) enemy;
                        if (shielded.isVulnerable()) {
                            enemy.takeDamage(bullet.getDamage());
                            bullet.deactivate();
                        } else {
                            bullet.deactivate();
                        }
                    } else {
                        enemy.takeDamage(bullet.getDamage());
                        bullet.deactivate();
                    }
                    break;
                }
            }
        }

        // ANYONE => Check if enemy bullets hitting player ( ONLY IF ENEMY AND BULLET ARE ON SCREEN )

        for (Enemy enemy : enemies) {
            if (enemy.isDead() || !enemy.isActive()) continue;

            if (enemy instanceof BasicEnemy) {
                BasicEnemy basicEnemy = (BasicEnemy) enemy;

                if (!basicEnemy.isOnScreen()) {
                    continue; // Skip collision if enemy is off screen
                }

                ArrayList<Bullet> enemyBullets = basicEnemy.getBullets();

                for (int i = enemyBullets.size() - 1; i >= 0; i--) {
                    Bullet bullet = enemyBullets.get(i);
                    if (!bullet.isActive()) continue;

                    double bulletScreenX = bullet.getPositionX() - cameraX;
                    boolean bulletOnScreen = bulletScreenX > -100 && bulletScreenX < sceneWidth + 100;

                    if (!bulletOnScreen) {
                        continue;
                    }

                    Rectangle2D bulletBounds = bullet.getBounds();
                    Rectangle2D playerBounds = player.getBounds();

                    if (bulletBounds.intersects(playerBounds)) {
                        player.takeDamage(bullet.getDamage());
                        bullet.deactivate();
                        break;
                    }
                }
            }
        }
    }

    public static void checkMeleeCollisions(Player player, ArrayList<Enemy> enemies) {
        Rectangle2D playerBounds = player.getBounds();

        for (Enemy enemy : enemies) {
            if (enemy.isDead() || !enemy.isActive()) continue;

            if (enemy instanceof ShieldedEnemy) {
                ShieldedEnemy shielded = (ShieldedEnemy) enemy;
                Rectangle2D enemyBounds = shielded.getMeeleBounds();

                if (playerBounds.intersects(enemyBounds)) {

                        double playerVelY = player.getVelocityY();

                        if (playerVelY >= 0 && player.isOnGround()) {

                            double playerX = player.getPositionX();
                            double enemyX = shielded.getPositionX();
                            

                            boolean movingTowardsEnemy = false;
                            if (playerX < enemyX && player.getRightPressed()) {

                                movingTowardsEnemy = true;
                            } else if (playerX > enemyX && player.getLeftPressed()) {

                                movingTowardsEnemy = true;
                            }
                            
                            if (movingTowardsEnemy) {

                                player.revertToPrevX();
                                player.setLeftPressed(false);
                                player.setRightPressed(false);
                            }

                        }

                        shielded.tryMeleeHit(player);
                    }
                }
        }
    }
}