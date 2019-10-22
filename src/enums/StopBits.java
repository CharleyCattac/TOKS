package enums;

public enum StopBits {
    STOPBITS_1(1), STOPBITS_1_5(3),
    STOPBITS_2(2);

    private final int value;

    private StopBits(int value) {
        this.value = value;
    }

    public int getValue() {
        return value;
    }
}
