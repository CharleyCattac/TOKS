package services;

import entities.ComPort;
import UI.SettingsBlock;
import UI.OutputBlock;
import UI.InputBlock;
import enums.BaudRate;
import enums.DataBits;
import enums.Parity;
import enums.StopBits;
import jssc.SerialPortException;

public class Mediator {
    private final int textFieldWidth = 38;
    private final int textFieldHeight = 6;

    private InputBlock inputBlock;
    private OutputBlock outputBlock;
    private SettingsBlock settingsBlock;
    private ComPort comPort;

    private final int dataloadSize = PackageManager.DATALOAD_SIZE;
    private char[] dataload = new char[dataloadSize];
    private int sourceCode = 1;
    private int destinationCode = 1;
    private boolean errorEmul = false;

    public Mediator() {
         String[] fields = {
                "Com ports",
                "Baud rate",
                "Data bits",
                "Stop bit",
                 "Parity",
                 "Source address",
                 "Destination address",
                 "Error"
        };
        inputBlock = new InputBlock(this, "Input   ", textFieldWidth, textFieldHeight);
        outputBlock = new OutputBlock("Output", textFieldWidth, textFieldHeight);
        settingsBlock = new SettingsBlock(this, fields);
        comPort = new ComPort(this);
        clearDataload();
    }

    public void openPort(String portName, BaudRate baudRate, DataBits dataBits,
                         StopBits stopBits, Parity parityMode) throws SerialPortException{
        comPort.initializePort(portName, baudRate.getValue(), dataBits.getValue(),
                stopBits.getValue(), parityMode.getValue());
    }

    public void openPort(String portName, BaudRate baudRate, DataBits dataBits,
                         StopBits stopBits, Parity parityMode,
                         String source, String destination, boolean error) throws SerialPortException{
        try{
            sourceCode = Integer.parseInt(source);
            destinationCode = Integer.parseInt(destination);
            if (sourceCode < 0 || sourceCode > 255 ||
                    destinationCode < 0 || destinationCode > 255)
                throw new SerialPortException("Oops", "I", "Did it again");
            errorEmul = error;
        } catch (Exception ex){
            throw new SerialPortException("Yes", "This", "Sucks");
            //sorry not sorry
        }
        comPort.initializePort(portName, baudRate.getValue(), dataBits.getValue(),
                stopBits.getValue(), parityMode.getValue());
    }

    public void closePort()throws SerialPortException {
        comPort.closePort();
    }

    public void enableFormatting() {
        inputBlock.getTextField().setFocusable(true);
    }

    public void disableFormatting() {
        inputBlock.getTextField().setFocusable(false);
    }

    public void outputData(String rawPackage) {
        String unparsedPackage = PackageManager.unparsePackage(rawPackage);
        /*
        char inFlag = Integer.parseInt(rawPackage.substring(0, rawPackage.indexOf("_")));
        System.out.println(inFlag);
        if (--inFlag != flag){
            sendInfoMessage("Error!");
            return;
        }
        String noErrorPackage = rawPackage.substring(rawPackage.indexOf("_") + 1);
        int dest = Integer.parseInt(noErrorPackage.substring(0, rawPackage.indexOf("_")));
        if (dest != sourceCode)
            return;
        outputBlock.getTextField().append(noErrorPackage.substring(rawPackage.indexOf("_") + 1));
        sendInfoMessage(rawPackage);
         */
        //int dest = Integer.parseInt(rawPackage.substring(0, rawPackage.indexOf("_")));
        //if (dest != sourceCode)
        //    return;
        outputBlock.getTextField().append(rawPackage.substring(rawPackage.indexOf("_") + 1));
        sendInfoMessage(rawPackage);
    }

    public void transferData(char data) {
        putCharIntoDataload(data);
        if (!dataloadIsFull())
            return;
        //String string = "" + data;
        comPort.sendMessage(PackageManager.parseMessage(destinationCode,
                sourceCode, errorEmul, dataload));
        clearDataload();
    }
    private boolean dataloadIsFull(){
        for (int i = 0; i < dataloadSize; i++){
            if(dataload[i] == 0b0000_0000) return false;
        }
        return true;
    }
    private void putCharIntoDataload(char ch){
        for (int i = 0; i < dataloadSize; i++){
            if(dataload[i] == 0b0000_0000) {
                dataload[i] = ch;
                break;
            }
        }
    }
    private void clearDataload(){
        for (int i = 0; i < dataloadSize; i++){
            dataload[i] = 0;
        }
    }

    public void sendInfoMessage(String data) {
        settingsBlock.sendInfoMessage(data);
    }

    public OutputBlock getInputBlock() {
        return inputBlock;
    }

    public OutputBlock getOutputBlock() {
        return outputBlock;
    }

    public SettingsBlock getSettingsBlock() {
        return settingsBlock;
    }
}
