package wetd;

public class MyData {

	private final long time;
    private final String message;

    public MyData(long time, String message) {
        this.time = time;
        this.message = message;
    }

    public long getTime() {
        return time;
    }

    public String getMessage() {
        return message;
    }
}
