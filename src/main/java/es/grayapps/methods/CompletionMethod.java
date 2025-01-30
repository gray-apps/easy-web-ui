package es.grayapps.methods;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import es.grayapps.exceptions.EasyWebUIException;
import es.grayapps.exceptions.EasyWebUIExceptionRuntime;
import es.grayapps.methods.response.CompletionResponse;

import java.io.Serializable;
import java.util.Comparator;
import java.util.List;
import java.util.Map;
import java.util.Objects;

/**
 * CompletionMethod is a class that represents a method to request a completion from the server.
 * It implements the IMethod interface and is serializable.
 *
 * @author javiergg
 */
public class CompletionMethod implements IMethod<CompletionResponse>, Serializable {

    private final String model;
    private final String message;
    private final String role;
    private final ObjectMapper mapper = new ObjectMapper();

    /**
     * Creates a new CompletionMethod with the given model, message, and role.
     *
     * @param model the model to use for the completion.
     * @param message the message to use for the completion.
     * @param role the role to use for the completion.
     */
    public CompletionMethod(String model, String message, String role) {
        this.model = Objects.requireNonNull(model);
        this.message = Objects.requireNonNull(message);
        this.role = Objects.requireNonNull(role);
    }

    /**
     * Returns the HTTP method type for this request.
     *
     * @return the HTTP method type.
     */
    @Override
    public MethodType getMethod() {
        return MethodType.POST;
    }

    /**
     * Returns the path for this request.
     *
     * @return the path.
     */
    @Override
    public String getPath() {
        return "/api/chat/completions";
    }

    /**
     * Returns the body of the request as a JSON string.
     *
     * @return the body of the request.
     * @throws JsonProcessingException if an error occurs while processing JSON.
     */
    @Override
    public String getBody() throws JsonProcessingException {
        return mapper.writeValueAsString(Map.of("model", model, "messages", List.of(Map.of("content", message, "role", role))));
    }

    /**
     * Deserializes the JSON response into a CompletionResponse object.
     *
     * @param json the JSON response.
     * @return the deserialized CompletionResponse.
     * @throws EasyWebUIException if an error occurs while parsing JSON.
     */
    @Override
    public CompletionResponse deserialize(String json) throws EasyWebUIException {
        try {
            return mapper.readValue(json, CompletionUnparsedResponse.class).getResponse();
        } catch (JsonProcessingException e) {
            throw new EasyWebUIException("Error parsing json", e);
        }
    }

    /**
     * CompletionUnparsedResponse is a class that represents the unparsed response from the server.
     */
    @JsonIgnoreProperties(ignoreUnknown = true)
    private static class CompletionUnparsedResponse {
        private String id;
        private List<CompletionUnparsedChoiceResponse> choices;

        /**
         * Returns the parsed CompletionResponse from the unparsed response.
         *
         * @return the parsed CompletionResponse.
         */
        public CompletionResponse getResponse(){
            this.choices.sort(Comparator.comparing(CompletionUnparsedChoiceResponse::getIndex));
            CompletionUnparsedChoiceResponse choice =  this.choices.stream().findFirst().orElseThrow(()-> new EasyWebUIExceptionRuntime("No choices found"));
            return new CompletionResponse(choice.getMessage().getContent(), choice.getMessage().getRole());
        }

        public String getId() {
            return id;
        }

        public void setId(String id) {
            this.id = id;
        }

        public List<CompletionUnparsedChoiceResponse> getChoices() {
            return choices;
        }

        public void setChoices(List<CompletionUnparsedChoiceResponse> choices) {
            this.choices = choices;
        }
    }

    /**
     * CompletionUnparsedChoiceResponse is a class that represents an unparsed choice in the response.
     */
    @JsonIgnoreProperties(ignoreUnknown = true)
    private static class CompletionUnparsedChoiceResponse {
        private Integer index;
        private CompletionUnparsedMessageResponse message;

        public Integer getIndex() {
            return index;
        }

        public void setIndex(Integer index) {
            this.index = index;
        }

        public CompletionUnparsedMessageResponse getMessage() {
            return message;
        }

        public void setMessage(CompletionUnparsedMessageResponse message) {
            this.message = message;
        }
    }

    /**
     * CompletionUnparsedMessageResponse is a class that represents an unparsed message in the response.
     */
    @JsonIgnoreProperties(ignoreUnknown = true)
    private static class CompletionUnparsedMessageResponse {
        private String content;
        private String role;

        public String getContent() {
            return content.replaceAll("<think>.*?</think>\\s*", "");
        }

        public void setContent(String content) {
            this.content = content;
        }

        public String getRole() {
            return role;
        }

        public void setRole(String role) {
            this.role = role;
        }
    }
}