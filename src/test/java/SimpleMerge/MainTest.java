package SimpleMerge;

import javafx.scene.control.Button;
import org.hamcrest.CoreMatchers;
import org.junit.Assert;
import org.junit.BeforeClass;
import org.junit.Test;
import org.testfx.api.FxRobot;
import org.testfx.api.FxToolkit;

public class MainTest {
    FxRobot fx = new FxRobot();
    CoreMatchers match = new CoreMatchers();

    @BeforeClass
    public static void setup() throws Exception {
        FxToolkit.registerPrimaryStage();
        FxToolkit.setupApplication(Main.class);
    }
    @Test
    public void loadButtonTest(){
        fx.clickOn("#load");
        
    }
    @Test
    public void editButtonTest() {
        fx.clickOn("#edit");
    }

    @Test
    public void saveButtonTest(){
        fx.clickOn("#save");
    }

    @Test
    public void editSaveTest(){
        fx.clickOn("#edit").write("asdf");
        fx.clickOn("#save");
    }

    @Test
    public void loadEditSaveTest(){
        fx.clickOn("#load");
        //미완성
    }
}