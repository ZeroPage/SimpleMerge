package SimpleMerge.control;

import SimpleMerge.diff.Block;
import SimpleMerge.diff.Merger;
import SimpleMerge.util.FileHelper;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.IndexRange;
import javafx.scene.control.Label;
import javafx.scene.layout.VBox;
import org.fxmisc.richtext.InlineCssTextArea;

import java.io.File;
import java.io.IOException;
import java.net.URL;
import java.nio.charset.Charset;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardOpenOption;
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

    private File currentOpenFile;

    public void updateBlockStyle(Block block, Merger.BlockState blockState) {
        for (int i = block.start(); i < block.end(); i++) {
            textArea.setStyle(i, BlockStyle.getCssById(blockState));
        }
    }

    public void updateText(Block block, List<String> items, boolean includeLastItem) {
        textArea.replaceText(calculateIndexRange(block), String.join("\n", items) + (includeLastItem == true ? "" : "\n"));
    }

    public static class BlockStyle {
        public static String getCssById(Merger.BlockState blockState) {
            switch (blockState) {
                case IDENTICAL:
                    return "-fx-fill: black;";
                case MERGED:
                    return "-fx-background-fill: lightgreen;";
                case DIFF:
                    return "-fx-background-fill: yellow;";
                case FOCUSED:
                    return "-fx-background-fill: lightblue;";
                default:
                    throw new RuntimeException("Undefined BlockStyle id : " + blockState);
            }
        }
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
        File file = selector.saveFile();
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

    public void resetStyle() {
        for (int i = 0; i < textArea.getText().split("\n", -1).length; i++) {
            textArea.setStyle(i, "-fx-fill: black");
        }
    }

    private IndexRange calculateIndexRange(Block block) {
        String[] splitted = textArea.getText().split("\n", -1);
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

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        textArea.setPrefSize(1000, 1000);
    }
}
