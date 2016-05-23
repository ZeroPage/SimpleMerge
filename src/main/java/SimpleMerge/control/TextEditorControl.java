package SimpleMerge.control;

import SimpleMerge.TextEditorController;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.control.Button;
import javafx.scene.control.TextArea;
import javafx.scene.layout.VBox;

import java.io.IOException;

/**
 * Created by JinGyeong Jeong on 16. 5. 22.
 */
public class TextEditorControl extends VBox implements TextEditorController {
    @FXML
    private Button loadButton, editButton, saveButton;
    @FXML
    private TextArea textArea;

    public TextEditorControl() {
        FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("text_editor.fxml"));
        fxmlLoader.setRoot(this);
        fxmlLoader.setController(this);
        try {
            fxmlLoader.load();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public void onLoadButtonClick() {
        System.out.println(getId() + ": onLoadButtonClick()");
    }

    @Override
    public void onEditButtonClick() {
        System.out.println(getId() + ": onEditButtonClick()");
    }

    @Override
    public void onSaveButtonClick() {
        System.out.println(getId() + ": onSaveButtonClick()");
    }
}
