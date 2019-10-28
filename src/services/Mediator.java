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

import java.util.Timer;

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
                "Stop bits",
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

    /*
    public void openPort(String portName, BaudRate baudRate, DataBits dataBits,
                         StopBits stopBits, Parity parityMode) throws SerialPortException{
        comPort.initializePort(portName, baudRate.getValue(), dataBits.getValue(),
                stopBits.getValue(), parityMode.getValue());
    }
     */

    public void openPort(String portName, BaudRate baudRate, DataBits dataBits,
                         StopBits stopBits, Parity parityMode,
                         String source, String destination, boolean error) throws SerialPortException{
        try{
            sourceCode = Integer.parseInt(source);
            destinationCode = Integer.parseInt(destination);
            if (sourceCode < 0 || sourceCode > 255 ||
                    destinationCode < 0 || destinationCode > 255 ||
                sourceCode == destinationCode)
                throw new SerialPortException("Oops", "I", "Did it again");
            PackageManager.setInitialSource(sourceCode);
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
        if (PackageManager.mismatchedSource) {
            return;
        }
        if (PackageManager.packageHadErrors){
            sendInfoMessage("Package arrived with errors");
            return;
        }
        outputBlock.getTextField().append(unparsedPackage);
        sendInfoMessage("Package arrived");
    }

    public void transferData(char data) {
        putCharIntoDataload(data);
        if (!dataloadIsFull())
            return;
        //String string = "" + data;
        comPort.sendMessage(PackageManager.parseMessage(destinationCode,
                sourceCode, errorEmul, dataload));
        sendInfoMessage(PackageManager.getInfoMessage());
        clearDataload();
    }
    private boolean dataloadIsFull(){
        for (int i = 0; i < dataloadSize; i++){
            if(dataload[i] == 0) return false;
        }
        return true;
    }
    private void putCharIntoDataload(char ch){
        for (int i = 0; i < dataloadSize; i++){
            if(dataload[i] == 0) {
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
