import javafx.scene.input.KeyCode;

public class KeyBindings {
    private static KeyBindings instance;
    
    private KeyCode leftKey;
    private KeyCode rightKey;
    private KeyCode upKey;
    private KeyCode downKey;
    private KeyCode shootKey;

    private KeyBindings() {
        resetToDefaults();
    }

    public static KeyBindings getInstance() {
        if (instance == null) {
            instance = new KeyBindings();
        }
        return instance;
    }

    public void resetToDefaults() {
        leftKey = KeyCode.LEFT;
        rightKey = KeyCode.RIGHT;
        upKey = KeyCode.UP;
        downKey = KeyCode.DOWN;
        shootKey = KeyCode.X;
    }

    public KeyCode getLeftKey() { return leftKey; }
    public KeyCode getRightKey() { return rightKey; }
    public KeyCode getUpKey() { return upKey; }
    public KeyCode getDownKey() { return downKey; }
    public KeyCode getShootKey() { return shootKey; }

    public void setLeftKey(KeyCode key) { this.leftKey = key; }
    public void setRightKey(KeyCode key) { this.rightKey = key; }
    public void setUpKey(KeyCode key) { this.upKey = key; }
    public void setDownKey(KeyCode key) { this.downKey = key; }
    public void setShootKey(KeyCode key) { this.shootKey = key; }
}
