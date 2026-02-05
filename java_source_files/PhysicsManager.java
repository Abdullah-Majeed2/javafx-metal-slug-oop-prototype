
import javafx.geometry.Rectangle2D;

public class PhysicsManager {

    private static final double MAX_FALL_SPEED = 5.0;
    private static final double GROUND_DETECTION_RANGE = 30.0;

    public static void updatePlayer(Player player, Level level) {
        // Apply gravity
        double newVelocityY = player.getVelocityY() + player.getGravity();
        if (newVelocityY > MAX_FALL_SPEED) {
            newVelocityY = MAX_FALL_SPEED;
        }
        player.setVelocityY(newVelocityY);

        // move player down
        player.setPositionY(player.getPositionY() + player.getVelocityY());

        // check for ground only if moving downward
        if (player.getVelocityY() >= 0) {
            Platform groundPlatform = findGroundPlatform(player, level);

            if (groundPlatform != null) {
                double platformTop = groundPlatform.getBounds().getMinY();
                player.setPositionY(platformTop - (Player.COLLISION_HEIGHT + Player.COLLISION_OFFSET_Y));
                player.setVelocityY(0);
                player.setOnGround(true);
            } else {
                player.setOnGround(false);
            }
        } else {
            // player is moving upward
            player.setOnGround(false);
        }

        if (player.getPositionY() > 1000) {
            resetPlayer(player, level);
        }
    }

    private static Platform findGroundPlatform(Player player, Level level) {

        double playerCenterX = player.getPositionX() + Player.COLLISION_OFFSET_X + (Player.COLLISION_WIDTH / 2.0);
        double playerFeet = player.getPositionY() + Player.COLLISION_OFFSET_Y + Player.COLLISION_HEIGHT;

        Platform closestPlatform = null;
        double closestDistance = Double.MAX_VALUE;

        for (GameObjects obj : level.getGameObjects()) {
            if (obj instanceof Platform) {
                Platform platform = (Platform) obj;
                Rectangle2D platBounds = platform.getBounds();

                double platLeft = platBounds.getMinX();
                double platRight = platBounds.getMaxX();
                double platTop = platBounds.getMinY();


                if (playerCenterX >= platLeft && playerCenterX <= platRight) {
                    double distance = playerFeet - platTop;


                    if (distance >= -15 && distance <= GROUND_DETECTION_RANGE) {

                        double absDistance = Math.abs(distance);
                        if (absDistance < Math.abs(closestDistance)) {
                            closestDistance = distance;
                            closestPlatform = platform;
                        }
                    }
                }
            }
        }

        return closestPlatform;
    }

    private static void resetPlayer(Player player, Level level) {
        player.setPosition(
                level.getPlayerStartX(),
                level.getPlayerStartY(),
                level.getLevelWidth(),
                0
        );
        player.setVelocityY(0);
        player.setOnGround(true);
    }
}