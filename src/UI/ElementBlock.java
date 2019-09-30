package UI;

import javax.swing.*;
import java.awt.*;

public class ElementBlock extends JPanel {

    private JPanel labelPanel;
    private JPanel textPanel;
    private JTextArea textField;
    private JLabel label;
    JPanel sequencePanel;

    public ElementBlock(String labelName, int textFieldWidth, int textFieldHeight) {
        super(new BorderLayout());
        labelPanel = new JPanel(new GridLayout(1, 1));
        textPanel = new JPanel((new GridLayout(1, 1)));
        add(labelPanel, BorderLayout.WEST);
        add(textPanel, BorderLayout.CENTER);

        textField = new JTextArea();
        textField.setColumns(textFieldWidth);
        textField.setRows(textFieldHeight);

        label = new JLabel(labelName, JLabel.RIGHT);
        label.setLabelFor(textField);

        labelPanel.add(label);
        sequencePanel = new JPanel(new FlowLayout(FlowLayout.LEFT));
        sequencePanel.add(textField);
        textPanel.add(sequencePanel);
    }
}
