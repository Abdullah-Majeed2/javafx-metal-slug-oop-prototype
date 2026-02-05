import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.geometry.Rectangle2D;
import javafx.scene.shape.Rectangle;
import javafx.scene.paint.Color;
import java.util.ArrayList;

public class ShieldedEnemy extends Enemy {

    private static final double COLLISION_WIDTH = 65;
    private static final double COLLISION_HEIGHT = 115;
    private static final double MELEE_RANGE = 80;
    private static final double CHASE_SPEED = 1.8;
    private static final double ATTACK_COOLDOWN = 2.5;
    private static final double ACTIVATION_DISTANCE = 600;
    private static final double RUN_IN_SPEED = 1.5;

    private SpriteAnimation runAnim;
    private SpriteAnimation meleeAnim;
    private SpriteAnimation deathAnim;

    private EnemyState currentState;
    private boolean isVulnerable;
    private long lastAttackTime;
    private long lastDamageTime;

    private boolean facingRight;
    private double scale;
    private boolean hasBeenActivated;
    private double deathPositionX;

    public ShieldedEnemy( double spawnX, double y) {
        super(spawnX, y, 70, CHASE_SPEED, 10);

        this.positionX = spawnX;
        this.positionY = y;

        this.scale = 3.5;
        this.currentState = EnemyState.RUNNING_IN;
        this.isVulnerable = false;
        this.facingRight = false;
        this.isActive = false;
        this.lastAttackTime = 0;
        this.hasBeenActivated = false;

        this.spriteView = new ImageView();
        spriteView.setScaleX(scale);
        spriteView.setScaleY(scale);

        loadAnimations();
        runAnim.play();
    }

    private void loadAnimations() {
        try {
            Image runSheet = new Image(getClass().getResourceAsStream("/Assets/Enemies/Shielded Soldier/Run/Run.png"));
            ArrayList<Rectangle2D> runFrames = SpriteSheetLoader.loadFrames("/Assets/Enemies/Shielded Soldier/Run/Run_data.json");
            runAnim = new SpriteAnimation(spriteView, runSheet, runFrames, 10);
            runAnim.setToFirstFrame();
            runAnim.play();

            Image meleeSheet = new Image(getClass().getResourceAsStream("/Assets/Enemies/Shielded Soldier/Attack/Attack.png"));
            ArrayList<Rectangle2D> meleeFrames = SpriteSheetLoader.loadFrames("/Assets/Enemies/Shielded Soldier/Attack/Attack_data.json");
            meleeAnim = new SpriteAnimation(spriteView, meleeSheet, meleeFrames, 10);
            meleeAnim.setToFirstFrame();

            Image deathSheet = new Image(getClass().getResourceAsStream("/Assets/Enemies/Shielded Soldier/Death/Death.png"));
            ArrayList<Rectangle2D> deathFrames = SpriteSheetLoader.loadFrames("/Assets/Enemies/Shielded Soldier/Death/Death_data.json");
            deathAnim = new SpriteAnimation(spriteView, deathSheet, deathFrames, 8);

        } catch (Exception e) {
            System.err.println("Failed to load ShieldedEnemy animations: " + e.getMessage());
            e.printStackTrace();
        }
    }

    @Override
    public void update(double cameraX, double sceneWidth) {
        if (!isAlive()) {

            spriteView.setX(deathPositionX - cameraX);
            double yOffset = 20;
            spriteView.setY(positionY + yOffset);
            return;
        }

        spriteView.setX(positionX - cameraX);
        
        double yOffset = 0;
        if (currentState == EnemyState.RUNNING_IN || currentState == EnemyState.CHASING) {
            yOffset = 20;
        } else if (currentState == EnemyState.DYING) {
            yOffset = 20;
        }
        spriteView.setY(positionY + yOffset);

    }

