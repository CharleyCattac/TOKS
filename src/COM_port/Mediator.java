package COM_port;

import UI.DebugControlBlock;
import UI.ElementBlock;

public class Mediator {
    private ElementBlock inputBlock;
    private ElementBlock outputBlock;
    private DebugControlBlock debugControlBlock;
    private String[] fields = {
            "Com ports",
            "Baud rate",
            "Data bits",
            "Parity",
            "Stop bit"
    };

    public Mediator() {
        inputBlock = new ElementBlock("Input   ", 40, 6);
        outputBlock = new ElementBlock("Output", 40, 6);
        debugControlBlock = new DebugControlBlock("Debug & Control", fields);
    }

    public ElementBlock getInputBlock() {
        return inputBlock;
    }

    public ElementBlock getOutputBlock() {
        return outputBlock;
    }

    public DebugControlBlock getDebugControlBlock() {
        return debugControlBlock;
    }
}
