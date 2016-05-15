package main;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.Parent;
import javafx.scene.control.TextArea;
import javafx.stage.FileChooser;

import java.io.BufferedInputStream;
import java.io.BufferedReader;
import java.io.File;
import java.io.IOException;
import java.nio.charset.Charset;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

public class Controller {
    private FileChooser fileChooser = new FileChooser();
    @FXML protected Parent root;
    @FXML protected TextArea textArea;

    @FXML protected void openFile(ActionEvent event) {
        final Charset charset = StandardCharsets.UTF_8;

        File file = fileChooser.showOpenDialog(root.getScene().getWindow());
        if (file != null) {
            Path path = Paths.get(file.getPath());
            StringBuffer buffer = new StringBuffer();
            try (BufferedReader reader = Files.newBufferedReader(path, charset)) {
                String line = null;
                while ((line = reader.readLine()) != null) {
                    buffer.append(line);
                    buffer.append('\n');
                }
            } catch (IOException x) {
                System.err.format("IOException: %s%n", x);
            }
            textArea.setText(buffer.toString());
        }
    }

    @FXML protected void changeText(ActionEvent event) {
        textArea.setText("Change Success.");
    }
}
