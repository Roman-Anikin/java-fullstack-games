package ui.gui;

import backend.racing.Car;
import backend.racing.GameStatus;
import backend.racing.Model;
import backend.racing.PressedKey;
import javafx.application.Platform;
import javafx.event.EventHandler;
import javafx.scene.Group;
import javafx.scene.Scene;
import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.image.Image;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.stage.Stage;

import java.io.InputStream;
import java.util.List;

import static backend.racing.GameUtils.*;

public class GuiRacing implements EventHandler<KeyEvent> {

    private final GraphicsContext gc;
    private final PressedKey key;
    private boolean isWindowClose;
    private final Image blackCar;
    private final Image redCar;
    private final Image greyCar;

    public GuiRacing(Stage stage, PressedKey key) {
        Canvas canvas = new Canvas(GAME_FIELD_WIDTH * DOT_WIDTH + SCORE_FIELD_WIDTH * 6,
                GAME_FIELD_HEIGHT * DOT_HEIGHT);
        gc = canvas.getGraphicsContext2D();
        this.key = key;
        Group group = new Group();
        group.getChildren().add(canvas);
        Scene scene = new Scene(group);
        scene.setOnKeyPressed(this);
        stage.setScene(scene);
        stage.setTitle("Racing");
        stage.setResizable(false);
        stage.setOnCloseRequest(windowEvent -> {
            isWindowClose = true;
            Platform.exit();
        });
        blackCar = loadImage("black");
        redCar = loadImage("red");
        greyCar = loadImage("grey");
        stage.show();
    }

    public boolean isWindowClose() {
        return isWindowClose;
    }

    public void updateView(Model model) {
        gc.clearRect(0, 0, GAME_FIELD_WIDTH * DOT_WIDTH + SCORE_FIELD_WIDTH * 6,
                GAME_FIELD_HEIGHT * DOT_HEIGHT);
        drawGameField();
        drawScoreField(model);
        if (model.getStatus() != GameStatus.IN_GAME) {
            drawMessage(model.getStatus());
        } else {
            drawCar(model.getPlayerCar(), blackCar);
            drawRivalCars(model.getRivalCars());
        }
    }

    @Override
    public void handle(KeyEvent keyEvent) {
        key.setKeyCode(mapKeys(keyEvent.getCode()));
    }

    private Image loadImage(String color) {
        InputStream inputStream = getClass()
                .getClassLoader()
                .getResourceAsStream("resources/images/" + color + ".png");
        if (inputStream == null) {
            throw new RuntimeException("Png not found");
        }
        return new Image(inputStream);
    }

    private void drawMessage(GameStatus status) {
        String message = "";
        if (status == GameStatus.WIN) {
            message = "You Win!";
        } else if (status == GameStatus.LOOSE) {
            message = "Game Over";
        }
        gc.setFont(new Font(18));
        gc.fillText(message, DOT_WIDTH * 3,
                (double) (GAME_FIELD_HEIGHT * DOT_HEIGHT) / 2 - DOT_HEIGHT * 2);
        gc.fillText("Press ENTER To Start", DOT_WIDTH,
                (double) (GAME_FIELD_HEIGHT * DOT_HEIGHT) / 2 - DOT_HEIGHT);
    }

    private void drawGameField() {
        gc.setFill(Color.LIGHTGRAY);
        gc.fillRect(0, 0, GAME_FIELD_WIDTH * DOT_WIDTH, GAME_FIELD_HEIGHT * DOT_HEIGHT);
        gc.setStroke(Color.BLACK);
        gc.setLineWidth(2);
        gc.strokeRect(0, 0, GAME_FIELD_WIDTH * DOT_WIDTH, GAME_FIELD_HEIGHT * DOT_HEIGHT);
    }

    private void drawScoreField(Model model) {
        gc.setFill(Color.BLACK);
        gc.setFont(new Font(14));
        gc.fillText("SCORE", GAME_FIELD_WIDTH * DOT_WIDTH + DOT_WIDTH * 2, DOT_HEIGHT * 3);
        gc.fillText(String.valueOf(model.getScore()),
                GAME_FIELD_WIDTH * DOT_WIDTH + DOT_WIDTH * 3, DOT_HEIGHT * 4);
        gc.fillText("HIGHEST SCORE", GAME_FIELD_WIDTH * DOT_WIDTH + 10, DOT_HEIGHT * 6);
        gc.fillText(String.valueOf(model.getHighestScore()),
                GAME_FIELD_WIDTH * DOT_WIDTH + DOT_WIDTH * 3, DOT_HEIGHT * 7);
        gc.fillText("LEVEL", GAME_FIELD_WIDTH * DOT_WIDTH + DOT_WIDTH * 2, DOT_HEIGHT * 9);
        gc.fillText(String.valueOf(model.getLevel()),
                GAME_FIELD_WIDTH * DOT_WIDTH + DOT_WIDTH * 3, DOT_HEIGHT * 10);
    }

    private void drawCar(Car car, Image carImage) {
        gc.drawImage(carImage, (car.getBody()[0][1].x - 1) * DOT_WIDTH, car.getBody()[0][1].y * DOT_HEIGHT,
                CAR_WIDTH * DOT_WIDTH, CAR_HEIGHT * DOT_HEIGHT);
    }

    private void drawRivalCars(List<Car> cars) {
        int color = 0;
        for (Car car : cars) {
            switch (color) {
                case 0:
                    drawCar(car, redCar);
                    break;
                case 1:
                    drawCar(car, greyCar);
            }
            color++;
        }
    }

    private int mapKeys(KeyCode key) {
        if (key == null) {
            return 0;
        }
        switch (key) {
            case LEFT -> {
                return LEFT;
            }
            case RIGHT -> {
                return RIGHT;
            }
            case UP -> {
                return FORWARD;
            }
            case ENTER -> {
                return ENTER;
            }
            case SPACE -> {
                return PAUSE;
            }
            default -> {
                return 0;
            }
        }
    }
}
