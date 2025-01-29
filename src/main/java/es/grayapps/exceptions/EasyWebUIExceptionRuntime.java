package es.grayapps.exceptions;

public class EasyWebUIExceptionRuntime extends RuntimeException {
    public EasyWebUIExceptionRuntime(String message) {
        super(message);
    }

    public EasyWebUIExceptionRuntime(String message, Throwable cause) {
        super(message, cause);
    }
}
