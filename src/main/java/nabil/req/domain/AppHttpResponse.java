package nabil.req.domain;

import java.util.Map;

/**
 * @author Ahmed Nabil
 */
public record AppHttpResponse(String statusCode, Map<String, String> headers, String body) {
    @Override
    public String toString() {
        return " statusCode=" + statusCode +
                "\n headers=" + headers +
                "\n body=" + body;
    }
}
