package main;

import inputs.KeyboardInputs;
import utils.GameInterface;

import javax.imageio.ImageIO;
import javax.swing.*;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;

public class GamePanel2 extends JPanel implements GameInterface {
    private int xPosition = 100, yPosition = 650;
    private BufferedImage img, backgroundImg;
    private BufferedImage[] idleAnimation;
    private int aniTick, aniIndex, aniSpeed = 30;
    private final int GROUND_LEVEL = 650;
    private int jumpVelocity = 0;
    private final int JUMP_STRENGTH = -20;
    private final int GRAVITY = 1;
    private boolean isJumping = false;
    private boolean isMovingLeft = false;
    private ArrayList<Fireball> fireballs = new ArrayList<>();

    private int currentHP = 10;
    private final int MAX_HP = 10;
    private final int HP_BAR_WIDTH = 300;
    private final int HP_BAR_HEIGHT = 20;

    // Enemies list
    private ArrayList<Enemy2> enemies = new ArrayList<>();
    private int score = 0;
    private Game game;

    // Slowdown and damage
    private boolean isSlowed = false;
    private long slowdownStartTime = 0;
    private final long SLOWDOWN_DURATION = 2000; // 3 seconds
    private final double DAMAGE_PER_SECOND = 0.5;
    private long lastDamageTime = 0;

    private ArrayList<Platform> platforms = new ArrayList<>();

    public GamePanel2(Game game) {
        this.game = game;
        importImg();
        loadAnimation();
        setPanelSize();
        addKeyListener(new KeyboardInputs(this));
        setFocusable(true);
        requestFocusInWindow();

        // platforms
        platforms.add(new Platform(800, 200, 400, 20));
        platforms.add(new Platform(600, 300, 250, 20));
        platforms.add(new Platform(150, 450, 600, 20));
        platforms.add(new Platform(100, 550, 150, 20));

        for (int i = 0; i < 2; i++) {
            int xPosition = 200 + i * 300;
            int yPosition = GROUND_LEVEL;
            enemies.add(new Enemy2(xPosition, yPosition));
        }

        enemies.add(new Enemy2(900, 20));
        enemies.add(new Enemy2(1100, 10));
        enemies.add(new Enemy2(1200, 300));
        enemies.add(new Enemy2(1240, 450));
        enemies.add(new Enemy2(400, 300));
    }

