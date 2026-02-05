import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.geometry.Rectangle2D;
import javafx.scene.media.Media;
import javafx.scene.media.MediaPlayer;

import java.util.ArrayList;


public class Player extends Character {

    public static final double COLLISION_WIDTH = 65;
    public static final double COLLISION_HEIGHT = 115;

    public static final double COLLISION_OFFSET_X = -50;
    public static final double COLLISION_OFFSET_Y = -40;

    public static final double COLLISION_FLIP_ADJUST = 70;
    public static final double CROUCH_HEIGHT = 45;

    private static final double CROUCH_Y_OFFSET = 30;

    private static final double PLAYER_SPEED = 2;
    private static final double CROUCH_SPEED = .25;
    private static final int PLAYER_HEALTH = 100;

    private double scale;
    private double prevPositionX;
    private boolean leftPressed;
    private boolean rightPressed;
    private boolean isCrouching;
    private boolean jumpInputActive;
    private boolean canJumpAgain;
    private boolean facingRight;


    private boolean shootPressed;
    private Weapon currentWeapon;
    private ArrayList<Bullet> bullets;

    private double velocityY;
    private boolean isOnGround;
    private static final double GRAVITY = 0.55;
    private static final double JUMP_POWER = -15;

    private int framesOffGround = 0;

    private double minBoundaryX;
    private double maxBoundaryX;

    private SpriteAnimation idleAnim;
    private SpriteAnimation runAnim;
    private SpriteAnimation jumpAnim;
    private SpriteAnimation crouchIdleAnim;
    private SpriteAnimation crouchWalkAnim;
    private SpriteAnimation shootAnim;
    private SpriteAnimation shootRunAnim;
    private SpriteAnimation shootCrouchAnim;
    private SpriteAnimation deathAnim;

    private MediaPlayer deathSound;
    private MediaPlayer hitSound;

    private AnimationState currentState;

    private double idleBaseHeight;
    private double jumpBaseHeight;


    // respwan tracking
    private double respawnX;
    private double respawnY;
    private boolean isRespawning;


    public Player(double x, double y, double boundaryX) {
        super(x, y, PLAYER_HEALTH, PLAYER_SPEED);

        this.minBoundaryX = 0;
        this.maxBoundaryX = boundaryX;
        this.scale = 3.5;

        this.spriteView = new ImageView();
        spriteView.setScaleX(scale);
        spriteView.setScaleY(scale);

        loadAllAnimations();
        loadAllSounds();

        leftPressed = false;
        rightPressed = false;
        canJumpAgain = true;
        facingRight = true;
        isOnGround = true;
        velocityY = 0;

        currentState = AnimationState.IDLE;

        currentWeapon = new Pistol();
        bullets = new ArrayList<>();
        shootPressed = false;

        respawnX = x;
        respawnY = y;
        isRespawning = false;

        idleAnim.play();
    }

