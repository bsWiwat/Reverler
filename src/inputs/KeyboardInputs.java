package inputs;

import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import main.GamePanel;
import utils.GameInterface;

public class KeyboardInputs implements KeyListener {

    private GameInterface gamePanel;

    public KeyboardInputs(GameInterface gamePanel) {
        this.gamePanel = gamePanel;
    }

    @Override
    public void keyPressed(KeyEvent e) {
        switch (e.getKeyCode()) {
            case KeyEvent.VK_W -> gamePanel.jump();
            case KeyEvent.VK_A -> gamePanel.changeX(-15);
            case KeyEvent.VK_D -> gamePanel.changeX(15);
            case KeyEvent.VK_SPACE -> gamePanel.shootFireball();
        }
    }

    @Override
    public void keyReleased(KeyEvent e) { }

    @Override
    public void keyTyped(KeyEvent e) {}
}
