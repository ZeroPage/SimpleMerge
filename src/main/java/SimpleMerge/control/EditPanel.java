package SimpleMerge.control;

import SimpleMerge.util.FileHelper;
import SimpleMerge.util.Pair;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.control.Button;
import javafx.scene.layout.VBox;
import org.fxmisc.richtext.InlineCssTextArea;

import java.io.File;
import java.io.IOException;
import java.util.List;

public class EditPanel extends VBox{

    private EditPanelEventListener eventListener;
    private FileSelector selector;
    private String fileName = null;
    @FXML
    private Button load, edit, save;
    @FXML
    private InlineCssTextArea textArea;

    private List<Pair<Integer>> diffBlocks;

    private void emitLoad() {
        if (eventListener == null)
            return;
        eventListener.onLoad();
    }

    private void emitEdit() {
        if (eventListener == null)
            return;
        eventListener.onEdit();
    }

    private void emitSave() {
        if (eventListener == null)
            return;
        eventListener.onSave();
    }

    private void emitTextChanged() {
        if (eventListener == null)
            return;
        eventListener.onTextChanged();
    }

    @FXML
    private void onLoadButtonClick() {
        File file = selector.getFile();
        if(file != null){
            fileName = file.getName();
            try {
                textArea.replaceText(FileHelper.load(file));
                resetStyle();
            } catch (IOException e) {
                e.printStackTrace();
            }
            edit.setDisable(false);
            emitLoad();
        }
    }

    @FXML
    private void onEditButtonClick() {
        textArea.setDisable(false);
        edit.setDisable(true);
        emitEdit();
    }

    @FXML
    private void onSaveButtonClick() {
        File file = selector.getFile();
        if(fileName != null || file != null){
            try{
                FileHelper.save(file, textArea.getText());
            }
            catch(Exception e){
                e.printStackTrace();
            }
            save.setDisable(true);
            edit.setDisable(false);
            emitSave();
        }
        else{
            try {
                FileHelper.save(file, fileName);
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    public void setEventListener(EditPanelEventListener eventListener) {
        this.eventListener = eventListener;
    }

    public void setFileSelector(FileSelector selector) {
        this.selector = selector;
    }

    public EditPanel() {
        FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("edit_panel.fxml"));
        fxmlLoader.setRoot(this);
        fxmlLoader.setController(this);
        try {
            fxmlLoader.load();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    public String getText() {
        return textArea.getText();
    }

    public void setAsDiff(int from, int to) {
        for (int i = from; i <= to; i++) {
            setAsDiff(i);
        }
    }

    private void setAsDiff(int index) {
        textArea.setStyle(index, "-fx-fill: yellow");
    }

    private void resetStyle() {
        for (int i = 0; i < textArea.getText().split("\n").length; i++) {
            textArea.setStyle(i, "fx-fill: black");
        }
    }

    public void setDiffBlock(List<Pair<Integer>> diffBlocks) {
        resetStyle();
        this.diffBlocks = diffBlocks;
        for (Pair<Integer> block : diffBlocks) {
            setAsDiff(block.first, block.second);
        }
    }
}
