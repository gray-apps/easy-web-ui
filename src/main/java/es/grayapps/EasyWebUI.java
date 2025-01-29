package es.grayapps;

import com.fasterxml.jackson.core.JsonProcessingException;
import es.grayapps.methods.CompletionMethod;
import es.grayapps.methods.IMethod;
import es.grayapps.methods.response.CompletionResponse;
import es.grayapps.utils.HttpResponse;
import okhttp3.*;

import java.io.Serializable;
import java.util.Objects;
import java.util.concurrent.ExecutionException;

public class EasyWebUI {

    private String serverUrl;
    private String serverToken;
    private final OkHttpClient client = new OkHttpClient();

    public EasyWebUI() {
    }

    public EasyWebUI(String serverUrl, String serverToken) {
        this.serverUrl = Objects.requireNonNull(serverUrl);
        this.serverToken = Objects.requireNonNull(serverToken);
    }

    public CompletionResponse executeCompletion(CompletionMethod completionMethod) {
         return execute(completionMethod);
    }

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
        }catch (JsonProcessingException e){
            e.printStackTrace();
            throw new RuntimeException(e);
        } catch (ExecutionException e) {
            throw new RuntimeException(e);
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }
    }

    public static class EasyWebUIBuilder {
        private final EasyWebUI easyWebUI = new EasyWebUI();

        public static EasyWebUIBuilder builder() {

            return new EasyWebUIBuilder();
        }

        public EasyWebUIBuilder serverUrl(String serverUrl) {
            easyWebUI.serverUrl = Objects.requireNonNull(serverUrl);
            return this;
        }

        public EasyWebUIBuilder serverToken(String serverToken) {
            easyWebUI.serverToken = Objects.requireNonNull(serverToken);
            return this;
        }

        public EasyWebUI build() {
            Objects.requireNonNull(easyWebUI.serverUrl);
            Objects.requireNonNull(easyWebUI.serverToken);
            return easyWebUI;
        }
    }
}
