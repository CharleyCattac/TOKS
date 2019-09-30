package enums;

public enum ParityEnum {
    PARITY_NO("NONE", 0), PARTITY_ODD("0DD", 1),
    PARITY_EVEN("EVEN", 2), PARITY_MARK("MARK", 3),
    PARITY_SPACE("SPACE", 4);

    private final int value;
    private final String name;

    private ParityEnum(String name, int value) {
        this.name = name;
        this.value = value;
    }

    private int getValue() {
        return value;
    }

    public String getName() {
        return name;
    }
}
