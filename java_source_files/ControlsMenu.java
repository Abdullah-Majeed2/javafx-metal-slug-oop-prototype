import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.ScrollPane;
import javafx.scene.control.Slider;
import javafx.scene.input.KeyCode;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.scene.text.Text;
import javafx.stage.Stage;

public class ControlsMenu extends ScrollPane {
    private Stage stage;
    private Scene menuScene;
    private MenuSoundManager soundManager;
    private KeyBindings keyBindings;
    private GameEngine gameEngine;
    
    private Button leftKeyButton;
    private Button rightKeyButton;
    private Button upKeyButton;
    private Button downKeyButton;
    private Button shootKeyButton;
    private Button currentListeningButton;
    private Slider bgmSlider;
    private Slider sfxSlider;

    public ControlsMenu(Stage stage, Scene menuScene) {
        this(stage, menuScene, null);
    }

    public ControlsMenu(Stage stage, Scene menuScene, GameEngine gameEngine) {
        this.stage = stage;
        this.menuScene = menuScene;
        this.gameEngine = gameEngine;
        this.soundManager = new MenuSoundManager();
        this.keyBindings = KeyBindings.getInstance();
        
        VBox content = new VBox();
        content.setAlignment(Pos.CENTER);
        content.setSpacing(30);
        content.setPadding(new Insets(30));
        content.setStyle("-fx-background-color: linear-gradient(to bottom, #1a1a1a, #0d0d0d);");
        
        setPrefSize(1200, 750);
        setFitToWidth(true);
        setStyle("-fx-background: #0d0d0d; -fx-background-color: #0d0d0d;");
        setHbarPolicy(ScrollPane.ScrollBarPolicy.NEVER);
        setVbarPolicy(ScrollPane.ScrollBarPolicy.AS_NEEDED);

        Text title = new Text("CONTROLS CONFIGURATION");
        title.setFont(UITheme.boldFont(56));
        title.setFill(Color.web("#ffcc00"));
        title.setStroke(Color.web("#ff8800"));
        title.setStrokeWidth(5);
        title.setStrokeLineCap(javafx.scene.shape.StrokeLineCap.ROUND);
        title.setStrokeLineJoin(javafx.scene.shape.StrokeLineJoin.ROUND);

        Label instructions = new Label("Click on a button and press the key you want to assign");
        instructions.setFont(UITheme.boldFont(16));
        instructions.setTextFill(Color.web("#ffaa00"));

        GridPane grid = new GridPane();
        grid.setAlignment(Pos.CENTER);
        grid.setHgap(30);
        grid.setVgap(20);

        addControlRow(grid, 0, "Move Left:", "leftKeyButton");
        addControlRow(grid, 1, "Move Right:", "rightKeyButton");
        addControlRow(grid, 2, "Jump:", "upKeyButton");
        addControlRow(grid, 3, "Crouch:", "downKeyButton");
        addControlRow(grid, 4, "Shoot:", "shootKeyButton");

        if (gameEngine == null) {
            Text audioTitle = new Text("AUDIO SETTINGS");
            audioTitle.setFont(UITheme.boldFont(40));
            audioTitle.setFill(Color.web("#ffcc00"));
            audioTitle.setStroke(Color.web("#ff8800"));
            audioTitle.setStrokeWidth(4);
            audioTitle.setStrokeLineCap(javafx.scene.shape.StrokeLineCap.ROUND);
            audioTitle.setStrokeLineJoin(javafx.scene.shape.StrokeLineJoin.ROUND);

            bgmSlider = new Slider(0, 1, AudioSettings.getInstance().getBgmVolume());
            HBox bgmBox = createVolumeControl("BGM Volume:", bgmSlider, true);
            
            sfxSlider = new Slider(0, 1, AudioSettings.getInstance().getSfxVolume());
            HBox sfxBox = createVolumeControl("SFX Volume:", sfxSlider, false);

            Button backButton = createMilitaryButton("BACK TO MENU");
            backButton.setOnAction(e -> {
                soundManager.playClickSound();
                returnToMainMenu();
            });

            Button resetButton = createMilitaryButton("RESET TO DEFAULT");
            resetButton.setOnAction(e -> {
                soundManager.playClickSound();
                resetAllSettings();
            });

            content.getChildren().addAll(title, instructions, grid, audioTitle, bgmBox, sfxBox, resetButton, backButton);
        } else {
            Button backButton = createMilitaryButton("BACK TO MENU");
            backButton.setOnAction(e -> {
                soundManager.playClickSound();
                returnToMainMenu();
            });

            Button resetButton = createMilitaryButton("RESET TO DEFAULT");
            resetButton.setOnAction(e -> {
                soundManager.playClickSound();
                resetAllSettings();
            });

            content.getChildren().addAll(title, instructions, grid, resetButton, backButton);
        }
        setContent(content);
    }

