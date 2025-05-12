package infrastructure;

import br.com.pedromagno.infrastructure.MyHttpServer;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.io.*;
import java.net.Socket;

public class MyHttpServerIntegrationTest {
    @Test
    @DisplayName("O servidor deve retornar STATUS OK para requisição '/'")
    void testHttpServerResponseStatus200() throws Exception {
        // iniciar o servidor em outra thread
        new Thread(new Runnable() {
            @Override
            public void run() {
                new MyHttpServer(8080).start();
            }
        }).start();

        Thread.sleep(500); // Aguarda o servidor iniciar

        try(Socket clientSocketMock = new Socket("localhost", 8080)){
            OutputStream requestToServer = clientSocketMock.getOutputStream();
            InputStream responseFromServer = clientSocketMock.getInputStream();

            requestToServer.write("GET / HTTTP/1.1\r\nHost: localhost\r\n\r\n".getBytes());
            requestToServer.flush();

            BufferedReader reader = new BufferedReader(new InputStreamReader(responseFromServer));
            String response = reader.readLine();
            System.out.println(response);
            Assertions.assertTrue(response.contains("200 OK"));
        }
    }
}
