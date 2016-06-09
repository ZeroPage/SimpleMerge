package SimpleMerge.control;

import SimpleMerge.Config;
import javafx.application.Platform;
import org.junit.*;
import org.junit.rules.ExternalResource;
import org.testfx.api.FxRobot;
import org.testfx.api.FxToolkit;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.concurrent.TimeoutException;

import static org.junit.Assert.assertEquals;
import static org.mockito.Mockito.*;

public class EditPanelTest {
    FxRobot fx = new FxRobot();
    Path path = null;
    EditPanel editPanel;
    FileSelector fileSelector;

    @Rule
    public ExternalResource resource = new ExternalResource() {
        @Override
        protected void after() {
            super.after();
            path = null;
        }

        @Override
        protected void before() throws Throwable {
            super.before();
            path = Paths.get(this.getClass().getClassLoader().getResource("SimpleMerge/diff/multiline-1-A.txt").toURI());
        }
    };

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
    public void setUp() throws TimeoutException {
        editPanel = new EditPanel();
        fileSelector = mock(FileSelector.class);

        when(fileSelector.getFile()).thenReturn(path.toFile());
        editPanel.setFileSelector(fileSelector);

        FxToolkit.setupStage(stage -> Config.setWindowConstraints(stage));
        FxToolkit.setupSceneRoot(() -> editPanel);
        FxToolkit.showStage();
    }

    // Interaction Test
    @Test
    public void LoadTest() throws IOException {
        fx.clickOn("#load");
        verify(fileSelector).getFile();
        System.out.println(editPanel.getText());
        assertEquals(Files.readAllLines(path), editPanel.getText().split("\n"));
    }
}
