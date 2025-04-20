#pragma once

#include <chrono>
#include <thread>
#include "../../ui/tui/tui_snake.h"
#include "model.h"

namespace s21 {
  
class TuiController {

 public:
  TuiController(TuiSnake &tuiSnake, Model &model);

 private:
  TuiSnake &tuiSnake;
  Model &model;

  void run();
  int mapToKey(int key);
};

}  // namespace s21