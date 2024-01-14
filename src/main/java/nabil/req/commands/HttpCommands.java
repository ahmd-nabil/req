package nabil.req.commands;

import nabil.req.domain.AppHttpResponse;
import org.springframework.core.io.buffer.DataBuffer;
import org.springframework.core.io.buffer.DataBufferUtils;
import org.springframework.http.HttpMethod;
import org.springframework.http.MediaType;
import org.springframework.shell.standard.ShellComponent;
import org.springframework.shell.standard.ShellMethod;
import org.springframework.shell.standard.ShellOption;
import org.springframework.web.reactive.function.client.ClientResponse;
import org.springframework.web.reactive.function.client.WebClient;

import java.net.URI;
import java.nio.charset.StandardCharsets;

/**
 * @author Ahmed Nabil
 */
@ShellComponent
public class HttpCommands {

    private final WebClient webClient;
    public HttpCommands(WebClient webClient) {
        this.webClient = webClient;
    }

    @ShellMethod(key = "req", prefix = "-", value = "sends an http request, eg. req -X [http-verb] -L [URI]")
    public AppHttpResponse request(
            @ShellOption(help = "http verb {GET,POST,PUT,DELETE}", value = "X", defaultValue = "GET") String verb,
            @ShellOption(help = "uri of the request", value = "L") URI uri) {
        ClientResponse clientResponse = this.webClient.method(HttpMethod.valueOf(verb))
                .uri(uri)
                .accept(MediaType.ALL)
                .exchange()
                .block();
        return extractAppHttpResponse(clientResponse);
    }

    @ShellMethod(key = "get", prefix = "-", value = "eg. get http://localhost:8080")
    public AppHttpResponse get(
            @ShellOption(help = "uri of the request", value = "L") URI uri) {
       return request("GET", uri);
    }

    @ShellMethod(key = "post", prefix = "-", value = "eg. post http://localhost:8080/users -B '{\"name\":\"ahmed\", \"age\":24}'")
    public AppHttpResponse post(
            @ShellOption(help = "uri of the request", value = "L") URI uri,
            @ShellOption(help = "body of the post request", value = "B") String body
    ) {
        ClientResponse response = this.webClient.method(HttpMethod.valueOf("POST"))
                .uri(uri)
                .bodyValue(body)
                .header("Content-Type", MediaType.APPLICATION_JSON_VALUE)
                .exchange()
                .block();
        return extractAppHttpResponse(response);
    }

    @ShellMethod(key = "put", prefix = "-", value = "eg. put http://localhost:8080/users/1 -B '{\"name\":\"Levi\", \"age\":24}'")
    public AppHttpResponse put(
            @ShellOption(help = "uri of the request", value = "L") URI uri,
            @ShellOption(help = "body of the post request", value = "B") String body
    ) {
        ClientResponse response = this.webClient.method(HttpMethod.valueOf("PUT"))
                .uri(uri)
                .bodyValue(body)
                .header("Content-Type", MediaType.APPLICATION_JSON_VALUE)
                .exchange()
                .block();
        return extractAppHttpResponse(response);
    }

    @ShellMethod(key = "del", prefix = "-", value = "eg. del http://localhost:8080/users/1")
    public AppHttpResponse delete(
            @ShellOption(help = "uri of the request", value = "L") URI uri) {
        return request("DELETE", uri);
    }

    private AppHttpResponse extractAppHttpResponse(ClientResponse response) {
        assert response != null;
        return new AppHttpResponse(
                response.statusCode().toString(),
                response.headers().asHttpHeaders().toSingleValueMap(),
                response.bodyToMono(DataBuffer.class).map(dataBuffer -> {
                    byte[] bytes = new byte[dataBuffer.readableByteCount()];
                    dataBuffer.read(bytes);
                    DataBufferUtils.release(dataBuffer);
                    return new String(bytes, StandardCharsets.UTF_8);
                }).block()
        );
    }
}
