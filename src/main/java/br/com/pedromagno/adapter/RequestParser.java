package br.com.pedromagno.adapter;

import br.com.pedromagno.domain.HttpRequest;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;

/**
 * Esta classe é responável por tratar as requisições que são recebidas.
 */
public class RequestParser {
    public static HttpRequest parse(InputStream input) throws IOException {
        BufferedReader reader = new BufferedReader(new InputStreamReader(input));
        HttpRequest request = new HttpRequest();

        String linha = reader.readLine();
        if (linha == null || linha.isEmpty()){
            return request;
        }

        // Quebra a requisição em partes. Ex: GET / HTTP/1.1
        String[] partes = linha.split(" ");
        // Obtém a Método. Ex: GET
        request.setMethod(partes[0]);
        // Obtém o caminho. Ex: '/'
        request.setPath(partes[1]);
        // Obtém a versão. Ex: HTTP/1.1
        request.setVersion(partes[2]);

        // Percorre por todo o cabeçalho
        while (!(linha = reader.readLine()).isEmpty()){
            String[] header = linha.split(": ", 2);
            request.getHeaders().put(header[0], header[1]);
        }

        return request;
    }
}
