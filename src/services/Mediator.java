package services;

import COMPortWrap.ComPort;
import UI.SettingsBlock;
import UI.OutputBlock;
import UI.InputBlock;
import coding.CRCMaster;
import enums.BaudRate;
import enums.DataBits;
import enums.Parity;
import enums.StopBits;
import jssc.SerialPortException;

public class Mediator {

    private InputBlock inputBlock;
    private OutputBlock outputBlock;
    private SettingsBlock settingsBlock;
    private ComPort comPort;

    private final int dataLoadSize = PackageManager.DATALOAD_SIZE;
    private char[] dataLoad = new char[dataLoadSize];
    private int sourceCode = 1;
    private int destinationCode = 1;
    private boolean packageErrorEmulation = false;

    public Mediator() {
         String[] fields = {
                "Com ports",
                "Baud rate",
                "Data bits",
                "Stop bits",
                 "Parity",
                 "Source address",
                 "Destination address",
                 "Package error"
        };
        int textFieldHeight = 6;
        int textFieldWidth = 38;
        inputBlock = new InputBlock(this, "Input   ", textFieldWidth, textFieldHeight);
        outputBlock = new OutputBlock("Output", textFieldWidth, textFieldHeight);
        settingsBlock = new SettingsBlock(this, fields);
        comPort = new ComPort(this);
        clearDataload();
    }

    public void openPort(String portName, BaudRate baudRate, DataBits dataBits,
                         StopBits stopBits, Parity parityMode,
                         String source, String destination, boolean packageError)
                         throws SerialPortException{
        try{
            sourceCode = Integer.parseInt(source);
            destinationCode = Integer.parseInt(destination);
            if (sourceCode < 0 || sourceCode > 255 ||
                    destinationCode < 0 || destinationCode > 255 ||
                sourceCode == destinationCode)
                throw new SerialPortException("Oops", "I", "Did it again");
            PackageManager.setInitialSource(sourceCode);
            packageErrorEmulation = packageError;
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
        //String unparsedPackage = PackageManager.unpackMessage(rawPackage);
        String unparsedPackage = PackageManager.unpackMessage(CRCMaster.decode(rawPackage));
        if (PackageManager.isSourceMismatched()) {
            return;
        }
        if (PackageManager.doesPackageHaveErrors()){
            sendInfoMessage("Package arrived with errors; unable to fix");
            return;
        }
        outputBlock.getTextField().append(unparsedPackage);
        if (CRCMaster.errorWasDiscovered())
            sendInfoMessage("Package arrived with an error; error fixed");
        else
            sendInfoMessage("Package arrived");
    }

    public void transferData(char data) {
        putCharIntoDataload(data);
        if (!dataloadIsFull())
            return;
        String binaryMessage = CRCMaster.encode(PackageManager.packMessage(destinationCode,
                sourceCode, dataLoad), packageErrorEmulation);
        comPort.sendMessage(binaryMessage);
        sendInfoMessage(StringTranslator.stringToHex(binaryMessage));
        clearDataload();
    }
    private boolean dataloadIsFull(){
        for (int i = 0; i < dataLoadSize; i++){
            if(dataLoad[i] == 0) return false;
        }
        return true;
    }
    private void putCharIntoDataload(char ch){
        for (int i = 0; i < dataLoadSize; i++){
            if(dataLoad[i] == 0) {
                dataLoad[i] = ch;
                break;
            }
        }
    }
    private void clearDataload(){
        for (int i = 0; i < dataLoadSize; i++){
            dataLoad[i] = 0;
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
