public abstract class Weapon {
    protected int ammo;
    protected int maxAmmo;
    protected double fireRate;
    protected int damage;
    protected String name;
    protected long lastFireTime;

    public Weapon(int maxAmmo, double fireRate, int damage, String name) {
        this.maxAmmo = maxAmmo;
        this.fireRate = fireRate;
        this.damage = damage;
        this.ammo = maxAmmo;
        this.name = name;
        this.lastFireTime = 0;
    }

    public abstract Bullet fire(double x, double y, boolean facingRight, boolean isCrouching , boolean isMoving);

    public boolean canFire( boolean isCrouching , boolean isMoving) {
        if(isCrouching && isMoving) {
            return false;
        }
        long currentTime = System.currentTimeMillis();
        double timeSinceLastShot = (currentTime - lastFireTime) / 1700.0;

        if(isMoving) {
            timeSinceLastShot = (currentTime - lastFireTime) / 1000.0;
        }
        return hasAmmo() && timeSinceLastShot >= fireRate;
    }

    public boolean hasAmmo() {
        if (ammo > 0) {
            return true;
        }
        return false;
    }

    public int getAmmo() {
        return ammo;
    }

    public int getMaxAmmo() {
        return maxAmmo;
    }

    public String getName() {
        return name;
    }

    protected void updateFireTime() {
        lastFireTime = System.currentTimeMillis();
    }

}