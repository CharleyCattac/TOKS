package enums;

import jssc.*;


public enum BaudRateEnum implements MyEnums {

    BAUDRATE_100(100), BAUDRATE150(150);

    private final int value;

    private BaudRateEnum(int value){
        this.value = value;
    }

    public int getValue() {
        return value;
    }

    public MyEnums[] getValues() {
        return BaudRateEnum.values();
    }
}
