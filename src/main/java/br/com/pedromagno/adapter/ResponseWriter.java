package br.com.pedromagno.adapter;

import br.com.pedromagno.domain.HttpResponse;

import java.io.IOException;
import java.io.OutputStream;
import java.nio.charset.StandardCharsets;
import java.util.Map;

public class ResponseWriter {
    public static void write(HttpResponse response, OutputStream outputFromServer) throws IOException {
        outputFromServer.write(("HTTP/1.1 " + response.getStatusCode() + " " + response.getReasonPhrase() + "\r\n").getBytes(StandardCharsets.UTF_8));

        for (Map.Entry<String, String> header : response.getHeaders().entrySet()){
            outputFromServer.write((header.getKey() + ": " + header.getValue() + "\r\n").getBytes(StandardCharsets.UTF_8));
        }

        outputFromServer.write("\r\n".getBytes(StandardCharsets.UTF_8));

        if(response.getBody() != null){
            outputFromServer.write(response.getBody());
        }

        outputFromServer.flush();
        outputFromServer.close();
    }

}
