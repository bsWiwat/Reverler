package utils;

import java.awt.*;

public interface EnemyInterface {
    // Method to update the enemy's behavior (e.g., movement)
    void update();

    // Method to draw the enemy on the screen
    void draw(Graphics g);

    // Method to get the enemy's bounding rectangle (for collision detection)
    Rectangle getBounds();

    // Methods to get the enemy's position
    int getX();
    int getY();

    // Methods to set the enemy's position
    void setX(int xPosition);
    void setY(int yPosition);
}