    private void loadAllAnimations() {
        try {
            Image idleSheet = new Image(getClass().getResourceAsStream("/Assets/Player/Idle/Main Idle/Main_idle.png"));
            ArrayList<Rectangle2D> idleFrames = SpriteSheetLoader.loadFrames("/Assets/Player/Idle/Main Idle/Main_Idle_data.json");
            idleAnim = new SpriteAnimation(spriteView, idleSheet, idleFrames, 5);
            idleBaseHeight = idleFrames.get(0).getHeight();


            Image runSheet = new Image(getClass().getResourceAsStream("/Assets/Player/Run/Run_sheet.png"));
            ArrayList<Rectangle2D> runFrames = SpriteSheetLoader.loadFrames("/Assets/Player/Run/Run_data.json");
            runAnim = new SpriteAnimation(spriteView, runSheet, runFrames, 18);

            Image jumpSheet = new Image(getClass().getResourceAsStream("/Assets/Player/Jump/Jump_sheet.png"));
            ArrayList<Rectangle2D> jumpFrames = SpriteSheetLoader.loadFrames("/Assets/Player/Jump/Jump_data.json");
            jumpAnim = new SpriteAnimation(spriteView, jumpSheet, jumpFrames, 9);
            jumpBaseHeight = jumpFrames.get(0).getHeight();

            Image crouchIdleSheet = new Image(getClass().getResourceAsStream("/Assets/Player/Crouch Idle/Crouch_Idle.png"));
            ArrayList<Rectangle2D> crouchIdleFrames = SpriteSheetLoader.loadFrames("/Assets/Player/Crouch Idle/Crouch_Idle_data.json");
            crouchIdleAnim = new SpriteAnimation(spriteView, crouchIdleSheet, crouchIdleFrames, 5);

            Image crouchWalkSheet = new Image(getClass().getResourceAsStream("/Assets/Player/Crouch Walk/Crouch_Walk.png"));
            ArrayList<Rectangle2D> crouchWalkFrames = SpriteSheetLoader.loadFrames("/Assets/Player/Crouch Walk/Crouch_Walk_data.json");
            crouchWalkAnim = new SpriteAnimation(spriteView, crouchWalkSheet, crouchWalkFrames, 10);

            // shoting animation
            Image shootSheet = new Image(getClass().getResourceAsStream("/Assets/Player/Idle Fire/New folder/sprite_sheet.png"));
            ArrayList<Rectangle2D> shootFrames = SpriteSheetLoader.loadFrames("/Assets/Player/Idle Fire/New folder/sprite_sheet.json");
            shootAnim = new SpriteAnimation(spriteView, shootSheet, shootFrames, 20);

            // shoot + running animation
            Image shootRunSheet = new Image(getClass().getResourceAsStream("/Assets/Player/Run Fire/Run_Fire_sheet.png"));
            ArrayList<Rectangle2D> shootRunFrames = SpriteSheetLoader.loadFrames("/Assets/Player/Run Fire/Run_Fire_data.json");
            shootRunAnim = new SpriteAnimation(spriteView, shootRunSheet, shootRunFrames, 22);

            // shoot while croutch animation
            Image shootCrouchSheet = new Image(getClass().getResourceAsStream("/Assets/Player/Crouch Fire/Crouch_Fire.png"));
            ArrayList<Rectangle2D> shootCrouchFrames = SpriteSheetLoader.loadFrames("/Assets/Player/Crouch Fire/Crouch_Fire_data.json");
            shootCrouchAnim = new SpriteAnimation(spriteView, shootCrouchSheet, shootCrouchFrames, 18);

            Image deathSheet = new Image(getClass().getResourceAsStream("/Assets/Player/Death/Death.png"));
            ArrayList<Rectangle2D> deathFrames = SpriteSheetLoader.loadFrames("/Assets/Player/Death/Death_data.json");
            deathAnim = new SpriteAnimation(spriteView, deathSheet, deathFrames, 10);

            idleAnim.setToFirstFrame();


        } catch (Exception e) {
            System.err.println("Failed to load animations: " + e.getMessage());
            e.printStackTrace();
        }
    }

    private void loadAllSounds(){
        try {
        AudioSettings audioSettings = AudioSettings.getInstance();
        double sfxVol = audioSettings.getSfxVolume();
        
        Media media = new Media(getClass().getResource("/Assets/Player/Sounds/Death.wav").toString());
        deathSound = new MediaPlayer(media);
        if (sfxVol == 0.0) {
            deathSound.setMute(true);
        } else {
            deathSound.setVolume(sfxVol);
        }

        Media hit = new Media(getClass().getResource("/Assets/Player/Sounds/Gettinghit.wav").toString());
        hitSound = new MediaPlayer(hit);
        if (sfxVol == 0.0) {
            hitSound.setMute(true);
        } else {
            hitSound.setVolume(sfxVol * 0.2);
        }

        } catch (Exception e) {
            System.err.println("Failed to load SFX: " + e.getMessage());
            e.printStackTrace();
        }
    }

    public void updateSoundVolume() {
        AudioSettings audioSettings = AudioSettings.getInstance();
        double sfxVol = audioSettings.getSfxVolume();
        
        if (deathSound != null) {
            if (sfxVol == 0.0) {
                deathSound.setMute(true);
            } else {
                deathSound.setMute(false);
                deathSound.setVolume(sfxVol);
            }
        }
        
        if (hitSound != null) {
            if (sfxVol == 0.0) {
                hitSound.setMute(true);
            } else {
                hitSound.setMute(false);
                hitSound.setVolume(sfxVol * 0.2);
            }
        }
    }

