package main;

import javax.swing.*;

public class Game implements Runnable {

    private GameWindow gameWindow;
    private JPanel gamePanel;
    private Thread gameThread;
    private final int FPS_SET = 120;
    private StartPanel startPanel;

    public Game() {
        startPanel = new StartPanel(this);

        gameWindow = new GameWindow(startPanel);
        gameWindow.setVisible(true);
    }

    protected void startGameLoop() {
        gameThread = new Thread(this);
        gameThread.start();
    }

    @Override
    public void run() {

        double timePerFrame = 1000000000.0 / FPS_SET;
        long lastFrame = System.nanoTime();
        long now = System.nanoTime();

        int frames = 0;
        long lastCheck = System.currentTimeMillis();
        while (true) {

            now = System.nanoTime();
            if (System.nanoTime() - lastFrame >= timePerFrame) {
                gamePanel.repaint();
                lastFrame = now;
                frames++;

            }

            if (System.currentTimeMillis() - lastCheck >= 1000) {
                lastCheck = System.currentTimeMillis();
                System.out.println("FPS: " + frames);
                frames = 0;
            }
        }
    }

    public void transitionToGamePanel(JPanel panel) {
        // Switch StartPanel to GamePanel
        this.gamePanel = panel;
        gameWindow.setContentPane(gamePanel);
        gameWindow.revalidate();  // apply the new panel
        gamePanel.requestFocus();
        startGameLoop();
    }

    public void transitionToStartPanel() {
        gameWindow.setContentPane(startPanel);
        JOptionPane.showMessageDialog(null, "Play Again");
        gameWindow.revalidate();
        startPanel.requestFocus();
        gameWindow.repaint();
    }

    public static void main(String[] args) {
        new Game();
    }
}

