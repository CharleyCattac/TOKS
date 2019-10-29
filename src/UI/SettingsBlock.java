package UI;

import services.InfoLine;
import services.Mediator;
import enums.*;
import jssc.SerialPortList;

import javax.swing.*;
import java.awt.*;
import java.util.ArrayList;

public class SettingsBlock extends JPanel {

    private JComboBox<String> portsBox =
            new JComboBox<>(SerialPortList.getPortNames());
    private JComboBox<BaudRate> baudRateBox =
            new JComboBox<>(BaudRate.values());
    private JComboBox<DataBits> dataBitsBox =
            new JComboBox<>(DataBits.values());
    private JComboBox<StopBits> stopBitsBox =
            new JComboBox<>(StopBits.values());
    private JComboBox<Parity> parityBox =
            new JComboBox<>(Parity.values());

    private JTextField sourceField = new JTextField();
    private JTextField destinationField = new JTextField();
    private JCheckBox packageErrorBox = new JCheckBox();

    private JCheckBox codingErrorBox = new JCheckBox();

    private JButton refreshButton;
    private JButton connectButton;
    private InfoLine infoLine = new InfoLine();

    public SettingsBlock(Mediator mediator, String[] rows) {
        super(new BorderLayout());

        JPanel settingsPanel = new JPanel(new BorderLayout());
        JPanel labelPanel = new JPanel(new GridLayout(rows.length, 1));
        JPanel paramsPanel = new JPanel((new GridLayout(rows.length, 1)));
        settingsPanel.add(labelPanel, BorderLayout.WEST);
        settingsPanel.add(paramsPanel, BorderLayout.EAST);
        JPanel buttonPanel = new JPanel(new GridLayout(1, 2));

        JPanel extraPanel1 = new JPanel(new BorderLayout());
        JPanel extraPanel2 = new JPanel(new BorderLayout());
        ArrayList<JButton> extraButtons = new ArrayList<JButton>();
        for (int i = 0; i < 4; i++) {
            extraButtons.add(new JButton());
            extraButtons.get(i).setEnabled(false);
            extraButtons.get(i).setBorderPainted(false);
        }

        extraPanel2.add(settingsPanel, BorderLayout.PAGE_START);
        extraPanel2.add(buttonPanel, BorderLayout.CENTER);
        extraPanel2.add(infoLine.getField(), BorderLayout.PAGE_END);
        extraPanel1.add(extraButtons.get(3), BorderLayout.PAGE_START);
        extraPanel1.add(extraButtons.get(0), BorderLayout.WEST);
        extraPanel1.add(extraPanel2, BorderLayout.CENTER);
        extraPanel1.add(extraButtons.get(1), BorderLayout.EAST);
        extraPanel1.add(extraButtons.get(2), BorderLayout.PAGE_END);
        add(extraPanel1, BorderLayout.PAGE_START);

        for (int i = 0; i < rows.length; i++) {
            JLabel label = new JLabel(rows[i], JLabel.LEFT);
            labelPanel.add(label);

            if (i == Params.COM_PORT.getValue()) {
                paramsPanel.add(portsBox);
                portsBox.setSelectedItem(0);
            } else if (i == Params.BAUDRATE.getValue()) {
                paramsPanel.add(baudRateBox);
                baudRateBox.setSelectedItem(0);
            } else if (i == Params.DATABITS.getValue()) {
                paramsPanel.add(dataBitsBox);
                dataBitsBox.setSelectedItem(0);
            } else if (i == Params.PARITY.getValue()) {
                paramsPanel.add(parityBox);
                parityBox.setSelectedItem(0);
            } else if (i == Params.STOPBITS.getValue()) {
                paramsPanel.add(stopBitsBox);
                stopBitsBox.setSelectedItem(0);
            } else if (i == Params.SOURCE.getValue()) {
                paramsPanel.add(sourceField);
            } else if (i == Params.DESTINATION.getValue()) {
                paramsPanel.add(destinationField);
            } else if (i == Params.PACKAGE_ERROR.getValue()) {
                paramsPanel.add(packageErrorBox);
            } else if (i == Params.CODING_ERROR.getValue()) {
                paramsPanel.add(codingErrorBox);
            }
        }

        setParamsEnabled(SerialPortList.getPortNames().length != 0);

        refreshButton = new JButton();
        refreshButton.setBackground(Color.PINK);
        refreshButton.setText("Refresh");
        refreshButton.addActionListener((event) -> {
            String[] ports = SerialPortList.getPortNames();
            portsBox.removeAllItems();
            for (String s : ports) {
                portsBox.addItem(s);
            }
            if (ports.length == 0) {
                setButtonEnabled(connectButton, false);
                setParamsEnabled(false);
                sendInfoMessage("Couldn't discover any active pair of ports");
            }
            else {
                setButtonEnabled(connectButton, true);
                setParamsEnabled(true);
                sendInfoMessage("Active ports discovered");
            }
        });
        buttonPanel.add(refreshButton);

        connectButton = new JButton();
        connectButton.setText("Connect");
        setButtonEnabled(connectButton, SerialPortList.getPortNames().length != 0);
        connectButton.addActionListener((event) -> {
            try {
                if (connectButton.getText().equals("Connect") && isNotForbidden()) {
                    mediator.openPort((String) portsBox.getSelectedItem(),
                            (BaudRate) baudRateBox.getSelectedItem(),
                            (DataBits) dataBitsBox.getSelectedItem(),
                            (StopBits) stopBitsBox.getSelectedItem(),
                            (Parity) parityBox.getSelectedItem(),
                            sourceField.getText(),
                            destinationField.getText(),
                            packageErrorBox.isSelected());
                    mediator.enableFormatting();
                    setParamsEnabled(false);
                    connectButton.setText("Disconnect");
                    setButtonEnabled(refreshButton, false);
                    sendInfoMessage("Connected to " + portsBox.getSelectedItem());
                } else if (connectButton.getText().equals("Disconnect")) {
                    mediator.closePort();
                    mediator.disableFormatting();
                    setParamsEnabled(true);
                    connectButton.setText("Connect");
                    setButtonEnabled(connectButton, SerialPortList.getPortNames().length != 0);
                    setButtonEnabled(refreshButton, true);
                    sendInfoMessage("Disconnected from " + portsBox.getSelectedItem());
                }
            } catch (Exception ex){
                ex.printStackTrace();
                mediator.sendInfoMessage("Unable to perform requested action");
            }
        });
        buttonPanel.add(connectButton);
    }

    private void setParamsEnabled(boolean status){
        portsBox.setEnabled(status);
        baudRateBox.setEnabled(status);
        dataBitsBox.setEnabled(status);
        parityBox.setEnabled(status);
        stopBitsBox.setEnabled(status);
        sourceField.setEnabled(status);
        destinationField.setEnabled(status);
        packageErrorBox.setEnabled(status);
    }

    private void setButtonEnabled(JButton button, boolean status){
        button.setEnabled(status);
        button.setBackground(status? Color.PINK : Color.DARK_GRAY);
    }

    private boolean isNotForbidden() {
        if (((StopBits) stopBitsBox.getSelectedItem()).getValue() == 3 &&
                ((DataBits) dataBitsBox.getSelectedItem()).getValue() != 5
        || ((StopBits) stopBitsBox.getSelectedItem()).getValue() == 2 &&
                ((DataBits) dataBitsBox.getSelectedItem()).getValue() == 5) {
            sendInfoMessage("Forbidden combination!");
            return false;
        }
        return true;
    }

    public void sendInfoMessage(String info) {
        infoLine.writeToField(info);
    }
}
