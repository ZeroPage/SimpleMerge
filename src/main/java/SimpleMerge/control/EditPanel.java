package SimpleMerge.control;

import SimpleMerge.util.FileHelper;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.control.Button;
import javafx.scene.layout.VBox;
import javafx.stage.FileChooser;
import org.fxmisc.richtext.InlineCssTextArea;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.nio.charset.Charset;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Paths;

public class EditPanel extends VBox{

    private EditPanelEventListener eventListener;
    private FileSelector selector;
    private String fileName = null;
    @FXML
    private Button load, edit, save;
    @FXML
    private InlineCssTextArea textArea;

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
                textArea.setStyle(0, "-fx-fill: red;");
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
}
