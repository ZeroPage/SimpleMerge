package SimpleMerge;

import SimpleMerge.control.EditPanel;
import SimpleMerge.control.EditPanelEventListener;
import SimpleMerge.control.FileSelector;
import SimpleMerge.diff.LCS;
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
    private Button compare;

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
                chooser.getExtensionFilters().add(new FileChooser.ExtensionFilter("txt files (*.txt)", "*.txt"));
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
                chooser.getExtensionFilters().add(new FileChooser.ExtensionFilter("txt files (*.txt)", "*.txt"));
                return chooser.showOpenDialog(null);
            }
        });
    }

    public void compare(ActionEvent actionEvent) {
        LCS<String> lcs = new LCS<>();
        List<String> l = Arrays.asList(leftEditPanelText.split("\n"));
        List<String> r = Arrays.asList(rightEditPanelText.split("\n"));
        List<Pair<Integer>> commonIndexes = lcs.diff(l, r);

        int leftLastCommonLine, rightLastCommonLine;
        leftLastCommonLine = rightLastCommonLine = -1;
        for (Pair<Integer> pair : commonIndexes) {
            System.out.println("Common index (" + pair.first + "," + pair.second + "): " + l.get(pair.first));
            leftEditPanel.setAsDiff(leftLastCommonLine + 1, pair.first - 1);
            rightEditPanel.setAsDiff(rightLastCommonLine + 1, pair.second - 1);
            leftLastCommonLine = pair.first;
            rightLastCommonLine = pair.second;
        }

        int leftLastLine = l.size() - 1, rightLastLine = r.size() - 1;
        leftEditPanel.setAsDiff(leftLastCommonLine + 1, leftLastLine);
        rightEditPanel.setAsDiff(rightLastCommonLine + 1, rightLastLine);
    }
}
