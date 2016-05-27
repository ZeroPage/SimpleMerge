package SimpleMerge.control;

import javafx.fxml.FXMLLoader;
import javafx.scene.layout.VBox;

import java.io.IOException;

public class EditPanel extends VBox {
    public interface EventListener {
        void onLoad();
        void onSave();
        void onEdit();
        void onTextChanged();
    }
    private EventListener eventListener;

    public EditPanel() {
        FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("edit_panel.fxml"));
        fxmlLoader.setRoot(this);
        try {
            fxmlLoader.load();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    public void setEventListener(EventListener eventListener) {
        this.eventListener = eventListener;
    }

    public void emitLoad() {
        if (eventListener == null)
            return;
        eventListener.onLoad();
    }

    public void emitEdit() {
        if (eventListener == null)
            return;
        eventListener.onEdit();
    }

    public void emitSave() {
        if (eventListener == null)
            return;
        eventListener.onSave();
    }

    public void emitTextChanged() {
        if (eventListener == null)
            return;
        eventListener.onTextChanged();
    }
}
