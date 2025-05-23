package domain;

import br.com.pedromagno.domain.HttpResponse;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

public class HttpResponseTest {

    @Test
    @DisplayName("Deve retornar uma resposta com conteúdo padrão e http status OK")
    void shouldCreateOkResponseWithDefaultContentType(){
        byte[] body = "Teste".getBytes();
        HttpResponse response = HttpResponse.ok(body);

        Assertions.assertEquals(200, response.getStatusCode());
        Assertions.assertEquals("OK", response.getReasonPhrase());
        Assertions.assertArrayEquals(body, response.getBody());
        Assertions.assertEquals("text/plain", response.getHeaders().get("Content-Type"));
        Assertions.assertEquals(String.valueOf(body.length), response.getHeaders().get("Content-Length"));
    }

    @Test
    @DisplayName("Deve criar uma resposta com conteúdo customizado e http status OK")
    void shouldCreateOkResponseWithCustomContentType(){
        byte[] body = "<html></html>".getBytes();
        HttpResponse response = HttpResponse.ok(body, "text/html");

        Assertions.assertEquals("text/html", response.getHeaders().get("Content-Type"));
    }

    @Test
    @DisplayName("Deve criar uma resposta de não encontrado")
    void shouldCreateNotFoundResponse(){
        HttpResponse response = HttpResponse.notFound();

        Assertions.assertEquals(404, response.getStatusCode());
        Assertions.assertEquals("Not Found", response.getReasonPhrase());
        Assertions.assertEquals("404 Not Found", new String(response.getBody()));
    }
}
