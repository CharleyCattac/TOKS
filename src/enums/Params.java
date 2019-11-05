package enums;

public enum Params {
    COM_PORT(0), BAUDRATE(1),
    DATABITS(2), STOPBITS(3),
    PARITY(4),
    SOURCE(5), DESTINATION(6),
    PACKAGE_ERROR(7);

    private final int value;

    private Params(int value) {
        this.value = value;
    }

    public int getValue() {
        return value;
    }
}
