package SimpleMerge.control;

import SimpleMerge.Config;
import javafx.application.Platform;
import javafx.scene.Scene;
import org.junit.*;
import org.junit.rules.ExternalResource;
import org.junit.rules.TemporaryFolder;
import org.testfx.api.FxRobot;
import org.testfx.api.FxToolkit;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.util.concurrent.TimeoutException;
import java.util.stream.Collectors;

import static org.junit.Assert.assertArrayEquals;
import static org.junit.Assert.assertEquals;
import static org.mockito.Mockito.*;

public class EditPanelTest {
    @Rule
    public TemporaryFolder folder = new TemporaryFolder();
    FxRobot fx = new FxRobot();
    private Path contentPath;
    @Rule
    public ExternalResource resource = new ExternalResource() {
        @Override
        protected void after() {
            super.after();
            contentPath = null;
        }

        @Override
        protected void before() throws Throwable {
            super.before();
            contentPath = Paths.get(this.getClass().getClassLoader().getResource("SimpleMerge/diff/multiline-1-A.txt").toURI());
        }
    };
    private EditPanel editPanel;
    private FileSelector fileSelector;

    @BeforeClass
    public static void setUpClass() throws TimeoutException {
        FxToolkit.registerPrimaryStage();
    }

    @AfterClass
    public static void tearDownClass() throws TimeoutException {
        FxToolkit.hideStage();
        Platform.setImplicitExit(true);
    }

    @Before
    public void setUp() throws Exception {
        Path read = folder.newFile().toPath();
        Files.copy(contentPath, read, StandardCopyOption.REPLACE_EXISTING);

        editPanel = new EditPanel();
        fileSelector = mock(FileSelector.class);

        when(fileSelector.getFile()).thenReturn(read.toFile());
        when(fileSelector.saveFile()).thenReturn(folder.newFile());
        editPanel.setFileSelector(fileSelector);

        FxToolkit.setupStage(stage -> Config.setWindowConstraints(stage));
        FxToolkit.setupScene(() -> new Scene(editPanel));
        FxToolkit.showStage();
    }

    @After
    public void tearDown() throws Exception {
        FxToolkit.hideStage();
    }

    // Interaction Test
    @Test
    public void LoadTest() throws IOException {
        fx.clickOn("#load");
        verify(fileSelector).getFile();
        assertArrayEquals(Files.readAllLines(contentPath).toArray(), editPanel.getText().split("\n"));
    }

    @Test
    public void editTest() throws Exception {
        String input = "Hello, World!";
        fx.clickOn("#edit");
        fx.clickOn("#textArea").write(input);
        assertEquals(input, editPanel.getText());
    }

    @Test
    public void loadEditTest() {
        String input = "Text Example";
        fx.clickOn("#load");
        fx.clickOn("#edit");
        fx.clickOn("#textArea").write(input);
    }

    @Test
    public void saveAsTest() throws Exception {
        String input = "Hello, World!\nGoodbye, World!";
        fx.clickOn("#edit");
        fx.clickOn("#textArea").write(input);
        fx.clickOn("#save");
        verify(fileSelector).saveFile();

        assertEquals(input, Files.readAllLines(fileSelector.saveFile().toPath()).parallelStream().collect(Collectors.joining("\n")));
    }

    @Test
    public void saveTest() throws Exception {
        fx.clickOn("#load");
        verify(fileSelector).getFile();
        fx.clickOn("#edit");
        fx.clickOn("#textArea").write("Hello, World!");
        fx.clickOn("#save");

        assertEquals(editPanel.getText(), Files.readAllLines(fileSelector.getFile().toPath()).parallelStream().collect(Collectors.joining("\n")));
    }
}
