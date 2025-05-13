package br.com.pedromagno.adapter.file;

/**
 * Esta classe representa o resultado da tentatica de leitura de um arquivo.
 * Indica se o arquivo foi encontrado, seu conteúdo em bytes e o tipo MIME.
 *
 * @author Pedro Magno
 * @since 12/05/2025
 */

public record FileResult(boolean found, byte[] content, String contentType){

    public static FileResult notFound(){
        return new FileResult(false, null, null);
    }

    public static FileResult success(byte[] content, String contentType){
        return new FileResult(true, content, contentType != null ? contentType : "application/octet-stream");
    }
}
