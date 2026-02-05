import javafx.animation.AnimationTimer;
import javafx.scene.layout.Pane;
import java.util.ArrayList;


public class GameEngine extends Pane{

    private final double SCENE_WIDTH = 1200;
    private final double SCENE_HEIGHT = 750;

    private double cameraX;
    private Player player;
    private LevelManager levelManager;
    private InputManager inputManager;
    private ArrayList<Enemy> enemies;
    private ArrayList<POW> pow;
    private AnimationTimer gameloop;
    private boolean isPaused;
    private javafx.stage.Stage stage;
    private boolean escapePressed;

    public GameEngine(){
        cameraX = 0;
        isPaused = false;
        setPrefSize(SCENE_WIDTH,SCENE_HEIGHT);

        levelManager = new LevelManager();
        Level currentLevel = levelManager.getCurrentLevel();

        player = new Player(currentLevel.getPlayerStartX(),currentLevel.getPlayerStartY() , currentLevel.getLevelWidth());

        getChildren().addAll(currentLevel.getBackground() , player.getSpriteView());


        enemies = new ArrayList<>();
        pow = new ArrayList<>();
        addLevelPlatforms(currentLevel);
        addLevelEnemies(currentLevel);
        addLevelPOW(currentLevel);

        setupGameLoop();
    }

    public void setInputManager(InputManager manager) {
        this.inputManager = manager;
    }

    public void setStage(javafx.stage.Stage stage) {
        this.stage = stage;
    }

    private void addLevelPlatforms(Level currentLevel){
        ArrayList<GameObjects> entities = currentLevel.getGameObjects();
        for( GameObjects entity : entities){
            if(entity instanceof Platform){
                Platform platform = (Platform) entity;
                getChildren().add(platform.getCollisionBox());
            }
        }
    }

    private void addLevelEnemies(Level currentLevel){
        ArrayList<GameObjects> entities = currentLevel.getGameObjects();
        for( GameObjects entity : entities){
            if(entity instanceof Enemy){
                Enemy enemy = (Enemy) entity;
                enemies.add(enemy);
                getChildren().add(enemy.getSpriteView());

                // Add hitbox rectangles p-)
                if(enemy instanceof BasicEnemy){
                    BasicEnemy basic = (BasicEnemy) enemy;
                } else if(enemy instanceof ShieldedEnemy){
                    ShieldedEnemy shielded = (ShieldedEnemy) enemy;
                }
            }
        }
    }

    private void addLevelPOW(Level currentLevel){
        ArrayList<GameObjects> entities = currentLevel.getGameObjects();
        for( GameObjects entity : entities){
            if(entity instanceof POW){
                POW prisoner = (POW) entity;
                pow.add(prisoner);
                getChildren().add(prisoner.getSpriteView());
            }
        }
    }

    public void changeLevel(int levelNumber){
        getChildren().remove(levelManager.getCurrentLevel().getBackground());

        Level oldLevel = levelManager.getCurrentLevel();
        ArrayList<GameObjects> entities = oldLevel.getGameObjects();
        for (GameObjects entity : entities) {
            if (entity instanceof Platform) {
                Platform platform = (Platform) entity;
                getChildren().remove(platform.getCollisionBox());
            }
            if (entity instanceof Enemy) {
                Enemy enemy = (Enemy) entity;
                getChildren().remove(enemy.getSpriteView());

                // Remove hitboxes ;(
                if (enemy instanceof BasicEnemy) {
                    BasicEnemy basic = (BasicEnemy) enemy;
                } else if (enemy instanceof ShieldedEnemy) {
                    ShieldedEnemy shielded = (ShieldedEnemy) enemy;
                }

            }
            if (entity instanceof POW) {
                POW prisoner = (POW) entity;
                getChildren().remove(prisoner.getSpriteView());
            }
        }

        enemies.clear();

        levelManager.loadLevel(levelNumber);
        Level currentLevel = levelManager.getCurrentLevel();
        getChildren().add(currentLevel.getBackground());
        addLevelPlatforms(currentLevel);
        addLevelEnemies(currentLevel);
        addLevelPOW(currentLevel);

        player.setPosition(currentLevel.getPlayerStartX(), currentLevel.getPlayerStartY() ,  currentLevel.getLevelWidth() , 0);
        cameraX = 0;
    }

    private void setupGameLoop() {
        gameloop = new AnimationTimer() {
            public void handle(long now) {
                if (!isPaused) {
                    update();
                }
            }
        };
        gameloop.start();
    }

    public void pauseGame() {
        isPaused = true;
    }

    public void resumeGame() {
        isPaused = false;
    }

    public void stopGame() {
        if (gameloop != null) {
            gameloop.stop();
        }
        Level currentLevel = levelManager.getCurrentLevel();
        if (currentLevel != null) {
            currentLevel.stopMusic();
        }
    }

    private void showPauseMenu() {
        if (stage != null) {
            pauseGame();
            javafx.scene.Scene currentScene = stage.getScene();
            PauseMenu pauseMenu = new PauseMenu(stage, currentScene, this);
            javafx.scene.Scene pauseScene = new javafx.scene.Scene(pauseMenu);
            stage.setScene(pauseScene);
        }
    }

