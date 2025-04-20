#pragma once

namespace s21 {
class SnakePart {
 public:
  SnakePart();
  SnakePart(int x, int y);
  virtual ~SnakePart() = default;

  int getX() { return x; }
  int getY() { return y; }

  void setX(int x);
  void setY(int y);

  bool operator==(const SnakePart &snakePart);

 private:
  int x, y;
};
}  // namespace s21