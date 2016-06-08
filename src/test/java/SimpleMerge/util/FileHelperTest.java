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
import java.util.stream.Collectors;

import static org.junit.Assert.assertEquals;

public class FileHelperTest {
    private Path lorem;
    private String contents;

    @Rule
    public TemporaryFolder folder = new TemporaryFolder();

    @Rule
    public ExternalResource resource = new ExternalResource() {
        @Override
        protected void before() throws Throwable {
            super.before();
            lorem = Paths.get(this.getClass().getClassLoader().getResource("Lorem.txt").toURI());
            contents = Files.readAllLines(lorem, StandardCharsets.UTF_8).parallelStream().collect(Collectors.joining("\n"));
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
        // FIXME : Files.readAllLines remove last blank lines, so it make not equals
        String str = FileHelper.load(lorem.toFile());
        assertEquals(contents, str);
    }
}
