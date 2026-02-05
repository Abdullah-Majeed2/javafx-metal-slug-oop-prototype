public class Pistol extends Weapon {

    public Pistol() {
        super(20, 0.3, 30, "Pistol");
    }

    @Override
    public Bullet fire(double x, double y, boolean facingRight , boolean isCrouching , boolean isMoving) {
        if (!canFire(isCrouching , isMoving)) {
            return null;
        }


        updateFireTime();

        double bulletX = facingRight ? x + 40 : x - 10;
        double bulletY = y-10;

        if (isCrouching) {
            bulletY = y + 25;
        }

        return new Bullet(bulletX, bulletY, facingRight, damage , "Player");
    }

}
