package es.grayapps.methods;

import com.fasterxml.jackson.annotation.JsonProperty;

public class CompletionMessage {

    @JsonProperty("content")
    private final String message;
    private final String role;

    public CompletionMessage(String message, String role) {
        this.message = message;
        this.role = role;
    }

    public String getMessage() {
        return message;
    }

    public String getRole() {
        return role;
    }
}
