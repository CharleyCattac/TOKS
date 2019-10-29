package services;

import static services.BinaryStringAssistant.fromBinaryStringToByte;
import static services.BinaryStringAssistant.fromByteToBinaryString;

class PackageManager {
    static final int DATALOAD_SIZE = 7;
    static private final int PACKAGE_SIZE = DATALOAD_SIZE + 4;

    static private final String START_BYTE_STRING = "00001111";
    //static private final byte START_BYTE_VALUE = 0b00001111;
    //static private final byte ESC_BYTE_VALUE = 0b00011011;
    static private final byte LAST_BYTE_OK = 0;
    static private final byte LAST_BYTE_ERROR = 1;

    static private String hexMessage = "";
    static private int initSource = 1;

    static private boolean packageHasErrors = false;
    static private boolean mismatchedSource = false;

    static void setInitialSource(int source){
        initSource = source;
    }

    static String packMessage(int destination,
                              int source,
                              boolean errorEmulationEnabled,
                              char[] dataload) {
        initSource = source;
        byte[] packageData = new byte[PACKAGE_SIZE - 1];
        int index = 0;

        packageData[index++] = Integer.valueOf(destination).byteValue();
        packageData[index++] = Integer.valueOf(source).byteValue();
        for (int i = 0; i < DATALOAD_SIZE; i++) {
            packageData[index++] = (byte)dataload[i];
        }

        if (errorEmulationEnabled) {
            packageData[index] = LAST_BYTE_ERROR;
        } else {
            packageData[index] = LAST_BYTE_OK;
        }

        StringBuilder binaryBuilder = new StringBuilder();

        for (int i = 0; i < PACKAGE_SIZE - 1; i++) {
            binaryBuilder.append(fromByteToBinaryString(packageData[i]));
            /*
            if (packageData[i] == START_BYTE_VALUE) {
                binaryBuilder.append(fromByteToBinaryString((byte) (packageData[i] - 1)));
                binaryBuilder.append(fromByteToBinaryString(ESC_BYTE_VALUE));
            } else {
                binaryBuilder.append(fromByteToBinaryString(packageData[i]));
            }
             */
        }
        System.out.println(binaryBuilder.toString());
        String bitStuffedString = binaryBuilder.toString()
                .replace(START_BYTE_STRING.substring(0, START_BYTE_STRING.length() - 1),
                        START_BYTE_STRING.substring(0, START_BYTE_STRING.length() - 1) + "1");
        System.out.println(binaryBuilder.toString());
        setHexMessage(START_BYTE_STRING + bitStuffedString);
        return START_BYTE_STRING + bitStuffedString;
    }

    static String unpackMessage(String packageData) {
        packageHasErrors = false;
        mismatchedSource = false;

        if (!packageData.startsWith(START_BYTE_STRING)) {
            packageHasErrors = true;
            return null;
        }
        packageData = packageData.substring(Byte.SIZE);

        System.out.println(packageData);
        packageData = packageData.replace(START_BYTE_STRING.substring(0, START_BYTE_STRING.length() - 1) + "1",
                                                START_BYTE_STRING.substring(0, START_BYTE_STRING.length() - 1));
        System.out.println(packageData);

        int index = -1;
        byte[] rawPackageData = new byte[PACKAGE_SIZE - 1];
        for (int i = 0; i < packageData.length(); i += Byte.SIZE) {
            index++;
            rawPackageData[index] = fromBinaryStringToByte(packageData.substring(i, i + Byte.SIZE));
            /*
            if (index != 0)
                if (rawPackageData[index - 1] == (START_BYTE_VALUE - 1) &&
                    rawPackageData[index] == ESC_BYTE_VALUE &&
                    packageData.length() / Byte.SIZE > PACKAGE_SIZE){
                rawPackageData[index - 1] += 1;
                index--;
            }
             */
        }

        index = 0;
        if (rawPackageData[index] != ((Integer)initSource).byteValue()) {
            mismatchedSource = true;
            return null;
        }

        index += 2;
        StringBuilder message = new StringBuilder();
        for (int i = 0; i < DATALOAD_SIZE; i++) {
            message.append((char)rawPackageData[index++]);
        }

        if (rawPackageData[index] != LAST_BYTE_OK) {
            packageHasErrors = true;
            return null;
        }

        return message.toString();
    }

    private static void setHexMessage(byte[] packageData){
        StringBuilder hexString = new StringBuilder();

        for (byte element : packageData) {
            if (Integer.toHexString(Byte.toUnsignedInt(element)).length() == 1) {
                hexString.append("0");
            }
            hexString.append(Integer.toHexString(Byte.toUnsignedInt(element)));
            hexString.append("_");
        }

        hexString.deleteCharAt(hexString.length() - 1);
        hexMessage = hexString.toString();
    }

    private static void setHexMessage(String packageData){
        StringBuilder hexSuitable = new StringBuilder(packageData);
        int extraZeros = hexSuitable.length() % 8;
        if (extraZeros != 0) {
            extraZeros = Byte.SIZE - extraZeros;
            System.out.println(extraZeros);
        }
        for (int i = 0; i < extraZeros; i++) {
            hexSuitable.append('0');
        }

        byte[] bytePackageData = new byte[hexSuitable.length() / Byte.SIZE];
        int counter = 0;
        for (int i = 0; i < hexSuitable.length(); i += Byte.SIZE) {
            bytePackageData[counter++] = fromBinaryStringToByte(hexSuitable.substring(i, i + Byte.SIZE));
        }
        setHexMessage(bytePackageData);
    }

    static String getHexMessage(){
        return hexMessage;
    }

    static boolean doesPackageHaveErrors(){
        return packageHasErrors;
    }

    static boolean isSourceMismatched(){
        return  mismatchedSource;
    }

}

