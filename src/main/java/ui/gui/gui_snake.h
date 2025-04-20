#pragma once

#include <QMainWindow>
#include <QKeyEvent>
#include <QPainter>

#include "../../backend/snake/pressed_key.cpp"
#include "../../backend/snake/model.h"

namespace s21 {
class GuiSnake : public QMainWindow {
  Q_OBJECT

 public:
  GuiSnake(Model &model, PressedKey &key, QWidget *parent = nullptr);

  void updateView();

  protected:
   void keyPressEvent(QKeyEvent *e) override;
   void paintEvent(QPaintEvent *e) override;
 
  private:
   static const int DOT_WIDTH = 20;
   static const int DOT_HEIGHT = 20;
   static const int FIELD_WIDTH = 10;
   static const int FIELD_HEIGHT = 20;
   static const int SCORE_WIDTH = 100;
 
   Model &model;
   PressedKey &key;

   QRect gameArea();
   QRect scoreArea();
 
   void drawField(QPainter &painter);
   void drawScore(QPainter &painter);
   void drawGameResult(QPainter &painter);
   void drawSnake(QPainter &painter);
   void drawApple(QPainter &painter);
 };
}  // namespace s21