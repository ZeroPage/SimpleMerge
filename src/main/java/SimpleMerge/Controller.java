package SimpleMerge;

import SimpleMerge.control.EditPanel;
import SimpleMerge.control.EditPanelEventListener;
import SimpleMerge.control.FileSelector;
import SimpleMerge.diff.Diff;
import SimpleMerge.diff.Block;
import SimpleMerge.util.Pair;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.ListView;
import javafx.scene.layout.AnchorPane;
import javafx.stage.FileChooser;

import java.io.File;
import java.net.URL;
import java.util.Arrays;
import java.util.List;
import java.util.ResourceBundle;

public class Controller implements Initializable {
    @FXML
    private ListView<AnchorPane> textView;

    @FXML
    private EditPanel leftEditPanel, rightEditPanel;

    @FXML
    private Button compare, leftMerge, rightMerge;

    private String leftEditPanelText, rightEditPanelText;

    private void updateCompareButtonStateIfNeeded() {
        compare.setDisable(leftEditPanelText == null || rightEditPanelText == null);
    }

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        leftEditPanel.setEventListener(new EditPanelEventListener() {
            @Override
            public void onLoad() {
                leftEditPanelText = leftEditPanel.getText();
                updateCompareButtonStateIfNeeded();
            }

            @Override
            public void onSave() {
                // Not Implemented.
            }

            @Override
            public void onEdit() {
                // Not Implemented.
            }

            @Override
            public void onTextChanged() {
                // Not Implemented.
            }
        });
        leftEditPanel.setFileSelector(new FileSelector() {
            @Override
            public File getFile() {
                FileChooser chooser = new FileChooser();
                chooser.getExtensionFilters().addAll(
                    new FileChooser.ExtensionFilter("text files (*.txt)", "*.txt"),
                    new FileChooser.ExtensionFilter("All Files", "*.*"));
                return chooser.showOpenDialog(null);
            }
        });
        rightEditPanel.setEventListener(new EditPanelEventListener() {
            @Override
            public void onLoad() {
                rightEditPanelText = rightEditPanel.getText();
                updateCompareButtonStateIfNeeded();
            }

            @Override
            public void onSave() {
                // Not Implemented.
            }

            @Override
            public void onEdit() {
                // Not Implemented.
            }

            @Override
            public void onTextChanged() {
                // Not Implemented.
            }
        });
        rightEditPanel.setFileSelector(new FileSelector() {
            @Override
            public File getFile() {
                FileChooser chooser = new FileChooser();
                chooser.getExtensionFilters().addAll(
                    new FileChooser.ExtensionFilter("text files (*.txt)", "*.txt"),
                    new FileChooser.ExtensionFilter("All Files", "*.*"));
                return chooser.showOpenDialog(null);
            }
        });
    }

    public void compare(ActionEvent actionEvent) {
        leftEditPanel.resetStyle();
        rightEditPanel.resetStyle();
        leftEditPanelText = leftEditPanel.getText();
        rightEditPanelText = rightEditPanel.getText();
        List<String> l = Arrays.asList(leftEditPanelText.split("\n"));
        List<String> r = Arrays.asList(rightEditPanelText.split("\n"));
        Diff<String> diff = new Diff<>();
        diff.compare(l, r);
        Pair<List<Block>> diffBlockPair = diff.getDiffBlocks();
        if (diffBlockPair.first.size() == 0) {
            // TODO: notify it's identical.
            System.out.println("No diff blocks!");
            return;
        }
        leftEditPanel.startCompare(diffBlockPair.first);
        rightEditPanel.startCompare(diffBlockPair.second);
        leftMerge.setDisable(false);
        rightMerge.setDisable(false);
    }

    public void copyToLeft(ActionEvent actionEvent) {
        merge(rightEditPanel, leftEditPanel);
    }

    public void copyToRight(ActionEvent actionEvent) {
        merge(leftEditPanel, rightEditPanel);
    }

    private void merge(EditPanel from, EditPanel to) {
        to.replaceFocusedText(from.getFocusedText());
        if (!(to.moveFocusToNext() & from.moveFocusToNext())) {
           finishMerge();
        }
    }

    private void finishMerge() {
        leftMerge.setDisable(true);
        rightMerge.setDisable(true);
    }
}
