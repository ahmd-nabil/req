package nabil.req.commands;

import org.springframework.core.io.buffer.DataBuffer;
import org.springframework.core.io.buffer.DataBufferUtils;
import org.springframework.http.HttpMethod;
import org.springframework.http.MediaType;
import org.springframework.shell.standard.ShellComponent;
import org.springframework.shell.standard.ShellMethod;
import org.springframework.shell.standard.ShellOption;
import org.springframework.web.reactive.function.client.WebClient;

import java.nio.charset.StandardCharsets;
import java.util.stream.Collectors;

/**
 * @author Ahmed Nabil
 */
@ShellComponent
public class HttpCommands {

    private final WebClient webClient;
    public HttpCommands(WebClient webClient) {
        this.webClient = webClient;
    }

    @ShellMethod(key = "req", prefix = "-")
    public String request(
            @ShellOption(value = "p", defaultValue = "https://example.com/") String uri,
            @ShellOption(value = "X", defaultValue = "GET") String verb
    ) {
        return this.webClient.method(HttpMethod.valueOf(verb))
                .uri(uri)
                .accept(MediaType.ALL)
                .exchangeToFlux(response -> response.bodyToFlux(DataBuffer.class))
                .map(dataBuffer -> {
                    byte[] bytes = new byte[dataBuffer.readableByteCount()];
                    dataBuffer.read(bytes);
                    DataBufferUtils.release(dataBuffer);
                    return new String(bytes, StandardCharsets.UTF_8);
                })
                .collect(Collectors.joining())
                .block();
    }

    @ShellMethod(key = "get", prefix = "-")
    public String get(
            @ShellOption(value = "X", defaultValue = "https://example.com/") String uri
    ) {
       return request(uri, "GET");
    }
}
