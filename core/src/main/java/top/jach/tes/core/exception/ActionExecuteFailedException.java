package top.jach.tes.core.exception;

public class ActionExecuteFailedException extends Exception {
    public ActionExecuteFailedException() {
    }

    public ActionExecuteFailedException(String message) {
        super(message);
    }

    public ActionExecuteFailedException(String message, Throwable cause) {
        super(message, cause);
    }

    public ActionExecuteFailedException(Throwable cause) {
        super(cause);
    }

    public ActionExecuteFailedException(String message, Throwable cause, boolean enableSuppression, boolean writableStackTrace) {
        super(message, cause, enableSuppression, writableStackTrace);
    }
}