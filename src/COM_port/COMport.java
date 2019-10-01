package COM_port;

import jssc.*;
import services.Mediator;

public class COMport {

    private static SerialPort serialPort;
    private Mediator mediator;

    public COMport(Mediator mediator) {
        this.mediator = mediator;
    }

    public void initializePort(String portName, int baudRate, int dataBits,
                               int stopBits, int parityMode) {
        serialPort = new SerialPort(portName);

        try {
            serialPort.openPort();
            serialPort.setParams(baudRate,
                    dataBits,
                    stopBits,
                    parityMode);

            serialPort.setFlowControlMode(SerialPort.FLOWCONTROL_RTSCTS_IN |
                                          SerialPort.FLOWCONTROL_RTSCTS_OUT);

            serialPort.addEventListener(new PortReader(), SerialPort.MASK_RXCHAR);
        }
        catch (SerialPortException ex) {
            ex.printStackTrace();
            mediator.writeToDebug("Couldn't open port");
        }
    }

    public void closePort() {
        try {
            if (serialPort.closePort()) {
                serialPort = null;
            }
        }
        catch (SerialPortException ex) {
            mediator.writeToDebug("Couldn't close port!!!");
        }
    }

    public void sendMessage(String data) {
        try {
            serialPort.writeBytes(data.getBytes());
        }
        catch (SerialPortException ex) {
            ex.printStackTrace();
            mediator.writeToDebug("Trying to send data was failed!!!");
        }
    }

    private class PortReader implements SerialPortEventListener {
        @Override
        public void serialEvent(SerialPortEvent event) {
            if (event.isRXCHAR() && event.getEventValue() > 0) {
                try {
                    String data = serialPort.readString(event.getEventValue());
                    mediator.writeData(data);
                    System.out.println(data);
                }
                catch (SerialPortException ex) {
                    ex.printStackTrace();
                    mediator.writeToDebug("Trying to read message was unsuccessful!!!");
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
