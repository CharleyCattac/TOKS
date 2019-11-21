package services;

import UI.SettingsBlock;
import UI.OutputBlock;
import UI.InputBlock;
import values.Constants;

public class Mediator {

    private InputBlock inputBlock;
    private OutputBlock outputBlock;
    private SettingsBlock settingsBlock;

    private char[] burstModeBuffer = new char[Constants.BURST_SIZE];
    private boolean burstModeEnabled = false;
    private StringBuilder wholeMessage = new StringBuilder("");

    private boolean transmissionIsOver = true;

    public boolean isTransmissionIsOver() {
        return transmissionIsOver;
    }

    public Mediator() {
         String[] fields = {
                "Burst mode"
        };
        int textFieldHeight = 9;
        int textFieldWidth = 38;
        inputBlock = new InputBlock(this, "Input   ", textFieldWidth, textFieldHeight);
        outputBlock = new OutputBlock("Output", textFieldWidth, textFieldHeight);
        settingsBlock = new SettingsBlock(this, fields, textFieldWidth - 3, textFieldHeight - 4);
        clearPackage();
    }

    public void connect(boolean burstMode) {
        burstModeEnabled = burstMode;
        clearPackage();
    }

    public void disconnect() {
        clearPackage();
    }

    public void enableFormatting() {
        inputBlock.getTextField().setFocusable(true);
    }

    public void disableFormatting() {
        inputBlock.getTextField().setFocusable(false);
    }

    public void transferData(char data) {
        //обнулить счетчик
        int collisionCounter = 0;

        //пакетный режим -> пакет не полон -> продолжить набирать пакет
        if (burstModeEnabled && packageIsNotFull()) {
            addCharToPackage(data);
            if (packageIsNotFull()) return;
        }

        //флаг конца передачи, флаг осуществления пересылки и строка коллизий
        transmissionIsOver = false;
        boolean charHasBeenSent = false;
        StringBuilder collisions = new StringBuilder();

        //начало передачи
        while(!transmissionIsOver) {
            //ожидание освобождения канала
            while (channelIsBusy());

            //передача кадра
            if (!charHasBeenSent) {
                if (burstModeEnabled)
                    sentData(String.valueOf(burstModeBuffer));
                else
                    sentData(String.valueOf(data));
                charHasBeenSent = true;
            }
            else {
                //кадр уже был отправлен -> отправляем сбойный кадр
                sentData(String.valueOf(Constants.ERROR_SYMBOL));
            }

            //выжидание окна коллизий
            try {
                Thread.sleep(Constants.COLLISION_DURATION);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }

            //обработка возникновения коллизии
            if(collisionOccurred()) {
                //коллизия возникла -> инкрементация счетчика попыток
                collisions.append(Constants.ERROR_SYMBOL);
                collisionCounter++;

                //кол-во возможных попыток не превышено -> выжидание случайной задержки
                if (collisionCounter < Constants.MAX_COLLUSION_AMOUNT) {
                    try {
                        Thread.sleep(makeDelayInMillis(collisionCounter));
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                }
                //кол-во возможных попыток превышено -> конец передачи (ошибка)
                else {
                    sentData(String.valueOf(Constants.ERROR_SYMBOL));
                    conductTransmittion();
                    transmissionIsOver = true;
                    sendInfoMessage(collisions.toString() + "\t Failure");
                }
            }
            else {
                //коллизия не возникла -> конец передачи
                conductTransmittion();
                transmissionIsOver = true;
                sendInfoMessage(collisions.toString() + "\t Success");
                if (burstModeEnabled)
                    clearPackage();
            }
        }
    }

    private void sentData(String data) {
        wholeMessage.append(data);
    }

    private void conductTransmittion(){
        receiveData(wholeMessage.toString());
        wholeMessage = new StringBuilder("");
    }

    private void receiveData(String data) {
        //кадр сбойный -> вывод не осуществяется
        if (burstModeEnabled && data.length() < 14
                || !burstModeEnabled && data.length() < 11) {
            if (burstModeEnabled)
                outputBlock.getTextField().append(data.substring(0, 4));
            else
                outputBlock.getTextField().append(String.valueOf(data.charAt(0)));
        }
    }

    private boolean packageIsNotFull(){
        for (int i = 0; i < Constants.BURST_SIZE; i++){
            if(burstModeBuffer[i] == 0) return true;
        }
        return false;
    }

    private void addCharToPackage(char ch){
        for (int i = 0; i < Constants.BURST_SIZE; i++){
            if(burstModeBuffer[i] == 0) {
                burstModeBuffer[i] = ch;
                break;
            }
        }
    }

    private void clearPackage(){
        for (int i = 0; i < Constants.BURST_SIZE; i++){
            burstModeBuffer[i] = 0;
        }
    }

    private void sendInfoMessage(String data) {
        settingsBlock.sendInfoMessage(data);
    }

    private int makeDelayInMillis(int n) {
        return (int)Math.round(Math.random() * Math.pow(2, Math.min(n, Constants.MAX_COLLUSION_AMOUNT)));
    }

    private boolean channelIsBusy() {
        return (System.currentTimeMillis() % 2) == 1;
    }

    private boolean collisionOccurred() {
        return (System.currentTimeMillis() % 2) != 1;
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
