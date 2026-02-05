import javafx.scene.Scene;
import javafx.scene.input.KeyCode;

public class InputManager {
    private boolean left;
    private boolean right;
    private boolean jump;
    private boolean crouch;
    private boolean shoot;
    private boolean reload;
    private boolean escape;
    private KeyBindings keyBindings;

    public InputManager(Scene scene) {
        keyBindings = KeyBindings.getInstance();
        
        scene.setOnKeyPressed(e -> {
            if (e.getCode() == keyBindings.getLeftKey()) {
                setLeft(true);
            } else if (e.getCode() == keyBindings.getRightKey()) {
                setRight(true);
            } else if (e.getCode() == keyBindings.getUpKey()) {
                setJump(true);
            } else if (e.getCode() == keyBindings.getDownKey()) {
                setCrouch(true);
            } else if (e.getCode() == keyBindings.getShootKey()) {
                setShoot(true);
            } else if (e.getCode() == KeyCode.ESCAPE) {
                setEscape(true);
            }
        });

        scene.setOnKeyReleased(e -> {
            if (e.getCode() == keyBindings.getLeftKey()) {
                setLeft(false);
            } else if (e.getCode() == keyBindings.getRightKey()) {
                setRight(false);
            } else if (e.getCode() == keyBindings.getUpKey()) {
                setJump(false);
            } else if (e.getCode() == keyBindings.getDownKey()) {
                setCrouch(false);
            } else if (e.getCode() == KeyCode.ESCAPE) {
                setEscape(false);
            } else if (e.getCode() == keyBindings.getShootKey()) {
                setShoot(false);
            }
        });
    }

    private void setLeft(boolean left) { this.left = left; }
    private void setRight(boolean right) { this.right = right; }
    private void setJump(boolean jump) { this.jump = jump; }
    private void setCrouch(boolean crouch) { this.crouch = crouch; }
    private void setShoot(boolean shoot) {this.shoot = shoot; }
    private void setReload(boolean reload) {this.reload = reload; }
    private void setEscape(boolean escape) { this.escape = escape; }

    public boolean isLeft() { return left; }
    public boolean isRight() { return right; }
    public boolean isJump() { return jump; }
    public boolean isCrouch() { return crouch; }
    public boolean isShoot() { return shoot; }
    public boolean isReload() { return reload; }
    public boolean isEscape() { return escape; }

}