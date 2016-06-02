package SimpleMerge.control;

import SimpleMerge.util.FileHelper;
import SimpleMerge.util.Pair;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.control.Button;
import javafx.scene.control.IndexRange;
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
    private int focusedDiffBlockIndex = -1;

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

    private void setAsDiff(Pair<Integer> block) {
        for (int i = block.first; i <= block.second; i++) {
            textArea.setStyle(i, "-fx-fill: red");
        }
    }

    private void setAsFocused(Pair<Integer> block) {
        String[] splitted = textArea.getText().split("\n");
        int start = 0, end = 0;
        for (int i = 0; i < block.first; i++) {
            start += splitted[i].length() + 1;
        }
        for (int i = 0 ; i <= block.second; i++) {
            end += splitted[i].length() + 1;
        }
        textArea.selectRange(start, end);
    }

    private void setAsIdentical(int index) {
        textArea.setStyle(index, "-fx-fill: black");
    }

    private void setAsMerged(Pair<Integer> block) {
        for (int i = block.first; i <= block.second; i++) {
            textArea.setStyle(i, "-fx-fill: green");
        }
    }

    private void resetStyle() {
        for (int i = 0; i < textArea.getText().split("\n").length; i++) {
            textArea.setStyle(i, "-fx-fill: black");
        }
    }

    public void setDiffBlock(List<Pair<Integer>> diffBlocks) {
        resetStyle();
        this.diffBlocks = diffBlocks;
        for (Pair<Integer> block : diffBlocks) {
            setAsDiff(block);
        }
        if (diffBlocks.size() > 0) {
            setFocusDiffBlock(0);
        }
    }

    private void setFocusDiffBlock(int index) {
        if (index >= 0 && index < diffBlocks.size()) {
            setAsFocused(diffBlocks.get(index));
            focusedDiffBlockIndex = index;
        } else {
            textArea.selectRange(0, 0);
            focusedDiffBlockIndex = -1;
        }
    }

    public void unfocus() {
        if (focusedDiffBlockIndex == -1) {
            return;
        }
        setAsDiff(diffBlocks.get(focusedDiffBlockIndex));
        focusedDiffBlockIndex = -1;
    }

    private int getLineCount(String str) {
        int count = str.length() - str.replace("\n", "").length();
        return count == 0 ? 1 : count;
    }

    public void replaceFocusedText(String text) {
        updateDiffBlock(getLineCount(text) - getLineCount(getFocusedText()));
        textArea.replaceSelection(text);
        setAsMerged(diffBlocks.get(focusedDiffBlockIndex));
    }

    public String getFocusedText() {
        return textArea.getSelectedText();
    }

    public boolean moveFocusToNext() {
        IndexRange range = textArea.getSelection();
        textArea.setStyle(range.getStart(), range.getEnd(), "-fx-fill: black");
        setFocusDiffBlock(focusedDiffBlockIndex + 1);
        return focusedDiffBlockIndex != -1;
    }

    private void updateDiffBlock(int k) {
        if (k == 0) {
            System.out.println("No update Diff blocks");
            return;
        }
        diffBlocks.set(focusedDiffBlockIndex, new Pair<>(diffBlocks.get(focusedDiffBlockIndex).first, diffBlocks.get(focusedDiffBlockIndex).second + k));
        for (int i = focusedDiffBlockIndex + 1; i < diffBlocks.size(); i++) {
            diffBlocks.set(i, new Pair<>(diffBlocks.get(i).first + k, diffBlocks.get(i).second + k));
        }
    }
}
