package enums;

public enum StopBitsEnum {
    STOPBITS_1("1", 1), STOPBITS_1_5("1.5", 3),
    STOPBITS_2("2", 2);

    private final int value;
    private final String name;

    private StopBitsEnum(String name, int value) {
        this.name = name;
        this.value = value;
    }

    public int getValue() {
        return value;
    }

    public String getName() {
        return name;
    }
}
