#include "tui_snake.h"

using namespace s21;

TuiSnake::TuiSnake() {
    initscr();
    keypad(stdscr, TRUE);
    noecho();
    cbreak();
    curs_set(0);
    start_color();
    init_pair(1, COLOR_WHITE, COLOR_BLACK);
    attron(COLOR_PAIR(1));
    snake_field = createWindow(FIELD_HEIGHT + 2, FIELD_WIDTH + 2, 0, 0);
    score_field = createWindow(FIELD_HEIGHT + 2, FIELD_WIDTH + 5, 0, FIELD_WIDTH + 2);
}

TuiSnake::~TuiSnake() {
    delwin(snake_field);
    delwin(score_field);
    endwin();
}

void TuiSnake::updateView(Model& model) {
    drawSnakeField(model);
    drawScoreField(model);
}

int TuiSnake::readInput() {
    int key = getch();
    return key;
}

void TuiSnake::drawSnakeField(Model& model) {
    werase(snake_field);
    box(snake_field, 0, 0);
    if (model.getStatus() != IN_GAME) {
        drawGameResult(model.getStatus());
    } else {
        drawApple(model.getApple());
        drawSnake(model.getSnake());
    }
    wrefresh(snake_field);
}

void TuiSnake::drawScoreField(Model& model) {
    werase(score_field);
    box(score_field, 0, 0);
    mvwprintw(score_field, 2, 5, "Score");
    mvwprintw(score_field, 3, (FIELD_WIDTH + 5) / 2, std::to_string(model.getScore()).c_str());

    mvwprintw(score_field, 6, 1, "Highest score");
    mvwprintw(score_field, 7, (FIELD_WIDTH + 5) / 2, std::to_string(model.getHighestScore()).c_str());

    mvwprintw(score_field, 9, 5, "Level");
    mvwprintw(score_field, 10, (FIELD_WIDTH + 5) / 2, std::to_string(model.getLevel()).c_str());
    wrefresh(score_field);
}

void TuiSnake::drawApple(SnakePart& apple) {
    mvwprintw(snake_field, apple.getY() + 1, apple.getX() + 1, "*");
}

void TuiSnake::drawSnake(Snake& snake) {
    for (size_t i = 0; i < snake.getBody().size(); i++) {
        mvwprintw(snake_field, snake.getBody()[i].getY() + 1, snake.getBody()[i].getX() + 1, "O");
    }
}

void TuiSnake::drawGameResult(GameStatus status) {
    std::string text = "";
    if (status == WIN) {
        text = "You Win!";
      } else if (status == LOOSE) {
        text = "Game Over";
    }
    mvwprintw(snake_field, FIELD_HEIGHT / 2 - 2, 1, text.c_str());
    mvwprintw(snake_field, FIELD_HEIGHT / 2 - 1, 3, "Press");
    mvwprintw(snake_field, FIELD_HEIGHT / 2, 3, "Enter");
    mvwprintw(snake_field, FIELD_HEIGHT / 2 + 1, 2, "to start");
}

WINDOW* TuiSnake::createWindow(int height, int width, int startY, int startX) {
    WINDOW* window = newwin(height, width, startY, startX);
    box(window, 0, 0);
    refresh();
    return window;
}
