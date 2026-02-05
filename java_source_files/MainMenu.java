import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.effect.DropShadow;
import javafx.scene.effect.ColorAdjust;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.scene.text.Text;
import javafx.stage.Stage;

public class MainMenu extends VBox {
    private Stage stage;
    private Button startButton;
    private Button controlsButton;
    private Button exitButton;
    private MenuSoundManager soundManager;
    private static GameEngine currentGameEngine;

    public MainMenu(Stage stage) {
        this.stage = stage;
        this.soundManager = new MenuSoundManager();
        
        setPrefSize(1200, 750);
        setAlignment(Pos.CENTER);
        setSpacing(30);
        
        // theme
        setStyle("-fx-background-color: linear-gradient(to bottom, #1a1a1a, #0d0d0d);");

        // title
        Text title = new Text("METAL SLUG");
        title.setFont(UITheme.boldFont(120));
        title.setFill(Color.web("#ffcc00"));
        title.setStroke(Color.web("#ff8800"));
        title.setStrokeWidth(8);
        title.setStrokeLineCap(javafx.scene.shape.StrokeLineCap.ROUND);
        title.setStrokeLineJoin(javafx.scene.shape.StrokeLineJoin.ROUND);
        
        DropShadow shadow = new DropShadow();
        shadow.setOffsetY(10);
        shadow.setOffsetX(10);
        shadow.setColor(Color.web("#662200"));
        shadow.setRadius(15);
        shadow.setSpread(0.3);
        title.setEffect(shadow);

        Text subtitle = new Text("CLONE EDITION");
        subtitle.setFont(UITheme.boldFont(32));
        subtitle.setFill(Color.web("#ffaa00"));
        subtitle.setStroke(Color.web("#ff8800"));
        subtitle.setStrokeWidth(3);
        subtitle.setStrokeLineCap(javafx.scene.shape.StrokeLineCap.ROUND);
        subtitle.setStrokeLineJoin(javafx.scene.shape.StrokeLineJoin.ROUND);

        startButton = createMilitaryButton("START MISSION");
        controlsButton = createMilitaryButton("CONTROLS");
        exitButton = createMilitaryButton("EXIT");

        startButton.setOnAction(e -> {
            soundManager.playClickSound();
            startGame();
        });

        controlsButton.setOnAction(e -> {
            soundManager.playClickSound();
            showControlsMenu();
        });

        exitButton.setOnAction(e -> {
            soundManager.playClickSound();
            System.exit(0);
        });

        getChildren().addAll(title, subtitle, startButton, controlsButton, exitButton);
    }

    private Button createMilitaryButton(String text) {
        Button button = new Button(text);
        button.setPrefSize(300, 60);
        button.setFont(UITheme.boldFont(22));
        
        String defaultStyle = "-fx-background-color: linear-gradient(to bottom, #ffaa00, #ff8800); " +
                "-fx-text-fill: #1a1a1a; " +
                "-fx-border-color: #ff6600; " +
                "-fx-border-width: 4px; " +
                "-fx-border-radius: 0px; " +
                "-fx-background-radius: 0px; " +
                "-fx-cursor: hand; " +
                "-fx-font-weight: bold; " +
                "-fx-padding: 10px;";
        
        String hoverStyle = "-fx-background-color: linear-gradient(to bottom, #ffcc00, #ffaa00); " +
                "-fx-text-fill: #000000; " +
                "-fx-border-color: #ffff00; " +
                "-fx-border-width: 4px; " +
                "-fx-border-radius: 0px; " +
                "-fx-background-radius: 0px; " +
                "-fx-cursor: hand; " +
                "-fx-font-weight: bold; " +
                "-fx-padding: 10px;";
        
        button.setStyle(defaultStyle);
        
        button.setOnMouseEntered(e -> {
            button.setStyle(hoverStyle);
            soundManager.playClickSound();
        });
        
        button.setOnMouseExited(e -> button.setStyle(defaultStyle));
        
        return button;
    }

    private void startGame() {
        try {
            soundManager.stopSounds();
            BGMManager.stop();
            
            if (currentGameEngine != null) {
                currentGameEngine.stopGame();
                currentGameEngine = null;
                try {
                    Thread.sleep(100);
                } catch (InterruptedException ex) {}
            }
            
            currentGameEngine = new GameEngine();
            Scene gameScene = new Scene(currentGameEngine);

            InputManager inputManager = new InputManager(gameScene);
            currentGameEngine.setInputManager(inputManager);
            currentGameEngine.setStage(stage);

            stage.setScene(gameScene);
            gameScene.getRoot().requestFocus();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void showControlsMenu() {
        Scene currentMenuScene = stage.getScene();
        ControlsMenu controlsMenu = new ControlsMenu(stage, currentMenuScene);
        Scene controlsScene = new Scene(controlsMenu);
        stage.setScene(controlsScene);
    }
}
