package services;

class PackageManager {
    static final int DATALOAD_SIZE = 7;
    static private final int PACKAGE_SIZE = DATALOAD_SIZE + 4;

    static private final String START_BYTE_STRING = "00001110";
    static private final byte START_BYTE_VALUE = 0b00001110;
    static private final byte ESC_BYTE_VALUE = 0b00011011;
    static private final byte LAST_BYTE_OK = 0;
    static private final byte LAST_BYTE_ERROR = 1;

    static private String infoMessage = "";
    static private int initSource = 1;

    static public boolean packageHadErrors = false;
    static public boolean mismatchedSource = false;

    static void setInitialSource(int source){
        initSource = source;
    }

    static String parseMessage(int destination,
                               int source,
                               boolean errorEmulationEnabled,
                               char[] dataload) {
        initSource = source;
        byte[] packageData = new byte[PACKAGE_SIZE - 1];
        int counter = 0;

        packageData[counter++] = Integer.valueOf(destination).byteValue();
        packageData[counter++] = Integer.valueOf(source).byteValue();
        for (int i = 0; i < DATALOAD_SIZE; i++) {
            packageData[counter++] = (byte)dataload[i];
        }

        if (errorEmulationEnabled) {
            packageData[counter] = LAST_BYTE_ERROR;
        } else {
            packageData[counter] = LAST_BYTE_OK;
        }

        StringBuilder bitStuffingBuilder = new StringBuilder();

        for (int i = 0; i < PACKAGE_SIZE - 1; i++) {
            if (packageData[i] == START_BYTE_VALUE) {
                bitStuffingBuilder.append(fromByteToBinaryString((byte) (packageData[i] - 1)));
                bitStuffingBuilder.append(fromByteToBinaryString(ESC_BYTE_VALUE));
            } else {
                bitStuffingBuilder.append(fromByteToBinaryString(packageData[i] ));
            }
        }

        String bitStuffedString = bitStuffingBuilder.toString();
        setInfoMessage(START_BYTE_STRING + bitStuffedString);
        return START_BYTE_STRING + bitStuffedString;
    }

    static String unparsePackage(String packageData) {
        packageHadErrors = false;
        mismatchedSource = false;

        if (!packageData.startsWith(START_BYTE_STRING)) {
            packageHadErrors = true;
            return null;
        }

        int counter = -1;
        byte[] rawPackageData = new byte[PACKAGE_SIZE];
        for (int i = 0; i < packageData.length(); i += Byte.SIZE) {
            counter++;
            rawPackageData[counter] = fromBinaryStringToByte(packageData.substring(i, i + Byte.SIZE));
            if (counter != 0)
                if (rawPackageData[counter - 1] == (START_BYTE_VALUE - 1) &&
                    rawPackageData[counter] == ESC_BYTE_VALUE &&
                    packageData.length() / Byte.SIZE > PACKAGE_SIZE){
                rawPackageData[counter - 1] += 1;
                counter--;
            }
        }

        counter = 1;
        if (rawPackageData[counter] != ((Integer)initSource).byteValue()) {
            mismatchedSource = true;
            return null;
        }

        counter += 2;
        StringBuilder message = new StringBuilder();
        for (int i = 0; i < DATALOAD_SIZE; i++) {
            message.append((char)rawPackageData[counter++]);
        }

        if (rawPackageData[counter] != LAST_BYTE_OK) {
            packageHadErrors = true;
            return null;
        }

        return message.toString();
    }

    private static String fromByteToBinaryString(byte aByte) {
        int divider = (int)Math.pow(2, 7);
        StringBuilder binaryString = new StringBuilder();
        int andResult;
        for (int i = 0; i < 8; i++) {
            andResult = aByte & divider;
            if (andResult > 0) {
                binaryString.append("1");
            } else {
                binaryString.append("0");
            }
            divider >>= 1;
        }
        return binaryString.toString();
    }

    private static byte fromBinaryStringToByte(String string) {
        int result = 0;
        for (int i = 0; i < string.length(); i++) {
            if (string.charAt(i) == '1') {
                result += Math.pow(2, 7 - i);
            }
        }
        return ((Integer)result).byteValue();
    }

    private static void setInfoMessage(byte[] packageData){
        StringBuilder rawData = new StringBuilder();

        if (Integer.toHexString(Byte.toUnsignedInt(START_BYTE_VALUE)).length() == 1) {
            rawData.append("0");
        }

        rawData.append(Integer.toHexString(Byte.toUnsignedInt(START_BYTE_VALUE)));
        rawData.append("_");
        for (byte element : packageData) {
            if (Integer.toHexString(Byte.toUnsignedInt(element)).length() == 1) {
                rawData.append("0");
            }
            rawData.append(Integer.toHexString(Byte.toUnsignedInt(element)));
            rawData.append("_");
        }

        rawData.deleteCharAt(rawData.length() - 1);
        infoMessage = rawData.toString();
    }

    private static void setInfoMessage(String packageData){
        byte[] rawPackageData = new byte[packageData.length() / Byte.SIZE - 1];
        int counter = 0;
        for (int i = 8; i < packageData.length(); i += 8) {
            rawPackageData[counter++] = fromBinaryStringToByte(packageData.substring(i, i + Byte.SIZE));
        }
        setInfoMessage(rawPackageData);
    }

    static String getInfoMessage(){
        return infoMessage;
    }

}

