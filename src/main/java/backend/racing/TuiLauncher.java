package backend.racing;

import ui.tui.TuiRacing;

public class TuiLauncher {

    public static void main(String[] args) {
        new TuiController(new TuiRacing(), new Model());
    }
}
