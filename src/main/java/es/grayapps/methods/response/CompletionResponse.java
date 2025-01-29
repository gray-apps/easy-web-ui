package es.grayapps.methods.response;

import java.io.Serializable;

public class CompletionResponse implements Serializable {

    private final String content;
    private final String role;

    public CompletionResponse(String content, String role) {
        this.content = content;
        this.role = role;
    }

    public String getContent() {
        return content;
    }

    public String getRole() {
        return role;
    }
}
