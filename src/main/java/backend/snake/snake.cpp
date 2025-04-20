#include "snake.h"

using namespace s21;

Snake::Snake() {
  direction = DOWN;
  beginSize = 4;
  for (int i = 0; i < beginSize; i++) {
    body.emplace(body.begin(), SnakePart(1, i));
  }
}

void Snake::setDirection(Direction direction) { this->direction = direction; }