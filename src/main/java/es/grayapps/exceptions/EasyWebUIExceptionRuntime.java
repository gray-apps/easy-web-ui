package es.grayapps.exceptions;

/**
 * EasyWebUIExceptionRuntime is a runtime exception that is thrown when an error occurs in the EasyWebUI class.
 */
public class EasyWebUIExceptionRuntime extends RuntimeException {

    public EasyWebUIExceptionRuntime(String message) {
        super(message);
    }

    public EasyWebUIExceptionRuntime(String message, Throwable cause) {
        super(message, cause);
    }

    public EasyWebUIExceptionRuntime(Exception e) {
        super(e);
    }
}
