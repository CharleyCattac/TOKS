package services;

import javax.swing.*;

public class InfoLine {
    private JTextField field = new JTextField();

    public InfoLine() {
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
