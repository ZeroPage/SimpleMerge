package SimpleMerge;

import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.TextArea;
import javafx.stage.FileChooser;
import org.fxmisc.richtext.InlineCssTextArea;

import java.io.File;
import java.io.IOException;
import java.nio.charset.Charset;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Paths;

/**
 * Created by JinGyeong Jeong on 16. 5. 22.
 */
public class TextEditorController {
    @FXML
    private Button load, edit, save;
    @FXML
    private InlineCssTextArea textArea;

    @FXML
    public void onLoadButtonClick() {
        File file = getFile();
        try {
            textArea.replaceText(readFile(file.getPath(), StandardCharsets.UTF_8));
            textArea.setStyle(0, "-fx-fill: red;");
        } catch (IOException e) {
            e.printStackTrace();
        }
        edit.setDisable(false);
        //System.out.println(": onLoadButtonClick()");
    }

    @FXML
    public void onEditButtonClick() {
        textArea.setDisable(false);
        edit.setDisable(true);
        //System.out.println(": onEditButtonClick()");
    }

    @FXML
    public void onSaveButtonClick() {
        System.out.println( ": onSaveButtonClick()");
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
