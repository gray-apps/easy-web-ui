package es.grayapps.methods.response;

import java.io.Serializable;

/**
 * CompletionResponse is a class that represents the response of a completion method.
 */
public class CompletionResponse implements Serializable {

    private final String content;
    private final String role;

    /**
     * Creates a new instance of CompletionResponse with the provided content and role.
     *
     * @param content the content of the response.
     * @param role    the role of the response.
     */
    public CompletionResponse(String content, String role) {
        this.content = content;
        this.role = role;
    }

    /**
     * Returns the content of the response.
     *
     * @return the content of the response.
     */
    public String getContent() {
        return content;
    }

    /**
     * Returns the role of the response.
     *
     * @return the role of the response.
     */
    public String getRole() {
        return role;
    }
}
