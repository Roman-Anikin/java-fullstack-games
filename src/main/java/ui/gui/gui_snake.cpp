#include "gui_snake.h"

using namespace s21;

GuiSnake::GuiSnake(Model &model, PressedKey &key, QWidget *parent) :
  model(model), key(key), QMainWindow(parent) {
  setFixedSize(DOT_WIDTH * FIELD_WIDTH + SCORE_WIDTH, DOT_HEIGHT * FIELD_HEIGHT);
  setWindowTitle("Snake");
}

void GuiSnake::updateView() {
  update();
}

void GuiSnake::keyPressEvent(QKeyEvent *e) {
  key.setKey(e->key());
}

void GuiSnake::paintEvent(QPaintEvent *e) {
  Q_UNUSED(e);
  QPainter painter(this);
  painter.setClipRect(scoreArea());
  drawScore(painter);
  painter.setClipRect(gameArea());
  drawField(painter);
  if (model.getStatus() != IN_GAME) {
    drawGameResult(painter);
    return;
  }
  drawSnake(painter);
  drawApple(painter);
}

QRect GuiSnake::gameArea() {
  return QRect(0, 0, DOT_WIDTH * FIELD_WIDTH, height());
}

QRect GuiSnake::scoreArea() {
  return QRect(DOT_WIDTH * FIELD_WIDTH, 0, SCORE_WIDTH, height());
}

void GuiSnake::drawField(QPainter &painter) {
  painter.fillRect(gameArea(), Qt::lightGray);
}

void GuiSnake::drawScore(QPainter &painter) {
  painter.fillRect(scoreArea(), Qt::white);
  QString text = "\n\nScore\n" + QString::number(model.getScore()) + "\n\n\nHighest score\n" +
         QString::number(model.getHighestScore()) + "\n\n\nLevel\n" +
         QString::number(model.getLevel());
  painter.setFont(QFont("Arial", 11, QFont::ExtraLight));
  painter.drawText(scoreArea(), Qt::AlignHCenter, text);
}

void GuiSnake::drawGameResult(QPainter &painter) {
  QString text = "";
  if (model.getStatus() == WIN) {
    text = "You Win!";
  } else if (model.getStatus() == LOOSE) {
    text = "Game Over";
  }
  painter.setFont(QFont("Arial", 13, QFont::Bold));
  painter.drawText(gameArea(), Qt::AlignCenter,
                    text + "\nPress Enter to start");
}

void GuiSnake::drawSnake(QPainter &painter) {
  for (size_t i = 0; i < model.getSnake().getBody().size(); i++) {
    painter.setBrush(i == 0 ? Qt::yellow : Qt::black);
    painter.drawEllipse(model.getSnake().getBody()[i].getX() * DOT_WIDTH, 
                        model.getSnake().getBody()[i].getY() * DOT_HEIGHT, 
                        DOT_WIDTH, DOT_HEIGHT);
  }
}

void GuiSnake::drawApple(QPainter &painter) {
  painter.setBrush(Qt::red);
  painter.drawEllipse(model.getApple().getX() * DOT_WIDTH, 
                      model.getApple().getY() * DOT_HEIGHT,
                      DOT_WIDTH, DOT_HEIGHT);
}