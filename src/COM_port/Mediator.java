package COM_port;

import UI.DebugControlBlock;
import UI.ElementBlock;
import enums.BaudRateEnum;
import enums.DataBitsEnum;
import enums.ParityEnum;
import enums.StopBitsEnum;

public class Mediator {
    private ElementBlock inputBlock;
    private ElementBlock outputBlock;
    private DebugControlBlock debugControlBlock;
    private COMport aCOMport;
    private String[] fields = {
            "Com ports",
            "Baud rate",
            "Data bits",
            "Parity",
            "Stop bit"
    };

    public Mediator() {
        inputBlock = new ElementBlock(this, "Input   ", 40, 6);
        outputBlock = new ElementBlock(this, "Output", 40, 6);
        debugControlBlock = new DebugControlBlock(this, fields);
        aCOMport = new COMport(this);
    }

    public void openPort(String portName, BaudRateEnum baudRate, DataBitsEnum dataBits,
                         StopBitsEnum stopBits, ParityEnum parityMode) {
        aCOMport.initializePort(portName, baudRate.getValue(), dataBits.getValue(),
                stopBits.getValue(), parityMode.getValue());
    }

    public void closePort() {
        aCOMport.closePort();
    }

    public void enableFormatting() {
        inputBlock.getTextField().setFocusable(true);
    }

    public void disableFormatting() {
        inputBlock.getTextField().setFocusable(false);
    }

    public void writeData(String data) {
        outputBlock.getTextField().append(data);
    }

    public void transferData(char data) {
        String string = "" + data;
        aCOMport.sendMessage(string);
    }

    public void writeToDebug(String data) {
        debugControlBlock.writeToDebug(data);
    }

    public ElementBlock getInputBlock() {
        return inputBlock;
    }

    public ElementBlock getOutputBlock() {
        return outputBlock;
    }

    public DebugControlBlock getDebugControlBlock() {
        return debugControlBlock;
    }
}
