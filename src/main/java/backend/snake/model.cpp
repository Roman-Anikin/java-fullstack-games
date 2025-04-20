#include "model.h"

using namespace s21;

Model::Model() {
  snake = std::make_unique<Snake>();
  apple = std::make_unique<SnakePart>();
  isMoveBlock = true;
}

Snake &Model::getSnake() { return *snake; }
SnakePart &Model::getApple() { return *apple; }
int Model::getSpeed() { return speed; }
int Model::getScore() { return score; }
int Model::getHighestScore() { return highestScore; }
int Model::getLevel() { return level; }
GameStatus Model::getStatus() { return status; }

void Model::initGame() {
  snake = std::make_unique<Snake>();
  apple = std::make_unique<SnakePart>();
  speed = 300;
  score = 0;
  readScore();
  level = 0;
  status = IN_GAME;
  isPaused = false;
  isBoosted = false;
  isMoveBlock = false;
  addApple();
}

void Model::addApple() {
  unsigned seed = static_cast<unsigned>(std::chrono::system_clock::now().time_since_epoch().count());
  std::mt19937 gen(seed);
  std::uniform_int_distribution<> x_dist(0, FIELD_WIDTH - 1);
  std::uniform_int_distribution<> y_dist(0, FIELD_HEIGHT - 1);
  bool collision = true;
  while(collision) {
    apple->setX(x_dist(gen));
    apple->setY(y_dist(gen));
    collision = false;
    for (size_t i = 0; i < snake->getBody().size(); i++) {
      if (apple->getX() == snake->getBody()[i].getX() &&
          apple->getY() == snake->getBody()[i].getY()) {
            collision = true;
            break;
      }
    }
  }
}

void Model::moveSnake() {
  if (isPaused) {
    return;
  }
  SnakePart newPart;
  switch (snake->getDirection()) {
    case Snake::Direction::LEFT: {
      newPart =
          SnakePart(snake->getBody()[0].getX() - 1, snake->getBody()[0].getY());
      break;
    }
    case Snake::Direction::RIGHT: {
      newPart =
          SnakePart(snake->getBody()[0].getX() + 1, snake->getBody()[0].getY());
      break;
    }
    case Snake::Direction::UP: {
      newPart =
          SnakePart(snake->getBody()[0].getX(), snake->getBody()[0].getY() - 1);
      break;
    }
    case Snake::Direction::DOWN: {
      newPart =
          SnakePart(snake->getBody()[0].getX(), snake->getBody()[0].getY() + 1);
      break;
    }
  }
  snake->getBody().emplace(snake->getBody().begin(), newPart);
  snake->getBody().pop_back();
  checkField();
  checkApple();
  isMoveBlock = false;
}

void Model::handleInput(int key) {
  if (key == ENTER && status != IN_GAME) {
    initGame();
    return;
  }
  if (key == ESCAPE && status == IN_GAME && !isPaused) {
    isMoveBlock = true;
    return;
  }
  if (key == SPACE && status == IN_GAME) {
    isPaused = !isPaused;
    return;
  }
  if (isMoveBlock || isPaused) {
    return;
  }
  Snake::Direction direction = snake->getDirection();
  if (key == LEFT && direction != Snake::Direction::RIGHT) {
    snake->setDirection(Snake::Direction::LEFT);
  }
  if (key == RIGHT && direction != Snake::Direction::LEFT) {
    snake->setDirection(Snake::Direction::RIGHT);
  }
  if (key == UP && direction != Snake::Direction::DOWN) {
    snake->setDirection(Snake::Direction::UP);
  }
  if (key == DOWN && direction != Snake::Direction::UP) {
    snake->setDirection(Snake::Direction::DOWN);
  }
  if ((key == ENTER) && status == IN_GAME) {
    if (isBoosted) {
      speed += 75;
    } else {
      speed -= 75;
    }
    isBoosted = !isBoosted;
  }
  isMoveBlock = true;
}

void Model::checkField() {
  for (size_t i = 1; i < snake->getBody().size(); ++i) {
    if (snake->getBody()[0] == snake->getBody()[i]) {
      status = LOOSE;
      break;
    }
  }
  if (snake->getBody()[0].getX() >= FIELD_WIDTH ||
      snake->getBody()[0].getX() < 0 ||
      snake->getBody()[0].getY() >= FIELD_HEIGHT ||
      snake->getBody()[0].getY() < 0) {
      status = LOOSE;
  }
}

void Model::checkApple() {
  if (apple->getX() == snake->getBody()[0].getX() &&
      apple->getY() == snake->getBody()[0].getY()) {
    snake->getBody().emplace_back(SnakePart(snake->getBody().back().getX(),
                                            snake->getBody().back().getY()));                 
    increaseScore();
    increaseLevel();
    addApple();
    saveScore();
  }
}

void Model::increaseScore() {
  score++;
  if (score > highestScore) {
    highestScore = score;
  }
  if (score == 200) {
    status = WIN;
  }
}

void Model::increaseLevel() {
  if (score % 5 == 0 && level < 10) {
    level++;
    speed -= SPEED_UP;
  }
}

void Model::readScore() {
  std::ifstream ifs("src/main/resources/snakeScore.txt");
  ifs >> highestScore;
  ifs.close();
}

void Model::saveScore() {
  std::ofstream ofs("src/main/resources/snakeScore.txt");
  ofs << highestScore;
  ofs.close();
}
