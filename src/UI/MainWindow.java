package UI;

import javax.swing.*;
import javax.swing.border.Border;
//import javax.xml.bind.Element;
import java.awt.*;

public class MainWindow {

    private JFrame mainWindow;
    private ElementBlock inputPanel;
    private ElementBlock outputPanel;
    private DebugControlBlock debugControlPanel;

    public void Application() {
        mainWindow = new JFrame();
        mainWindow.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        mainWindow.setTitle("COM messenger");
        mainWindow.setSize(500, 400);
        mainWindow.setLocationRelativeTo(null);

        inputPanel = new ElementBlock("Input   ", 40, 6);
        outputPanel = new ElementBlock("Output", 40, 6);
        String[] fields = { "Com ports", "Baud rate", "Data bits", "Parity", "Stop bit" };
        debugControlPanel = new DebugControlBlock("Debug & Control", fields);
        JButton button = new JButton();
        button.setText("Connect");

        mainWindow.getContentPane().add(inputPanel, BorderLayout.PAGE_START);
        mainWindow.getContentPane().add(outputPanel, BorderLayout.CENTER);
        mainWindow.getContentPane().add(debugControlPanel, BorderLayout.PAGE_END);
//        mainWindow.getContentPane().add(button, BorderLayout.AFTER_LAST_LINE);

        mainWindow.setVisible(true);
        mainWindow.setResizable(false);
    }
}
