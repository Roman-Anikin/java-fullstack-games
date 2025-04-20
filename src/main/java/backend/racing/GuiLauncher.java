package backend.racing;

import javafx.application.Application;
import javafx.stage.Stage;
import ui.gui.GuiRacing;

public class GuiLauncher extends Application {

    public static void main(String[] args) {
        Application.launch();
    }

    @Override
    public void start(Stage stage) {
        PressedKey key = new PressedKey();
        new GuiController(new GuiRacing(stage, key), key, new Model());
    }
}
