package es.grayapps;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

class EasyWebUITest {

    @Test
    void testEasyWebUIBuilder() {
        EasyWebUI builtEasyWebUI = EasyWebUI.EasyWebUIBuilder.builder()
                .serverUrl("http://example.com")
                .serverToken("token")
                .build();

        assertNotNull(builtEasyWebUI);
        assertEquals("http://example.com", builtEasyWebUI.getServerUrl());
        assertEquals("token", builtEasyWebUI.getServerToken());
    }
}