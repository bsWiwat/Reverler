package main;

import java.awt.Graphics;
import java.awt.Color;
import java.awt.Rectangle;

public class Fireball {
    private int x, y, velocityX;
    private boolean isPlayerShot;

    public Fireball(int x, int y, int velocityX, boolean isPlayerShot) {
        this.x = x;
        this.y = y;
        this.velocityX = velocityX;
        this.isPlayerShot = isPlayerShot;
    }

    public int getX() {
        return x;
    }

    public boolean isPlayerShot() {
        return isPlayerShot;
    }

    // Update fireball
    public void update() {
        x += velocityX;
    }

    public void draw(Graphics g) {
        g.setColor(Color.RED);
        g.fillOval(x, y, 20, 10);
    }

    // hitbox
    public Rectangle getBounds() {
        return new Rectangle(x, y, 20, 10);
    }
}
