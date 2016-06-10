package SimpleMerge.util;

import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExternalResource;
import org.junit.rules.TemporaryFolder;

import java.io.File;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

import static org.junit.Assert.assertEquals;

public class FileHelperTest {
    @Rule
    public TemporaryFolder folder = new TemporaryFolder();
    private Path lorem;
    private String contents;
    @Rule
    public ExternalResource resource = new ExternalResource() {
        @Override
        protected void before() throws Throwable {
            super.before();
            lorem = Paths.get(this.getClass().getClassLoader().getResource("Lorem.txt").toURI());
            contents = new String(Files.readAllBytes(lorem), StandardCharsets.UTF_8);
        }

        @Override
        protected void after() {
            super.after();
            lorem = null;
            contents = null;
        }
    };

    @Test
    public void save() throws Exception {
        File file = folder.newFile("Lorem-output.txt");
        FileHelper.save(file, contents);
        assertEquals(Files.readAllLines(lorem), Files.readAllLines(file.toPath()));
    }

    @Test
    public void load() throws Exception {
        String str = FileHelper.load(lorem.toFile());
        assertEquals(contents, str);
    }
}
