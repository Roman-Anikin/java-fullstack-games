package backend.racing;

import org.springframework.stereotype.Component;

import java.awt.*;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import static backend.racing.GameUtils.*;

@Component
public class Model {

    private final List<Car> rivalCars;
    private Car playerCar;
    private int speed;
    private int score;
    private int highestScore;
    private int level;
    private GameStatus status;
    private boolean isPaused;
    private boolean isSpeedBoosted;

    public Model() {
        rivalCars = new ArrayList<>();
        for (int i = 0; i < NUMBER_OF_RIVALS; i++) {
            rivalCars.add(CarGenerator.generateRivalCar());
        }
        speed = START_SPEED;
    }

    public Car getPlayerCar() {
        return playerCar;
    }

    public List<Car> getRivalCars() {
        return rivalCars;
    }

    public int getSpeed() {
        return speed;
    }

    public int getScore() {
        return score;
    }

    public int getHighestScore() {
        return highestScore;
    }

    public int getLevel() {
        return level;
    }

    public GameStatus getStatus() {
        return status;
    }

    public void moveCars() {
        if (isPaused) {
            return;
        }
        for (Car rivalCar : rivalCars) {
            if (rivalCar.isFree()) {
                continue;
            }
            Point[][] carBody = rivalCar.getBody();
            for (Point[] carParts : carBody) {
                for (Point carPart : carParts) {
                    if (carPart == null) {
                        continue;
                    }
                    carPart.y++;
                }
            }
            checkCollision();
            if (carBody[0][1].y == CAR_DISTANCE + 1) {
                addRivalCarToRoad();
            }
            if (carBody[0][1].y > GAME_FIELD_HEIGHT) {
                rivalCar.setFree(true);
                CarGenerator.setRivalCoordinates(carBody);
            }
            if (carBody[0][1].y == GAME_FIELD_HEIGHT - 1) {
                increaseScore();
            }
        }
    }

    public void handleInput(int key) {
        if (key == ENTER && status != GameStatus.IN_GAME) {
            init();
            return;
        }
        if (status != GameStatus.IN_GAME) {
            return;
        }
        if (key == PAUSE) {
            isPaused = !isPaused;
            return;
        }
        if (key == LEFT && playerCar.getRoadLane() != 0 && !isPaused) {
            playerCar.setRoadLane(playerCar.getRoadLane() - 1);
            changeRoadLane(playerCar, playerCar.getRoadLane());
        } else if (key == RIGHT && playerCar.getRoadLane() != 2 && !isPaused) {
            playerCar.setRoadLane(playerCar.getRoadLane() + 1);
            changeRoadLane(playerCar, playerCar.getRoadLane());
        } else if (key == FORWARD && !isPaused) {
            if (isSpeedBoosted) {
                speed += BOOST_SPEED;
            } else {
                speed -= BOOST_SPEED;
            }
            isSpeedBoosted = !isSpeedBoosted;
        }
        checkCollision();
    }

    private void init() {
        playerCar = CarGenerator.generatePlayerCar();
        speed = START_SPEED;
        score = 0;
        highestScore = readScore();
        level = 0;
        status = GameStatus.IN_GAME;
        addRivalCarToRoad();
    }

    private void addRivalCarToRoad() {
        Car rival = rivalCars.stream()
                .filter(Car::isFree)
                .findFirst()
                .orElseThrow(RuntimeException::new);
        rival.setFree(false);
        rival.setRoadLane(new Random().nextInt(3));
        changeRoadLane(rival, rival.getRoadLane());
    }

    private void changeRoadLane(Car car, int line) {
        int x;
        for (int i = 1; i <= car.getBody().length; i++) {
            x = i % 2 == 0 ? line * 3 : line * 3 + 1;
            for (int j = 0; j < car.getBody()[i - 1].length; j++) {
                if (car.getBody()[i - 1][j] != null) {
                    car.getBody()[i - 1][j].x = x;
                    x++;
                }
            }
        }
    }

    private void checkCollision() {
        for (Car rival : rivalCars) {
            for (int i = 0; i < playerCar.getBody().length; i++) {
                for (int j = 0; j < playerCar.getBody().length; j++) {
                    if (checkFrontCollision(rival) || checkRearCollision(rival, i, j)) {
                        gameOver();
                        return;
                    }
                }
            }
        }
    }

    private boolean checkFrontCollision(Car rival) {
        return playerCar.getBody()[0][CAR_WIDTH / 2].equals(rival.getBody()[CAR_HEIGHT - 1][CAR_WIDTH / 2]);
    }

    private boolean checkRearCollision(Car rival, int i, int j) {
        return playerCar.getBody()[i][CAR_WIDTH / 2].equals(rival.getBody()[j][CAR_WIDTH / 2]);
    }

    private void gameOver() {
        for (Car rivalCar : rivalCars) {
            CarGenerator.setRivalCoordinates(rivalCar.getBody());
            rivalCar.setFree(true);
        }
        status = score == WIN_SCORE ? GameStatus.WIN : GameStatus.LOOSE;
    }

    private int readScore() {
        int score = 0;
        try {
            score = Integer.parseInt(Files.readString(Path.of(FILE_PATH)));
        } catch (IOException e) {
            System.out.println(e.getMessage());
        }
        return score;
    }

    private void saveScore() {
        try {
            Files.writeString(Path.of(FILE_PATH), String.valueOf(highestScore));
        } catch (IOException e) {
            System.out.println(e.getMessage());
        }
    }

    private void increaseScore() {
        score++;
        if (score % 5 == 0 && level < 10) {
            level++;
            speed -= SPEED_UP;
        }
        if (highestScore < score) {
            highestScore = score;
        }
        if (score == WIN_SCORE) {
            gameOver();
        }
        saveScore();
    }
}
