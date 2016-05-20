package main;

import static org.testfx.api.FxAssert.verifyThat;
import static org.testfx.api.FxToolkit.registerPrimaryStage;
import static org.testfx.api.FxToolkit.setupApplication;
import static org.testfx.api.FxToolkit.setupStage;

import javafx.stage.Stage;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;
import org.testfx.api.FxRobot;
import org.testfx.matcher.base.NodeMatchers;

public class MainTest{
    FxRobot fx = new FxRobot();

    @BeforeClass
    public static void setupSpec() throws Exception {
        Stage primaryStage = registerPrimaryStage();
        setupStage(stage -> stage.show());
    }

    @Before
    public void setup() throws Exception {
        setupApplication(Main.class);
    }

    @Test
    public void changeTextTest() {
        fx.clickOn("#changeButton");

        verifyThat("#textArea", NodeMatchers.hasText("Change Success."));
    }
}