    private void addControlRow(GridPane grid, int row, String labelText, String buttonFieldName) {
        Label label = new Label(labelText);
        label.setFont(UITheme.boldFont(18));
        label.setTextFill(Color.web("#ffcc00"));
        label.setPrefWidth(200);

        Button button = createControlButton(buttonFieldName);
        
        switch (buttonFieldName) {
            case "leftKeyButton": leftKeyButton = button; break;
            case "rightKeyButton": rightKeyButton = button; break;
            case "upKeyButton": upKeyButton = button; break;
            case "downKeyButton": downKeyButton = button; break;
            case "shootKeyButton": shootKeyButton = button; break;
        }

        grid.add(label, 0, row);
        grid.add(button, 1, row);
    }

    private Button createControlButton(String action) {
        Button button = new Button();
        button.setPrefSize(200, 50);
        button.setFont(UITheme.boldFont(16));
        
        String defaultStyle = "-fx-background-color: linear-gradient(to bottom, #ffaa00, #ff8800); " +
                "-fx-text-fill: #1a1a1a; " +
                "-fx-border-color: #ff6600; " +
                "-fx-border-width: 2px; " +
                "-fx-border-radius: 0px; " +
                "-fx-background-radius: 0px; " +
                "-fx-cursor: hand; " +
                "-fx-padding: 8px;";
        
        button.setStyle(defaultStyle);
        
        updateButtonLabel(button, action);
        
        button.setOnAction(e -> {
            soundManager.playClickSound();
            startListeningForKey(button, action);
        });
        
        return button;
    }

    private void updateButtonLabel(Button button, String action) {
        KeyCode key = null;
        switch (action) {
            case "leftKeyButton": key = keyBindings.getLeftKey(); break;
            case "rightKeyButton": key = keyBindings.getRightKey(); break;
            case "upKeyButton": key = keyBindings.getUpKey(); break;
            case "downKeyButton": key = keyBindings.getDownKey(); break;
            case "shootKeyButton": key = keyBindings.getShootKey(); break;
        }
        button.setText(key != null ? key.getName() : "NONE");
    }

    private void updateButtonLabels() {
        updateButtonLabel(leftKeyButton, "leftKeyButton");
        updateButtonLabel(rightKeyButton, "rightKeyButton");
        updateButtonLabel(upKeyButton, "upKeyButton");
        updateButtonLabel(downKeyButton, "downKeyButton");
        updateButtonLabel(shootKeyButton, "shootKeyButton");
    }

    private void startListeningForKey(Button button, String action) {
        currentListeningButton = button;
        button.setText("PRESS KEY...");
        button.setStyle("-fx-background-color: #ffcc00; " +
                "-fx-text-fill: #1a1a1a; " +
                "-fx-border-color: #ff8800; " +
                "-fx-border-width: 3px; " +
                "-fx-border-radius: 8px; " +
                "-fx-background-radius: 8px;");
        
        Scene scene = getScene();
        if (scene != null) {
            scene.setOnKeyPressed(e -> {
                KeyCode newKey = e.getCode();
                
                switch (action) {
                    case "leftKeyButton": keyBindings.setLeftKey(newKey); break;
                    case "rightKeyButton": keyBindings.setRightKey(newKey); break;
                    case "upKeyButton": keyBindings.setUpKey(newKey); break;
                    case "downKeyButton": keyBindings.setDownKey(newKey); break;
                    case "shootKeyButton": keyBindings.setShootKey(newKey); break;
                }
                
                button.setText(newKey.getName());
                button.setStyle("-fx-background-color: linear-gradient(to bottom, #ffaa00, #ff8800); " +
                        "-fx-text-fill: #1a1a1a; " +
                        "-fx-border-color: #ff6600; " +
                        "-fx-border-width: 2px; " +
                        "-fx-border-radius: 8px; " +
                        "-fx-background-radius: 8px; " +
                        "-fx-cursor: hand;");
                
                currentListeningButton = null;
                scene.setOnKeyPressed(null); // Remove listener
            });
        }
    }

