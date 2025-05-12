package br.com.pedromagno.infrastructure;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.*;
import java.net.ServerSocket;
import java.net.Socket;

public class MyHttpServer {
    private final int port;
    private final Logger LOGGER = LoggerFactory.getLogger(MyHttpServer.class);

    public MyHttpServer(int port){
        this.port = port;
    }

    public int getPort(){
        return port;
    }

    private void handleClient(Socket clientSocket) {
        try{
            BufferedReader in = new BufferedReader(new InputStreamReader(clientSocket.getInputStream()));
            BufferedWriter out = new BufferedWriter(new OutputStreamWriter(clientSocket.getOutputStream()));

            // Lê a primeira linha da requisição (Método, caminho, versão)
            String requestMessage = in.readLine();
            if(requestMessage == null || requestMessage.isEmpty()){
                return;
            }

            System.out.println("Requisição recebida: " + requestMessage);
            LOGGER.info("LOGGER - Requisição recebida: " + requestMessage);

            String body = "Hello from MyOwnHttpServer!";
            String response = "HTTP/1.1 200 OK\r\n" +
                    "Content-Type: text/plain\r\n" +
                    "Content-Length: " + body.length() + "\r\n" +
                    "Connection: close\r\n" +
                    "\r\n" +
                    body;

            out.write(response);
            out.flush();
        } catch (IOException e) {
            System.err.println("Erro ao processar o clientSocket: " + e.getMessage());
            LOGGER.error("LOGGER - Erro ao processar o clientSocket: " + e.getMessage());
        } finally {
            try{
                clientSocket.close();
            } catch (IOException e) {
                System.err.println("Erro ao fechar o clienteSocket: " + e.getMessage());
                LOGGER.error("Erro ao fechar o clienteSocket: " + e.getMessage());
            }
        }
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
                handleClient(clientSocket);
            }
        } catch (IOException e) {
            LOGGER.error("Falha ao iniciar o servidor: " + e.getMessage());
        }
    }
}
