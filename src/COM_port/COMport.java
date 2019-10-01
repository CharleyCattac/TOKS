package COM_port;

import jssc.*;

public class COMport {

    private static SerialPort serialPort;
    private Mediator mediator;

    public COMport(Mediator mediator) {
        this.mediator = mediator;
    }

    public void initializePort(String portName, int baudRate, int dataBits,
                               int stopBits, int parityMode) {
        System.out.println(portName);
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
        }
    }

    public void closePort() {
        try {
            if (serialPort.closePort()) {
                serialPort = null;
            }
        }
        catch (SerialPortException ex) {}
        System.out.println("I close port");
    }

    public void sendMessage(String data) {
        try {
            serialPort.writeBytes(data.getBytes());
        }
        catch (SerialPortException ex) {
            ex.printStackTrace();
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
