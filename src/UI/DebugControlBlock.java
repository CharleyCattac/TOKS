package UI;

import enums.*;
import jssc.SerialPortList;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class DebugControlBlock extends JPanel {

    private JComboBox<String> comPortsComboBox = new JComboBox<>(SerialPortList.getPortNames());
    private JComboBox<BaudRateEnum> baudRateComboBox = new JComboBox<>(BaudRateEnum.values());
    private JComboBox<DataBitsEnum> dataBitsComboBox = new JComboBox<>(DataBitsEnum.values());
    private JComboBox<ParityEnum> parityComboBox = new JComboBox<>(ParityEnum.values());
    private JComboBox<StopBitsEnum> stopBitsComboBox = new JComboBox<>(StopBitsEnum.values());
    private JLabel[] labels;

    public DebugControlBlock(String blockName, String[] rows) {
        super(new BorderLayout());
        JPanel labelPanel = new JPanel(new GridLayout(rows.length, 1));
        JPanel comboBoxPanel = new JPanel((new GridLayout(rows.length, 1)));
        JPanel buttonPanel = new JPanel();
        add(labelPanel, BorderLayout.WEST);
        add(comboBoxPanel, BorderLayout.EAST);
        add(buttonPanel, BorderLayout.AFTER_LAST_LINE);

        for(int i = 0; i < rows.length; i++) {
            JLabel label = new JLabel(rows[i], JLabel.RIGHT);
            labelPanel.add(label);

            switch (i) {
                case 0: {
                    comboBoxPanel.add(comPortsComboBox);
                }
                case 1: {
                    comboBoxPanel.add(baudRateComboBox);
                }
                case 2: {
                    comboBoxPanel.add(dataBitsComboBox);
                }
                case 3: {
                    comboBoxPanel.add(stopBitsComboBox);
                }
                case 4: {
                    comboBoxPanel.add(parityComboBox);
                }
            }
        }

        JButton button = new JButton();
        button.setText("Connect");
//        button.addActionListener(new ActionListener() {
//            @Override
//            public void actionPerformed(ActionEvent e) {
//                    button.setText("Disconnect");
//            }
//        });
        button.addActionListener((event) -> button.setText("Disconnect"));
        buttonPanel.add(button);
//        JLabel label = new JLabel(labelName, JLabel.RIGHT);
//        label.setLabelFor(aJTextArea);
//
//        labelField.add(label);
//        JPanel p = new JPanel(new FlowLayout(FlowLayout.LEFT));
//        p.add(aJTextArea);
//        textField.add(p);
    }
}
