package coding;

public class CRCMaster {
    public static final int polynomialPower = 3;
    private static final String extraNullBytes = "000";
    private static final String polynomial = "1011";

    public static String encode(String message) {
        return message + getMod(message + extraNullBytes, polynomial);
    }

    public static String decode(String encodedMessage) {
        String messageWithoutCRCField = encodedMessage.substring(0, encodedMessage.length() - polynomialPower);
        return amountOfSingles(getMod(encodedMessage, polynomial)) == 0 ?
                messageWithoutCRCField : recoverMessage(messageWithoutCRCField);
    }

    private static String recoverMessage(String message) {
        String mod = getMod(message, polynomial);
        if (amountOfSingles(mod) > 1) {
            return shiftRight(recoverMessage(shiftLeft(message)));
        }
        else {
            return extendedXOR(mod, message);
        }
    }

    private static int amountOfSingles(String raw) {
        int count = 0;
        for (int i = 0; i < raw.length(); i++) {
            if (raw.charAt(i) == '1') {
                count++;
            }
        }
        return count;
    }

    private static String getMod(String dividend, String divider) {
        StringBuilder sb = new StringBuilder(dividend);
        for (int i = 0; i <= dividend.length() - divider.length(); i++) {
            String s = divide(sb.substring(i, i + divider.length()), divider);
            if (s != null) {
                sb.delete(i, i + divider.length());
                sb.insert(i, s);
            }
        }
        return sb.substring(sb.length() - polynomialPower);
    }

    private static String divide(String s1, String s2) {
        if (s1.length() == s2.length()) {
            if (s1.charAt(0) == '1') {
                return XOR(s1, s2);
            }
        }
        return null;
    }

    private static String XOR(String s1, String s2) {
        StringBuilder xorString = new StringBuilder();
        for (int i = 0; i < s1.length(); i++)
            xorString.append(s1.charAt(i) ^ s2.charAt(i));
        return xorString.toString();
    }

    private static String extendedXOR(String s1, String s2) {
        StringBuilder sb1 = new StringBuilder(s1);
        StringBuilder sb2 = new StringBuilder(s2);
        int lengthDifference;
        if (sb1.length() > sb2.length()) {
            lengthDifference = sb1.length() - sb2.length();
            for (int i = 0; i < lengthDifference; i++) {
                sb2.insert(0,'0');
            }
        }
        else {
            lengthDifference = sb2.length() - sb1.length();
            for (int i = 0; i < lengthDifference; i++) {
                sb1.insert(0,'0');
            }
        }
        return XOR(sb1.toString(), sb2.toString());
    }

    private static String shiftLeft(String raw) {
        StringBuilder res = new StringBuilder(raw);
        char temp = res.charAt(0);
        res.deleteCharAt(0);
        res.append(temp);
        return res.toString();
    }

    private static String shiftRight(String raw) {
        StringBuilder res = new StringBuilder(raw);
        char temp = res.charAt(res.length() - 1);
        res.deleteCharAt(res.length() - 1);
        res.insert(0,temp);
        return res.toString();
    }

}
