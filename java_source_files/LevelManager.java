
public class LevelManager {

    private Level currentLevel;
    private int currentLevelNumber;


    public LevelManager(){
        currentLevelNumber = 1;
        loadLevel(currentLevelNumber);
    }

    public void loadLevel(int levelNumber){
        switch (levelNumber){
            case 1:
                this.currentLevel = new Level1();

            default:
                this.currentLevel = new Level1();
                levelNumber = 1;
        }

        this.currentLevelNumber = levelNumber;
    }

    public Level getCurrentLevel(){
        return currentLevel;
    }

    public int getCurrentLevelNumber(){
        return currentLevelNumber;
    }

}

