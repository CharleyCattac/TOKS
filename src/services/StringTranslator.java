package services;

class StringTranslator {
    static String fromByteToBinaryString(byte byteValue) {
        int divider = (int)Math.pow(2, 7);
        StringBuilder binaryString = new StringBuilder();
        int andResult;
        for (int i = 0; i < 8; i++) {
            andResult = byteValue & divider;
            if (andResult > 0) {
                binaryString.append("1");
            } else {
                binaryString.append("0");
            }
            divider >>= 1;
        }
        return binaryString.toString();
    }

    static byte fromBinaryStringToByte(String string) {
        int result = 0;
        for (int i = 0; i < string.length(); i++) {
            if (string.charAt(i) == '1') {
                result += Math.pow(2, 7 - i);
            }
        }
        return ((Integer)result).byteValue();
    }

    private static String stringToHex(byte[] packageData){
        StringBuilder hexString = new StringBuilder();

        for (byte element : packageData) {
            if (Integer.toHexString(Byte.toUnsignedInt(element)).length() == 1) {
                hexString.append("0");
            }
            hexString.append(Integer.toHexString(Byte.toUnsignedInt(element)));
            hexString.append("_");
        }

        hexString.deleteCharAt(hexString.length() - 1);
        return hexString.toString();
    }

    static String stringToHex(String packageData){
        StringBuilder hexSuitable = new StringBuilder(packageData);

        int extraZeros = hexSuitable.length() % 8;

        if (extraZeros != 0) {
            extraZeros = Byte.SIZE - extraZeros;
        }
        for (int i = 0; i < extraZeros; i++) {
            hexSuitable.append('0');
        }

        byte[] bytePackageData = new byte[hexSuitable.length() / Byte.SIZE];
        int counter = 0;
        for (int i = 0; i < hexSuitable.length(); i += Byte.SIZE) {
            bytePackageData[counter++] = fromBinaryStringToByte(hexSuitable.substring(i, i + Byte.SIZE));
        }
        return stringToHex(bytePackageData);
    }
}
