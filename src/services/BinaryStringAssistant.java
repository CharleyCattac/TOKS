package services;

class BinaryStringAssistant {
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
}
