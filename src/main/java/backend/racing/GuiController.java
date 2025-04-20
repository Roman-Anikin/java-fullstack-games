package backend.racing;

import ui.gui.GuiRacing;
import javafx.animation.AnimationTimer;

public class GuiController {

    private final GuiRacing render;
    private final PressedKey key;
    private final Model model;
    private long last = System.nanoTime();

    public GuiController(GuiRacing render, PressedKey key, Model model) {
        this.render = render;
        this.key = key;
        this.model = model;
        init();
    }

    private void init() {
        AnimationTimer timer = new AnimationTimer() {
            @Override
            public void handle(long now) {
                if (now - last >= model.getSpeed() * 1_000_000L) {
                    if (model.getStatus() == GameStatus.IN_GAME) {
                        model.moveCars();
                        last = now;
                    }
                }
                model.handleInput(key.getKeyCode());
                render.updateView(model);
                key.setKeyCode(0);
                if (render.isWindowClose()) {
                    stop();
                }
            }
        };
        timer.start();
    }
}
