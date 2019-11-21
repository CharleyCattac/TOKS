package UI;

import services.Mediator;

import javax.swing.*;
import java.awt.*;
import java.util.ArrayList;

public class SettingsBlock extends JPanel {

    private JCheckBox burstModeBox = new JCheckBox();
    private JButton connectButton;
    private OutputBlock infoBlock;

    public SettingsBlock(Mediator mediator, String[] rows,
                         int infoFieldWidth, int infoFieldHeight) {
        super(new BorderLayout());
        infoBlock = new OutputBlock("", infoFieldWidth, infoFieldHeight);

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
        //extraPanel2.add(infoLine.getArea(), BorderLayout.PAGE_END);
        extraPanel2.add(infoBlock, BorderLayout.PAGE_END);
        extraPanel1.add(extraButtons.get(3), BorderLayout.PAGE_START);
        extraPanel1.add(extraButtons.get(0), BorderLayout.WEST);
        extraPanel1.add(extraPanel2, BorderLayout.CENTER);
        extraPanel1.add(extraButtons.get(1), BorderLayout.EAST);
        extraPanel1.add(extraButtons.get(2), BorderLayout.PAGE_END);
        add(extraPanel1, BorderLayout.PAGE_START);

        JLabel label = new JLabel(rows[0], JLabel.LEFT);
        labelPanel.add(label);
        paramsPanel.add(burstModeBox);

        connectButton = new JButton();
        connectButton.setText("Connect");
        connectButton.addActionListener((event) -> {
            if (connectButton.getText().equals("Connect")) {
                mediator.connect(burstModeBox.isSelected());
                mediator.enableFormatting();
                setParamsEnabled(false);
                connectButton.setText("Disconnect");
                sendInfoMessage("Connection established");
            } else if (connectButton.getText().equals("Disconnect")) {
                mediator.disconnect();
                mediator.disableFormatting();
                setParamsEnabled(true);
                connectButton.setText("Connect");
                connectButton.setEnabled(true);
                sendInfoMessage("Disconnected");
            }
        });
        buttonPanel.add(connectButton);
    }

    private void setParamsEnabled(boolean status){
        burstModeBox.setEnabled(status);
    }

    public void sendInfoMessage(String info) {
        infoBlock.getTextField().append(info + '\n');
    }
}
