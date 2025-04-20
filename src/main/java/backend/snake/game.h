#pragma once

#include <QApplication>
#include <windows.h>
#include <string>
#include "../../ui/gui/gui_snake.h"
#include "gui_controller.h"

namespace s21 {

class Game {
 public:
  Game(std::string mode);

 private:
  inline static const std::string TUI = "tui";
  inline static const std::string GUI = "gui";

  void tuiInit();
  void guiInit();
};
}  // namespace s21