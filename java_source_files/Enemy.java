import javafx.geometry.Rectangle2D;
import javafx.scene.media.Media;
import javafx.scene.media.MediaPlayer;

public abstract class Enemy extends Character {
    protected int damage;
    protected boolean isActive;
    protected MediaPlayer deathSound;

    public Enemy(double x, double y, int health, double speed, int damage) {
        super(x, y, health, speed);
        this.damage = damage;
        this.isActive = false;
        loadDeathSound();
    }

    public abstract Rectangle2D getBounds();

    public void setActive(boolean active) {
        this.isActive = active;
    }

    public boolean isActive() {
        return isActive;
    }

    public boolean isDead() {
        return !isAlive();
    }


    public boolean isReadyToRemove() {
        return isDead();
    }

    public int getDamage() {
        return damage;
    }

    protected void loadDeathSound() {
        AudioSettings audioSettings = AudioSettings.getInstance();
        double sfxVol = audioSettings.getSfxVolume();
        
        Media media = new Media(getClass().getResource("/Assets/Enemies/Sounds/Death.wav").toString());
        deathSound = new MediaPlayer(media);
        if (sfxVol == 0.0) {
            deathSound.setMute(true);
        } else {
            deathSound.setVolume(sfxVol);
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
    }
}