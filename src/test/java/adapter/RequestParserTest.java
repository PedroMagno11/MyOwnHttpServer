package adapter;

import br.com.pedromagno.adapter.RequestParser;
import br.com.pedromagno.domain.HttpRequest;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.nio.charset.StandardCharsets;

public class RequestParserTest {

    @Test
    @DisplayName("Deve fazer o parser de uma requisição simles do tipo GET")
    void shouldParseSimpleGetRequest() throws Exception {
        String raw = "GET /hello HTTP/1.1\r\nHOST: localhost\r\nUser-Agent: test\r\n\r\n";
        ByteArrayInputStream inputStream = new ByteArrayInputStream(raw.getBytes(StandardCharsets.UTF_8));

        HttpRequest request = RequestParser.parse(inputStream);

        Assertions.assertEquals("GET", request.getMethod());
        Assertions.assertEquals("/hello", request.getPath());
        Assertions.assertEquals("HTTP/1.1", request.getVersion());
        Assertions.assertEquals("localhost", request.getHeaders().get("HOST"));
        Assertions.assertEquals("test", request.getHeaders().get("User-Agent"));
    }

    @Test
    @DisplayName("Deve retornar requisição vazia quando a entrada for vazia")
    void shouldReturnEmptyRequestOnEmptyInput() throws IOException {
        ByteArrayInputStream inputStream = new ByteArrayInputStream("\r\n".getBytes(StandardCharsets.UTF_8));
        HttpRequest request = RequestParser.parse(inputStream);

        Assertions.assertNull(request.getMethod());
    }
}
