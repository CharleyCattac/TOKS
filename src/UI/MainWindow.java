package UI;

import javax.swing.*;
import java.awt.*;
import services.Mediator;

public class MainWindow {

    private JFrame mainWindow = new JFrame();

    public void Application() {
        mainWindow.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        mainWindow.setTitle("Channel emulator");
        mainWindow.setSize(540, 540);
        mainWindow.setLocationRelativeTo(null);

        Mediator mediator = new Mediator();

        mainWindow.getContentPane().add(mediator.getInputBlock(), BorderLayout.PAGE_START);
        mainWindow.getContentPane().add(mediator.getOutputBlock(), BorderLayout.CENTER);
        mainWindow.getContentPane().add(mediator.getSettingsBlock(), BorderLayout.PAGE_END);

        mainWindow.setVisible(true);
        mainWindow.setResizable(false);
    }
}
