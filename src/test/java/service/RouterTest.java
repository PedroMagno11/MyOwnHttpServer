package service;

import br.com.pedromagno.domain.HttpRequest;
import br.com.pedromagno.domain.HttpResponse;
import br.com.pedromagno.service.Router;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.io.File;
import java.nio.file.Files;

public class RouterTest {

    private Router router;

    @BeforeEach
    void setup(){
        router = new Router();
    }

    @Test
    @DisplayName("Deve retornar a mensagem de boas vindas ao acessar o caminho '/'")
    void shouldReturnWelcomeMessageFromRootPath(){
        HttpRequest request = new HttpRequest();
        request.setMethod("GET");
        request.setPath("/");

        HttpResponse response = router.handle(request);

        Assertions.assertEquals(200, response.getStatusCode());
        Assertions.assertTrue(new String(response.getBody()).contains("Bem-vindo"));
    }

    @Test
    @DisplayName("Deve retornar o json de status da aplicação ao acessar o caminho '/status'")
    void shouldReturnStatusJsonForStatusPath(){
        HttpRequest request = new HttpRequest();
        request.setMethod("GET");
        request.setPath("/status");

        HttpResponse response = router.handle(request);
        Assertions.assertEquals(200, response.getStatusCode());
        Assertions.assertEquals("application/json", response.getHeaders().get("Content-Type"));
        Assertions.assertEquals("{\"status\":\"OK\"}", new String(response.getBody()));
    }

    @Test
    @DisplayName("Deve retornar um arquivo, se existir")
    void shouldReturnStaticFileIfExists() throws Exception{

        // Cria um arquivo fake na pasta public
        File mockFile = new File("public/test.txt");
        mockFile.getParentFile().mkdirs();
        Files.writeString(mockFile.toPath(), "Conteúdo de teste");

        HttpRequest request = new HttpRequest();
        request.setMethod("GET");
        request.setPath("/static/test.txt");

        HttpResponse response = router.handle(request);

        Assertions.assertEquals(200, response.getStatusCode());
        Assertions.assertTrue(new String(response.getBody()).contains("Conteúdo de teste"));

        // apaga o arquivo fake criado para o teste
        mockFile.delete();
    }

    @Test
    @DisplayName("Deve retornar status 404 se o arquivo não for encontrado")
    void shouldReturn404IfStaticFileNotFound(){
        HttpRequest request = new HttpRequest();
        request.setMethod("GET");
        request.setPath("/static/naoexiste.html");

        HttpResponse response = router.handle(request);

        Assertions.assertEquals(404, response.getStatusCode());
        Assertions.assertEquals(new String(response.getBody()), "404 Not Found");
    }

    @Test
    @DisplayName("Deve retornar status 404 quando o caminho requisitado for desconhecido")
    void shouldReturn404ForUnknowPath(){
        HttpRequest request = new HttpRequest();
        request.setMethod("GET");
        request.setPath("/desconhecido");

        HttpResponse response = router.handle(request);

        Assertions.assertEquals(404, response.getStatusCode());
    }
}
