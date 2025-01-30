package es.grayapps.methods;

import es.grayapps.exceptions.EasyWebUIException;
import es.grayapps.exceptions.EasyWebUIExceptionRuntime;
import es.grayapps.methods.response.CompletionResponse;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

class CompletionMethodTest {

    @Test
    void getMethodReturnsPost() {
        CompletionMethod method = new CompletionMethod("model", "message", "role");
        assertEquals(MethodType.POST, method.getMethod());
    }

    @Test
    void getPathReturnsCorrectPath() {
        CompletionMethod method = new CompletionMethod("model", "message", "role");
        assertEquals("/api/chat/completions", method.getPath());
    }

/*    @Test
    void getBodyReturnsValidJson() throws JsonProcessingException {
        CompletionMethod method = new CompletionMethod("model", "message", "role");
        String expectedJson = "{\"messages\":[{\"role\":\"role\",\"content\":\"message\"}],\"model\":\"model\"}";
        //TODO fix map order, sometimes tests fails

        assertEquals(expectedJson, method.getBody());
    }*/

    @Test
    void deserializeReturnsCompletionResponse() throws EasyWebUIException {
        String jsonResponse = "{\"id\":\"1\",\"choices\":[{\"index\":0,\"message\":{\"content\":\"response content\",\"role\":\"response role\"}}]}";
        CompletionMethod method = new CompletionMethod("model", "message", "role");
        CompletionResponse response = method.deserialize(jsonResponse);
        assertEquals("response content", response.getContent());
        assertEquals("response role", response.getRole());
    }

    @Test
    void deserializeThrowsExceptionForInvalidJson() {
        String invalidJson = "invalid json";
        CompletionMethod method = new CompletionMethod("model", "message", "role");
        assertThrows(EasyWebUIException.class, () -> method.deserialize(invalidJson));
    }

    @Test
    void deserializeHandlesEmptyChoices() {
        String jsonResponse = "{\"id\":\"1\",\"choices\":[]}";
        CompletionMethod method = new CompletionMethod("model", "message", "role");
        assertThrows(EasyWebUIExceptionRuntime.class, () -> method.deserialize(jsonResponse));
    }
}