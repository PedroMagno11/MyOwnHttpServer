package br.com.pedromagno.infrastructure;

import br.com.pedromagno.service.Router;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.*;
import java.net.ServerSocket;
import java.net.Socket;

/**
 * Esta classe inicia o servidor (ServerSocket), aceita conexões e delega para o ClientHandler.
 *
 * @author Pedro Magno
 * @since 11/05/2025
 */

public class MyHttpServer {
    private final int port;
    private final Logger LOGGER = LoggerFactory.getLogger(MyHttpServer.class);

    public MyHttpServer(int port){
        this.port = port;
    }

    public int getPort(){
        return port;
    }

    public void start(){
        try {
            ServerSocket serverSocket = new ServerSocket(port);
            LOGGER.info("Servidor rodando na porta: " + port);
            LOGGER.info("Acesse via http://localhost:" + port);
            serverSocket.setReuseAddress(true);

            while(true){
                Socket clientSocket = serverSocket.accept();
                LOGGER.info("Cliente conectado: " + clientSocket.getInetAddress().getHostAddress());
                ClientHandler clientHandler = new ClientHandler(clientSocket, new Router());
                Thread thread = new Thread(clientHandler);
                thread.start();
            }
        } catch (IOException e) {
            LOGGER.error("Falha ao iniciar o servidor: " + e.getMessage());
        }
    }
}
