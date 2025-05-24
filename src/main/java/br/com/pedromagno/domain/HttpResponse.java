package br.com.pedromagno.domain;

import java.io.IOException;
import java.io.OutputStream;
import java.nio.charset.StandardCharsets;
import java.util.HashMap;
import java.util.Map;

public class HttpResponse {
    private int statusCode;
    private String reasonPhrase;
    private Map<String, String> headers = new HashMap<>();
    private byte[] body;

    public static HttpResponse ok(byte[] body){
        return ok(body, "text/plain");
    }

    public static HttpResponse ok(byte[] body, String contentType){
        HttpResponse response = new HttpResponse();
        response.setStatusCode(200);
        response.setReasonPhrase("OK");
        response.setBody(body);
        response.addHeader("Content-Type", contentType);
        response.addHeader("Content-Length", String.valueOf(body.length));
        response.addHeader("Connection", "close");
        return response;
    }

    public static HttpResponse notFound(){
        HttpResponse response = new HttpResponse();
        response.setStatusCode(404);
        response.setReasonPhrase("Not Found");
        byte[] body = "404 Not Found".getBytes();
        response.setBody(body);
        response.addHeader("Content-Type", "text/plain");
        response.addHeader("Content-Length", String.valueOf(body.length));
        response.addHeader("Connection", "close");
        return response;
    }

    public static HttpResponse badRequest(byte[] body) {
        HttpResponse response = new HttpResponse();
        response.setStatusCode(400);
        response.setReasonPhrase("Bad Request");
        response.setBody(body);
        response.addHeader("Content-Type", "text/plain");
        response.addHeader("Content-Length", String.valueOf(body.length));
        response.addHeader("Connection", "close");
        return response;
    }

    public static HttpResponse badRequestJson(String message) {
        String json = "{\"error\":\"" + message.replace("\"", "\\\"") + "\"}";
        byte[] body = json.getBytes(StandardCharsets.UTF_8);
        HttpResponse response = new HttpResponse();
        response.setStatusCode(400);
        response.setReasonPhrase("Bad Request");
        response.setBody(body);
        response.addHeader("Content-Type", "application/json");
        response.addHeader("Content-Length", String.valueOf(body.length));
        response.addHeader("Connection", "close");
        return response;
    }

    public int getStatusCode() {
        return statusCode;
    }

    public void setStatusCode(int statusCode) {
        this.statusCode = statusCode;
    }

    public String getReasonPhrase() {
        return reasonPhrase;
    }

    public void setReasonPhrase(String reasonPhrase) {
        this.reasonPhrase = reasonPhrase;
    }

    public Map<String, String> getHeaders() {
        return headers;
    }

    public void addHeader(String key, String value) {
        headers.put(key, value);
    }

    public void setHeaders(Map<String, String> headers) {
        this.headers = headers;
    }

    public byte[] getBody() {
        return body;
    }

    public void setBody(byte[] body) {
        this.body = body;
    }

}
