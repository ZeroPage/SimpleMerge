package SimpleMerge;

import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.ListView;
import javafx.scene.layout.AnchorPane;
import org.fxmisc.richtext.InlineCssTextArea;

import java.net.URL;
import java.util.ResourceBundle;

public class Controller implements Initializable {
    @FXML
    private ListView<AnchorPane> textView;
    @FXML
    private InlineCssTextArea leftView, rightView;

    @FXML
    private Button leftLoad, rightLoad;

    String ret;

    @Override
    public void initialize(URL location, ResourceBundle resources) {

    }
}
