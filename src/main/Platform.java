package main;

package main;

import java.awt.Graphics;
import java.awt.Color;
import java.awt.Rectangle;

public class Platform {
    private int x, y, width, height;

    public Platform(int x, int y, int width, int height) {
        this.x = x;
        this.y = y;
        this.width = width;
        this.height = height;
    }

    // Draw the platform as a filled rectangle
    public void draw(Graphics g) {
        g.setColor(Color.GRAY);  // Platform color
        g.fillRect(x, y, width, height);
    }

    // Get the platform's bounding box (hitbox) for collision detection
    public Rectangle getBounds() {
        return new Rectangle(x, y, width, height);
    }

    public int getY() {
        return y;
    }
}