    private Button createMilitaryButton(String text) {
        Button button = new Button(text);
        button.setPrefSize(250, 50);
        button.setFont(UITheme.boldFont(16));
        
        String defaultStyle = "-fx-background-color: linear-gradient(to bottom, #ffaa00, #ff8800); " +
                "-fx-text-fill: #1a1a1a; " +
                "-fx-border-color: #ff6600; " +
                "-fx-border-width: 3px; " +
                "-fx-border-radius: 0px; " +
                "-fx-background-radius: 0px; " +
                "-fx-cursor: hand; " +
                "-fx-padding: 8px;";
        
        String hoverStyle = "-fx-background-color: linear-gradient(to bottom, #ffcc00, #ffaa00); " +
                "-fx-text-fill: #000000; " +
                "-fx-border-color: #ffff00; " +
                "-fx-border-width: 3px; " +
                "-fx-border-radius: 0px; " +
                "-fx-background-radius: 0px; " +
                "-fx-cursor: hand; " +
                "-fx-padding: 8px;";
        
        button.setStyle(defaultStyle);
        button.setOnMouseEntered(e -> {
            button.setStyle(hoverStyle);
            soundManager.playClickSound();
        });
        button.setOnMouseExited(e -> button.setStyle(defaultStyle));
        
        return button;
    }

    private HBox createVolumeControl(String labelText, Slider slider, boolean isBGM) {
        HBox box = new HBox(20);
        box.setAlignment(Pos.CENTER);
        
        Label label = new Label(labelText);
        label.setFont(UITheme.boldFont(18));
        label.setTextFill(Color.web("#00ff00"));
        label.setPrefWidth(150);
        
        slider.setPrefWidth(300);
        slider.setShowTickMarks(true);
        slider.setShowTickLabels(false);
        slider.setMajorTickUnit(0.25);
        slider.setBlockIncrement(0.1);
        
        slider.setStyle("-fx-control-inner-background: #ff6600;");
        
        Label valueLabel = new Label(String.format("%d%%", (int)(slider.getValue() * 100)));
        valueLabel.setFont(UITheme.boldFont(16));
        valueLabel.setTextFill(Color.web("#ffff00"));
        valueLabel.setPrefWidth(50);
        
        slider.valueProperty().addListener((obs, oldVal, newVal) -> {
            double volume = newVal.doubleValue();
            if (volume < 0.02) {
                volume = 0.0;
            }
            
            int displayPercent = (int)(volume * 100);
            valueLabel.setText(displayPercent == 0 ? "MUTED" : displayPercent + "%");
            
            if (isBGM) {
                AudioSettings.getInstance().setBgmVolume(volume);
            } else {
                AudioSettings.getInstance().setSfxVolume(volume);
            }
        });
        
        box.getChildren().addAll(label, slider, valueLabel);
        return box;
    }

    private void resetAllSettings() {
        keyBindings.resetToDefaults();
        updateButtonLabels();
        
        if (bgmSlider != null) {
            AudioSettings.getInstance().setBgmVolume(0.3);
            AudioSettings.getInstance().setSfxVolume(0.5);
            bgmSlider.setValue(0.3);
            sfxSlider.setValue(0.5);
        }
    }

    private void returnToMainMenu() {
        soundManager.stopSounds();
        stage.setScene(menuScene);
    }
}
