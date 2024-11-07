package main;

import inputs.KeyboardInputs;
import utils.GameInterface;

import javax.imageio.ImageIO;
import javax.swing.JPanel;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;

public class GamePanel extends JPanel implements GameInterface {
    private int xPosition = 100, yPosition = 650;
    private BufferedImage backgroundImg;
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
    private ArrayList<Enemy> enemies = new ArrayList<>();
    private int score = 0;
    private Game game;

    // Slowdown and damage
    private boolean isSlowed = false;
    private long slowdownStartTime = 0;
    private final long SLOWDOWN_DURATION = 3000; // 3 seconds
    private final double DAMAGE_PER_SECOND = 0.5;
    private long lastDamageTime = 0;

    private ArrayList<Platform> platforms = new ArrayList<>();

    public GamePanel(Game game) {
        this.game = game;
        importImg();
        loadAnimation();
        setPanelSize();
        addKeyListener(new KeyboardInputs(this));
        setFocusable(true);

        for (int i = 0; i < 5; i++) {
            int xPosition = 200 + i * 150;
            enemies.add(new Enemy(xPosition, GROUND_LEVEL));
        }

        platforms.add(new Platform(200, 600, 150, 20));
        platforms.add(new Platform(500, 500, 150, 20));
        platforms.add(new Platform(800, 300, 150, 20));
    }

    private void importImg() {
        InputStream bgIs = getClass().getResourceAsStream("/res/background2.png");
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

    // Update position platform
    @Override
    public void updatePosition() {
        if (isJumping) {
            yPosition += jumpVelocity;
            jumpVelocity += GRAVITY;

            boolean onPlatform = false;
            for (Platform platform : platforms) {
                if (isOnPlatform(platform) && isWithinPlatformXRange(platform)) {
                    yPosition = platform.getY() - 120; // Set player on top
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
        for (Enemy enemy : enemies) {
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
        return new Rectangle(xPosition, yPosition + 100, 120, 10).intersects(platform.getBounds());
    }

    public boolean isEnemyInRange(Enemy enemy) {
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

            for (int j = 0; j < enemies.size(); j++) {
                Enemy enemy = enemies.get(j);

                if (fireball.getBounds().intersects(enemy.getBounds())) {
                    enemies.remove(j);
                    fireballs.remove(i);
                    score++;
                    if (score >= 5) {
                        game.transitionToStartPanel(); // back to StartPanel
                    }
                    i--;
                    break;
                }
            }
        }

        fireballs.removeIf(fireball -> fireball.getX() > getWidth() || fireball.getX() < 0);

        for (Fireball fireball : fireballs) {
            fireball.update();
        }
    }

    @Override
    public void drawScore(Graphics g) {
        g.setColor(Color.CYAN);
        g.setFont(new Font("Arial", Font.BOLD, 20));
        g.drawString("Score: " + score + " / 5", getWidth() - 150, 30);
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

        if (score >= 5 || currentHP <= 0) {
            platforms.clear();
            enemies.clear();
            fireballs.clear();
            game.transitionToStartPanel();
        }

        for (Fireball fireball : fireballs) {
            fireball.draw(g);
        }

        for (Platform platform : platforms) {
            platform.draw(g);
        }

        for (Enemy enemy : enemies) {
            enemy.update();
            enemy.draw(g);
        }
    }
}
