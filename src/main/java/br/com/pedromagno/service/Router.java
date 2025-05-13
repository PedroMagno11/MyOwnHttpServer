package br.com.pedromagno.service;

import br.com.pedromagno.adapter.file.FileResult;
import br.com.pedromagno.adapter.file.StaticFileReader;
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
            FileResult file = StaticFileReader.read(path.substring("/static/".length()));

            if(!file.found()) {
                return HttpResponse.notFound();
            }
            return HttpResponse.ok(file.content(), file.contentType());
        }

        return HttpResponse.notFound();
    }
}
