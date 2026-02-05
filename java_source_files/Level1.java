import javafx.scene.media.Media;
import javafx.scene.media.MediaPlayer;

public class Level1 extends Level {

    public Level1() {
        super("/Assets/Levels/Level_1/Backgrounds/Map1.png",
                9600, 900, 50, 535);

        loadSFX();
    }

    public void loadSFX() {
        double bgmVol = AudioSettings.getInstance().getBgmVolume();
        BGMManager.start(
                "/Assets/Levels/Level_1/Sounds/Mission1.wav",
                "/Assets/Levels/Level_1/Sounds/BGM1.wav",
                bgmVol
        );
    }

    @Override
    protected void spawnGameObjects() {

        entities.add(new Platform(-20, 640, 20, 10));
        int startX = 0;

        int startY = 640;
        int endY = 520;
        int segments = 40;
        int width = 10;
        int height = 10;

        double dy = (startY - endY) / (double) segments;
        int dx = width;

        int x = startX;
        double y = startY;

        for (int i = 0; i < segments; i++) {
            entities.add(new Platform(x, (int) y, width, height));
            x += dx;
            y -= dy;
        }


        entities.add(new Platform(400, 520, 60, 10));


        startX = 460;
        startY = 530;
        endY = 650;
        segments = 32;
        width = 10;
        height = 10;

        dy = (startY - endY) / (double) segments;
        dx = width;

        x = startX;
        y = startY;

        for (int i = 0; i < segments; i++) {
            entities.add(new Platform(x, (int) y, width, height));
            x += dx;
            y -= dy;
        }

        entities.add(new Platform(780, 640, 60, 10));


        startX = 840;
        startY = 640;
        endY = 580;
        segments = 28;
        width = 10;
        height = 10;

        dy = (startY - endY) / (double) segments;
        dx = width;

        x = startX;
        y = startY;

        for (int i = 0; i < segments; i++) {
            entities.add(new Platform(x, (int) y, width, height));
            x += dx;
            y -= dy;
        }

        entities.add(new Platform(1100, 580, 40, 10));


        startX = 1140;
        startY = 580;
        endY = 690;
        segments = 39;
        width = 10;
        height = 10;

        dy = (startY - endY) / (double) segments;
        dx = width;

        x = startX;
        y = startY;

        for (int i = 0; i < segments; i++) {
            entities.add(new Platform(x, (int) y, width, height));
            x += dx;
            y -= dy;
        }

        entities.add(new Platform(1530, 690, 8300, 10));






        BasicEnemy be1 = new BasicEnemy( 2133, 595 + 6); entities.add(be1);

        BasicEnemy be2 = new BasicEnemy( 2666, 595 - 8); entities.add(be2);

        BasicEnemy be3 = new BasicEnemy( 3200, 595 + 6); entities.add(be3);


        BasicEnemy be4 = new BasicEnemy( 2800, 595 - 8); entities.add(be4);

        BasicEnemy be5 = new BasicEnemy( 3600, 595 + 6); entities.add(be5);

        BasicEnemy be6 = new BasicEnemy( 4000, 595 - 8); entities.add(be6);

        BasicEnemy be7 = new BasicEnemy( 4200, 595 + 6); entities.add(be7);

        BasicEnemy be8 = new BasicEnemy( 4600, 595 - 8); entities.add(be8);

        BasicEnemy be9 = new BasicEnemy( 5000, 595 + 6); entities.add(be9);

        BasicEnemy be10 = new BasicEnemy( 5600, 595 - 8); entities.add(be10);

        BasicEnemy be11 = new BasicEnemy( 7000, 595 + 6); entities.add(be11);
        BasicEnemy be12 = new BasicEnemy( 8000, 595 + 6); entities.add(be12);


        BasicEnemy be13 = new BasicEnemy( 6000, 595 + 6); entities.add(be13);

        BasicEnemy be14 = new BasicEnemy( 7500, 595 - 8); entities.add(be14);

        BasicEnemy be15 = new BasicEnemy( 8500, 595 + 6); entities.add(be15);


        BasicEnemy be16 = new BasicEnemy( 8900, 595 - 8); entities.add(be16);

        BasicEnemy be17 = new BasicEnemy( 9400, 595 + 6); entities.add(be17);

        BasicEnemy be18 = new BasicEnemy( 9800, 595 - 8); entities.add(be18);

        BasicEnemy be19 = new BasicEnemy( 10000, 595 + 6); entities.add(be19);

        BasicEnemy be20 = new BasicEnemy( 10500, 595 - 8); entities.add(be20);

        BasicEnemy be21 = new BasicEnemy( 11000, 595 + 6); entities.add(be21);

        BasicEnemy be22 = new BasicEnemy( 11700, 595 - 8); entities.add(be22);

        BasicEnemy be23 = new BasicEnemy( 12000, 595 + 6); entities.add(be23);
        BasicEnemy be24 = new BasicEnemy( 12500, 595 + 6); entities.add(be24);




        ShieldedEnemy shieldedEnemy2 = new ShieldedEnemy( 5000, 595);
        entities.add(shieldedEnemy2);

        ShieldedEnemy shieldedEnemy3 = new ShieldedEnemy( 6000, 595);
        entities.add(shieldedEnemy3);

        ShieldedEnemy shieldedEnemy4 = new ShieldedEnemy( 8000, 595);
        entities.add(shieldedEnemy4);

        ShieldedEnemy shieldedEnemy5 = new ShieldedEnemy( 10000, 595);
        entities.add(shieldedEnemy5);




        // The backgrounds npc'z

        POW runman1 = new POW(5000 , 650 , "Man Run");
        entities.add(runman1);
        POW runman2 = new POW(5000 , 640 , "Old Man Run");
        entities.add(runman2);
        POW runman3 = new POW(10000 , 670 , "Old Man Run");
        entities.add(runman3);
        POW runman4 = new POW(10000 , 670 , "Man Run");
        entities.add(runman4);
        POW runman5 = new POW(14000 , 640 , "Old Man Run");
        entities.add(runman5);

        POW prisoner = new POW(1950 , 275 , "Old Man Sitting");
        entities.add(prisoner);

        POW prisoner2 = new POW(2100 , 275 , "Old Man Sitting");
        entities.add(prisoner2);

        POW prisoner3 = new POW(1750 , 660 , "Old Man Log");
        entities.add(prisoner3);

        POW prisoner4 = new POW(3450 , 260 , "Old Man Log");
        entities.add(prisoner4);

        POW prisoner5 = new POW(3600 , 260 , "Old Man Log");
        entities.add(prisoner5);

        POW prisoner6 = new POW(6660 , 670 , "Man Log");
        entities.add(prisoner6);

        POW prisoner7 = new POW(7540 , 260 , "Old Man Sitting");
        entities.add(prisoner7);

        POW prisoner8 = new POW(8300 , 690 , "Man Log");
        entities.add(prisoner8);

        POW prisoner9 = new POW(8300 , 240 , "Old Man Sitting");
        entities.add(prisoner9);



        POW rope1 = new POW(5260 , 0 , "Rope");
        entities.add(rope1);
        POW rope2 = new POW(5260 , 25 , "Rope");
        entities.add(rope2);
        POW rope3 = new POW(5260 , 50 , "Rope");
        entities.add(rope3);
        POW hangman1 = new POW(5254 , 135 , "Man Hanging");
        entities.add(hangman1);
        POW rope4 = new POW(5260 , 75 , "Rope");
        entities.add(rope4);
        POW rope5 = new POW(5505 , 0 , "Rope");
        entities.add(rope5);
        POW rope6 = new POW(5505 , 25 , "Rope");
        entities.add(rope6);
        POW rope7 = new POW(5505 , 50 , "Rope");
        entities.add(rope7);
        POW hangman2 = new POW(5493 , 122 , "Old Man Hanging");
        entities.add(hangman2);
        POW rope8 = new POW(5505 , 75 , "Rope");
        entities.add(rope8);
        POW rope9 = new POW(5750 , 0 , "Rope");
        entities.add(rope9);
        POW rope10 = new POW(5750 , 25 , "Rope");
        entities.add(rope10);
        POW rope11 = new POW(5750 , 50 , "Rope");
        entities.add(rope11);
        POW hangman3 = new POW(5744 , 135 , "Man Hanging");
        entities.add(hangman3);
        POW rope12 = new POW(5750 , 75 , "Rope");
        entities.add(rope12);

        POW ceo1 = new POW(4300 , 247 , "CEO");
        entities.add(ceo1);


        System.out.println("Level 1 entities spawned!");
    }
}