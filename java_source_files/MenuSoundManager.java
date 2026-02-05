import javafx.scene.media.AudioClip;

public class MenuSoundManager {
    private AudioClip clickSound;
    private AudioClip hoverSound;

    public MenuSoundManager() {
        clickSound = null;
        hoverSound = null;
    }

    public void playClickSound() {
        if (clickSound != null) {
            clickSound.play();
        }
    }

    public void playHoverSound() {
        if (hoverSound != null) {
            hoverSound.play();
        }
    }

    public void stopSounds() {
        if (clickSound != null) {
            clickSound.stop();
        }
        if (hoverSound != null) {
            hoverSound.stop();
        }
    }
}
