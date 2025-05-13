package br.com.pedromagno.adapter.file;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;

/**
 * Classe responsável pela leitura de arquivos estáticos da pasta "public".
 * Pode ser usada para servir conteúdo estático (ex: HTML, imagens, CSS).
 *
 * @author Pedro Magno
 * @since 12/05/2025
 */
public class StaticFileReader {

    /**
     * Lê o arquivo localizado no caminho relativo fornecido.
     *
     * @param filePath Caminho do arquivo relativo à pasta "public".
     * @return Um {@link FileResult} contendo os dados do arquivo e o tipo de conteúdo,
     *         ou {@code notFound()} se o arquivo não existir ou for um diretório.
     */
    public static FileResult read(String filePath){
        File file = new File("public", filePath);
        if(!file.exists() || file.isDirectory()){
            return FileResult.notFound();
        }

        try{
            byte[] content = Files.readAllBytes(file.toPath());
            String contentType = Files.probeContentType(file.toPath());
            return FileResult.success(content, contentType);
        } catch (IOException e) {
            return FileResult.notFound();
        }
    }
}
