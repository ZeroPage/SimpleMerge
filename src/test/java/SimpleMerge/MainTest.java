package SimpleMerge;

import SimpleMerge.control.EditPanel;
import javafx.scene.control.Button;
import org.junit.BeforeClass;
import org.junit.Test;
import org.testfx.api.FxToolkit;

import static org.testfx.api.FxAssert.verifyThat;

public class MainTest {
    @BeforeClass
    public static void setup() throws Exception {
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
}
