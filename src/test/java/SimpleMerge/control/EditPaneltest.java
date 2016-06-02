package SimpleMerge.control;

import javafx.application.Platform;
import org.fxmisc.richtext.InlineCssTextArea;
import org.junit.*;
import org.junit.rules.ExternalResource;
import org.testfx.api.FxRobot;
import org.testfx.api.FxToolkit;

import java.io.File;
import java.net.URISyntaxException;
import java.util.concurrent.TimeoutException;

import static org.junit.Assert.assertEquals;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

public class EditPanelTest {
    File file = null;
    FxRobot fx = new FxRobot();

    @Rule
    public ExternalResource resource = new ExternalResource() {
        @Override
        protected void after() {
            super.after();
            file = null;
        }

        @Override
        protected void before() throws Throwable {
            super.before();
            try {
                file = new File(this.getClass().getClassLoader().getResource("SimpleMerge/diff/multiline-1-A.txt").toURI());
            } catch (URISyntaxException e) {
                e.printStackTrace();
            }
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
        EditPanel editPanel = new EditPanel();
        EditPanelEventListener editPanelEventListener = mock(EditPanelEventListener.class);
        FileSelector fileSelector = mock(FileSelector.class);

        when(fileSelector.getFile()).thenReturn(file);
        editPanel.setEventListener(editPanelEventListener, fileSelector);

        FxToolkit.setupSceneRoot(() -> editPanel);
        FxToolkit.showStage();
    }

    @Test
    public void LoadTest() {
        String s = "It's slow comparing words\n" +
                "and characters. Instead,\n" +
                "WinMerge compares lines.\n" +
                "Believe it or else.";
        fx.clickOn("#load");
        InlineCssTextArea textArea = fx.lookup("#textArea").query();
        System.out.println(fx.lookup("#textArea"));

        assertEquals(s, textArea.getText());
    }
}
