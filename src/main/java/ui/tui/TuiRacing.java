package ui.tui;

import backend.racing.Car;
import backend.racing.GameStatus;
import backend.racing.Model;
import com.googlecode.lanterna.Symbols;
import com.googlecode.lanterna.TerminalPosition;
import com.googlecode.lanterna.TextColor;
import com.googlecode.lanterna.graphics.TextGraphics;
import com.googlecode.lanterna.input.KeyStroke;
import com.googlecode.lanterna.screen.Screen;
import com.googlecode.lanterna.terminal.DefaultTerminalFactory;
import com.googlecode.lanterna.terminal.swing.SwingTerminalFontConfiguration;

import java.awt.*;
import java.io.IOException;
import java.util.Arrays;
import java.util.List;

import static backend.racing.GameUtils.*;
import static com.googlecode.lanterna.terminal.swing.AWTTerminalFontConfiguration.BoldMode.EVERYTHING;

public class TuiRacing {

    private Screen screen;
    private TextGraphics tg;
    private boolean isConsoleClosed;

    public TuiRacing() {
        try {
            Font font = new Font("Lucida Console", Font.PLAIN, 14);
            SwingTerminalFontConfiguration config = new SwingTerminalFontConfiguration(true, EVERYTHING,
                    font);
            DefaultTerminalFactory factory = new DefaultTerminalFactory()
                    .setTerminalEmulatorFontConfiguration(config)
                    .setForceTextTerminal(false)
                    .setTerminalEmulatorTitle("Racing");
            screen = factory.createScreen();
            screen.setCursorPosition(null);
            screen.startScreen();
            tg = screen.newTextGraphics();
            tg.setForegroundColor(TextColor.ANSI.WHITE_BRIGHT);
        } catch (IOException e) {
            System.out.println(Arrays.toString(e.getStackTrace()));
        }
    }

    public boolean isConsoleClosed() {
        return isConsoleClosed;
    }

    public void updateView(Model model) {
        screen.clear();
        drawGameField();
        drawScoreField(model);
        if (model.getStatus() != GameStatus.IN_GAME) {
            drawMessage(model.getStatus());
        } else {
            drawPlayerCar(model.getPlayerCar());
            drawRivalCars(model.getRivalCars());
        }
        try {
            screen.refresh();
        } catch (IOException e) {
            System.out.println(Arrays.toString(e.getStackTrace()));
        }
    }

    public int readInput() {
        KeyStroke key = null;
        try {
            key = screen.pollInput();
        } catch (IOException ignored) {
        }
        return mapKeys(key);
    }

    private void drawMessage(GameStatus status) {
        String message = "";
        if (status == GameStatus.WIN) {
            message = "You Win!";
        } else if (status == GameStatus.LOOSE) {
            message = "Game Over";
        }
        tg.putString(1, GAME_FIELD_HEIGHT / 2 - 2, message);
        tg.putString(3, GAME_FIELD_HEIGHT / 2 - 1, "Press");
        tg.putString(3, GAME_FIELD_HEIGHT / 2, "ENTER");
        tg.putString(2, GAME_FIELD_HEIGHT / 2 + 1, "To Start");
    }

    private void drawGameField() {
        for (int i = 0; i < GAME_FIELD_HEIGHT + 2; i++) {
            for (int j = 0; j < GAME_FIELD_WIDTH + 2; j++) {
                if (i == 0 && j == 0) {
                    tg.setCharacter(j, i, Symbols.DOUBLE_LINE_TOP_LEFT_CORNER);
                }
                if (i == 0 && j == GAME_FIELD_WIDTH + 1) {
                    tg.setCharacter(j, i, Symbols.DOUBLE_LINE_T_DOWN);
                }
                if (i == GAME_FIELD_HEIGHT + 1 && j == 0) {
                    tg.setCharacter(j, i, Symbols.DOUBLE_LINE_BOTTOM_LEFT_CORNER);
                }
                if (i == GAME_FIELD_HEIGHT + 1 && j == GAME_FIELD_WIDTH + 1) {
                    tg.setCharacter(j, i, Symbols.DOUBLE_LINE_T_UP);
                }
                if ((i == 0 || i == GAME_FIELD_HEIGHT + 1) && (j > 0 && j < GAME_FIELD_WIDTH + 1)) {
                    tg.setCharacter(j, i, Symbols.DOUBLE_LINE_HORIZONTAL);
                }
                if ((i > 0 && i < GAME_FIELD_HEIGHT + 1) && (j == 0 || j == GAME_FIELD_WIDTH + 1)) {
                    tg.setCharacter(j, i, Symbols.DOUBLE_LINE_VERTICAL);
                }
            }
        }
    }

