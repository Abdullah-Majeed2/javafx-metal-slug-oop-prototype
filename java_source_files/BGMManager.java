import javafx.scene.media.Media;
import javafx.scene.media.MediaPlayer;

public class BGMManager {
    private static MediaPlayer intro;
    private static MediaPlayer bgm;

    public static void start(String introPath, String bgmPath, double volume) {
        stop();

        Media introMedia = new Media(BGMManager.class.getResource(introPath).toString());
        intro = new MediaPlayer(introMedia);
        intro.setVolume(volume);

        Media bgmMedia = new Media(BGMManager.class.getResource(bgmPath).toString());
        bgm = new MediaPlayer(bgmMedia);
        bgm.setCycleCount(MediaPlayer.INDEFINITE);
        bgm.setVolume(volume);

        intro.setOnEndOfMedia(() -> {
            double v = AudioSettings.getInstance().getBgmVolume();
            if (bgm != null && v > 0.0) {
                bgm.play();
            }
        });

        if (volume > 0.0) {
            intro.play();
        }
    }

    public static void setVolume(double volume) {
        if (intro != null) {
            if (volume == 0.0) {
                if (intro.getStatus() == MediaPlayer.Status.PLAYING) intro.pause();
                intro.setVolume(0.0);
            } else {
                intro.setVolume(volume);
            }
        }
        if (bgm != null) {
            if (volume == 0.0) {
                if (bgm.getStatus() == MediaPlayer.Status.PLAYING) bgm.pause();
                bgm.setVolume(0.0);
            } else {
                bgm.setVolume(volume);
                if (bgm.getStatus() == MediaPlayer.Status.PAUSED && (intro == null || intro.getStatus() != MediaPlayer.Status.PLAYING)) {
                    bgm.play();
                }
            }
        }
    }

    public static void stop() {
        if (intro != null) {
            try {
                intro.setOnEndOfMedia(null);
                intro.stop();
            } catch (Exception ignored) {}
            try { intro.dispose(); } catch (Exception ignored) {}
            intro = null;
        }
        if (bgm != null) {
            try { bgm.stop(); } catch (Exception ignored) {}
            try { bgm.dispose(); } catch (Exception ignored) {}
            bgm = null;
        }
    }
}
