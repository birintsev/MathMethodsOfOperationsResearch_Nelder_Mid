package ua.edu.sumdu;

public class NelderMidException extends ArithmeticException {
    private Throwable cause;

    public NelderMidException(Throwable cause) {
        this.cause = cause;
    }

    public NelderMidException(String s, Throwable cause) {
        super(s);
        this.cause = cause;
    }

    public NelderMidException(String message) {
        super(message);
    }

    @Override
    public Throwable getCause() {
        return cause;
    }

    public void setCause(Throwable cause) {
        this.cause = cause;
    }
}
