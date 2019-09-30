package enums;

public enum MyEnums {
    COM_PORT(0), BAUDRATE(1),
    DATABITS(2), PARTIY(3),
    STOPBITS(4);

    private final int value;

    private MyEnums(int value) {
        this.value = value;
    }

    public int getValue() {
        return value;
    }
}
