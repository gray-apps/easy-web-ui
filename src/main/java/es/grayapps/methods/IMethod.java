package es.grayapps.methods;

import com.fasterxml.jackson.core.JsonProcessingException;
import es.grayapps.exceptions.EasyWebUIException;

/**
 * IMethod is an interface that defines the necessary methods to make a request and process the response.
 *
 * @param <T> the type of the response expected to be deserialized.
 */
public interface IMethod<T> {

    /**
     * Returns the HTTP method type for this request.
     *
     * @return the HTTP method type.
     */
    MethodType getMethod();

    /**
     * Returns the path for this request.
     *
     * @return the path.
     */
    String getPath();

    /**
     * Returns the body of the request as a JSON string.
     *
     * @return the body of the request.
     * @throws JsonProcessingException if an error occurs while processing JSON.
     */
    String getBody() throws JsonProcessingException;

    /**
     * Deserializes the JSON response into an object of type T.
     *
     * @param json the JSON response.
     * @return the deserialized object of type T.
     * @throws EasyWebUIException if an error occurs while parsing JSON.
     */
    T deserialize(String json) throws EasyWebUIException;
}