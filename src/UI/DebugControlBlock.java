package UI;

import enums.*;
import jssc.SerialPort;
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

            if (i == MyEnums.COM_PORT.getValue()) {
                comboBoxPanel.add(comPortsComboBox);
            } else if (i == MyEnums.BAUDRATE.getValue()) {
                comboBoxPanel.add(baudRateComboBox);
            } else if (i == MyEnums.DATABITS.getValue()) {
                comboBoxPanel.add(dataBitsComboBox);
            } else if (i == MyEnums.PARTIY.getValue()) {
                comboBoxPanel.add(parityComboBox);
            } else if (i == MyEnums.STOPBITS.getValue()) {
                comboBoxPanel.add(stopBitsComboBox);
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
        button.addActionListener((event) -> {
                    if (button.getText().equals("Connect")) {
                        button.setText("Disconnect");
                    } else if (button.getText().equals("Disconnect")) {
                        button.setText("Connect");
                    }
                });
        buttonPanel.add(button);

//        comPortsComboBox.addActionListener(event -> {
//            if (comPortsComboBox.getSelectedItem())
//                }
//        JLabel label = new JLabel(labelName, JLabel.RIGHT);
//        label.setLabelFor(aJTextArea);
//
//        labelField.add(label);
//        JPanel p = new JPanel(new FlowLayout(FlowLayout.LEFT));
//        p.add(aJTextArea);
//        textField.add(p);
    }
}
