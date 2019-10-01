package enums;

public enum ParityEnum {
    PARITY_NO( 0), PARTITY_ODD( 1),
    PARITY_EVEN( 2), PARITY_MARK( 3),
    PARITY_SPACE( 4);

    private final int value;

    private ParityEnum(int value) {
        this.value = value;
    }

    public int getValue() {
        return value;
    }
}
