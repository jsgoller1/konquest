package input;

public enum KeyCode {
    SPACE(32), UP(38), DOWN(40), LEFT(37), RIGHT(39), ESCAPE(27);

    private final int code;

    private KeyCode(int code) {
        this.code = code;
    }

    public int getCode() {
        return code;
    }
}
