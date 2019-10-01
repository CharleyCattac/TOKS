package enums;

public enum StopBitsEnum {
    STOPBITS_1(1), STOPBITS_1_5(3),
    STOPBITS_2(2);

    private final int value;

    private StopBitsEnum(int value) {
        this.value = value;
    }

    public int getValue() {
        return value;
    }
}
