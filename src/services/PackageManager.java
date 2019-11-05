package services;

import static services.StringTranslator.fromBinaryStringToByte;
import static services.StringTranslator.fromByteToBinaryString;

class PackageManager {
    static final int DATALOAD_SIZE = 7;
    static private final int PACKAGE_SIZE = DATALOAD_SIZE + 3;

    static private final String START_BYTE_STRING = "00001111";
    //static private final byte START_BYTE_VALUE = 0b00001111;
    //static private final byte ESC_BYTE_VALUE = 0b00011011;

    static private String hexMessage = "";
    static private int initSource = 1;

    static private boolean packageHasErrors = false;
    static private boolean mismatchedSource = false;

    static void setInitialSource(int source){
        initSource = source;
    }

    static String packMessage(int destination,
                              int source,
                              char[] dataload) {
        initSource = source;
        byte[] packageData = new byte[PACKAGE_SIZE - 1];
        int index = 0;

        packageData[index++] = Integer.valueOf(destination).byteValue();
        packageData[index++] = Integer.valueOf(source).byteValue();
        for (int i = 0; i < DATALOAD_SIZE; i++) {
            packageData[index++] = (byte)dataload[i];
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

        String bitStuffedString = binaryBuilder.toString()
                .replace("0000111", "00001110");

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

        packageData = packageData.replace("00001110","0000111");

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

        return message.toString();
    }

    static boolean doesPackageHaveErrors(){
        return packageHasErrors;
    }

    static boolean isSourceMismatched(){
        return  mismatchedSource;
    }

}

