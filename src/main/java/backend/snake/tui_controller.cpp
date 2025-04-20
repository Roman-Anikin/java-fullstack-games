#include "tui_controller.h"

using namespace s21;

TuiController::TuiController(TuiSnake &tuiSnake, Model &model)
    : tuiSnake(tuiSnake), model(model) {
  run();
}

void TuiController::run() {
    tuiSnake.updateView(model);
    model.handleInput(mapToKey(getch()));
    while(true) {
        timeout(50);
        if (model.getStatus() == IN_GAME) {
            model.moveSnake();
        }
        tuiSnake.updateView(model);
        int key = tuiSnake.readInput();
        model.handleInput(mapToKey(key));
        std::this_thread::sleep_for(std::chrono::milliseconds(model.getSpeed()));
    }
}

int TuiController::mapToKey(int key) {
    switch (key) {
        case 259: return UP;
        case 258: return DOWN;
        case 260: return LEFT;
        case 261: return RIGHT;
        case 10: return ENTER;
        case 27: return ESCAPE;
        case 32: return SPACE;
        default: return 0;
    }
}