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


public class CompletionMethod implements IMethod<CompletionResponse>, Serializable {

    private String model;
    private String message;
    private String role;
    private final ObjectMapper mapper = new ObjectMapper();

    public CompletionMethod(String model, String message, String role) {
        this.model = model;
        this.message = message;
        this.role = role;
    }

    @Override
    public MethodType getMethod() {
        return MethodType.POST;
    }

    @Override
    public String getPath() {
        return "/api/chat/completions";
    }

    @Override
    public String getBody() throws JsonProcessingException {
        return mapper.writeValueAsString(Map.of("model", model, "messages", List.of(Map.of("content", message, "role", role))));
    }

    @Override
    public CompletionResponse deserialize(String json) throws EasyWebUIException {
        try {
             return mapper.readValue(json, CompletionUnparsedResponse.class).getResponse();
        } catch (JsonProcessingException e) {
            throw new EasyWebUIException("Error parsing json", e);
        }
    }

    @JsonIgnoreProperties(ignoreUnknown = true)
    private static class CompletionUnparsedResponse {
        private String id;
        private List<CompletionUnparsedChoiceResponse> choices;

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
