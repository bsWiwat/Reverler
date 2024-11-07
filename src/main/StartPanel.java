package main;

import javax.imageio.ImageIO;
import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.io.InputStream;

public class StartPanel extends JPanel {
    private JButton playButton, playButton2;
    private BufferedImage backgroundImg;
    public boolean started = false;

    private Game game;

    public StartPanel(Game game) {
        this.game = game;
        setLayout(null);  // center the button
        importImg();
        setPanelSize();

        playButton = new JButton("Play level 1");
        playButton.setBounds(500, 350, 150, 50);
        playButton.setFont(new Font("Arial", Font.PLAIN, 20));

        playButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                startGame(1);
                started = true;
            }
        });

        playButton2 = new JButton("Play level 2");
        playButton2.setBounds(500, 450, 150, 50);
        playButton2.setFont(new Font("Arial", Font.PLAIN, 20));

        playButton2.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                startGame(2);
                started = true;
            }
        });

        add(playButton2);
        add(playButton);
    }

    private void startGame(int l) {
        if (l == 1) {
            game.transitionToGamePanel(new GamePanel(game));
        } else {
            game.transitionToGamePanel(new GamePanel2(game));
        }
    }

    private void setPanelSize() {
        Dimension size = new Dimension(1280, 800);
        setPreferredSize(size);
    }

    private void importImg() {
        InputStream bgIs = getClass().getResourceAsStream("/res/background3.png");
        try {
            backgroundImg = ImageIO.read(bgIs);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);
        if (backgroundImg != null) {
            g.drawImage(backgroundImg, 0, 0, getWidth(), getHeight(), this);  // Draw image scaled to panel size
        }
    }
}
