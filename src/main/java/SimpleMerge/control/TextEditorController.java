package SimpleMerge.control;

import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.stage.FileChooser;
import org.fxmisc.richtext.InlineCssTextArea;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.nio.charset.Charset;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Paths;

public class TextEditorController {
    @FXML
    private Button load, edit, save;
    @FXML
    private InlineCssTextArea textArea;
    private String fileName;
    @FXML
    public void onLoadButtonClick() {
        File file = getFile();
        if(file != null){
            fileName = file.getName();
            try {
                textArea.replaceText(readFile(file.getPath(), StandardCharsets.UTF_8));
                textArea.setStyle(0, "-fx-fill: red;");
            } catch (IOException e) {
                e.printStackTrace();
            }
            edit.setDisable(false);
        }
    }

    @FXML
    public void onEditButtonClick() {
        textArea.setDisable(false);
        edit.setDisable(true);
    }

    @FXML
    public void onSaveButtonClick() {
        FileChooser fileChooser= new FileChooser();
        fileChooser.getExtensionFilters().add(new FileChooser.ExtensionFilter("txt files (*.txt)", "*.txt"));
        File file = fileChooser.showSaveDialog(null);
        if(file != null){
            try{
                SaveFile(textArea.getText(), file);
            }
            catch(Exception e){
                e.printStackTrace();
            }
            save.setDisable(true);
            edit.setDisable(false);
        }
    }

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
    private File getFile(){
        FileChooser chooser = new FileChooser();
        chooser.getExtensionFilters().add(new FileChooser.ExtensionFilter("txt files (*.txt)", "*.txt"));
        return chooser.showOpenDialog(null);

    }
    private String readFile(String path, Charset encoding)throws IOException {
        byte[] content = Files.readAllBytes(Paths.get(path));
        return new String(content, encoding);
    }

}
