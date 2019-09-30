package UI;

import COM_port.Mediator;
import enums.*;
import jssc.SerialPort;
import jssc.SerialPortList;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.LinkedList;

public class DebugControlBlock extends JPanel {

    private JComboBox<String> comPortsComboBox =
            new JComboBox<>(SerialPortList.getPortNames());
    private JComboBox<BaudRateEnum> baudRateComboBox =
            new JComboBox<>(BaudRateEnum.values());
    private JComboBox<DataBitsEnum> dataBitsComboBox =
            new JComboBox<>(DataBitsEnum.values());
    private JComboBox<ParityEnum> parityComboBox =
            new JComboBox<>(ParityEnum.values());
    private JComboBox<StopBitsEnum> stopBitsComboBox =
            new JComboBox<>(StopBitsEnum.values());
    private JButton connectButton;
    private Mediator mediator;
    private JLabel[] labels;

    public DebugControlBlock(Mediator mediator, String[] rows) {
        super(new BorderLayout());
        ActionListener listener = new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                if (isAllFieldsNonEmpty()) {
                    connectButton.setEnabled(true);
                }
            }
        };

        comPortsComboBox.addActionListener(listener);
        baudRateComboBox.addActionListener(listener);
        dataBitsComboBox.addActionListener(listener);
        parityComboBox.addActionListener(listener);
        stopBitsComboBox.addActionListener(listener);

        this.mediator = mediator;
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
                comPortsComboBox.setSelectedItem(null);
            } else if (i == MyEnums.BAUDRATE.getValue()) {
                comboBoxPanel.add(baudRateComboBox);
                baudRateComboBox.setSelectedItem(null);
            } else if (i == MyEnums.DATABITS.getValue()) {
                comboBoxPanel.add(dataBitsComboBox);
                dataBitsComboBox.setSelectedItem(null);
            } else if (i == MyEnums.PARTIY.getValue()) {
                comboBoxPanel.add(parityComboBox);
                parityComboBox.setSelectedItem(null);
            } else if (i == MyEnums.STOPBITS.getValue()) {
                comboBoxPanel.add(stopBitsComboBox);
                stopBitsComboBox.setSelectedItem(null);
            }
        }

        connectButton = new JButton();
        connectButton.setText("Connect");
        connectButton.setEnabled(false);
        connectButton.addActionListener((event) -> {
                    if (connectButton.getText().equals("Connect")) {
//                        System.out.println((String)comPortsComboBox.getSelectedItem());
//                        System.out.println(((BaudRateEnum)baudRateComboBox.getSelectedItem()).getValue());
//                        System.out.println(((DataBitsEnum)dataBitsComboBox.getSelectedItem()).getValue());
//                        System.out.println(((StopBitsEnum)stopBitsComboBox.getSelectedItem()).getValue());
//                        System.out.println(((ParityEnum)parityComboBox.getSelectedItem()).getValue());
                        mediator.enableFormatting();
                        comPortsComboBox.setEditable(false);
                        baudRateComboBox.setEditable(false);
                        dataBitsComboBox.setEditable(false);
                        stopBitsComboBox.setEditable(false);
                        parityComboBox.setEditable(false);
//                        mediator.openPort(comPortsComboBox.getName(),
//                                (BaudRateEnum)baudRateComboBox.getSelectedItem(),
//                                (DataBitsEnum)dataBitsComboBox.getSelectedItem(),
//                                (StopBitsEnum)stopBitsComboBox.getSelectedItem(),
//                                (ParityEnum)parityComboBox.getSelectedItem());
                        connectButton.setText("Disconnect");
                    } else if (connectButton.getText().equals("Disconnect")) {
                        mediator.closePort();
                        mediator.disableFormatting();
                        comPortsComboBox.setSelectedItem(null);
                        baudRateComboBox.setSelectedItem(null);
                        dataBitsComboBox.setSelectedItem(null);
                        parityComboBox.setSelectedItem(null);
                        stopBitsComboBox.setSelectedItem(null);
                        connectButton.setText("Connect");
                        connectButton.setEnabled(false);
                    }
                });
        buttonPanel.add(connectButton);
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

    public boolean isAllFieldsNonEmpty() {
        if (comPortsComboBox.getSelectedItem() != null &&
            baudRateComboBox.getSelectedItem() != null &&
            dataBitsComboBox.getSelectedItem() != null &&
            parityComboBox.getSelectedItem() != null &&
            stopBitsComboBox.getSelectedItem() != null) {
            return true;
        } else {
            return false;
        }
    }
}
