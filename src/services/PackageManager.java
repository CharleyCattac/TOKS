package services;

public class PackageManager {
    static public final int DATALOAD_SIZE = 7;

    static private final String START_BYTE = "11111110";
    static private final byte START_BYTE_INT = 0b1111110;
    static private final byte LAST_BYTE = 0;
    static private final byte ERROR_LAST_BYTE = 1;
    static private final int PACKAGE_SIZE = DATALOAD_SIZE + 4;

    static private String infoMessage = new String("");

    public static String parseMessage(int destination,
                                      int source,
                                      boolean errorEmulationEnabled,
                                      char[] dataload) {
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

        StringBuilder stringWithBitStuffing = new StringBuilder();
        for (int i = 0; i < PACKAGE_SIZE - 1; i++) {
            stringWithBitStuffing.append(fromByteToBits(packageData[i]));
        }

        String bitStuffingString = stringWithBitStuffing.toString();
        bitStuffingString = bitStuffingString.replace("1111111", "11111111");

        return START_BYTE + bitStuffingString;
    }

    public static String unparsePackage(String packageData) {
        packageData = packageData.replace("11111111", "111111");
        boolean hasErrors = false;

        if (!packageData.startsWith(START_BYTE)) {
            hasErrors = true;
        }

        int counter = 0;
        byte[] rawPackageData = new byte[PACKAGE_SIZE - 1];
        for (int i = 8; i < packageData.length(); i += 8) {
            rawPackageData[counter++] = fromStringToByte(packageData.substring(i, i + 8));
        }

        counter = 2;

        StringBuilder message = new StringBuilder();
        for (int i = 0; i < DATALOAD_SIZE; i++) {
            message.append((char)rawPackageData[counter++]);
        }

        if (rawPackageData[counter] != LAST_BYTE) {
            hasErrors = true;
        }

        return message.toString();
    }

    private static String fromByteToBits(byte aByte) {
        int divider = (int)Math.pow(2, 7);
        StringBuilder bitString = new StringBuilder();
        int checkInt;
        for (int i = 0; i < 8; i++) {
            checkInt = aByte & divider;
            if (checkInt > 0) {
                bitString.append("1");
            } else {
                bitString.append("0");
            }
            divider >>= 1;
        }
        return bitString.toString();
    }

    private static byte fromStringToByte(String string) {
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
        rawData.append(" ");
        for (byte element : packageData) {
            if (Integer.toHexString(Byte.toUnsignedInt(element)).length() == 1) {
                rawData.append("0");
            }
            rawData.append(Integer.toHexString(Byte.toUnsignedInt(element)));
            rawData.append(" ");
        }

        infoMessage = new String(rawData.toString());
    }

    public static String getInfoMessage(){
        return infoMessage;
    }

}

