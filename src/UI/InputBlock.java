package UI;

import services.Mediator;

import javax.swing.*;
import java.awt.event.*;

public class InputBlock extends ElementBlock {

    private int lastPositionInFile = 1;

    public InputBlock(Mediator mediator, String labelName,
                      int textFieldWidth, int textFieldHeight) {
        super(mediator, labelName, textFieldWidth, textFieldHeight);

        getTextField().setEditable(false);

        String[] keys = {"UP", "DOWN", "LEFT", "RIGHT", "DELETE", "BACK_SPACE", "CONTROL"};
        InputMap inputMap = getTextField().getInputMap();
        for (String key : keys) {
            inputMap.put(KeyStroke.getKeyStroke(key), "none");
        }
        inputMap.put(KeyStroke.getKeyStroke("CONTROL"), "none");

        getTextField().addFocusListener(new FocusListener() {
            public void focusGained(FocusEvent e) {
                getTextField().setEditable(true);
            }

            public void focusLost(FocusEvent e) {
                getTextField().setEditable(false);
            }
        });

        getTextField().addMouseListener(new MouseListener() {
            @Override
            public void mouseClicked(java.awt.event.MouseEvent e) {
            }

            @Override
            public void mousePressed(java.awt.event.MouseEvent e) {
                getTextField().setCaretPosition(lastPositionInFile - 1);
                getTextField().setEditable(false);
            }

            @Override
            public void mouseReleased(java.awt.event.MouseEvent e) {
                getTextField().setCaretPosition(lastPositionInFile - 1);
                getTextField().setEditable(true);
            }

            @Override
            public void mouseEntered(java.awt.event.MouseEvent e) {
            }

            @Override
            public void mouseExited(java.awt.event.MouseEvent e) {
            }
        });

        getTextField().addKeyListener(new KeyListener() {
            @Override
            public void keyTyped(KeyEvent e) {
                if (getTextField().isFocusable()) {
                    if (e.getKeyChar() == '\b') {
                        if (lastPositionInFile > 1) {
                            lastPositionInFile--;
                        }
                    } else {
                        mediator.transferData(e.getKeyChar());
                        lastPositionInFile++;
                    }
                }
            }

            @Override
            public void keyPressed(KeyEvent e) {
            }

            @Override
            public void keyReleased(KeyEvent e) {
            }
        });
    }
}
