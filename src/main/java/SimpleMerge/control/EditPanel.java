package SimpleMerge.control;

import SimpleMerge.diff.Block;
import SimpleMerge.util.FileHelper;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.IndexRange;
import javafx.scene.control.Label;
import javafx.scene.layout.VBox;
import javafx.stage.FileChooser;
import org.fxmisc.richtext.InlineCssTextArea;

import java.io.File;
import java.io.IOException;
import java.net.URL;
import java.nio.charset.Charset;
import java.nio.file.*;
import java.util.List;
import java.util.ResourceBundle;

public class EditPanel extends VBox implements Initializable{

    private EditPanelEventListener eventListener;
    private FileSelector selector;
    private String fileName = null;
    @FXML
    private Button load, edit, save;
    @FXML
    private InlineCssTextArea textArea;

    @FXML
    private Label pathLabel;

    private List<Block> diffBlocks;
    private int currentBlockIndex;
    private File currentOpenFile;

    private static class BlockStyle {
        public static String Identical = "-fx-background-fill: white;";
        public static String Merged = "-fx-background-fill: lightgreen;";
        public static String Diff = "-fx-background-fill: yellow;";
        public static String Focused = "-fx-background-fill: lightblue;";
    }

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
        load.setDisable(true);
        currentOpenFile = selector.getFile();
        load.setDisable(false);
        if(currentOpenFile != null){
            fileName = currentOpenFile.getName();
            pathLabel.setText(currentOpenFile.getPath());
            try {
                textArea.setDisable(false);
                textArea.replaceText(FileHelper.load(currentOpenFile));
                textArea.setEditable(false);
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
        textArea.setEditable(true);
        edit.setDisable(true);
        save.setDisable(false);
        emitEdit();
    }

    @FXML
    private void onSaveButtonClick() {
        textArea.setEditable(false);
        String content = textArea.getText();

        if(pathLabel.getText().equals("")){
            saveAs(content);
        }
        else{
            save(content);
        }
        emitSave();
    }

    private void save(String content){
        Path filePath = Paths.get(pathLabel.getText());
        try {
            Files.write(filePath, content.getBytes(Charset.forName("UTF-8")), StandardOpenOption.TRUNCATE_EXISTING);
        } catch (IOException e) {
            e.printStackTrace();
        }finally{
            save.setDisable(true);
            edit.setDisable(false);
        }
    }

    private void saveAs(String content){
        FileChooser saveFileChooser = new FileChooser();
        saveFileChooser.getExtensionFilters().addAll(
            new FileChooser.ExtensionFilter("text files (*.txt)", "*.txt"),
            new FileChooser.ExtensionFilter("All Files", "*.*"));
        File file = saveFileChooser.showSaveDialog(null);
        if(file != null) {
            try{
                FileHelper.save(file, content);
                pathLabel.setText(file.getPath());
            }catch(IOException e) {
                e.printStackTrace();
            }finally{
                save.setDisable(true);
                edit.setDisable(false);
            }
        }
        else{
            save.setDisable(false);
            edit.setDisable(true);
            textArea.setEditable(true);
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


    private void setAsFocused(Block block) {
        String[] splitted = textArea.getText().split("\n");
        int start = 0, end = 0;
        for (int i = 0; i < block.start(); i++) {
            start += splitted[i].length() + 1;
        }
        for (int i = 0 ; i < block.end(); i++) {
            end += splitted[i].length() + 1;
        }
        textArea.selectRange(start, end);
    }

    private void setAsIdentical(int index) {
        textArea.setStyle(index, "-fx-fill: black");
    }

    private void setAsMerged(Block block) {
        for (int i = block.start(); i < block.end(); i++) {
            textArea.setStyle(i, "-fx-fill: green");
        }
    }

    public void resetStyle() {
        for (int i = 0; i < textArea.getText().split("\n").length; i++) {
            textArea.setStyle(i, "-fx-fill: black");
        }
    }

    private void setBlockStyle(Block block, String style) {
        for (int i = block.start(); i < block.end(); i++) {
            textArea.setStyle(i, style);
        }
    }

    private String getBlockText(Block block) {
        StringBuilder stringBuilder = new StringBuilder();
        String[] splitted = textArea.getText().split("\n");
        System.out.println(getId() + " block.start(): " + block.start() + ", block.end(): " + block.end() + ", splitted.length: " + splitted.length);
        for (int i = block.start(); i < block.end(); i++) {
            stringBuilder.append(splitted[i]);
            if (i < splitted.length - 1) {
                stringBuilder.append('\n');
            }
        }
        return stringBuilder.toString();
    }

    private IndexRange calculateIndexRange(Block block) {
        String[] splitted = textArea.getText().split("\n");
        int start = 0, end = 0;
        for (int i = 0; i < block.end(); i++) {
            if (i < block.start()) {
                start += splitted[i].length();
                if (i < splitted.length - 1) {
                    start +=1;
                }
            }
            end += splitted[i].length();
            if (i < splitted.length - 1) {
                end += 1;
            }
        }
        return new IndexRange(start, end);
    }

    public void startCompare(List<Block> diffBlocks) {
        resetStyle();
        this.diffBlocks = diffBlocks;
        for (Block block : diffBlocks) {
            setBlockStyle(block, BlockStyle.Diff);
        }
        setFocusDiffBlock(0);
    }

    private void setFocusDiffBlock(int index) {
        currentBlockIndex = index;
        if (currentBlockIndex < diffBlocks.size()) {
            setBlockStyle(diffBlocks.get(currentBlockIndex), BlockStyle.Focused);
        }
    }

    private int getLineCount(String str) {
        // FIXME: Use linear search for counting newline character.
        return 1 + str.length() - str.replace("\n", "").length();
    }

    public void replaceFocusedText(String text) {
        int currentLineCount = getLineCount(getFocusedText());
        textArea.replaceText(calculateIndexRange(diffBlocks.get(currentBlockIndex)), text);
        updateDiffBlock(getLineCount(text) - currentLineCount);
        setBlockStyle(diffBlocks.get(currentBlockIndex), BlockStyle.Merged);
    }

    public String getFocusedText() {
        setBlockStyle(diffBlocks.get(currentBlockIndex), BlockStyle.Identical);
        return getBlockText(diffBlocks.get(currentBlockIndex));
    }

    public boolean moveFocusToNext() {
        setFocusDiffBlock(currentBlockIndex + 1);
        return currentBlockIndex < diffBlocks.size();
    }

    private void updateDiffBlock(int k) {
        if (k == 0) {
            System.out.println("No update Diff blocks");
            return;
        }
        diffBlocks.get(currentBlockIndex).update(0, k);
        for (int i = currentBlockIndex + 1; i < diffBlocks.size(); i++) {
            diffBlocks.get(i).update(k);
        }
    }

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        textArea.setPrefSize(1000, 1000);
    }
}
