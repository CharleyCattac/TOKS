package enums;

public enum StopBitsEnum implements MyEnums {
    STOPBITS_1("1", 1), STOPBITS_1_5("1.5", 2),
    STOPBITS_2("2", 3);

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

    public MyEnums[] getValues() {
        return StopBitsEnum.values();
    }
}
