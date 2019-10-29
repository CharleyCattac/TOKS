package COMPortWrap;

import jssc.*;
import services.Mediator;

public class ComPort {

    private SerialPort serialPort;
    private Mediator mediator;

    public ComPort(Mediator mediator) {
        this.mediator = mediator;
    }

    public void initializePort(String portName, int baudRate, int dataBits,
                               int stopBits, int parityMode)
                                throws SerialPortException {
        serialPort = new SerialPort(portName);

        serialPort.openPort();
        serialPort.setParams(baudRate,
                dataBits,
                stopBits,
                parityMode);
        serialPort.setFlowControlMode(SerialPort.FLOWCONTROL_RTSCTS_IN |
                                      SerialPort.FLOWCONTROL_RTSCTS_OUT);
        serialPort.addEventListener(new PortReader(), SerialPort.MASK_RXCHAR);
    }

    public void closePort() throws SerialPortException{
        if (serialPort.closePort()) {
            serialPort = null;
        }
    }

    public void sendMessage(String pack) {
        try {
            serialPort.writeBytes(pack.getBytes());
        }
        catch (SerialPortException ex) {
            ex.printStackTrace();
            mediator.sendInfoMessage("Attempt to send message was failed");
        }
    }

    private class PortReader implements SerialPortEventListener {
        @Override
        public void serialEvent(SerialPortEvent event) {
            if (event.isRXCHAR() && event.getEventValue() > 0) {
                try {
                    String data = serialPort.readString(event.getEventValue());
                    mediator.outputData(data);
                }
                catch (SerialPortException ex) {
                    ex.printStackTrace();
                    mediator.sendInfoMessage("Attempt to read message was failed");
                }
            }
        }
    }
}
