package main;

import utils.EnemyInterface;

import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.io.InputStream;

public class Enemy2 implements EnemyInterface {
    private int xPosition, yPosition;
    private BufferedImage img;
    public final int WIDTH = 110, HEIGHT = 110;
    private boolean movingRight = true;
    private long lastFireballTime = 0; // Track last fireball time
    private final long FIREBALL_COOLDOWN = 5000; // 5 seconds cooldown

    // Constructor
    public Enemy2(int x, int y) {
        this.xPosition = x;
        this.yPosition = y;
        loadImg();
    }

    // Check if the enemy can shoot
    public boolean canShoot() {
        return System.currentTimeMillis() - lastFireballTime >= FIREBALL_COOLDOWN;
    }

    // Mark the time of the last shot
    public void shoot() {
        lastFireballTime = System.currentTimeMillis();
    }

    // Load the enemy image
    private void loadImg() {
        InputStream is = getClass().getResourceAsStream("/res/eb1.png");
        try {
            img = ImageIO.read(is);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    // Change the image when the direction changes
    private void changeImage() {
        try {
            img = ImageIO.read(getClass().getResource("/res/eb1.png"));
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void changeImage2() {
        try {
            img = ImageIO.read(getClass().getResource("/res/eb1.png"));
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    // Check if the enemy is moving right
    public boolean isMovingRight() {
        return movingRight;
    }

    // Update movement direction
    @Override
    public void update() {
        if (movingRight) {
            xPosition += 2;
            if (xPosition > 1280) {
                movingRight = false;
                changeImage();
            }
        } else {
            xPosition -= 2;
            if (xPosition < 0) {
                movingRight = true;
                changeImage2();
            }
        }
    }

    // Draw the enemy image at its current position
    @Override
    public void draw(Graphics g) {
        g.drawImage(img, xPosition, yPosition, WIDTH, HEIGHT, null);
    }

    // Get bounding rectangle for collision
    @Override
    public Rectangle getBounds() {
        return new Rectangle(xPosition, yPosition, WIDTH, HEIGHT);
    }

    // Getters
    @Override
    public int getX() {
        return xPosition;
    }

    @Override
    public int getY() {
        return yPosition;
    }

    // Setters
    @Override
    public void setX(int xPosition) {
        this.xPosition = xPosition;
    }

    @Override
    public void setY(int yPosition) {
        this.yPosition = yPosition;
    }
}
