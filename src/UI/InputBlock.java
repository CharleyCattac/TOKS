package UI;

import services.Mediator;

import java.awt.*;
import java.awt.event.*;

public class InputBlock extends OutputBlock {

    private int lastPositionInFile = 1;

    public InputBlock(Mediator mediator, String labelName,
                      int textFieldWidth, int textFieldHeight) {
        super(labelName, textFieldWidth, textFieldHeight);

        getTextField().setEditable(false);
        getTextField().getCaret().setVisible(false);

        getTextField().addFocusListener(new FocusListener() {
            public void focusGained(FocusEvent e) {
                getTextField().setEditable(true);
                getTextField().getCaret().setVisible(true);
                getTextField().setCaretPosition(lastPositionInFile - 1);
            }

            public void focusLost(FocusEvent e) {
                getTextField().getCaret().setVisible(false);
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
                getTextField().setCaretColor(Color.BLACK);
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
                if (getTextField().isFocusable() && getTextField().isEditable()) {
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
                if (e.getKeyCode() == KeyEvent.VK_BACK_SPACE ||
                    e.getKeyCode() == KeyEvent.VK_DELETE ||
                    e.getKeyCode() == KeyEvent.VK_UP ||
                    e.getKeyCode() == KeyEvent.VK_DOWN ||
                    e.getKeyCode() == KeyEvent.VK_LEFT ||
                    e.getKeyCode() == KeyEvent.VK_RIGHT) {
                        e.consume();
                }
            }

            @Override
            public void keyReleased(KeyEvent e) {
            }
        });
    }
}
