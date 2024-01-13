package nabil.req.commands;

import nabil.req.domain.AppHttpResponse;
import org.springframework.core.io.buffer.DataBuffer;
import org.springframework.core.io.buffer.DataBufferUtils;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.MediaType;
import org.springframework.shell.standard.ShellComponent;
import org.springframework.shell.standard.ShellMethod;
import org.springframework.shell.standard.ShellOption;
import org.springframework.util.MultiValueMap;
import org.springframework.web.reactive.function.client.ClientResponse;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Mono;

import java.net.URI;
import java.nio.charset.StandardCharsets;
import java.util.Map;

/**
 * @author Ahmed Nabil
 */
@ShellComponent
public class HttpCommands {

    private final WebClient webClient;
    public HttpCommands(WebClient webClient) {
        this.webClient = webClient;
    }

    @ShellMethod(key = "req", prefix = "-", value = "sends an http request")
    public AppHttpResponse request(
            @ShellOption(help = "uri of the request", value = "L") URI uri,
            @ShellOption(help = "http verb {GET,POST,PUT,DELETE}", value = "X", defaultValue = "GET") String verb) {
        ClientResponse clientResponse = this.webClient.method(HttpMethod.valueOf(verb))
                .uri(uri)
                .accept(MediaType.ALL)
                .exchange()
                .block();
        return extractAppHttpResponse(clientResponse);
    }

    @ShellMethod(key = "get", prefix = "-")
    public AppHttpResponse get(
            @ShellOption(help = "uri of the request", value = "L") URI uri) {
       return request(uri, "GET");
    }

    @ShellMethod(key = "post", prefix = "-")
    public AppHttpResponse post(
            @ShellOption(help = "uri of the request", value = "L") String uri,
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

    @ShellMethod(key = "put", prefix = "-")
    public AppHttpResponse put(
            @ShellOption(help = "uri of the request", value = "L") String uri,
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

    @ShellMethod(key = "del", prefix = "-")
    public AppHttpResponse delete(
            @ShellOption(help = "uri of the request", value = "L") URI uri) {
        return request(uri, "DELETE");
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
