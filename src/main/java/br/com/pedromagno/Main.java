package br.com.pedromagno;

import br.com.pedromagno.infrastructure.MyHttpServer;

public class Main {
    public static void main(String[] args) {
        MyHttpServer server = new MyHttpServer(8080);
        server.start();
    }
}