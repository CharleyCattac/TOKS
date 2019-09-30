package UI;

import javax.swing.*;
import java.awt.*;
import COM_port.Mediator;

public class MainWindow {

    private JFrame mainWindow;
//    private ElementBlock inputPanel;
//    private ElementBlock outputPanel;
//    private DebugControlBlock debugControlPanel;


    public void Application() {
        mainWindow = new JFrame();
        mainWindow.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        mainWindow.setTitle("COM messenger");
        mainWindow.setSize(500, 410);
        mainWindow.setLocationRelativeTo(null);

        Mediator mediator = new Mediator();

//        inputPanel = ;
//        outputPanel = new ElementBlock("Output", 40, 6);
//        debugControlPanel = new DebugControlBlock("Debug & Control", fields);

        mainWindow.getContentPane().add(mediator.getInputBlock(), BorderLayout.PAGE_START);
        mainWindow.getContentPane().add(mediator.getOutputBlock(), BorderLayout.CENTER);
        mainWindow.getContentPane().add(mediator.getDebugControlBlock(), BorderLayout.PAGE_END);

        mainWindow.setVisible(true);
        mainWindow.setResizable(false);
    }
}
