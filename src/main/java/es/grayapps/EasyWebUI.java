package es.grayapps;

import com.fasterxml.jackson.core.JsonProcessingException;
import es.grayapps.exceptions.EasyWebUIExceptionRuntime;
import es.grayapps.methods.CompletionMethod;
import es.grayapps.methods.IMethod;
import es.grayapps.methods.response.CompletionResponse;
import es.grayapps.utils.HttpResponse;
import okhttp3.MediaType;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;

import java.io.Serializable;
import java.util.Objects;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.TimeUnit;

/**
 * EasyWebUI is a class that allows
 * to interact with a server using the HTTP protocol.
 *
 * @author javiergg
 */
public class EasyWebUI {
    public String getServerUrl() {
        return serverUrl;
    }

    public String getServerToken() {
        return serverToken;
    }

    private String serverUrl;
    private String serverToken;
    private final OkHttpClient client = new OkHttpClient.Builder()
            .connectTimeout(10, TimeUnit.MINUTES)
            .readTimeout(10, TimeUnit.MINUTES)
            .build();

    /**
     * Creates a new instance of EasyWebUI.
     */
    public EasyWebUI() {
    }

    /**
     * Creates a new instance of EasyWebUI with the provided server URL and server token.
     *
     * @param serverUrl   the server URL.
     * @param serverToken the server token.
     * @throws NullPointerException if serverUrl or serverToken are null.
     */
    public EasyWebUI(String serverUrl, String serverToken) {
        this.serverUrl = Objects.requireNonNull(serverUrl);
        this.serverToken = Objects.requireNonNull(serverToken);
    }

    /**
     * Executes the provided completion method.<br>
     * A completion is ask for a completion for a message,
     *  so server will answer that you say on input.
     *
     * @param completionMethod the completion method to execute.
     * @return the response of the completion method.
     */
    public CompletionResponse executeCompletion(CompletionMethod completionMethod) {
         return execute(completionMethod);
    }

    /**
     * Executes the provided method.
     *
     * @param method the method to execute.
     * @param <T> the type of the response.
     * @return the response of the method.
     */
    private <T extends Serializable> T execute(IMethod<T> method)  {
        try {
            HttpResponse<T, IMethod<T>> callback = new HttpResponse<>(method);
            RequestBody body = RequestBody.create(method.getBody(), MediaType.get("application/json"));
            Request request = new Request.Builder()
                    .url(serverUrl+method.getPath())
                    .method(method.getMethod().name(), body)
                    .addHeader("Authorization", "Bearer " + serverToken)
                    .build();
            client.newCall(request).enqueue(callback);
            return callback.get();
        } catch (JsonProcessingException | ExecutionException | InterruptedException e) {
            throw new EasyWebUIExceptionRuntime(e);
        }
    }

    /**
     * Builder for the EasyWebUI class.
     */
    public static class EasyWebUIBuilder {
        private final EasyWebUI easyWebUI = new EasyWebUI();

        /**
         * Creates a new instance of EasyWebUIBuilder.
         *
         * @return a new instance of EasyWebUIBuilder.
         */
        public static EasyWebUIBuilder builder() {
            return new EasyWebUIBuilder();
        }

        /**
         * Sets the server URL.
         *
         * @param serverUrl the server URL.
         * @return the current instance of EasyWebUIBuilder.
         * @throws NullPointerException if serverUrl is null.
         */
        public EasyWebUIBuilder serverUrl(String serverUrl) {
            easyWebUI.serverUrl = Objects.requireNonNull(serverUrl);
            return this;
        }

        /**
         * Sets the server token.
         *
         * @param serverToken the server token.
         * @return the current instance of EasyWebUIBuilder.
         * @throws NullPointerException if serverToken is null.
         */
        public EasyWebUIBuilder serverToken(String serverToken) {
            easyWebUI.serverToken = Objects.requireNonNull(serverToken);
            return this;
        }

        /**
         * Builds an instance of EasyWebUI with the provided parameters.
         *
         * @return a new instance of EasyWebUI.
         * @throws NullPointerException if serverUrl or serverToken are null.
         */
        public EasyWebUI build() {
            Objects.requireNonNull(easyWebUI.serverUrl);
            Objects.requireNonNull(easyWebUI.serverToken);
            return easyWebUI;
        }
    }
}
