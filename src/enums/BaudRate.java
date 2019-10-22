package enums;

public enum BaudRate {

    BAUDRATE_110(110), BAUDRATE_300(300),
    BAUDRATE_600(600), BAUDRATE_1200(1200),
    BAUDRATE_4800(4800), BAUDRATE_9600(9600),
    BAUDRATE_14400(14400), BAUDRATE_19200(19200),
    BAUDRATE_38400(38400), BAUDRATE_57600(57600),
    BAUDRATE_115200(115200);

    private final int value;

    private BaudRate(int value){
        this.value = value;
    }

    public int getValue() {
        return value;
    }
}
