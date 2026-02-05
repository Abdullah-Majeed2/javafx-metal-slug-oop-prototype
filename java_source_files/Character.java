import java.util.ArrayList;

public abstract class Character extends GameObjects implements Damageable {
    protected int health;
    protected int maxHealth;
    protected double speed;

    public Character(double x, double y, int health, double speed) {
        super(x, y);
        this.health = health;
        this.maxHealth = health;
        this.speed = speed;
    }

    // Methodzz of Damageable Interface :o ;)
    public void takeDamage(int damage) {
        this.health -= damage;
        if (health <= 0) {
            this.health = 0;
        }
    }

    public int getHealth() {
        return health;
    }

    public boolean isAlive() {
        if (health > 0){
            return true;
        }
        return false;
    }

    public int getMaxHealth() {
        return maxHealth;
    }

    public double getSpeed() {
        return speed;
    }

    public ArrayList<Bullet> getBullets() {
        return null;
    }

}
