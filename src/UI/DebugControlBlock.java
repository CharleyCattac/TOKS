package UI;

import javafx.scene.control.ComboBox;
import services.DebugLine;
import services.Mediator;
import enums.*;
import jssc.SerialPortList;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

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
    private DebugLine debugLine = new DebugLine();

    public DebugControlBlock(Mediator mediator, String[] rows) {
        super(new BorderLayout());

        JPanel settingsPanel = new JPanel(new BorderLayout());
        JPanel labelPanel = new JPanel(new GridLayout(rows.length, 1));
        JPanel comboBoxPanel = new JPanel((new GridLayout(rows.length, 1)));
        settingsPanel.add(labelPanel, BorderLayout.WEST);
        settingsPanel.add(comboBoxPanel, BorderLayout.EAST);
        JPanel buttonPanel = new JPanel(new BorderLayout());

        add(settingsPanel, BorderLayout.PAGE_START);
        add(buttonPanel, BorderLayout.CENTER);
        add(debugLine.getField(), BorderLayout.PAGE_END);

        for (int i = 0; i < rows.length; i++) {
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

        ActionListener listener = event -> {
            if (isAllFieldsNonEmpty()) {
                connectButton.setEnabled(true);
                writeToDebug("All paratemers are setted");
            }
        };

        for (Component element : comboBoxPanel.getComponents()) {
            ((JComboBox<?>)element).addActionListener(listener);
        }

        connectButton = new JButton();
        connectButton.setText("Connect");
        connectButton.setEnabled(false);
        connectButton.addActionListener((event) -> {
            if (connectButton.getText().equals("Connect")) {
                if (isValidate()) {
                    mediator.openPort((String) comPortsComboBox.getSelectedItem(),
                            (BaudRateEnum) baudRateComboBox.getSelectedItem(),
                            (DataBitsEnum) dataBitsComboBox.getSelectedItem(),
                            (StopBitsEnum) stopBitsComboBox.getSelectedItem(),
                            (ParityEnum) parityComboBox.getSelectedItem());
                    mediator.enableFormatting();
                    for (Component element : comboBoxPanel.getComponents()) {
                        element.setEnabled(false);
                    }
                    connectButton.setText("Disconnect");
                    writeToDebug("Connect was established");
                }
            } else if (connectButton.getText().equals("Disconnect")) {
                mediator.closePort();
                mediator.disableFormatting();
                for (Component element : comboBoxPanel.getComponents()) {
                    element.setEnabled(true);
                    ((JComboBox<?>)element).setSelectedItem(null);
                }
                connectButton.setText("Connect");
                connectButton.setEnabled(false);
                writeToDebug("Set all parameters to be able to connect to com port");
            }
        });
        buttonPanel.add(connectButton);
    }

    private boolean isAllFieldsNonEmpty() {
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

    private boolean isValidate() {
        if (((StopBitsEnum)stopBitsComboBox.getSelectedItem()).getValue() == 3 &&
                ((DataBitsEnum)dataBitsComboBox.getSelectedItem()).getValue() != 5) {
            writeToDebug("It's forbidden to use 1,5 stop bits not with 5 data bits");
            return false;
        }
        if (((StopBitsEnum)stopBitsComboBox.getSelectedItem()).getValue() == 2 &&
                ((DataBitsEnum)dataBitsComboBox.getSelectedItem()).getValue() == 5) {
            writeToDebug("It's forbidden to use 2 stop bits with 5 data bits");
            return false;
        }
        return true;
    }

    public void writeToDebug(String info) {
        debugLine.writeToField(info);
    }
}
