package utils;

import java.awt.Graphics;

public interface GameInterface {

    // Methods for character actions
    void jump();
    void shootFireball();
    void changeX(int value);

    // Methods for updating game elements
    void updatePosition();
    void updateAnimation();
    void updateFireballs();

    // Method for drawing the health bar
    void drawHealthBar(Graphics g);
    void drawScore (Graphics g);

    // Method for applying effects
    void applySlowdown();
    void applyDamage();
}
