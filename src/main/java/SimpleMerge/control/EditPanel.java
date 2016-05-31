package SimpleMerge.control;

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
                textArea.replaceText(readFile(file.getPath(), StandardCharsets.UTF_8));
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
                SaveFile(textArea.getText(), file);
            }
            catch(Exception e){
                e.printStackTrace();
            }
            save.setDisable(true);
            edit.setDisable(false);
            emitSave();
        }
        else{
            SaveFile(fileName, file);
        }
    }

//    public File getFile(){
//        FileChooser chooser = new FileChooser();
//        chooser.getExtensionFilters().add(new FileChooser.ExtensionFilter("txt files (*.txt)", "*.txt"));
//        return chooser.showOpenDialog(null);
//    }

    private void SaveFile(String content, File file){
        try{
            FileWriter fileWriter = new FileWriter(file);
            fileWriter.write(content);
            fileWriter.close();
        }
        catch(IOException io){
            io.printStackTrace();
        }
    }
    private String readFile(String path, Charset encoding)throws IOException {
        byte[] content = Files.readAllBytes(Paths.get(path));
        return new String(content, encoding);
    }

    public void setEventListener(EditPanelEventListener eventListener, FileSelector selector) {
        this.eventListener = eventListener;
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
