package services;

class PackageManager {
    static final int DATALOAD_SIZE = 7;
    static private final int PACKAGE_SIZE = DATALOAD_SIZE + 4;

    static private final String START_BYTE = "11111110";
    static private final byte START_BYTE_INT = 0b1111110;
    static private final byte LAST_BYTE = 0;
    static private final byte ERROR_LAST_BYTE = 1;

    static private String infoMessage = new String("");
    static private int initSource = 1;

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
            packageData[counter] = ERROR_LAST_BYTE;
        } else {
            packageData[counter] = LAST_BYTE;
        }

        StringBuilder bitStuffedString = new StringBuilder();
        for (int i = 0; i < PACKAGE_SIZE - 1; i++) {
            bitStuffedString.append(fromByteToBinaryString(packageData[i]));
        }

        return START_BYTE + bitStuffedString.toString();
    }

    static String unparsePackage(String packageData) {
        boolean packageHasErrors = false;

        if (!packageData.startsWith(START_BYTE)) {
            packageHasErrors = true;
        }

        int counter = 0;
        byte[] rawPackageData = new byte[PACKAGE_SIZE - 1];
        for (int i = 8; i < packageData.length(); i += 8) {
            rawPackageData[counter++] = fromBinaryStringToByte(packageData.substring(i, i + Byte.SIZE));
        }

        counter = 0;
        if (rawPackageData[counter++] != ((Integer)initSource).byteValue()) {
            setInfoMessage("".getBytes());
            return null;
        }

        counter++;
        StringBuilder message = new StringBuilder();
        for (int i = 0; i < DATALOAD_SIZE; i++) {
            message.append((char)rawPackageData[counter++]);
        }

        if (rawPackageData[counter] != LAST_BYTE) {
            packageHasErrors = true;
        }

        if (packageHasErrors){
            setInfoMessage("Package has errors!".getBytes());
            return null;
        }
        else {
            setInfoMessage(rawPackageData);
            return message.toString();
        }
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
        if (Integer.toHexString(Byte.toUnsignedInt(START_BYTE_INT)).length() == 1) {
            rawData.append("0");
        }
        rawData.append(Integer.toHexString(Byte.toUnsignedInt(START_BYTE_INT)));
        rawData.append("_");
        for (byte element : packageData) {
            if (Integer.toHexString(Byte.toUnsignedInt(element)).length() == 1) {
                rawData.append("0");
            }
            rawData.append(Integer.toHexString(Byte.toUnsignedInt(element)));
            rawData.append("_");
        }

        infoMessage = rawData.toString();
    }

    static String getInfoMessage(){
        return infoMessage;
    }

}

