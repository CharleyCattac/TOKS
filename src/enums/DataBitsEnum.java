package enums;

public enum DataBitsEnum implements MyEnums {
    DATABITS_5(5), DATABITS_6(6),
    DATABITS_7(7), DATABITS_8(8);

    private final int value;

    private DataBitsEnum(int value) {
        this.value = value;
    }

    public int getValue() {
        return value;
    }

    public MyEnums[] getValues() {
        return DataBitsEnum.values();
    }
}
