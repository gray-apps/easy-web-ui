package es.grayapps.exceptions;

public class EasyWebUIException extends Exception {
    public EasyWebUIException(String message) {
        super(message);
    }

    public EasyWebUIException(String message, Throwable cause) {
        super(message, cause);
    }
}
