#include "snake_part.h"

using namespace s21;

SnakePart::SnakePart() { x = y = 0; }

SnakePart::SnakePart(int x, int y) : x(x), y(y) {}

void SnakePart::setX(int x) { this->x = x; }

void SnakePart::setY(int y) { this->y = y; }

bool SnakePart::operator==(const SnakePart &snakePart) {
  return x == snakePart.x && y == snakePart.y;
}