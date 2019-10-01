package UI;

import COM_port.Mediator;

import javax.swing.*;
import java.awt.*;
import java.awt.event.*;

public class ElementBlock extends JPanel {

    private Mediator mediator;
    private JPanel labelPanel;
    private JPanel textPanel;
    private JTextArea textField;
    private JLabel label;
    private JPanel sequencePanel;
    private int lastPositionInFile = 1;

    public ElementBlock(Mediator mediator, String labelName,
                        int textFieldWidth, int textFieldHeight) {
        super(new BorderLayout());

        this.mediator = mediator;
        labelPanel = new JPanel(new GridLayout(1, 1));
        textPanel = new JPanel((new GridLayout(1, 1)));
        add(labelPanel, BorderLayout.WEST);
        add(textPanel, BorderLayout.CENTER);

        textField = new JTextArea();
        textField.setColumns(textFieldWidth);
        textField.setRows(textFieldHeight);
        textField.setAutoscrolls(true);
        textField.setLineWrap(true);
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
        textField.addMouseListener(new MouseListener() {
            @Override
            public void mouseClicked(java.awt.event.MouseEvent e) {
            }

            @Override
            public void mousePressed(java.awt.event.MouseEvent e) {
                textField.setCaretPosition(lastPositionInFile - 1);
                textField.setEditable(false);
            }

            @Override
            public void mouseReleased(java.awt.event.MouseEvent e) {
                textField.setCaretPosition(lastPositionInFile - 1);
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
                        if (lastPositionInFile > 1) {
                            lastPositionInFile--;
                        }
                    } else {
                        mediator.transferData(e.getKeyChar());
//                        System.out.println(e.getKeyChar());
                        lastPositionInFile++;
                    }
//                    System.out.println(lastPositionInFile);
                }
            }

            @Override
            public void keyPressed(KeyEvent e) {

            }

            @Override
            public void keyReleased(KeyEvent e) {

            }
        });

            textField.setFocusable(false);
            textField.setEditable(false);

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
