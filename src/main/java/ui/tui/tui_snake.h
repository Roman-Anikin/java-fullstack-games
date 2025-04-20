#pragma once

#include "../../backend/snake/model.h"
#include "../../backend/snake/snake.h"
#include <curses.h>

namespace s21 {
  class TuiSnake {
    public:
      TuiSnake();
      ~TuiSnake();

      void updateView(Model& model);
      int readInput();
        
    private:
      static const int FIELD_WIDTH = 10;
      static const int FIELD_HEIGHT = 20;

      WINDOW* snake_field;
      WINDOW* score_field;

      void drawSnakeField(Model& model);
      void drawScoreField(Model& model);
      void drawApple(SnakePart& apple);
      void drawSnake(Snake& snake);
      void drawGameResult(GameStatus status);
      WINDOW* createWindow(int height, int width, int startY, int startX);
    };

}  // namespace s21