package input;

/*
 * Up = 38 Down = 40 Left = 37 Right = 39 Space = 32
 */


public enum KeyCode {
    SPACE(32), UP(38), DOWN(40), LEFT(37), RIGHT(39);

    private final int code;

    private KeyCode(int code) {
        this.code = code;
    }

    public int getCode() {
        return code;
    }
}
