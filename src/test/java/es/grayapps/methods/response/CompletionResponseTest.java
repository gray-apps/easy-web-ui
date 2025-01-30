package es.grayapps.methods.response;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNull;

class CompletionResponseTest {

    @Test
    void getContentReturnsCorrectContent() {
        CompletionResponse response = new CompletionResponse("test content", "test role");
        assertEquals("test content", response.getContent());
    }

    @Test
    void getRoleReturnsCorrectRole() {
        CompletionResponse response = new CompletionResponse("test content", "test role");
        assertEquals("test role", response.getRole());
    }

    @Test
    void constructorHandlesNullContent() {
        CompletionResponse response = new CompletionResponse(null, "test role");
        assertNull(response.getContent());
    }

    @Test
    void constructorHandlesNullRole() {
        CompletionResponse response = new CompletionResponse("test content", null);
        assertNull(response.getRole());
    }

    @Test
    void constructorHandlesEmptyContent() {
        CompletionResponse response = new CompletionResponse("", "test role");
        assertEquals("", response.getContent());
    }

    @Test
    void constructorHandlesEmptyRole() {
        CompletionResponse response = new CompletionResponse("test content", "");
        assertEquals("", response.getRole());
    }
}