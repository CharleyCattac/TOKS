package UI;

import javax.swing.*;
import java.awt.*;

public class OutputBlock extends JPanel {

    private JPanel labelPanel;
    private JPanel textPanel;
    private JTextArea textField;
    private JLabel label;
    private JPanel sequencePanel;

    public OutputBlock(String labelName,
                       int textFieldWidth, int textFieldHeight) {
        super(new BorderLayout());

        JPanel extraPanel = new JPanel(new BorderLayout());
        JButton extraButton = new JButton();
        extraButton.setEnabled(false);
        extraButton.setBorderPainted(false);

        labelPanel = new JPanel(new GridLayout(1, 1));
        textPanel = new JPanel((new GridLayout(1, 1)));
        extraPanel.add(labelPanel, BorderLayout.WEST);
        extraPanel.add(textPanel, BorderLayout.CENTER);
        add(extraButton, BorderLayout.WEST);
        add(extraPanel, BorderLayout.CENTER);

        textField = new JTextArea();
        textField.setColumns(textFieldWidth);
        textField.setRows(textFieldHeight);
        textField.setAutoscrolls(true);
        textField.setLineWrap(true);
        textField.setFocusable(false);

        JScrollPane sp = new JScrollPane(textField);
        sp.setHorizontalScrollBarPolicy(ScrollPaneConstants.HORIZONTAL_SCROLLBAR_NEVER);

        label = new JLabel(labelName, JLabel.RIGHT);
        label.setLabelFor(sp);

        labelPanel.add(label);
        sequencePanel = new JPanel(new FlowLayout(FlowLayout.LEFT));
        sequencePanel.add(sp);
        textPanel.add(sequencePanel);

        extraButton.setBackground(Color.PINK);
        labelPanel.setBackground(Color.PINK);
        sequencePanel.setBackground(Color.PINK);
    }

    public JTextArea getTextField() {
        return textField;
    }
}
