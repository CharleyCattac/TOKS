package UI;

import javax.swing.*;
import java.awt.*;
import java.awt.event.*;

public class ElementBlock extends JPanel {

    private JPanel labelPanel;
    private JPanel textPanel;
    private JTextArea textField;
    private JLabel label;
    JPanel sequencePanel;
    int lastPosition = 1;

    public ElementBlock(String labelName, int textFieldWidth, int textFieldHeight) {
        super(new BorderLayout());
        labelPanel = new JPanel(new GridLayout(1, 1));
        textPanel = new JPanel((new GridLayout(1, 1)));
        add(labelPanel, BorderLayout.WEST);
        add(textPanel, BorderLayout.CENTER);

        textField = new JTextArea();
        textField.addFocusListener(new FocusListener() {
            public void focusGained(FocusEvent e) {
                textField.setEditable(true);
                System.out.println("Focus is gotten");
            }
            public void focusLost(FocusEvent e) {
                textField.setEditable(false);
                System.out.println("Focus is lost");
            }
        });
        textField.setColumns(textFieldWidth);
        textField.setRows(textFieldHeight);
        textField.setAutoscrolls(true);
        textField.setLineWrap(true);

        textField.addMouseListener(new MouseListener() {
            @Override
            public void mouseClicked(java.awt.event.MouseEvent e) {
            }

            @Override
            public void mousePressed(java.awt.event.MouseEvent e) {
                textField.setCaretPosition(lastPosition - 1);
                textField.setEditable(false);
            }

            @Override
            public void mouseReleased(java.awt.event.MouseEvent e) {
                textField.setCaretPosition(lastPosition - 1);
                textField.setEditable(true);
            }

            @Override
            public void mouseEntered(java.awt.event.MouseEvent e) {
            }

            @Override
            public void mouseExited(java.awt.event.MouseEvent e) {
            }
        });
        textField.addKeyListener(new KeyListener() {
            @Override
            public void keyTyped(KeyEvent e) {
                if (textField.isEditable()) {
                    if (e.getKeyChar() == '\b') {
                        if (lastPosition > 1) {
                            lastPosition--;
                        }
                    } else {
                        lastPosition++;
                    }
                    System.out.println(lastPosition);
                }
            }

            @Override
            public void keyPressed(KeyEvent e) {

            }

            @Override
            public void keyReleased(KeyEvent e) {

            }
        });

        if (labelName.equals("Output")) {
            textField.setFocusable(false);
        }

        JScrollPane sp = new JScrollPane(textField);
        sp.setHorizontalScrollBarPolicy(ScrollPaneConstants.HORIZONTAL_SCROLLBAR_NEVER);

        label = new JLabel(labelName, JLabel.RIGHT);
        label.setLabelFor(sp);

        labelPanel.add(label);
        sequencePanel = new JPanel(new FlowLayout(FlowLayout.LEFT));
        sequencePanel.add(sp);
        textPanel.add(sequencePanel);
    }

    public JTextArea getTextField() {
        return textField;
    }
}
