package br.com.pedromagno.infrastructure;

import br.com.pedromagno.adapter.RequestParser;
import br.com.pedromagno.adapter.ResponseWriter;
import br.com.pedromagno.domain.HttpRequest;
import br.com.pedromagno.domain.HttpResponse;
import br.com.pedromagno.service.Router;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.*;
import java.net.Socket;

/**
 * @author Pedro Magno
 * @since 11/05/2025
 */
public class ClientHandler implements Runnable {
    private final Socket clientSocket;
    private final Router router;

    private final Logger LOGGER = LoggerFactory.getLogger(MyHttpServer.class);


    public ClientHandler(Socket clientSocket, Router router) {
        this.clientSocket = clientSocket;
        this.router = router;
    }

    @Override
    public void run() {
        try{
            InputStream inputFromClient = clientSocket.getInputStream();
            OutputStream outputFromServer = clientSocket.getOutputStream();

            // Parse da requisição
            HttpRequest request = RequestParser.parse(inputFromClient);

            // Processa a requisicao via service
            HttpResponse response = router.handle(request);

            // Monta a resposta do servidor e envia
            ResponseWriter.write(response, outputFromServer);

        } catch (IOException e) {
            LOGGER.error("LOGGER - Erro ao processar o clientSocket: " + e.getMessage());
        } finally {
            try{
                clientSocket.close();
            } catch (IOException e) {
                LOGGER.error("Erro ao fechar o clienteSocket: " + e.getMessage());
            }
        }
    }


}
