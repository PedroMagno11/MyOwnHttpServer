package domain;

import br.com.pedromagno.domain.HttpRequest;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

public class HttpRequestTest {
    @Test
    @DisplayName("Deve definer e obter campos")
    void shouldSetAndGetFields() {
        HttpRequest request = new HttpRequest();
        request.setMethod("POST");
        request.setPath("/api");
        request.setVersion("HTTP/1.1");
        request.getHeaders().put("Content-Type", "application/json");
        request.setBody("{\"key\":\"value\"}".getBytes());

        Assertions.assertEquals("POST", request.getMethod());
        Assertions.assertEquals("/api", request.getPath());
        Assertions.assertEquals("HTTP/1.1", request.getVersion());
        Assertions.assertEquals("application/json", request.getHeaders().get("Content-Type"));
        Assertions.assertArrayEquals("{\"key\":\"value\"}".getBytes(), request.getBody());
    }
}
