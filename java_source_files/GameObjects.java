import javafx.scene.image.ImageView;

public abstract class GameObjects {
    protected double positionX;
    protected double positionY;
    protected ImageView spriteView;

    public GameObjects(double x, double y) {
        this.positionX = x;
        this.positionY = y;
    }

    public abstract void update(double cameraX , double sceneWidth);

    public double getPositionX() {
        return positionX;
    }

    public double getPositionY() {
        return positionY;
    }

    public ImageView getSpriteView() {
        return spriteView;
    }

    public void setPosition(double x, double y) {
        this.positionX = x;
        this.positionY = y;
        if(spriteView != null) {
            spriteView.setX(x);
            spriteView.setY(y);
        }
    }
}
