package SimpleMerge;

import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.TextArea;

/**
 * Created by JinGyeong Jeong on 16. 5. 22.
 */
public interface TextEditorController {
    @FXML
    void onLoadButtonClick();
    @FXML
    void onEditButtonClick();
    @FXML
    void onSaveButtonClick();
}
