package es.grayapps.methods;

import com.fasterxml.jackson.core.JsonProcessingException;
import es.grayapps.exceptions.EasyWebUIException;
import okhttp3.internal.http.HttpMethod;

public interface IMethod<T> {

    MethodType getMethod() ;

    String getPath();

    String getBody() throws JsonProcessingException;

    T deserialize(String json) throws EasyWebUIException;
}
