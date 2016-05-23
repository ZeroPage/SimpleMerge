package SimpleMerge;

import javafx.fxml.FXML;

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
