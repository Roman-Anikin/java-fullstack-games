#pragma once

#include <vector>

#include "snake_part.h"

namespace s21 {

class Snake {
 public:
  Snake();

  enum Direction { UP, DOWN, LEFT, RIGHT };

  std::vector<SnakePart> &getBody() { return body; }
  Direction getDirection() { return direction; }
  void setDirection(Direction direction);

 private:
  std::vector<SnakePart> body;
  Direction direction;
  int beginSize;
};
}  // namespace s21