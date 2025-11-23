package input;

public enum KeyCode {
    BACKSPACE(8), ESCAPE(27), SPACE(32), LEFT(37), UP(38), RIGHT(39), DOWN(40);

    private final int code;

    private KeyCode(int code) {
        this.code = code;
    }

    public int getCode() {
        return code;
    }
}
