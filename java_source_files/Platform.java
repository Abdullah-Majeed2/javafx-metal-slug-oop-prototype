
import javafx.scene.image.ImageView;
import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;
import javafx.geometry.Rectangle2D;

public class Platform extends GameObjects {
    private double width;
    private double height;
    private Rectangle collisionBox;

    public Platform(double x , double y , double width, double height) {
        super(x, y);
        this.width = width;
        this.height = height;

        this.collisionBox = new Rectangle(width, height);
        this.collisionBox.setFill(Color.TRANSPARENT);

        this.spriteView = new ImageView();
    }

    @Override
    public void update(double cameraX, double sceneWidth) {
        if(collisionBox != null) {
            collisionBox.setX(positionX - cameraX);
            collisionBox.setY(positionY);
        }
    }

    public Rectangle2D getBounds(){
        return new Rectangle2D(positionX, positionY, width, height) {
        };
    }

    public Rectangle getCollisionBox() {
        return collisionBox;
    }

    public double getWidth() {
        return width;
    }

    public double getHeight() {
        return height;
    }
}
