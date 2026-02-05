import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.geometry.Rectangle2D;
import javafx.scene.shape.Rectangle;
import javafx.scene.paint.Color;
import java.util.ArrayList;

public class BasicEnemy extends Enemy {

    private static final double COLLISION_WIDTH = 65;
    private static final double COLLISION_HEIGHT = 115;
    private static final double FIRE_RATE = 1.5;
    private static final double ACTIVATION_DISTANCE = 600;
    private static final double RUN_SPEED = 1.5;
    private static final double RUN_DISTANCE = 350; // enemy runs fot these pixel & then shoot

    private SpriteAnimation runAnim;
    private SpriteAnimation shootAnim;
    private SpriteAnimation deathAnim;

    private EnemyState currentState;
    private long lastShotTime;
    private ArrayList<Bullet> bullets;

    private boolean facingRight;
    private boolean isOnScreen;
    private double scale;
    private boolean hasBeenActivated;
    private double distanceTraveled;

    public BasicEnemy( double x, double y) {
        super(x, y, 60, RUN_SPEED, 15);

        this.lastShotTime = 0;
        this.bullets = new ArrayList<>();
        this.currentState = EnemyState.WAITING;
        this.facingRight = false;
        this.isActive = false;
        this.isOnScreen = false;
        this.hasBeenActivated = false;
        this.distanceTraveled = 0;
        this.spriteView = new ImageView();

        this.scale = 3.5;
        spriteView.setScaleX(scale);
        spriteView.setScaleY(scale);

        loadAnimations();
    }

    private void loadAnimations() {
        try {
            Image runSheet = new Image(getClass().getResourceAsStream("/Assets/Enemies/Basic Soldier/Run/Run.png"));
            ArrayList<Rectangle2D> runFrames = SpriteSheetLoader.loadFrames("/Assets/Enemies/Basic Soldier/Run/Run_data.json");
            runAnim = new SpriteAnimation(spriteView, runSheet, runFrames, 10);

            Image shootSheet = new Image(getClass().getResourceAsStream("/Assets/Enemies/Basic Soldier/Shoot/Shoot.png"));
            ArrayList<Rectangle2D> shootFrames = SpriteSheetLoader.loadFrames("/Assets/Enemies/Basic Soldier/Shoot/Shoot_data.json");
            shootAnim = new SpriteAnimation(spriteView, shootSheet, shootFrames, 10);

            Image deathSheet = new Image(getClass().getResourceAsStream("/Assets/Enemies/Basic Soldier/Death/Death.png"));
            ArrayList<Rectangle2D> deathFrames = SpriteSheetLoader.loadFrames("/Assets/Enemies/Basic Soldier/Death/Death_data.json");
            deathAnim = new SpriteAnimation(spriteView, deathSheet, deathFrames, 12);

            shootAnim.setToFirstFrame();


        } catch (Exception e) {
            System.err.println("Failed to load BasicEnemy animations: " + e.getMessage());
            e.printStackTrace();
        }
    }

    @Override
    public void update(double cameraX, double sceneWidth) {
        if (!isAlive() && currentState != EnemyState.DYING)
            return;

        // updating teh sprite according to camera
        spriteView.setX(positionX - cameraX);
        spriteView.setY(positionY);

        double onScreenX = positionX - cameraX;
        isOnScreen = onScreenX > -100 && onScreenX < sceneWidth + 30;
    }

    public void updateBehavior(double playerX ) {
        if (!isActive) {
            return;
        }

        if (!hasBeenActivated) {
            hasBeenActivated = true;
            distanceTraveled = 0;
            switchState(EnemyState.RUNNING_IN);
        }

        if (!isAlive() && currentState == EnemyState.DYING) {
            if (deathAnim != null && deathAnim.getLoopCount() >= 1) {
                health = 0;
            }
            return;
        }

        if (!isAlive())
            return;

        switch (currentState) {
            case WAITING:
                break;

            case RUNNING_IN:
                double moveAmount = speed;
                positionX -= moveAmount;
                distanceTraveled += moveAmount;
                facingRight = false;

                // Stop and shoot when the condition we sett is met
                if (distanceTraveled >= RUN_DISTANCE) {
                    switchState(EnemyState.SHOOTING);
                }
                break;

            case SHOOTING:
                facingRight = playerX > positionX;

                // shoot only if on screen
                if (isOnScreen) {
                    shoot();
                }
                break;

            case DYING:
                break;
        }

        spriteView.setScaleX(facingRight ? -Math.abs(scale) : Math.abs(scale));
    }


    private void shoot() {
        long currentTime = System.currentTimeMillis();
        double timeSinceLastShot = (currentTime - lastShotTime) / 450.0;

        if (timeSinceLastShot >= FIRE_RATE) {
            double bulletX = facingRight ? positionX + 50 : positionX - 10;
            double bulletY = positionY;

            Bullet bullet = new Bullet(bulletX, bulletY, facingRight, damage, "Enemy");
            bullet.playSound();
            bullets.add(bullet);

            lastShotTime = currentTime;
        }
    }

    @Override
    public void takeDamage(int damage) {
        if (!isAlive() || currentState == EnemyState.DYING)
            return;

        super.takeDamage(damage);

        if (!isAlive()) {
            switchState(EnemyState.DYING);
        }
    }

    private void switchState(EnemyState newState) {
        if (currentState == newState) return;

        EnemyState oldState = currentState;

        switch (currentState) {
            case WAITING:
                break;
            case RUNNING_IN:
                runAnim.stop();
                break;
            case SHOOTING:
                shootAnim.stop();
                break;
            case DYING:
                deathAnim.stop();
                break;
        }

        currentState = newState;

        switch (currentState) {
            case WAITING:
                break;
            case RUNNING_IN:
                runAnim.play();
                break;
            case SHOOTING:
                shootAnim.play();
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


    public ArrayList<Bullet> getBullets() {
        return bullets;
    }

    public boolean shouldActivate(double playerX, double cameraX, double sceneWidth) {
        if (isActive) return false;

        if (Math.abs(positionX - playerX) <= ACTIVATION_DISTANCE) return true;

        double rightEdge = cameraX + sceneWidth;
        if (positionX - rightEdge <= ACTIVATION_DISTANCE ) {
            return true;
        }

        return false;
    }

    @Override
    public boolean isReadyToRemove() {
        // Only remove after death animation has completed
        if (currentState == EnemyState.DYING) {
            if (deathAnim == null) return true;
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

    public boolean isOnScreen() {
        return isOnScreen;
    }
}