package es.grayapps.utils;

import es.grayapps.exceptions.EasyWebUIException;
import es.grayapps.methods.IMethod;
import okhttp3.Call;
import okhttp3.Response;
import okhttp3.ResponseBody;
import org.junit.jupiter.api.Test;

import java.io.IOException;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

class HttpResponseTest {

    @Test
    void onFailureCompletesExceptionally() {
        HttpResponse<String, IMethod<String>> httpResponse = new HttpResponse<>(mock(IMethod.class));
        IOException exception = new IOException("Network error");
        httpResponse.onFailure(mock(Call.class), exception);
        assertTrue(httpResponse.isCompletedExceptionally());
    }

    @Test
    void onResponseCompletesSuccessfully() throws IOException, EasyWebUIException {
        IMethod<String> mockMethod = mock(IMethod.class);
        when(mockMethod.deserialize(anyString())).thenReturn("Success");
        HttpResponse<String, IMethod<String>> httpResponse = new HttpResponse<>(mockMethod);

        Response mockResponse = mock(Response.class);
        ResponseBody mockBody = mock(ResponseBody.class);
        when(mockResponse.body()).thenReturn(mockBody);
        when(mockBody.string()).thenReturn("Response body");

        httpResponse.onResponse(mock(Call.class), mockResponse);
        assertTrue(httpResponse.isDone());
        assertEquals("Success", httpResponse.join());
    }

    @Test
    void onResponseHandlesEmptyBody() throws IOException {
        HttpResponse<String, IMethod<String>> httpResponse = new HttpResponse<>(mock(IMethod.class));
        Response mockResponse = mock(Response.class);
        when(mockResponse.body()).thenReturn(null);

        httpResponse.onResponse(mock(Call.class), mockResponse);
        assertTrue(httpResponse.isCompletedExceptionally());
    }

    @Test
    void onResponseHandlesDeserializationException() throws IOException, EasyWebUIException {
        IMethod<String> mockMethod = mock(IMethod.class);
        when(mockMethod.deserialize(anyString())).thenThrow(new EasyWebUIException("Deserialization error"));
        HttpResponse<String, IMethod<String>> httpResponse = new HttpResponse<>(mockMethod);

        Response mockResponse = mock(Response.class);
        ResponseBody mockBody = mock(ResponseBody.class);
        when(mockResponse.body()).thenReturn(mockBody);
        when(mockBody.string()).thenReturn("Response body");

        httpResponse.onResponse(mock(Call.class), mockResponse);
        assertTrue(httpResponse.isCompletedExceptionally());
    }
}