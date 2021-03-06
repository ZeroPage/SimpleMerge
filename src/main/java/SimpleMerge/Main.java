package SimpleMerge;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

public class Main extends Application {
    public static void main(String[] args) {
        launch(args);
    }

    @Override
    public void start(Stage primaryStage) throws Exception {
        FXMLLoader loader = new FXMLLoader(getClass().getClassLoader().getResource("SimpleMerge/main.fxml"));
        Parent root = loader.load();

        primaryStage.setTitle("SimpleMerge");
        Config.setWindowConstraints(primaryStage);

        primaryStage.setScene(new Scene(root));
        primaryStage.show();
    }
}
