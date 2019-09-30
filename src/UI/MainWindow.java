package UI;

import javax.swing.*;
import java.awt.*;
import COM_port.Mediator;

public class MainWindow {

    private JFrame mainWindow = new JFrame();

    public void Application() {
        mainWindow.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        mainWindow.setTitle("COM messenger");
        mainWindow.setSize(500, 410);
        mainWindow.setLocationRelativeTo(null);

        Mediator mediator = new Mediator();

        mainWindow.getContentPane().add(mediator.getInputBlock(), BorderLayout.PAGE_START);
        mainWindow.getContentPane().add(mediator.getOutputBlock(), BorderLayout.CENTER);
        mainWindow.getContentPane().add(mediator.getDebugControlBlock(), BorderLayout.PAGE_END);

        mainWindow.setVisible(true);
        mainWindow.setResizable(false);
    }
}
