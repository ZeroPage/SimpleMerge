package SimpleMerge;

import SimpleMerge.control.EditPanel;
import SimpleMerge.control.FileSelector;
import SimpleMerge.diff.Merger;
import javafx.application.Platform;
import javafx.scene.control.Button;
import org.fxmisc.richtext.InlineCssTextArea;
import org.junit.*;
import org.testfx.api.FxRobot;
import org.testfx.api.FxToolkit;

import java.net.URI;
import java.net.URISyntaxException;
import java.nio.file.Paths;
import java.util.concurrent.TimeoutException;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;
import static org.testfx.api.FxAssert.verifyThat;

public class MainTest {
    FxRobot fx = new FxRobot();

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
    public void setup() throws Exception {
        FxToolkit.setupApplication(Main.class);
        FxToolkit.showStage();
    }

    @After
    public void tearDown() throws Exception {
        FxToolkit.hideStage();
    }

    // Side-by-Side edit panels.
    @Test
    public void shouldHaveTwoEditPanels() {
        verifyThat("#leftEditPanel", (EditPanel editPanel) -> editPanel.isVisible());
        verifyThat("#leftEditPanel #load", (Button b) -> !b.isDisabled() && b.getText().equals("Load"));
        verifyThat("#leftEditPanel #edit", (Button b) -> !b.isDisabled() && b.getText().equals("Edit"));
        verifyThat("#leftEditPanel #save", (Button b) -> !b.isDisabled() && b.getText().equals("Save"));

        verifyThat("#rightEditPanel", (EditPanel editPanel) -> editPanel.isVisible());
        verifyThat("#rightEditPanel #load", (Button b) -> !b.isDisabled() && b.getText().equals("Load"));
        verifyThat("#rightEditPanel #edit", (Button b) -> !b.isDisabled() && b.getText().equals("Edit"));
        verifyThat("#rightEditPanel #save", (Button b) -> !b.isDisabled() && b.getText().equals("Save"));
    }

    // Button for Functionality of Comparing
    @Test
    public void shouldHaveCompareButton() {
        verifyThat("#compare", (Button b) -> b.isDisabled() && b.getText().equals("Compare"));
    }

    // Buttons for Functionality of Merging
    @Test
    public void shouldHaveCopyToRightButtonAndCopyToLeftButton() {
        verifyThat("#leftMerge", (Button b) -> b.isDisabled() && b.getText().equals("Copy to Left"));
        verifyThat("#rightMerge", (Button b) -> b.isDisabled() && b.getText().equals("Copy to Right"));
    }

    private void setMockFileSelector(String editPanelId, String resourcePath) {
        FileSelector fileSelector = mock(FileSelector.class);
        try {
            URI resourceURI = this.getClass().getClassLoader().getResource(resourcePath).toURI();
            when(fileSelector.getFile()).thenReturn(Paths.get(resourceURI).toFile());
        } catch (URISyntaxException e) {
            throw new RuntimeException(e);
        }
        EditPanel editPanel = fx.lookup(editPanelId).query();
        editPanel.setFileSelector(fileSelector);
    }

    @Test
    public void shouldBeAbleToLoadFile() {
        setMockFileSelector("#leftEditPanel", "SimpleMerge/diff/multiline-1-A.txt");
        fx.clickOn("#leftEditPanel #load");
        verifyThat("#leftEditPanel", (EditPanel editPanel) -> editPanel.getText().length() > 10);
    }

    @Test
    public void shouldBeAbleToCompare() {
        setMockFileSelector("#leftEditPanel", "SimpleMerge/diff/multiline-1-A.txt");
        setMockFileSelector("#rightEditPanel", "SimpleMerge/diff/multiline-1-B.txt");
        fx.clickOn("#leftEditPanel #load");
        fx.clickOn("#rightEditPanel #load");
        verifyThat("#compare", (Button b) -> !b.isDisabled());
    }

    @Test
    public void shouldNotBeAbleToCopyBeforeCompare() {
        setMockFileSelector("#leftEditPanel", "SimpleMerge/diff/multiline-1-A.txt");
        setMockFileSelector("#rightEditPanel", "SimpleMerge/diff/multiline-1-B.txt");
        fx.clickOn("#leftEditPanel #load");
        fx.clickOn("#rightEditPanel #load");

        verifyThat("#leftMerge", (Button b) -> b.isDisabled());
        verifyThat("#rightMerge", (Button b) -> b.isDisabled());
    }