    private void drawScoreField(Model model) {
        for (int i = 0; i < SCORE_FIELD_HEIGHT + 2; i++) {
            for (int j = GAME_FIELD_WIDTH + 2; j < SCORE_FIELD_WIDTH; j++) {
                if (i == 0 && j == SCORE_FIELD_WIDTH - 1) {
                    tg.setCharacter(j, i, Symbols.DOUBLE_LINE_TOP_RIGHT_CORNER);
                }
                if (i == GAME_FIELD_HEIGHT + 1 && j == SCORE_FIELD_WIDTH - 1) {
                    tg.setCharacter(j, i, Symbols.DOUBLE_LINE_BOTTOM_RIGHT_CORNER);
                }
                if ((i == 0 || i == GAME_FIELD_HEIGHT + 1) && j < SCORE_FIELD_WIDTH - 1) {
                    tg.setCharacter(j, i, Symbols.DOUBLE_LINE_HORIZONTAL);
                }
                if ((i > 0 && i < GAME_FIELD_HEIGHT + 1) && j == SCORE_FIELD_WIDTH - 1) {
                    tg.setCharacter(j, i, Symbols.DOUBLE_LINE_VERTICAL);
                }
            }
        }
        tg.putString(GAME_FIELD_WIDTH + 4, 3, "SCORE");
        tg.putString(GAME_FIELD_WIDTH + 6, 4, String.valueOf(model.getScore()));
        tg.putString(GAME_FIELD_WIDTH + 3, 6, "HIGHEST");
        tg.putString(GAME_FIELD_WIDTH + 4, 7, "SCORE");
        tg.putString(GAME_FIELD_WIDTH + 6, 8, String.valueOf(model.getHighestScore()));
        tg.putString(GAME_FIELD_WIDTH + 4, 10, "LEVEL");
        tg.putString(GAME_FIELD_WIDTH + 6, 11, String.valueOf(model.getLevel()));
    }

    private void drawPlayerCar(Car car) {
        tg.setForegroundColor(TextColor.ANSI.GREEN);
        for (int i = 0; i < car.getBody().length; i++) {
            for (int j = 0; j < car.getBody()[i].length; j++) {
                if (car.getBody()[i][j] == null || car.getBody()[i][j].y == -1 ||
                        car.getBody()[i][j].y > GAME_FIELD_HEIGHT - 1) {
                    continue;
                }
                tg.setCharacter(new TerminalPosition(car.getBody()[i][j].x + 1, car.getBody()[i][j].y + 1),
                        CAR_PART);
            }
        }
        tg.setForegroundColor(TextColor.ANSI.WHITE_BRIGHT);
    }

    private void drawRivalCars(List<Car> cars) {
        cars.forEach(this::drawPlayerCar);
    }

    private int mapKeys(KeyStroke key) {
        if (key == null) {
            return 0;
        }
        switch (key.getKeyType()) {
            case ArrowLeft -> {
                return LEFT;
            }
            case ArrowRight -> {
                return RIGHT;
            }
            case ArrowUp -> {
                return FORWARD;
            }
            case Enter -> {
                return ENTER;
            }
            case Character -> {
                return PAUSE;
            }
            case EOF -> {
                isConsoleClosed = true;
                return 0;
            }
            default -> {
                return 0;
            }
        }
    }
}

