package SimpleMerge;

import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.ListView;
import javafx.scene.layout.AnchorPane;
import javafx.stage.FileChooser;
import org.fxmisc.richtext.InlineCssTextArea;

import java.io.File;
import java.io.IOException;
import java.net.URL;
import java.nio.charset.Charset;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ResourceBundle;

public class Controller implements Initializable {
    @FXML
    private ListView<AnchorPane> textView;
    @FXML
    private InlineCssTextArea leftView, rightView;

    @FXML
    private Button leftLoad, rightLoad;

    String ret;

    @FXML
    void RightLoadBtnAction(){
        File file = getFile();
        try {
            rightView.replaceText(readFile(file.getPath(), StandardCharsets.UTF_8));
            rightView.setStyle(0, "-fx-fill: red;");

        } catch (IOException e) {
            e.printStackTrace();
        }


    }
    @FXML
    void LeftLoadBtnAction(){
        File file = getFile();
        try {
            leftView.replaceText(readFile(file.getPath(), StandardCharsets.UTF_8));
        } catch (IOException e) {
            e.printStackTrace();
        }

    }
    private File getFile(){
        FileChooser chooser = new FileChooser();
        chooser.getExtensionFilters().add(new FileChooser.ExtensionFilter("txt files (*.txt)", "*.txt"));
        return chooser.showOpenDialog(null);

    }
    private String readFile(String path, Charset encoding)throws IOException{
        byte[] content = Files.readAllBytes(Paths.get(path));
        return new String(content, encoding);
    }


    @Override
    public void initialize(URL location, ResourceBundle resources) {

    }
}