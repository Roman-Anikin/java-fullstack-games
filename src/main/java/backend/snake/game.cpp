#include "game.h"
#include <iostream>

using namespace s21;

Game::Game(std::string mode) {
 if (mode == TUI) {
  tuiInit();
 } else if (mode == GUI) {
  guiInit();
 }
}

void Game::tuiInit() {
  char serverPath[MAX_PATH];
  GetModuleFileName(NULL, serverPath, MAX_PATH);
  std::string exeDir(serverPath);
  size_t lastSlash = exeDir.find_last_of("\\/");
  if (lastSlash != std::string::npos) {
      exeDir = exeDir.substr(0, lastSlash + 1);
  }
  std::string gamePath = exeDir + "TuiSnake.exe";
  std::string cmd = "cmd /c start \"\" \"" + gamePath + "\"";
  system(cmd.c_str());
}

void Game::guiInit() {
  int argc = 1;
  char arg0[] = "Snake";
  char* argv[] = {arg0, nullptr};
  QApplication app(argc, argv);
  Model model;
  PressedKey key;
  GuiSnake window(model, key);
  GuiController controller(model, window, key);
  window.show();
  app.exec();
}