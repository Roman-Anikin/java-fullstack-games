#include "gui_controller.h"

using namespace s21;

GuiController::GuiController(Model &model, GuiSnake &guiSnake, PressedKey &key) :
  model(model), guiSnake(guiSnake), key(key) {
  run();
}

void GuiController::run() {
  QTimer* timer = new QTimer(this);
  connect(timer, &QTimer::timeout, this, [this, timer](){
    model.handleInput(mapToKey(key.getKey()));
    if (model.getStatus() == IN_GAME) {
        model.moveSnake();
    }
    guiSnake.updateView();
    key.setKey(0);
    timer->setInterval(model.getSpeed());
  });
  timer->start(model.getSpeed());
}

int GuiController::mapToKey(int key) {
  switch (key) {
      case 16777235: return UP;
      case 16777237: return DOWN;
      case 16777234: return LEFT;
      case 16777236: return RIGHT;
      case 16777220: return ENTER;
      case 16777216: return ESCAPE;
      case 32: return SPACE;
      default: return 0;
  }
}