    public void updateVolume() {
        Level currentLevel = levelManager.getCurrentLevel();
        if (currentLevel != null) {
            currentLevel.updateVolume();
        }
        if (player != null) {
            player.updateSoundVolume();
        }
        for (Enemy enemy : enemies) {
            enemy.updateSoundVolume();
        }
    }

    private void update() {
        if (inputManager.isEscape() && !escapePressed) {
            escapePressed = true;
            showPauseMenu();
            return;
        }
        if (!inputManager.isEscape()) {
            escapePressed = false;
        }

        player.setLeftPressed(inputManager.isLeft());
        player.setRightPressed(inputManager.isRight());
        player.setJumpPressed(inputManager.isJump());
        player.setCrouchPressed(inputManager.isCrouch());
        player.setShootPressed(inputManager.isShoot());

        player.update(cameraX ,SCENE_WIDTH);

        PhysicsManager.updatePlayer(player, levelManager.getCurrentLevel());

        updateEnemies();
        CollisionManager.checkBulletCollisions(player, enemies, cameraX, SCENE_WIDTH);
        CollisionManager.checkMeleeCollisions(player, enemies);

        updatePOW();

        updatePlatforms();
        updateAllBullets();
        updateCamera();
    }

    private void updateCamera() {
        double targetCameraX = player.getPositionX() - SCENE_WIDTH / 3.5;

        if(targetCameraX > cameraX) {
            double minCameraX = 0;
            double maxCameraX = (levelManager.getCurrentLevel().getLevelWidth() - 20) - SCENE_WIDTH;

            cameraX = Math.max(minCameraX, Math.min(targetCameraX, maxCameraX));
        }

        levelManager.getCurrentLevel().getBackground().setTranslateX(-cameraX);
        player.getSpriteView().setX(player.getPositionX() - cameraX);
    }

    private void updatePlatforms() {
        Level currentLevel = levelManager.getCurrentLevel();
        ArrayList<GameObjects> entities = currentLevel.getGameObjects();
        for (GameObjects entity : entities) {
            if (entity instanceof Platform) {
                entity.update(cameraX ,SCENE_WIDTH);
            }
        }
    }

    private void updateEnemies() {
        for (int i = enemies.size() - 1; i >= 0; i--) {
            Enemy enemy = enemies.get(i);

            if (!enemy.isActive() && shouldActivate(enemy)) {
                enemy.setActive(true);
            }

            enemy.update(cameraX, SCENE_WIDTH);

            if (enemy.isActive()) {
                if (enemy instanceof BasicEnemy) {
                    BasicEnemy basic = (BasicEnemy) enemy;
                    basic.updateBehavior(player.getPositionX());
                } else if (enemy instanceof ShieldedEnemy) {
                    ShieldedEnemy shielded = (ShieldedEnemy) enemy;
                    shielded.updateBehavior(player);
                }
            }

            if (enemy.isDead()) {
                boolean readyToRemove = enemy.isReadyToRemove();
                if (!readyToRemove) {
                    continue;
                }
                if(enemy instanceof BasicEnemy){
                    BasicEnemy basic = (BasicEnemy) enemy;
                    ArrayList<Bullet> enemyBullets = basic.getBullets();

                    for (Bullet bullet : enemyBullets) {
                        getChildren().remove(bullet.getBulletImage());
                    }
                    enemyBullets.clear();

                } else if(enemy instanceof ShieldedEnemy){
                    ShieldedEnemy shielded = (ShieldedEnemy) enemy;
                }

                getChildren().remove(enemy.getSpriteView());
                enemies.remove(i);
            }
        }
    }

    private boolean shouldActivate(Enemy enemy) {
        if (enemy instanceof BasicEnemy) {
            BasicEnemy basic = (BasicEnemy) enemy;
            return basic.shouldActivate(player.getPositionX(), cameraX, SCENE_WIDTH);
        } else if (enemy instanceof ShieldedEnemy) {
            ShieldedEnemy shielded = (ShieldedEnemy) enemy;
            return shielded.shouldActivate(player.getPositionX(), cameraX, SCENE_WIDTH);
        }
        return false;
    }

    private void updateAllBullets() {
        ArrayList<Character> shooters = new ArrayList<>();
        shooters.add(player);

        for (Enemy enemy : enemies) {
            if (enemy instanceof BasicEnemy) {
                shooters.add(enemy);
            }
        }

        for (Character shooter : shooters) {
            updateBulletsForEntity(shooter.getBullets());
        }
    }

    private void updateBulletsForEntity(ArrayList<Bullet> bullets) {
        for (Bullet bullet : bullets) {
            if (!getChildren().contains(bullet.getBulletImage())) {
                getChildren().add(bullet.getBulletImage());
            }
        }

        for (int i = bullets.size() - 1; i >= 0; i--) {
            Bullet bullet = bullets.get(i);
            bullet.update(cameraX);

            if (!bullet.isActive() || bullet.isOutOfBounds(levelManager.getCurrentLevel().getLevelWidth())) {
                getChildren().remove(bullet.getBulletImage());
                bullets.remove(i);
            }
        }
    }

    private void updatePOW(){
        for(POW prisoner: pow){
            prisoner.update(cameraX,player.getPositionX());
        }
    }
}