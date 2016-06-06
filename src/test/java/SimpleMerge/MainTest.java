package SimpleMerge;

import SimpleMerge.control.EditPanel;
import SimpleMerge.control.FileSelector;
import javafx.scene.control.Button;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;
import org.testfx.api.FxRobot;
import org.testfx.api.FxToolkit;

import java.net.URI;
import java.net.URISyntaxException;
import java.nio.file.Paths;

import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;
import static org.testfx.api.FxAssert.verifyThat;

public class MainTest {
    FxRobot fx = new FxRobot();
    FileSelector leftFileSelector, rightFileSelector;

    @BeforeClass
    public static void setup() throws Exception {
        FxToolkit.registerPrimaryStage();
        FxToolkit.setupApplication(Main.class);
    }

    @Before
    public void setupFileSelector() throws URISyntaxException {
        ClassLoader classLoader = this.getClass().getClassLoader();

        EditPanel left = fx.lookup("#leftEditPanel").query();
        EditPanel right = fx.lookup("#rightEditPanel").query();
        leftFileSelector = mock(FileSelector.class);
        rightFileSelector = mock(FileSelector.class);
        URI leftResourceURI = classLoader.getResource("SimpleMerge/diff/multiline-1-A.txt").toURI();
        URI rightResourceURI = classLoader.getResource("SimpleMerge/diff/multiline-1-B.txt").toURI();
        when(leftFileSelector.getFile()).thenReturn(Paths.get(leftResourceURI).toFile());
        when(rightFileSelector.getFile()).thenReturn(Paths.get(rightResourceURI).toFile());

        left.setFileSelector(leftFileSelector);
        right.setFileSelector(rightFileSelector);
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

    @Test
    public void shouldBeAbleToLoadFile() {
        fx.clickOn("#leftEditPanel #load");
        verifyThat("#leftEditPanel", (EditPanel editPanel) -> editPanel.getText().length() > 10);
    }
}
