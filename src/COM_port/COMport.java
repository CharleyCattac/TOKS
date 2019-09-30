package COM_port;

import javafx.embed.swing.JFXPanel;
import jssc.*;

public class COMport {

    private static SerialPort serialPort;

    static public void main(String[] args) {

        serialPort = new SerialPort(SerialPortList.getPortNames()[0]);

        try {
            serialPort.openPort();
            serialPort.setParams(SerialPort.BAUDRATE_9600,
                    SerialPort.DATABITS_8,
                    SerialPort.STOPBITS_1,
                    SerialPort.PARITY_NONE);

            serialPort.setFlowControlMode(SerialPort.FLOWCONTROL_RTSCTS_IN |
                                          SerialPort.FLOWCONTROL_RTSCTS_OUT);

//            serialPort.addEventListener(new PortReader(), SerialPort.MASK_RXCHAR);


            serialPort.writeBytes("Test".getBytes());
            byte[] buffer = serialPort.readBytes(10);
            serialPort.closePort();

        }
        catch (SerialPortException ex) {
            ex.printStackTrace();
        }
    }

    private static class PortReader implements SerialPortEventListener {

        public void serialEvent(SerialPortEvent event) {
            if (event.isRXCHAR() && event.getEventValue() == 10) {
                try {
                    String data = serialPort.readString(event.getEventValue());
                    System.out.println(data);
                    byte buffer[] = serialPort.readBytes(10);
                    System.out.println(buffer.toString());
                    serialPort.writeString("Get data");
                }
                catch (SerialPortException ex) {
                    ex.printStackTrace();
                }
            }
            else if (event.isCTS()) {
                if (event.getEventValue() == 1) {
                    System.out.println("CTS - ON");
                }
                else {
                    System.out.println("CTS - OFF");
                }
            }
            else {
                System.out.println("DSR - OFF");
            }
        }
    }
}
