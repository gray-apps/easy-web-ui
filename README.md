# EasyWebUI

**EasyWebUI** is a library in progress designed to facilitate the connection between Java applications and WebUI. It
provides a simple and efficient interface to interact with web services, allowing developers to focus on business logic
without worrying about the details of HTTP communication.

## Features

- **Easy to use**: Simplifies the configuration and use of HTTP connections.
- **Integration with OkHttp**: Uses OkHttp to efficiently handle HTTP requests.
- **JSON Support**: Integration with Jackson for JSON processing.
- **Extensible**: Designed to be easily extensible with new methods and functionalities.

## Installation

Since EasyWebUI is not yet available in Maven repositories, you can include it manually in your project. Follow these
steps:

1. Clone the EasyWebUI repository:
    ```sh
    git clone https://github.com/GrayAppsOfficial/EasyWebUI.git
    ```

2. Navigate to the project directory:
    ```sh
    cd EasyWebUI
    ```

3. Install the library in your local Maven repository:
    ```sh
    mvn install
    ```

4. Add the dependency to your `pom.xml` file:
    ```xml
    <dependency>
        <groupId>es.grayapps</groupId>
        <artifactId>EasyWebUI</artifactId>
        <version>0.1</version>
    </dependency>
    ```

## Usage

Below is a basic example of how to use EasyWebUI:

```java
import es.grayapps.EasyWebUI;
import es.grayapps.methods.CompletionMethod;
import es.grayapps.methods.response.CompletionResponse;

public class Main {
    public static void main(String[] args) {
        EasyWebUI easyWebUI = EasyWebUI.EasyWebUIBuilder.builder()
                .serverUrl("https://api.example.com")
                .serverToken("your_token_here")
                .build();

        CompletionMethod method = new CompletionMethod();
        CompletionResponse response = easyWebUI.executeCompletion(method);

        System.out.println(response);
    }
}