#include "tui_controller.h"
#include <windows.h>

using namespace s21;

int main() {
    Sleep(100);
    AllocConsole();
    freopen("CONIN$", "r", stdin);
    freopen("CONOUT$", "w", stdout);
    freopen("CONOUT$", "w", stderr);
    TuiSnake tuiSnake;
    Model model;
    new TuiController(tuiSnake, model);
}