package es.grayapps.utils;

import es.grayapps.methods.IMethod;
import es.grayapps.exceptions.EasyWebUIException;
import es.grayapps.exceptions.EasyWebUIExceptionRuntime;
import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.Response;
import okhttp3.ResponseBody;
import org.jetbrains.annotations.NotNull;

import java.io.IOException;
import java.io.Serializable;
import java.util.concurrent.CompletableFuture;

public class HttpResponse<T extends Serializable, Method extends IMethod<T>> extends CompletableFuture<T> implements Callback {

    private final Method method;

    public HttpResponse(Method method) {
        this.method = method;
    }

    @Override
    public void onFailure(@NotNull Call call, @NotNull IOException e) {
        completeExceptionally(e);
    }

    @Override
    public void onResponse(@NotNull Call call, @NotNull Response response) throws IOException {
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