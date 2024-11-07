package main;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class StartPanel extends JPanel {
    private JButton playButton;

    public StartPanel() {
        setLayout(null);  // Use absolute positioning to center the button

        // Set up the background color or image if needed
        setBackground(Color.CYAN);  // Set the background color or you can add a background image

        // Create the Play button and set its properties
        playButton = new JButton("Play");
        playButton.setBounds(500, 350, 150, 50);  // Position the button at the center of the screen
        playButton.setFont(new Font("Arial", Font.PLAIN, 20));

        // Add ActionListener to the Play button
        playButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                // When the Play button is clicked, transition to the game screen
                startGame();
            }
        });

        // Add the button to the panel
        add(playButton);
    }

    // Method to transition to the game screen
    private void startGame() {
        // Remove StartScreen and load GamePanel
        GameWindow.changePanel(new GamePanel());
    }
}

