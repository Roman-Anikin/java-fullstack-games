#pragma once

#include <fstream>
#include <memory>
#include <random>
#include <string>
#include <stdio.h>
#include <chrono>

#include "snake.h"
#include "keys.cpp"
#include "game_status.cpp"

namespace s21 {
class Model {
  
 public:
  Model();

  void initGame();
  void addApple();
  void moveSnake();
  void handleInput(int key);

  void checkField();
  void checkApple();

  Snake &getSnake();
  SnakePart &getApple();
  int getSpeed();
  int getScore();
  int getHighestScore();
  int getLevel();
  GameStatus getStatus();

 private:
  static const int FIELD_WIDTH = 10;
  static const int FIELD_HEIGHT = 20;
  static const int SPEED_UP = 20;

  std::unique_ptr<Snake> snake;
  std::unique_ptr<SnakePart> apple;
  int speed = 300;
  int score = 0;
  int highestScore = 0;
  int level = 0;
  GameStatus status;
  bool isPaused;
  bool isBoosted;
  bool isMoveBlock;

  void increaseScore();
  void increaseLevel();
  void readScore();
  void saveScore();
};
}  // namespace s21