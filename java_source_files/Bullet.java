import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.media.Media;
import javafx.scene.media.MediaPlayer;
import javafx.scene.shape.Rectangle;
import javafx.scene.paint.Color;
import javafx.geometry.Rectangle2D;

public class Bullet {
    private double positionX;
    private double positionY;
    private double velocityX;
    private int damage;
    private String firedBy;
    private boolean active;
    private ImageView bulletImage;
    private MediaPlayer shootSound;

    private static final double BULLET_SPEED = 8;
    private static final double BULLET_WIDTH = 10;
    private static final double BULLET_HEIGHT = 4;

    public Bullet(double startX, double startY, boolean facingRight, int damage , String firedBy) {

        this.positionX = startX;
        this.positionY = startY;
        this.velocityX = facingRight ? BULLET_SPEED : -BULLET_SPEED;
        this.damage = damage;
        this.firedBy = firedBy;
        this.active = true;
        loadAssets();
    }

    private void loadAssets() {
        AudioSettings audioSettings = AudioSettings.getInstance();
        double sfxVol = audioSettings.getSfxVolume();

        if(firedBy.equals("Player")) {
            Image image = new Image(getClass().getResourceAsStream("/Assets/Player/bullet.png"));
            bulletImage = new ImageView(image);
            bulletImage.setScaleX(2);
            bulletImage.setScaleY(2);

            Media shootMedia = new Media(getClass().getResource("/Assets/Player/Sounds/Shoot3.wav").toString());
            shootSound = new MediaPlayer(shootMedia);
            if (sfxVol == 0.0) {
                shootSound.setMute(true);
            } else {
                shootSound.setVolume(sfxVol);
            }

        }

        else if(firedBy.equals("Enemy")) {
            Image image = new Image(getClass().getResourceAsStream("/Assets/Enemies/EnemyBullet.png"));
            bulletImage = new ImageView(image);
            bulletImage.setScaleX(2.2);
            bulletImage.setScaleY(2.2);

            Media shootMedia = new Media(getClass().getResource("/Assets/Enemies/Sounds/Shoot1.wav").toString());
            shootSound = new MediaPlayer(shootMedia);
            if (sfxVol == 0.0) {
                shootSound.setMute(true);
            } else {
                shootSound.setVolume(sfxVol);
            }
        }
    }

    public void update(double cameraX) {
        if (!active) return;

        // Move da bullet
        positionX += velocityX;

        // Update da visual position
        bulletImage.setX(positionX - cameraX);
        bulletImage.setY(positionY);
    }

    public void deactivate() {
        this.active = false;
    }

    public boolean isActive() {
        return active;
    }

    public Rectangle2D getBounds() {
        return new Rectangle2D(positionX, positionY, BULLET_WIDTH, BULLET_HEIGHT);
    }

    public ImageView getBulletImage() {
        return bulletImage;
    }

    public int getDamage() {
        return damage;
    }

    public double getPositionX() {
        return positionX;
    }

    public double getPositionY() {
        return positionY;
    }

    // Check if bullet is offscreen :) :(
    public boolean isOutOfBounds(double levelWidth) {
        return positionX < 0 || positionX > levelWidth;
    }

    public void playSound() {
        shootSound.play();
    }
}