    private void importImg() {
        InputStream bgIs = getClass().getResourceAsStream("/res/background4.png");
        try {
            backgroundImg = ImageIO.read(bgIs);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    // player animations
    private void loadAnimation() {
        idleAnimation = new BufferedImage[3];
        try {
            idleAnimation[0] = ImageIO.read(getClass().getResource("/res/p1.png"));
            idleAnimation[1] = ImageIO.read(getClass().getResource("/res/p2.png"));
            idleAnimation[2] = ImageIO.read(getClass().getResource("/res/p3.png"));
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    // panel size
    private void setPanelSize() {
        Dimension size = new Dimension(1280, 800);
        setPreferredSize(size);
    }

    // position to include platform
    @Override
    public void updatePosition() {
        if (isJumping) {
            yPosition += jumpVelocity;
            jumpVelocity += GRAVITY;

            // Check player platform
            boolean onPlatform = false;
            for (Platform platform : platforms) {
                if (isOnPlatform(platform) && isWithinPlatformXRange(platform)) {
                    yPosition = platform.getY() - 110; // Set player on top of platform
                    isJumping = false;
                    jumpVelocity = 0;
                    onPlatform = true;
                    break;
                }
            }

            // fall to the ground
            if (!onPlatform && yPosition >= GROUND_LEVEL) {
                yPosition = GROUND_LEVEL;
                isJumping = false;
            }
        } else {
            boolean onPlatform = false;
            for (Platform platform : platforms) {
                if (isOnPlatform(platform) && isWithinPlatformXRange(platform)) {
                    onPlatform = true;
                    break;
                }
            }

            if (!onPlatform && yPosition < GROUND_LEVEL) {
                isJumping = true;
                jumpVelocity = 0;
            }
        }

        // Slowdown and damage
        for (Enemy2 enemy : enemies) {
            if (isEnemyInRange(enemy)) {
                applySlowdown();
                applyDamage();
            }
        }

    }

    private boolean isWithinPlatformXRange(Platform platform) {
        return xPosition + 60 >= platform.getX() && xPosition + 60 <= platform.getX() + platform.getWidth();
    }

    private boolean isOnPlatform(Platform platform) {
        return new Rectangle(xPosition, yPosition + 120, 120, 10).intersects(platform.getBounds());
    }

    public boolean isEnemyInRange(Enemy2 enemy) {
        int horizontalRange = 50;
        int verticalRange = 30;

        return Math.abs(enemy.getX() - xPosition) <= horizontalRange &&
                Math.abs(enemy.getY() - yPosition) <= verticalRange;
    }

    @Override
    public void applySlowdown() {
        if (!isSlowed) {
            isSlowed = true;
            slowdownStartTime = System.currentTimeMillis();
        }

        if (System.currentTimeMillis() - slowdownStartTime > SLOWDOWN_DURATION) {
            isSlowed = false;
        }
    }

    @Override
    public void applyDamage() {
        if (isSlowed && System.currentTimeMillis() - lastDamageTime > 1000) {
            currentHP -= DAMAGE_PER_SECOND;
            lastDamageTime = System.currentTimeMillis();
        }
    }

    @Override
    public void drawHealthBar(Graphics g) {
        g.setColor(Color.GRAY);
        g.fillRect(10, 10, HP_BAR_WIDTH, HP_BAR_HEIGHT);

        g.setColor(Color.GREEN);
        g.fillRect(10, 10, (int) ((currentHP / (float) MAX_HP) * HP_BAR_WIDTH), HP_BAR_HEIGHT);

        g.setColor(Color.BLACK);
        g.drawString("HP: " + currentHP + "/" + MAX_HP, 10, 25);
    }

    @Override
    public void shootFireball() {
        int velocityX = isMovingLeft ? -10 : 10;
        fireballs.add(new Fireball(xPosition + 60, yPosition + 20, velocityX, true));
    }

    @Override
    public void jump() {
        if (!isJumping) {
            jumpVelocity = JUMP_STRENGTH;
            isJumping = true;
        }
    }

    @Override
    public void changeX(int value) {
        isMovingLeft = value < 0;
        this.xPosition += value;
    }

    @Override
    public void updateAnimation() {
        aniTick++;
        if (aniTick >= aniSpeed) {
            aniTick = 0;
            aniIndex++;
            if (aniIndex >= idleAnimation.length) {
                aniIndex = 0;
            }
        }
    }

    @Override
    public void updateFireballs() {
        for (int i = 0; i < fireballs.size(); i++) {
            Fireball fireball = fireballs.get(i);

            if (fireball.isPlayerShot()) {
                // player fireballs hit enemies
                for (int j = 0; j < enemies.size(); j++) {
                    Enemy2 enemy = enemies.get(j);
                    if (fireball.getBounds().intersects(enemy.getBounds())) {
                        enemies.remove(j);
                        fireballs.remove(i);
                        score++;
                        i--;
                        break;
                    }
                }
            } else {
                // enemy fireballs hit player
                if (fireball.getBounds().intersects(new Rectangle(xPosition, yPosition, 120, 120))) {
                    // fireball hit player
                    currentHP -= DAMAGE_PER_SECOND;
                    fireballs.remove(i);
                    i--;
                    break;
                }
            }
        }

        fireballs.removeIf(fireball -> fireball.getX() > getWidth() || fireball.getX() < 0);

        // Update all fireballs
        for (Fireball fireball : fireballs) {
            fireball.update();
        }
    }

    public void updateEnemyFireballs() {
        for (Enemy2 enemy : enemies) {
            if (enemy.canShoot()) {
                // shoots fireball
                int fireballVelocity = enemy.isMovingRight() ? 5 : -5; // based on enemy movement
                fireballs.add(new Fireball(enemy.getX() + enemy.WIDTH / 2, enemy.getY() + enemy.HEIGHT / 2, fireballVelocity, false));
                enemy.shoot();
            }
        }
    }

    @Override
    public void drawScore(Graphics g) {
        g.setColor(Color.CYAN);
        g.setFont(new Font("Arial", Font.BOLD, 20));
        g.drawString("Score: " + score + " / 7", getWidth() - 150, 30);
    }

    @Override
    public void paintComponent(Graphics g) {
        super.paintComponent(g);

        g.drawImage(backgroundImg, 0, 0, getWidth(), getHeight(), null);

        updatePosition();
        updateAnimation();
        updateFireballs();

        g.drawImage(idleAnimation[aniIndex], xPosition, yPosition, 120, 120, null);

        drawHealthBar(g);
        drawScore(g);

        if (score >= 7 || currentHP <= 0) {
            platforms.clear();
            enemies.clear();
            fireballs.clear();
            game.transitionToStartPanel(); // back to Start Panel
        }

        for (Fireball fireball : fireballs) {
            fireball.draw(g);
        }

        for (Platform platform : platforms) {
            platform.draw(g);
        }

        for (Enemy2 enemy : enemies) {
            enemy.update();
            enemy.draw(g);
        }

        updateEnemyFireballs();
    }
}
