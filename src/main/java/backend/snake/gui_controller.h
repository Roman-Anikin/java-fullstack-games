#pragma once

#include <QObject>
#include <QTimer>

#include "model.h"
#include "pressed_key.cpp"
#include "../../ui/gui/gui_snake.h"

namespace s21 {
class GuiController : public QObject {
  Q_OBJECT

 public:
  GuiController(Model &model, GuiSnake &guiSnake, PressedKey &key);

 private:
  Model &model;
  GuiSnake &guiSnake;
  PressedKey &key;

  void run();
  int mapToKey(int key);
};

}  // namespace s21