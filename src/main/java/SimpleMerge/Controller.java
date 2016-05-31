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
        }, new FileSelector() {
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
        }, new FileSelector() {
            @Override
            public File getFile() {
                FileChooser chooser = new FileChooser();
                chooser.getExtensionFilters().add(new FileChooser.ExtensionFilter("txt files (*.txt)", "*.txt"));
                return chooser.showOpenDialog(null);
            }
        });
    }

    public void compare(ActionEvent actionEvent) {
        // Not Implemented.
        LCS<String> lcs = new LCS<>();
        List<String> l = Arrays.asList(leftEditPanelText.split("\n"));
        List<String> r = Arrays.asList(rightEditPanelText.split("\n"));
        List<Pair<Integer>> commonIndexes = lcs.diff(l, r);

        for (int i=0; i<commonIndexes.size(); i++) {
            Pair<Integer> pair = commonIndexes.get(i);
            System.out.println("Common index (" + pair.first + "," + pair.second + "): " + l.get(pair.first));
        }
    }
}
