package backend.racing;

import ui.tui.TuiRacing;

public class TuiController {

    private final TuiRacing render;
    private final Model model;

    public TuiController(TuiRacing render, Model model) {
        this.render = render;
        this.model = model;
        init();
    }

    private void init() {
        while (true) {
            int key = render.readInput();
            model.handleInput(key);
            if (model.getStatus() == GameStatus.IN_GAME) {
                model.moveCars();
            }
            render.updateView(model);
            if (render.isConsoleClosed()) {
                break;
            }
            try {
                Thread.sleep(model.getSpeed());
            } catch (InterruptedException ignored) {
            }
        }
    }
}
