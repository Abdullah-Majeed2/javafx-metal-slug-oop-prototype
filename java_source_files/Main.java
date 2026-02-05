import javafx.application.Application;
import javafx.scene.Scene;
import javafx.stage.Stage;

public class Main extends Application{
    public static void main(String[] args){
        launch(args);
    }

    public void start(Stage stage) {
        try {
            // Set up theme and font
            UITheme.init();
            // Create main menu
            MainMenu mainMenu = new MainMenu(stage);
            Scene menuScene = new Scene(mainMenu);

            stage.setScene(menuScene);
            stage.setTitle("Metal Slug Clone");
            stage.setResizable(false);

            stage.show();

            menuScene.getRoot().requestFocus();

        } catch (Exception e){
            e.printStackTrace();
        }
    }
}
