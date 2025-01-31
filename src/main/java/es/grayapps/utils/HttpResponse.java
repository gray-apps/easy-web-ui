package es.grayapps.utils;

import es.grayapps.exceptions.EasyWebUIException;
import es.grayapps.exceptions.EasyWebUIExceptionRuntime;
import es.grayapps.methods.IMethod;
import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.Response;
import okhttp3.ResponseBody;
import org.jetbrains.annotations.NotNull;

import java.io.IOException;
import java.io.Serializable;
import java.util.concurrent.CompletableFuture;

/**
 * HttpResponse is a class that handles the HTTP response and deserializes it into an object of type T.
 *
 * @param <T>      the type of the response expected to be deserialized.
 * @param <Method> the type of the method that makes the request.
 */
public class HttpResponse<T extends Serializable, Method extends IMethod<T>> extends CompletableFuture<T> implements Callback {

    private final Method method;

    /**
     * Creates a new instance of HttpResponse with the provided method.
     *
     * @param method the method that makes the request.
     */
    public HttpResponse(Method method) {
        this.method = method;
    }

    /**
     * Called when the request fails.
     *
     * @param call the call that was made.
     * @param e    the exception that was thrown.
     */
    @Override
    public void onFailure(@NotNull Call call, @NotNull IOException e) {
        completeExceptionally(e);
    }

    /**
     * Called when the request is successful.
     *
     * @param call the call that was made.
     * @param response the response that was received.
     * @throws IOException if an error occurs while reading the response.
     */
    @Override
    public void onResponse(@NotNull Call call, @NotNull Response response) throws IOException {
        if (response.code() != 200) {
            completeExceptionally(new EasyWebUIException("Api returned error code: " + response.code()));
            return;
        }
        try(ResponseBody body = response.body()) {
            if (body == null) {
                completeExceptionally(new EasyWebUIExceptionRuntime("Api returned empty response."));
            } else {
                try {
                    complete(method.deserialize(body.string()));
                } catch (EasyWebUIException|EasyWebUIExceptionRuntime e) {
                    completeExceptionally(e);
                }
            }
        }
    }

}