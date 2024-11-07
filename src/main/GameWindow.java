package main;

import javax.swing.*;

public class GameWindow extends JFrame {

    public GameWindow(JPanel panel) {
        setTitle("Reverler");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        add(panel);
        setLocationRelativeTo(null);
        setResizable(false);
        pack();
    }
}
