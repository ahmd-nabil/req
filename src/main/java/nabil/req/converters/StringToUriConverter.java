package nabil.req.converters;

import org.springframework.core.convert.converter.Converter;
import org.springframework.stereotype.Component;

import java.net.URI;
import java.net.URISyntaxException;

/**
 * @author Ahmed Nabil
 */
@Component
public class StringToUriConverter implements Converter<String, URI> {

    @Override
    public URI convert(String source) {
        source = parseUri(source);
        try {
            URI uri = new URI(source);
            if(uri.getScheme() == null) {
                throw new URISyntaxException(source, "URI Scheme is not specified");
            }
            if(uri.getHost() == null) {
                throw new URISyntaxException(source, "URI Host is not specified");
            }
            return uri;
        } catch (URISyntaxException e) {
            throw new RuntimeException(e.getMessage());
        }
    }

    private String parseUri(String source) {
        StringBuilder sb = new StringBuilder(source.trim());
        int first = sb.indexOf(":");
        int last = sb.lastIndexOf(":");

        if(first == last) {
            sb.insert(0, "http://");
        }

        if(first == last && first == 0) {
            sb.insert(sb.lastIndexOf(":"), "localhost");
        }
        return sb.toString();
    }
}
