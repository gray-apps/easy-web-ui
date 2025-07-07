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
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * EasyWebUI is a class that allows
 * to interact with a server using the HTTP protocol.
 *
 * @author javiergg
 */
public class EasyWebUI {

    private static final Logger logger = Logger.getLogger(EasyWebUI.class.getName());

    private final OkHttpClient client = new OkHttpClient.Builder()
            .connectTimeout(10, TimeUnit.MINUTES)
            .readTimeout(10, TimeUnit.MINUTES)
            .writeTimeout(10, TimeUnit.MINUTES)
            .callTimeout(10, TimeUnit.MINUTES)
            .retryOnConnectionFailure(true)
            .addInterceptor(chain -> {
                Request original = chain.request();
                Request request = original.newBuilder()
                        .header("Accept", "application/json")
                        .header("User-Agent", "EasyWebUI/1.0")
                        .build();
                logger.fine("Intercepted request: " + request.url());
                return chain.proceed(request);
            })
            .build();

    private String serverUrl;
    private String serverToken;

    /**
     * Creates a new instance of EasyWebUI.
     */
    public EasyWebUI() {
        logger.info("EasyWebUI instance created with empty constructor.");
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
        logger.info("EasyWebUI instance created with URL: " + serverUrl);
    }

    public String getServerUrl() {
        return serverUrl;
    }

    public String getServerToken() {
        return serverToken;
    }

    /**
     * Executes the provided completion method.<br>
     * A completion is ask for a completion for a message,
     * so server will answer that you say on input.
     *
     * @param completionMethod the completion method to execute.
     * @return the response of the completion method.
     */
    public CompletionResponse executeCompletion(CompletionMethod completionMethod) {
        logger.info("Executing CompletionMethod: " + completionMethod.getClass().getSimpleName());
        return execute(completionMethod);
    }

    /**
     * Executes the provided method.
     *
     * @param method the method to execute.
     * @param <T>    the type of the response.
     * @return the response of the method.
     */
    private <T extends Serializable> T execute(IMethod<T> method) {
        try {
            logger.fine("Preparing HTTP request for method: " + method.getClass().getSimpleName());
            logger.fine("Request path: " + method.getPath());
            logger.fine("Request method: " + method.getMethod());
            logger.fine("Request body: " + method.getBody());

            HttpResponse<T, IMethod<T>> callback = new HttpResponse<>(method);

            RequestBody body = RequestBody.create(
                    method.getBody(),
                    MediaType.get("application/json")
            );

            Request request = new Request.Builder()
                    .url(serverUrl + method.getPath())
                    .method(method.getMethod().name(), body)
                    .addHeader("Authorization", "Bearer " + serverToken)
                    .build();

            logger.fine("Sending request to: " + request.url());

            client.newCall(request).enqueue(callback);
            T response = callback.get();

            logger.fine("Received response of type: " + (response != null ? response.getClass().getSimpleName() : "null"));

            return response;
        } catch (JsonProcessingException e) {
            logger.log(Level.SEVERE, "JSON processing error: " + e.getMessage(), e);
            throw new EasyWebUIExceptionRuntime(e);
        } catch (ExecutionException e) {
            logger.log(Level.SEVERE, "Execution error during HTTP call: " + e.getMessage(), e);
            throw new EasyWebUIExceptionRuntime(e);
        } catch (InterruptedException e) {
            logger.log(Level.SEVERE, "Request interrupted: " + e.getMessage(), e);
            Thread.currentThread().interrupt();
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
