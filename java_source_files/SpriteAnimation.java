import javafx.animation.AnimationTimer;
import javafx.geometry.Rectangle2D;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import java.util.ArrayList;

public class SpriteAnimation {
    private final ImageView imageView;
    private final Image spriteSheet;
    private final ArrayList<Rectangle2D> frames;
    private final long frameDurationNs;

    private int currentFrame = 0;
    private long lastUpdateTime = 0;
    private boolean isPlaying = false;
    private int loopCount = 0;

    private final AnimationTimer animationTimer;

    public SpriteAnimation(ImageView imageView, Image spriteSheet, ArrayList<Rectangle2D> frames, double fps) {
        this.imageView = imageView;
        this.spriteSheet = spriteSheet;
        this.frames = frames;
        this.frameDurationNs = (long)(1_000_000_000.0 / fps);

        this.animationTimer = new AnimationTimer() {
            @Override
            public void handle(long now) {
                if (!isPlaying) return;

                long elapsedNs = now - lastUpdateTime;

                if (elapsedNs >= frameDurationNs) {
                    currentFrame = currentFrame + 1;

                    if (currentFrame >= frames.size()) {
                        currentFrame = 0;
                        loopCount++;
                    }

                    Rectangle2D frame = frames.get(currentFrame);
                    imageView.setViewport(frame);

                    lastUpdateTime = now;
                }
            }
        };

        animationTimer.start();
    }

    public void play() {
        if (frames == null || frames.isEmpty()) {
            System.err.println("Cannot play - no frames available");
            return;
        }

        imageView.setImage(spriteSheet);

        currentFrame = 0;
        loopCount = 0;
        lastUpdateTime = 0;
        isPlaying = true;

        imageView.setViewport(frames.get(0));
    }

    public void stop() {
        isPlaying = false;
        currentFrame = 0;
        loopCount = 0;
        lastUpdateTime = 0;

        if (frames != null && !frames.isEmpty()) {
            imageView.setViewport(frames.get(0));
        }
    }

    public void setToFirstFrame() {
        if (frames != null && !frames.isEmpty()) {
            imageView.setImage(spriteSheet);
            imageView.setViewport(frames.get(0));
        }
    }

    public boolean isPlaying() {
        return isPlaying;
    }

    public int getLoopCount() {
        return loopCount;
    }
}