    public void updateBehavior(Player player) {
        if (!isAlive() || !isActive) return;

        if (!hasBeenActivated) {
            hasBeenActivated = true;
            switchState(EnemyState.RUNNING_IN);
        }

        double distanceToPlayer = Math.abs(positionX - player.getPositionX());
        long currentTime = System.currentTimeMillis();
        double timeSinceAttack = (currentTime - lastAttackTime) / 1000.0;

        if (player.isOnGround() && currentState != EnemyState.DYING) {
            facingRight = player.getPositionX() > positionX;
        }

        if (currentState != EnemyState.DYING) {
            if (currentState == EnemyState.RUNNING_IN) {
                positionX += (facingRight ? RUN_IN_SPEED : -RUN_IN_SPEED);

                if (distanceToPlayer <= MELEE_RANGE) {
                    switchState(EnemyState.MELEE_ATTACK);
                    lastAttackTime = currentTime;
                }
            }
            else if (currentState == EnemyState.CHASING) {
                if (distanceToPlayer <= MELEE_RANGE && timeSinceAttack >= ATTACK_COOLDOWN) {
                    switchState(EnemyState.MELEE_ATTACK);
                    lastAttackTime = currentTime;
                } else {
                    positionX += (facingRight ? speed : -speed);
                }
                isVulnerable = false;
            }
            else if (currentState == EnemyState.MELEE_ATTACK) {
                if (distanceToPlayer <= MELEE_RANGE && timeSinceAttack >= ATTACK_COOLDOWN) {
                    lastAttackTime = currentTime;
                    meleeAnim.stop();
                    meleeAnim.play();
                } else if (distanceToPlayer > MELEE_RANGE) {
                    switchState(EnemyState.CHASING);
                }

                checkMeleeVulnerability();
            }
        }

        if (currentState == EnemyState.DYING) {
            if (deathAnim.getLoopCount() >= 1) {
                health = 0;
            }
        }

        spriteView.setScaleX(facingRight ? -Math.abs(scale) : Math.abs(scale));
    }


    public void tryMeleeHit(Player player) {
        if (!isAlive() || currentState != EnemyState.MELEE_ATTACK) return;
        if (!isVulnerable) return;

        double distanceToPlayer = Math.abs(positionX - player.getPositionX());
        if (distanceToPlayer > MELEE_RANGE) return;

        long currentTime = System.currentTimeMillis();
        double timeSinceLastDamage = (currentTime - lastDamageTime) / 1000.0;

        if (timeSinceLastDamage >= 0.3) {
            player.takeDamage(damage);
            lastDamageTime = currentTime;
        }
    }

    private void checkMeleeVulnerability() {
        SpriteAnimation currentMelee = meleeAnim;

        if (currentMelee != null && currentMelee.isPlaying()) {
            isVulnerable = true;
        }
        else{
            isVulnerable = false;
        }
    }

    @Override
    public void takeDamage(int damage) {
        if (!isAlive() || currentState == EnemyState.DYING) return;

        if (currentState != EnemyState.MELEE_ATTACK || !isVulnerable) {

            return;
        }

        super.takeDamage(damage);


        if (!isAlive()) {
            deathPositionX = positionX;
            switchState(EnemyState.DYING);
        }
    }

    private void switchState(EnemyState newState) {
        if (currentState == newState) return;

        switch (currentState) {
            case RUNNING_IN:
                runAnim.stop();
                break;
            case CHASING:
                runAnim.stop();
                break;
            case MELEE_ATTACK:
                meleeAnim.stop();
                break;
            case DYING:
                deathAnim.stop();
                break;
        }

        currentState = newState;

        switch (currentState) {
            case RUNNING_IN:
                runAnim.play();

                break;
            case CHASING:
                runAnim.play();

                break;
            case MELEE_ATTACK:
                if (meleeAnim != null) {
                    meleeAnim.play();

                }
                break;
            case DYING:
                deathAnim.play();
                deathSound.play();

                break;
        }
    }


    public Rectangle2D getBounds() {
        return new Rectangle2D(positionX, positionY + Player.COLLISION_OFFSET_Y, COLLISION_WIDTH, COLLISION_HEIGHT);
    }


    public boolean isVulnerable() {
        return isVulnerable;
    }

    public boolean shouldActivate(double playerX, double cameraX, double sceneWidth) {
        if (isActive) return false;

        if (Math.abs(positionX - playerX) <= ACTIVATION_DISTANCE)
            return true;

        double rightEdge = cameraX + sceneWidth;

        if (positionX - rightEdge <= ACTIVATION_DISTANCE && positionX - rightEdge >= -sceneWidth) {
            return true;
        }

        double leftEdge = cameraX;
        if (leftEdge - positionX <= ACTIVATION_DISTANCE && leftEdge - positionX >= -sceneWidth) {
            return true;
        }

        return false;
    }

    @Override
    public boolean isReadyToRemove() {
        if (currentState == EnemyState.DYING) {
            if (deathAnim == null)
                return true;
            return deathAnim.getLoopCount() >= 1;
        }
        return false;
    }

    public void setActive(boolean active) {
        this.isActive = active;
    }

    public boolean isActive() {
        return isActive;
    }

    public Rectangle2D getMeeleBounds(){
        return new Rectangle2D(positionX - 80, positionY + Player.COLLISION_OFFSET_Y, COLLISION_WIDTH + 80, COLLISION_HEIGHT);
    }
}