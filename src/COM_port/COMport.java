package COM_port;

import jssc.*;

public class COMport {

    private static SerialPort serialPort;

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

//            serialPort.addEventListener(new PortReader(), SerialPort.MASK_RXCHAR);


            serialPort.writeBytes("Test".getBytes());
            byte[] buffer = serialPort.readBytes(10);
            serialPort.closePort();

        }
        catch (SerialPortException ex) {
            ex.printStackTrace();
        }
    }

    public void closePort() {
        System.out.println("I close port");
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
