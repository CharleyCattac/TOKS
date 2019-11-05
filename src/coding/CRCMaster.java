package coding;

import services.BinaryStringAssistant;

import java.awt.*;
import static services.BinaryStringAssistant.fromBinaryStringToByte;
import java.util.Random;

public class CRCMaster {
    private static final int POLYNOMIAL_POWER = 7;
    private static final String extraNullBytes = "0000000";
    private static final String POLYNOMIAL = "10000011"; //131

    private static boolean errorDiscovered = false;
    private static String CRCstr;

    public static String encode(String message, boolean enableErrorEmulation) {
        String CRC = divideOnPolynomial(message + extraNullBytes);
        System.out.println("Message + extraNullBytes = " + message + " " + extraNullBytes);
        System.out.println("CRC = 0" + CRC);
        if (enableErrorEmulation) {
            Random random = new Random();
            int index = Byte.SIZE * 3 + random.nextInt(Byte.SIZE * POLYNOMIAL_POWER - 2);
            message = makeSingleError(message, index);
        }
        CRCstr = CRC;
        return message + CRC;
    }

    public static String decode(String encodedMessage) {
        if (calculateWeight(divideOnPolynomial(encodedMessage)) > 0) {
            System.out.println("Weight " + calculateWeight(divideOnPolynomial(encodedMessage)));
            errorDiscovered = true;
            encodedMessage = fixSingleError(encodedMessage);
            assert encodedMessage != null;
            return encodedMessage.substring(0, encodedMessage.length() - POLYNOMIAL_POWER);
        } else {
            errorDiscovered = false;
            return encodedMessage.substring(0, encodedMessage.length() - POLYNOMIAL_POWER);
        }
    }

    public static boolean errorWasDiscovered(){
        return errorDiscovered;
    }

    private static String makeSingleError(String binary, int index) {
        String buf = binary.substring(0, index);
        char a = binary.charAt(index);
        binary = binary.substring(index + 1);
        return a == '0' ? (buf + "1" + binary) : (buf + "0" + binary);
    }

    public static String getHexCRC(){
        StringBuilder hexCRC = new StringBuilder();
        hexCRC.append("_");
        byte CRC = BinaryStringAssistant.fromBinaryStringToByte(CRCstr.length() == Byte.SIZE ? CRCstr : "0" + CRCstr);
        if (Integer.toHexString(Byte.toUnsignedInt(CRC)).length() == 1) {
            hexCRC.append("0");
        }
        hexCRC.append(Integer.toHexString(Byte.toUnsignedInt(CRC)));
        return hexCRC.toString();

    }

    private static String XORWithPolynomial(String a) {
        if (a.length() != Byte.SIZE)
            throw new IllegalArgumentException("String's length must be equal to byte size");
        StringBuilder result = new StringBuilder("");
        for (int i = 0; i < 8; i++) {
            if (a.charAt(i) == POLYNOMIAL.charAt(i)) {
                result.append("0");
            } else {
                result.append("1");
            }
        }
        return result.toString();
    }

    private static String divideOnPolynomial(String binaryString) {
        binaryString = removeNullBytes(binaryString);
        if (binaryString.length() == POLYNOMIAL_POWER) {
            return binaryString;
        }

        String result;
        while (true) {
            String buf = binaryString.substring(0, Byte.SIZE);
            binaryString = binaryString.substring(Byte.SIZE);
            binaryString = XORWithPolynomial(buf) + binaryString;
            binaryString = removeNullBytes(binaryString);
            if (binaryString.length() == POLYNOMIAL_POWER) {
                result = binaryString;
                break;
            } else if (binaryString.length() == Byte.SIZE) {
                result = XORWithPolynomial(binaryString).substring(1);
                break;
            }
        }
        return result;
    }

    private static String removeNullBytes(String binary) {
        if (binary.length() < Byte.SIZE)
            throw new IllegalArgumentException("String must contain at least 7 chars");
        char a = binary.charAt(0);
        while (a == '0') {
            binary = binary.substring(1);
            if (binary.length() == 7) {
                break;
            }
            a = binary.charAt(0);
        }
        return binary;
    }

    private static String fixSingleError(String binary) {
        for (int i = 0; i < binary.length(); i++) {
            binary = invertChar(binary, i);
            if(divideOnPolynomial(binary).equals(extraNullBytes)) {
                return binary;
            }
            else {
                binary = invertChar(binary, i);
            }
        }
        return null;
    }

    private static String invertChar(String binaryString, int index) {
        String buf = binaryString.substring(0, index);
        if (binaryString.charAt(index) == '0') {
            binaryString = binaryString.substring(index + 1);
            binaryString = buf + "1" + binaryString;
        } else {
            binaryString = binaryString.substring(index + 1);
            binaryString = buf + "0" + binaryString;
        }
        return binaryString;
    }

    private static int calculateWeight(String bin) {
        int res = 0;
        for (int i = 0; i < bin.length(); i++) {
            if (bin.charAt(i) == '1') {
                res++;
            }
        }
        return res;
    }
}
