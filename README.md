[![Maven Central](https://img.shields.io/maven-central/v/es.grayapps/easy-web-ui.svg)](https://search.maven.org/artifact/es.grayapps/easy-web-ui)
[![License: GPL v3](https://img.shields.io/badge/License-GPLv3-blue.svg)](https://www.gnu.org/licenses/gpl-3.0.html)

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

EasyWebUI is now available in Maven Central. You can include it in your project by adding the following dependency to
your `pom.xml` file:

```xml

<dependency>
    <groupId>es.grayapps</groupId>
    <artifactId>easy-web-ui</artifactId>
    <version>0.3.0</version>
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
```

## Links

- **Repository**: [GitHub](https://github.com/gray-apps/easy-web-ui)
- **Project Page**: [GrayApps](https://grayapps.es)
- **License**: [GNU GENERAL PUBLIC LICENSE Version 3](https://www.gnu.org/licenses/gpl-3.0.html)

```