    @Override
    public void update(double cameraX, double sceneWidth) {

        prevPositionX = positionX;

        if (!isAlive()) {
            if (currentState != AnimationState.DEATH) {
                switchAnimation(AnimationState.DEATH);
            }
            spriteView.setX(positionX - cameraX);
            spriteView.setY(positionY);


            if (deathAnim != null && deathAnim.getLoopCount() >= 1 && !isRespawning) {
                respawn();
            }
            return;
        }


        if (!jumpInputActive) {
            canJumpAgain = true;
        }

        if (jumpInputActive && canJumpAgain && isOnGround && !isCrouching) {
            jump();
            canJumpAgain = false;
        }

        boolean isMoving = false;
        double currentSpeed = isCrouching ? CROUCH_SPEED : PLAYER_SPEED;

        if (leftPressed && positionX > minBoundaryX) {
            this.positionX -= currentSpeed;
            this.facingRight = false;
            isMoving = true;
        }
        if (rightPressed && positionX < maxBoundaryX) {
            this.positionX += currentSpeed;
            this.facingRight = true;
            isMoving = true;
        }

        double onScreenX = positionX - cameraX;
        if (!isRespawning) {
            if (onScreenX < 0) {
                positionX = cameraX;
            }
            if (onScreenX > sceneWidth - COLLISION_WIDTH) {
                positionX = cameraX + sceneWidth - COLLISION_WIDTH;
            }
        }

        if (isOnGround) {
            framesOffGround = 0;
            if (isRespawning) {
                isRespawning = false;
            }
        } else {
            framesOffGround++;
        }


        if (shootPressed && currentWeapon.canFire(isMoving , isCrouching)) {
            Bullet newBullet = currentWeapon.fire(positionX, positionY, facingRight, isCrouching , isMoving );
            if (newBullet != null) {
                newBullet.playSound();
                bullets.add(newBullet);
            }
        }



        AnimationState newState = determineAnimationState(isMoving);

        if (newState != currentState) {
            switchAnimation(newState);
        }

        double currentScale = scale;

        if (currentState == AnimationState.RUN) {
            currentScale = scale * 0.95;
        }
        if (currentState == AnimationState.SHOOT_CROUCH) {
            currentScale = scale * 0.95;
        }
        if (currentState == AnimationState.SHOOT_RUN) {
            currentScale = scale * 0.95;
        }
        if (currentState == AnimationState.SHOOT) {
            //currentScale = scale * 0.95;
        }
        spriteView.setScaleX(facingRight ? Math.abs(currentScale) : -Math.abs(currentScale));
        spriteView.setScaleY(Math.abs(currentScale));

        double visualYOffset = calculateYOffset();
        spriteView.setX(positionX - cameraX);
        spriteView.setY(positionY + visualYOffset);

    }

    private double calculateYOffset() {
        double offset = 0;

        switch (currentState) {
            case IDLE:
                break;
            case RUN:
                offset = -8;
                break;
            case JUMP:
                offset = (idleBaseHeight - jumpBaseHeight) * scale;
                break;
            case CROUCH_WALK , CROUCH_IDLE:
                offset = CROUCH_Y_OFFSET;
                break;
            case SHOOT_CROUCH:
                offset = 24;
                break;
            case SHOOT_RUN:
                offset = -10;
                break;
        }

        return offset;
    }

    private AnimationState determineAnimationState(boolean isMoving) {


        if (shootPressed) {
            if (isCrouching && isMoving && isOnGround) {
                return AnimationState.CROUCH_WALK;
            }
            else if (isCrouching && isOnGround) {
                return AnimationState.SHOOT_CROUCH;
            }else if(leftPressed && rightPressed && isOnGround) {
                return AnimationState.SHOOT;
            }
            else if (isMoving && isOnGround) {
                return AnimationState.SHOOT_RUN;
            } else if (isOnGround) {
                return AnimationState.SHOOT;
            }

        }


        if (!isOnGround && framesOffGround > 5) {
            return AnimationState.JUMP;

        }

        if (isCrouching && isMoving && isOnGround) {
            return AnimationState.CROUCH_WALK;
        }

        if (isCrouching && isOnGround) {
            return AnimationState.CROUCH_IDLE;
        }

        if(leftPressed && rightPressed) {
            return AnimationState.IDLE;
        }

        if (isMoving) {
            if (isOnGround || framesOffGround <= 15) {
                return AnimationState.RUN;
            }
        }

        return AnimationState.IDLE;
    }

    private void switchAnimation(AnimationState newState) {
        stopCurrentAnimation();

        currentState = newState;
        startCurrentAnimation();
    }

