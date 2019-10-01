package UI;

import javax.swing.*;

public class DebugLine {
    private JTextField field = new JTextField();

    public DebugLine() {
        field.setFocusable(false);
        field.setHorizontalAlignment(0);
    }
    public void writeToField(String info) {
        field.setText(info);
    }

    public JTextField getField() {
        return field;
    }
}
