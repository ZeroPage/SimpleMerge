package SimpleMerge.control;

import javafx.fxml.FXMLLoader;
import javafx.scene.layout.VBox;

import java.io.IOException;

public class TextEditorControl extends VBox {

    public TextEditorControl() {
        FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("text_editor.fxml"));
        fxmlLoader.setRoot(this);
        try {
            fxmlLoader.load();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
}
