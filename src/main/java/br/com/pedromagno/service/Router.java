package br.com.pedromagno.service;

import br.com.pedromagno.domain.HttpRequest;
import br.com.pedromagno.domain.HttpResponse;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;

/**
 *  Classe responável pela lógica de roteamento: delega a uma função com base na URL/método.
 *
 * @author Pedro Magno
 * @since 11/05/2025
 */

public class Router {
    public HttpResponse handle(HttpRequest request){

        String method = request.getMethod();
        String path = request.getPath();


        if ("GET".equals(method) && "/".equals(path)){
            return HttpResponse.ok("Bem-vindo ao MyOwnHttpServer!".getBytes());
        }

        if ("GET".equals(method) && "/status".equals(path)){
            return HttpResponse.ok("{\"status\":\"OK\"}".getBytes(), "application/json");
        }

        if(path.startsWith("/static/")){
            return serveStaticFile(path.substring("/static/".length()));
        }

        return HttpResponse.notFound();
    }

    private HttpResponse serveStaticFile(String filePath){
        File file = new File("public", filePath);
        if(!file.exists() || file.isDirectory()){
            return HttpResponse.notFound();
        }

        try{
            byte[] contentBytes = Files.readAllBytes(file.toPath());
            String contentType = Files.probeContentType(file.toPath());


            HttpResponse response = HttpResponse.ok(contentBytes, contentType != null ? contentType : "application/octet-stream");
            response.addHeader("Content-Length", String.valueOf(contentBytes.length));
            return response;

        } catch(IOException e){
            return HttpResponse.notFound();
        }
    }
}
