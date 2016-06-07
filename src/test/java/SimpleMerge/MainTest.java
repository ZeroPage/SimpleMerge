package SimpleMerge;

import SimpleMerge.control.EditPanel;
import SimpleMerge.control.FileSelector;
import javafx.scene.control.Button;
import org.junit.Before;
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

    @Before
    public void setup() throws Exception {
        FxToolkit.registerPrimaryStage();
        FxToolkit.setupApplication(Main.class);
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
}
