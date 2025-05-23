package adapter;

import br.com.pedromagno.adapter.ResponseWriter;
import br.com.pedromagno.domain.HttpResponse;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.nio.charset.StandardCharsets;

public class ResponseWriterTest {
    @Test
    @DisplayName("Deve escrever um HttpResponse corretamente")
    void shouldWriteHttpResponseCorrectly() throws IOException {
        byte[] body = "Hello, World!".getBytes(StandardCharsets.UTF_8);
        HttpResponse response = HttpResponse.ok(body, "text/plain");

        ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
        ResponseWriter.write(response, outputStream);

        String output = outputStream.toString(StandardCharsets.UTF_8);
        Assertions.assertTrue(output.contains("HTTP/1.1 200 OK"));
        Assertions.assertTrue(output.contains("Content-Type: text/plain"));
        Assertions.assertTrue(output.contains("Content-Length: " + body.length));
        Assertions.assertTrue(output.contains("Hello, World!"));
    }
}
