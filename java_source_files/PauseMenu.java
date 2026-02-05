import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.effect.DropShadow;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.scene.text.Text;
import javafx.stage.Stage;

public class PauseMenu extends VBox {
    private Stage stage;
    private Scene gameScene;
    private GameEngine gameEngine;
    private MenuSoundManager soundManager;
    private Button resumeButton;
    private Button controlsButton;
    private Button exitButton;

    public PauseMenu(Stage stage, Scene gameScene, GameEngine gameEngine) {
        this.stage = stage;
        this.gameScene = gameScene;
        this.gameEngine = gameEngine;
        this.soundManager = new MenuSoundManager();

        setPrefSize(1200, 750);
        setAlignment(Pos.CENTER);
        setSpacing(30);
        
        setStyle("-fx-background-color: rgba(10, 10, 10, 0.95);");

        Text title = new Text("GAME PAUSED");
        title.setFont(UITheme.boldFont(100));
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

        resumeButton = createMilitaryButton("RESUME");
        controlsButton = createMilitaryButton("CONTROLS");
        exitButton = createMilitaryButton("EXIT TO MENU");

        resumeButton.setOnAction(e -> {
            soundManager.playClickSound();
            resumeGame();
        });

        controlsButton.setOnAction(e -> {
            soundManager.playClickSound();
            showControlsMenu();
        });

        exitButton.setOnAction(e -> {
            soundManager.playClickSound();
            exitToMainMenu();
        });

        getChildren().addAll(title, resumeButton, controlsButton, exitButton);
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

    private void resumeGame() {
        soundManager.stopSounds();
        gameEngine.resumeGame();
        stage.setScene(gameScene);
    }

    private void showControlsMenu() {
        Scene currentPauseScene = stage.getScene();
        ControlsMenu controlsMenu = new ControlsMenu(stage, currentPauseScene, gameEngine);
        Scene controlsScene = new Scene(controlsMenu);
        stage.setScene(controlsScene);
    }

    private void exitToMainMenu() {
        soundManager.stopSounds();
        
        gameEngine.stopGame();
        
        try {
            Thread.sleep(150);
        } catch (InterruptedException ex) {}
        
        MainMenu mainMenu = new MainMenu(stage);
        Scene menuScene = new Scene(mainMenu);
        stage.setScene(menuScene);
    }
}
