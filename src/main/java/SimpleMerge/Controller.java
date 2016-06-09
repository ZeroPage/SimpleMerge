package SimpleMerge;

import SimpleMerge.control.EditPanel;
import SimpleMerge.control.EditPanelEventListener;
import SimpleMerge.control.FileSelector;
import SimpleMerge.diff.Block;
import SimpleMerge.diff.Diff;
import SimpleMerge.diff.Merger;
import SimpleMerge.util.Pair;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.stage.FileChooser;

import java.io.File;
import java.net.URL;
import java.util.Arrays;
import java.util.LinkedList;
import java.util.List;
import java.util.ResourceBundle;

public class Controller implements Initializable {
    @FXML
    private EditPanel leftEditPanel, rightEditPanel;

    @FXML
    private Button compare, leftMerge, rightMerge;

    private Merger<String> merger;

    private void updateCompareButtonStateIfNeeded() {
        compare.setDisable(leftEditPanel.getText().length() == 0 || rightEditPanel.getText().length() == 0);
    }

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        leftEditPanel.setEventListener(new EditPanelEventListener() {
            @Override
            public void onLoad() {
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
            @Override
            public File saveFile(){
                FileChooser saveFileChooser = new FileChooser();
                saveFileChooser.getExtensionFilters().addAll(
                    new FileChooser.ExtensionFilter("text files (*.txt)", "*.txt"),
                    new FileChooser.ExtensionFilter("All Files", "*.*"));
                return saveFileChooser.showSaveDialog(null);
            }
        });
        rightEditPanel.setEventListener(new EditPanelEventListener() {
            @Override
            public void onLoad() {
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
            @Override
            public File saveFile(){
                FileChooser saveFileChooser = new FileChooser();
                saveFileChooser.getExtensionFilters().addAll(
                    new FileChooser.ExtensionFilter("text files (*.txt)", "*.txt"),
                    new FileChooser.ExtensionFilter("All Files", "*.*"));
                return saveFileChooser.showSaveDialog(null);
            }
        });
    }

    public void compare(ActionEvent actionEvent) {
        leftEditPanel.resetStyle();
        rightEditPanel.resetStyle();
        List<String> l = new LinkedList<>(Arrays.asList(leftEditPanel.getText().split("\n", -1)));
        List<String> r = new LinkedList<>(Arrays.asList(rightEditPanel.getText().split("\n", -1)));
        Diff<String> diff = new Diff<>();
        diff.compare(l, r);
        Pair<List<Block>> diffBlockPair = diff.getDiffBlocks();
        if (diffBlockPair.first.size() == 0) {
            // TODO: notify it's identical.
            System.out.println("No diff blocks!");
            return;
        }
        merger = new Merger<>(l, r, diffBlockPair);
        merger.setMergeEventListener(new Merger.MergeEventListener() {
            @Override
            public void onStart() {
                leftMerge.setDisable(false);
                rightMerge.setDisable(false);
            }
            @Override
            public void onEnd() {
                leftMerge.setDisable(true);
                rightMerge.setDisable(true);
            }
        });
        merger.setOnUpdateBlockStyleFirst(new Merger.UpdateBlockStyleEventListener() {
            @Override
            public void onUpdateBlockStyle(Block block, Merger.BlockState blockState) {
                leftEditPanel.updateBlockStyle(block, blockState);
            }
        });
        merger.setOnUpdateBlockStyleSecond(new Merger.UpdateBlockStyleEventListener() {
            @Override
            public void onUpdateBlockStyle(Block block, Merger.BlockState blockState) {
                rightEditPanel.updateBlockStyle(block, blockState);
            }
        });
        merger.setOnUpdateItemsFirst(new Merger.UpdateItemsEventListener<String>() {
            @Override
            public void onUpdateItems(Block block, List<String> items, boolean includeLastItem) {
                leftEditPanel.updateText(block, items, includeLastItem);
            }
        });
        merger.setOnUpdateItemsSecond(new Merger.UpdateItemsEventListener<String>() {
            @Override
            public void onUpdateItems(Block block, List<String> items, boolean includeLastItem) {
                rightEditPanel.updateText(block, items, includeLastItem);
            }
        });
        merger.start();
    }

    public void copyToLeft(ActionEvent actionEvent) {
        merger.mergeWithSecondItem();
    }

    public void copyToRight(ActionEvent actionEvent) {
        merger.mergeWithFirstItem();
    }
}
