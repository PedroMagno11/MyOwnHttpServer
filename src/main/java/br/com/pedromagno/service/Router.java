package br.com.pedromagno.service;

import br.com.pedromagno.adapter.file.FileResult;
import br.com.pedromagno.adapter.file.StaticFileReader;
import br.com.pedromagno.domain.HttpRequest;
import br.com.pedromagno.domain.HttpResponse;
import br.com.pedromagno.utils.AccessTracker;

import java.io.File;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
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

        if (method == null || path == null) {
            return HttpResponse.badRequest("Requisição malformada".getBytes(StandardCharsets.UTF_8));
        }


        if ("GET".equals(method) && "/".equals(path)){
            StringBuilder builder = new StringBuilder();
            builder.append("Bem-vindo ao MyOwnHttpServer!\n");
            builder.append("Esse servidor HTTP foi desenvolvido por Pedro Magno.\n");
            builder.append("Sim, esse projeto e bem simples. Atualmente so oferece suporte a requisicoes GET.\n");
            builder.append("Para acessar a api: http://localhost:8080/api\n");
            builder.append("Para acessar o site: http://localhost:8080/static/index.html\n");
            builder.append("Para acessar as estatisticas de acesso: http://localhost:8080/info\n");
            String content = builder.toString();
            return HttpResponse.ok(content.getBytes(StandardCharsets.UTF_8));
        }

        if ("GET".equals(method) && "/status".equals(path)){
            return HttpResponse.ok("{\"status\":\"OK\"}".getBytes(), "application/json");
        }

        if("GET".equals(method) && "/api".equals(path)){

            FileResult file = StaticFileReader.read("/api/api.json");
            if(!file.found()){
                return HttpResponse.notFoundJson();
            }
            return HttpResponse.ok(file.content(), file.contentType());

        }

        if("GET".equals(method) && "/info".equals(path)){
            AccessTracker tracker = AccessTracker.getInstance();
            String json = String.format("{\"totalAcessos\": %d, \"conexoesAtivas\": %d, \"visitantesUnicos\": %d}", tracker.getTotalAcessos(), tracker.getConexoesAtivas(), tracker.getVisitantesUnicos());
            return HttpResponse.ok(json.getBytes(StandardCharsets.UTF_8));
        }

        if("GET".equals(method) && path.contains("/api/navios")){
            FileResult file = StaticFileReader.read("/api/navios.json");
            if(!file.found()){
                return HttpResponse.notFoundJson();
            }
            return HttpResponse.ok(file.content(), file.contentType());
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
