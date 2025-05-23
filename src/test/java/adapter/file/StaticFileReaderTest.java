package adapter.file;

import br.com.pedromagno.adapter.file.FileResult;
import br.com.pedromagno.adapter.file.StaticFileReader;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.io.File;
import java.nio.file.Files;

public class StaticFileReaderTest {
    @Test
    @DisplayName("Deve retornar not fount quando o arquivo não existir")
    void shouldReturnNotFoundWhenFileDoesNotExist() {
        FileResult result = StaticFileReader.read("arquivo_inexistente.txt");
        Assertions.assertFalse(result.found());
    }

    @Test
    @DisplayName("Deve retornar o conteúdo do arquivo quando o arquivo existir")
    void shouldReturnFileContentWhenFileExists() throws Exception {
        File file = new File("public/test_reader.txt");
        file.getParentFile().mkdirs();
        Files.writeString(file.toPath(), "Conteúdo do arquivo");

        FileResult result = StaticFileReader.read("test_reader.txt");

        Assertions.assertTrue(result.found());
        Assertions.assertEquals("Conteúdo do arquivo", new String(result.content()));
        Assertions.assertNotNull(result.contentType());

        file.delete();
    }
}