    @Test
    public void shouldHighlightTextOnCompare() {
        setMockFileSelector("#leftEditPanel", "SimpleMerge/diff/multiline-1-A.txt");
        setMockFileSelector("#rightEditPanel", "SimpleMerge/diff/multiline-1-B.txt");
        fx.clickOn("#leftEditPanel #load");
        fx.clickOn("#rightEditPanel #load");
        fx.clickOn("#compare");

        verifyThat("#leftEditPanel #textArea", (InlineCssTextArea ta) ->
            ta.getStyleAtPosition(0, 0).equals(EditPanel.BlockStyle.getCssById(Merger.BlockState.FOCUSED)) &&
                ta.getStyleAtPosition(1, 0).equals(EditPanel.BlockStyle.getCssById(Merger.BlockState.FOCUSED)) &&
                ta.getStyleAtPosition(2, 0).equals(EditPanel.BlockStyle.getCssById(Merger.BlockState.IDENTICAL)) &&
                ta.getStyleAtPosition(3, 0).equals(EditPanel.BlockStyle.getCssById(Merger.BlockState.DIFF)));

        verifyThat("#rightEditPanel #textArea", (InlineCssTextArea ta) ->
            ta.getStyleAtPosition(0, 0).equals(EditPanel.BlockStyle.getCssById(Merger.BlockState.FOCUSED)) &&
                ta.getStyleAtPosition(1, 0).equals(EditPanel.BlockStyle.getCssById(Merger.BlockState.IDENTICAL)) &&
                ta.getStyleAtPosition(2, 0).equals(EditPanel.BlockStyle.getCssById(Merger.BlockState.DIFF)));
    }

    @Test
    public void editTextArea() {
        fx.clickOn("#leftEditPanel #edit");
        fx.clickOn("#leftEditPanel #textArea").write("ack");
        verifyThat("#leftEditPanel #textArea", (InlineCssTextArea ta) -> ta.getText(0).equals("ack"));

    }

    @Test
    public void saveTextArea() {
        setMockFileSelector("#leftEditPanel", "SimpleMerge/diff/multiline-1-A.txt");
        fx.clickOn("#leftEditPanel #load");
        fx.clickOn("#leftEditPanel #save");
    }

    @Test
    public void loadTextArea() {
        setMockFileSelector("#leftEditPanel", "SimpleMerge/diff/multiline-1-A.txt");
        fx.clickOn("#leftEditPanel #load");
        String leftText = ((EditPanel) fx.lookup("#leftEditPanel").query()).getText();
        assertNotNull(leftText);
    }

    @Test
    public void shouldBeIdenticalAfterConsecutiveCopyToLeft() {
        setMockFileSelector("#leftEditPanel", "SimpleMerge/diff/multiline-1-A.txt");
        setMockFileSelector("#rightEditPanel", "SimpleMerge/diff/multiline-1-B.txt");
        fx.clickOn("#leftEditPanel #load");
        fx.clickOn("#rightEditPanel #load");
        fx.clickOn("#compare");
        fx.clickOn("#leftMerge");
        fx.clickOn("#leftMerge");

        String leftText = ((EditPanel) fx.lookup("#leftEditPanel").query()).getText();
        String rightText = ((EditPanel) fx.lookup("#rightEditPanel").query()).getText();
        assertEquals(leftText, rightText);
    }

    @Test
    public void shouldBeIdenticalAfterConsecutiveCopyToRight() {
        setMockFileSelector("#leftEditPanel", "SimpleMerge/diff/multiline-1-A.txt");
        setMockFileSelector("#rightEditPanel", "SimpleMerge/diff/multiline-1-B.txt");
        fx.clickOn("#leftEditPanel #load");
        fx.clickOn("#rightEditPanel #load");
        fx.clickOn("#compare");
        fx.clickOn("#rightMerge");
        fx.clickOn("#rightMerge");

        String leftText = ((EditPanel) fx.lookup("#leftEditPanel").query()).getText();
        String rightText = ((EditPanel) fx.lookup("#rightEditPanel").query()).getText();
        assertEquals(leftText, rightText);
    }

    @Test
    public void shouldMergeCorrectly1() {
        setMockFileSelector("#leftEditPanel", "SimpleMerge/diff/multiline-2-A.txt");
        setMockFileSelector("#rightEditPanel", "SimpleMerge/diff/multiline-2-B.txt");
        fx.clickOn("#leftEditPanel #load");
        fx.clickOn("#rightEditPanel #load");
        fx.clickOn("#compare");

        assertEquals("aa\nbb", ((EditPanel) fx.lookup("#leftEditPanel").query()).getText());
        assertEquals("bb\ncc\n", ((EditPanel) fx.lookup("#rightEditPanel").query()).getText());

        fx.clickOn("#rightMerge");
        assertEquals("aa\nbb\ncc\n", ((EditPanel) fx.lookup("#rightEditPanel").query()).getText());

        fx.clickOn("#rightMerge");
        assertEquals("aa\nbb", ((EditPanel) fx.lookup("#rightEditPanel").query()).getText());
    }

    @Test
    public void shouldMergeCorrelectly2() {
        setMockFileSelector("#leftEditPanel", "SimpleMerge/diff/multiline-2-A.txt");
        setMockFileSelector("#rightEditPanel", "SimpleMerge/diff/multiline-2-B.txt");
        fx.clickOn("#leftEditPanel #load");
        fx.clickOn("#rightEditPanel #load");
        fx.clickOn("#compare");

        assertEquals("aa\nbb", ((EditPanel) fx.lookup("#leftEditPanel").query()).getText());
        assertEquals("bb\ncc\n", ((EditPanel) fx.lookup("#rightEditPanel").query()).getText());

        fx.clickOn("#leftMerge");
        assertEquals("bb", ((EditPanel) fx.lookup("#leftEditPanel").query()).getText());

        fx.clickOn("#leftMerge");
        assertEquals("bb\ncc\n", ((EditPanel) fx.lookup("#leftEditPanel").query()).getText());
    }
}
