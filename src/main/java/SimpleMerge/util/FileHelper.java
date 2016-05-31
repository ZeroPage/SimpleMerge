package SimpleMerge.util;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Paths;

public class FileHelper {
    public static void save(File file, String content) throws IOException {
        FileWriter fileWriter = new FileWriter(file);
        fileWriter.write(content);
        fileWriter.close();
    }

    public static String load(File file) throws IOException {
        return new String(Files.readAllBytes(Paths.get(file.getPath())), StandardCharsets.UTF_8);
    }
}
