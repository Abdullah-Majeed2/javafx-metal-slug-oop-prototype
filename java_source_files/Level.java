import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.media.MediaPlayer;

import java.util.ArrayList;


public abstract class Level {

    private Image background;
    private ImageView bgview;
    private double levelWidth;
    private double levelHeight;
    private double playerStartX;
    private double playerStartY;
    protected ArrayList<GameObjects> entities;
    protected MediaPlayer bgm;
    protected MediaPlayer introduction;


    public Level(String bgPath , double x , double y , double startX , double startY){
        background = new Image(getClass().getResourceAsStream(bgPath));
        bgview = new ImageView(background);

        this.playerStartX = startX;
        this.playerStartY = startY;
        this.levelWidth = x;
        this.levelHeight = y;
        this.entities = new ArrayList<>();

        bgview.setLayoutY(-65);
        bgview.setFitWidth(levelWidth);
        bgview.setFitHeight(levelHeight);

        spawnGameObjects();
    }

    protected abstract void spawnGameObjects();
    protected abstract void loadSFX();

    public ImageView getBackground(){

        return bgview;
    }

    public ArrayList<GameObjects> getGameObjects() {
        return entities;
    }

    public double getLevelWidth(){
        return levelWidth;
    }

    public double getLevelHeight(){
        return levelHeight;
    }

    public double getPlayerStartX(){
        return playerStartX;
    }

    public double getPlayerStartY(){
        return playerStartY;
    }

    public void stopMusic() {
        BGMManager.stop();
        // Clear local references if any were set in prior runs
        bgm = null;
        introduction = null;
    }

    public void updateVolume() {
        AudioSettings audioSettings = AudioSettings.getInstance();
        double bgmVol = audioSettings.getBgmVolume();
        
        BGMManager.setVolume(bgmVol);
    }
}
