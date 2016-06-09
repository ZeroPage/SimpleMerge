package SimpleMerge;

import javafx.geometry.Rectangle2D;
import javafx.stage.Screen;
import javafx.stage.Stage;

public class Config {
    public static void setWindowConstraints(Stage stage) {
        Rectangle2D screenSize = Screen.getPrimary().getVisualBounds();

        stage.setMaxWidth(screenSize.getWidth());
        stage.setMaxHeight(screenSize.getHeight());
        stage.setMinWidth(screenSize.getWidth() / 2);
        stage.setMinHeight(screenSize.getHeight() / 2);
    }
}
