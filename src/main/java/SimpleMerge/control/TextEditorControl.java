package SimpleMerge.control;

import javafx.fxml.FXMLLoader;
import javafx.scene.layout.VBox;

import java.io.IOException;

/**
 * Created by JinGyeong Jeong on 16. 5. 22.
 */
public class TextEditorControl extends VBox {

    public TextEditorControl() {
        FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("text_editor.fxml"));
        fxmlLoader.setRoot(this);
        // fxmlLoader.setController(new TextEditorController());
        try {
            fxmlLoader.load();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
}