    private void stopCurrentAnimation() {
        switch (currentState) {
            case IDLE:
                idleAnim.stop();
                break;
            case RUN:
                runAnim.stop();
                break;
            case JUMP:
                jumpAnim.stop();
                break;
            case CROUCH_IDLE:
                crouchIdleAnim.stop();
                break;
            case CROUCH_WALK:
                crouchWalkAnim.stop();
                break;
            case SHOOT:
                shootAnim.stop();
                break;
            case SHOOT_RUN:
                shootRunAnim.stop();
                break;
            case SHOOT_CROUCH:
                shootCrouchAnim.stop();
                break;
            case DEATH:
                deathAnim.stop();
                break;
        }
    }

    private void startCurrentAnimation() {
        switch (currentState) {
            case IDLE:
                idleAnim.play();
                break;
            case RUN:
                runAnim.play();
                break;
            case JUMP:
                jumpAnim.play();
                break;
            case CROUCH_IDLE:
                crouchIdleAnim.play();
                break;
            case CROUCH_WALK:
                crouchWalkAnim.play();
                break;
            case SHOOT:
                shootAnim.play();
                break;
            case SHOOT_RUN:
                shootRunAnim.play();
                break;
            case SHOOT_CROUCH:
                shootCrouchAnim.play();
                break;
            case DEATH:
                deathAnim.play();
                deathSound.seek(javafx.util.Duration.ZERO);
                deathSound.play();
                break;
        }
    }

    public void setLeftPressed(boolean pressed) {
        this.leftPressed = pressed;
    }
    public void setRightPressed(boolean pressed) {
        this.rightPressed = pressed;
    }
    public boolean getLeftPressed() {
        return this.leftPressed;
    }
    public boolean getRightPressed() {
        return this.rightPressed;
    }

    public void setCrouchPressed(boolean pressed) {
        if (isOnGround) {
            this.isCrouching = pressed;
        } else {
            this.isCrouching = false;
        }
    }

    public void setJumpPressed(boolean pressed) {
        this.jumpInputActive = pressed;
    }

    public void jump() {
        if (isOnGround) {
            velocityY = JUMP_POWER;
            isOnGround = false;
        }
    }

    public void setPositionY(double y) {
        this.positionY = y;
    }

    public void setPosition(double x, double y, double max, double min) {
        super.setPosition(x, y);
        this.maxBoundaryX = max;
        this.minBoundaryX = min;
        this.isRespawning = false;
    }

    private void respawn() {
        isRespawning = true;
        
        positionX = respawnX;
        positionY = -100;
        
        health = maxHealth;
        
        velocityY = 0;
        isOnGround = false;
        isCrouching = false;
        
        if (currentState == AnimationState.DEATH) {
            switchAnimation(AnimationState.IDLE);
        }
        
    }

    public double getVelocityY() {
        return velocityY;
    }
    public void setVelocityY(double velocityY) {
        this.velocityY = velocityY;
    }
    public double getGravity() {
        return GRAVITY;
    }
    public void setOnGround(boolean onGround) {
        this.isOnGround = onGround;
    }

    public boolean isOnGround() {
        return this.isOnGround;
    }

    public void revertToPrevX() {
        setPosition(prevPositionX, this.positionY);
    }

    public Rectangle2D getBounds() {
        if(currentState == AnimationState.DEATH) {
            return new Rectangle2D(0, 0 , 0, 0);
        }
        if (isCrouching) {
            double crouchHeight = CROUCH_HEIGHT + 15;
            double crouchWidth = COLLISION_WIDTH + 20;
            double reducedHeight = COLLISION_HEIGHT - crouchHeight;
            double crouchOffsetY = COLLISION_OFFSET_Y - 15;
            double flipAdjust = facingRight ? 0 : COLLISION_FLIP_ADJUST;
            return new Rectangle2D(positionX + COLLISION_OFFSET_X + flipAdjust, positionY + crouchOffsetY + reducedHeight, crouchWidth, crouchHeight);
        } else {
            double flipAdjust = facingRight ? 0 : COLLISION_FLIP_ADJUST;
            return new Rectangle2D(positionX + COLLISION_OFFSET_X + flipAdjust, positionY + COLLISION_OFFSET_Y, COLLISION_WIDTH, COLLISION_HEIGHT);
        }
    }

    public void setShootPressed(boolean pressed) {
        this.shootPressed = pressed;
    }

    public ArrayList<Bullet> getBullets() {
        return bullets;
    }

    public Weapon getCurrentWeapon() {
        return currentWeapon;
    }

    public void takeDamage(int damage) {


        hitSound.seek(javafx.util.Duration.ZERO);
        hitSound.play();
        super.takeDamage(damage);

        if (!isAlive()) {
            respawnX = positionX;
            respawnY = positionY;
        }
    }

}