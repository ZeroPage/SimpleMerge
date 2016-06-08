package SimpleMerge;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.geometry.Rectangle2D;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Screen;
import javafx.stage.Stage;

public class Main extends Application {
    @Override
    public void start(Stage primaryStage) throws Exception{
        Rectangle2D screenSize = Screen.getPrimary().getVisualBounds();

        FXMLLoader loader = new FXMLLoader(getClass().getClassLoader().getResource("SimpleMerge/main.fxml"));
        Parent root = loader.load();

        primaryStage.setTitle("SimpleMerge");
        primaryStage.setMaxWidth(screenSize.getWidth());
        primaryStage.setMaxHeight(screenSize.getHeight());
        primaryStage.setMinWidth(screenSize.getWidth() / 2);
        primaryStage.setMinHeight(screenSize.getHeight() / 2);
        primaryStage.setScene(new Scene(root));
        primaryStage.show();
    }

    public static void main(String[] args) {
        launch(args);
    }
}
