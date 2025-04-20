import backend.racing.Car;
import backend.racing.Model;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;

import static backend.racing.GameStatus.*;
import static backend.racing.GameUtils.*;
import static org.junit.jupiter.api.Assertions.*;

class ModelTests {

    private Model model;

    @BeforeEach
    void cleanSave() {
        model = new Model();
        try {
            Files.writeString(Path.of(FILE_PATH), String.valueOf(0));
        } catch (IOException e) {
            System.out.println(e.getMessage());
        }
    }

    @Test
    void modelCreation() {
        assertNotNull(model);
        assertNull(model.getPlayerCar());
        assertNotNull(model.getRivalCars());
        assertEquals(model.getRivalCars().size(), 2);
        assertEquals(model.getSpeed(), 300);
        assertEquals(model.getScore(), 0);
        assertEquals(model.getHighestScore(), 0);
        assertEquals(model.getLevel(), 0);
        assertNull(model.getStatus());
    }

    @Test
    void racingInitialization() {
        model.handleInput(ENTER);
        assertNotNull(model.getPlayerCar());
        assertEquals(model.getSpeed(), 300);
        assertEquals(model.getScore(), 0);
        assertEquals(model.getHighestScore(), 0);
        assertEquals(model.getLevel(), 0);
        assertEquals(model.getStatus(), IN_GAME);

        assertFalse(model.getRivalCars().get(0).isFree());
        assertTrue(model.getRivalCars().get(1).isFree());
        Car rivalCar = model.getRivalCars().get(0);

        assertNotEquals(rivalCar.getRoadLane(), -1);
        int y = -5;
        for (int i = 0; i < rivalCar.getBody().length; i++) {
            for (int j = 0; j < rivalCar.getBody()[i].length; j++) {
                if (rivalCar.getBody()[i][j] == null) {
                    continue;
                }
                assertNotEquals(rivalCar.getBody()[i][j].x, -1);
                assertEquals(rivalCar.getBody()[i][j].y, y);
            }
            y++;
        }
    }

    @Test
    void handleInputWhenStatusNotInGame() {
        assertNull(model.getStatus());
        model.handleInput(LEFT);
        assertNull(model.getPlayerCar());
        assertNull(model.getStatus());
    }

    @Test
    void handlePause() {
        model.handleInput(ENTER);
        model.moveCars();
        assertEquals(model.getRivalCars().get(0).getBody()[4][1].y, 0);

        model.handleInput(PAUSE);
        model.moveCars();
        assertEquals(model.getRivalCars().get(0).getBody()[4][1].y, 0);

        model.handleInput(PAUSE);
        model.moveCars();
        assertEquals(model.getRivalCars().get(0).getBody()[4][1].y, 1);
    }

    @Test
    void handleLeftWhenPlayerCarOnFirstLine() {
        model.handleInput(ENTER);

        Car car = model.getPlayerCar();
        assertEquals(car.getRoadLane(), 1);

        model.handleInput(LEFT);
        assertEquals(car.getRoadLane(), 0);

        model.handleInput(LEFT);
        assertEquals(car.getRoadLane(), 0);
        assertEquals(car.getBody()[1][0].x, 0);
        assertEquals(car.getBody()[3][0].x, 0);
    }

    @Test
    void handleLeftWhenPlayerCarOnSecondLine() {
        model.handleInput(ENTER);

        Car car = model.getPlayerCar();
        assertEquals(car.getRoadLane(), 1);

        model.handleInput(LEFT);
        assertEquals(car.getRoadLane(), 0);
        assertEquals(car.getBody()[1][0].x, 0);
        assertEquals(car.getBody()[3][0].x, 0);
    }

    @Test
    void handleLeftWhenPlayerCarOnThirdLine() {
        model.handleInput(ENTER);

        Car car = model.getPlayerCar();
        assertEquals(car.getRoadLane(), 1);

        model.handleInput(RIGHT);
        assertEquals(car.getRoadLane(), 2);

        model.handleInput(LEFT);
        assertEquals(car.getRoadLane(), 1);
        assertEquals(car.getBody()[1][0].x, 3);
        assertEquals(car.getBody()[3][0].x, 3);
    }

    @Test
    void handleLeftWhenPause() {
        model.handleInput(ENTER);

        Car car = model.getPlayerCar();
        assertEquals(car.getRoadLane(), 1);

        model.handleInput(PAUSE);
        model.handleInput(LEFT);

        assertEquals(car.getRoadLane(), 1);
        assertEquals(car.getBody()[1][0].x, 3);
        assertEquals(car.getBody()[3][0].x, 3);
    }

    @Test
    void handleRightWhenPlayerCarOnFirstLine() {
        model.handleInput(ENTER);

        Car car = model.getPlayerCar();
        assertEquals(car.getRoadLane(), 1);

        model.handleInput(LEFT);
        assertEquals(car.getRoadLane(), 0);

        model.handleInput(RIGHT);
        assertEquals(car.getRoadLane(), 1);
        assertEquals(car.getBody()[1][0].x, 3);
        assertEquals(car.getBody()[3][0].x, 3);
    }

    @Test
    void handleRightWhenPlayerCarOnSecondLine() {
        model.handleInput(ENTER);

        Car car = model.getPlayerCar();
        assertEquals(car.getRoadLane(), 1);

        model.handleInput(RIGHT);
        assertEquals(car.getRoadLane(), 2);
        assertEquals(car.getBody()[1][0].x, 6);
        assertEquals(car.getBody()[3][0].x, 6);
    }

    @Test
    void handleRightWhenPlayerCarOnThirdLine() {
        model.handleInput(ENTER);

        Car car = model.getPlayerCar();
        assertEquals(car.getRoadLane(), 1);

        model.handleInput(RIGHT);
        assertEquals(car.getRoadLane(), 2);

        model.handleInput(RIGHT);
        assertEquals(car.getRoadLane(), 2);
        assertEquals(car.getBody()[1][0].x, 6);
        assertEquals(car.getBody()[3][0].x, 6);
    }

    @Test
    void handleRightWhenPause() {
        model.handleInput(ENTER);

        Car car = model.getPlayerCar();
        assertEquals(car.getRoadLane(), 1);

        model.handleInput(PAUSE);
        model.handleInput(RIGHT);

        assertEquals(car.getRoadLane(), 1);
        assertEquals(car.getBody()[1][0].x, 3);
        assertEquals(car.getBody()[3][0].x, 3);
    }

    @Test
    void handleSpeedBoost() {
        model.handleInput(ENTER);

        model.handleInput(FORWARD);
        assertEquals(model.getSpeed(), START_SPEED - BOOST_SPEED);

        model.handleInput(FORWARD);
        assertEquals(model.getSpeed(), START_SPEED);
    }

    @Test
    void handleSpeedBoostWhenPause() {
        model.handleInput(ENTER);

        model.handleInput(PAUSE);
        model.handleInput(FORWARD);
        assertEquals(model.getSpeed(), START_SPEED);
    }

    @Test
    void moveRivalCars() {
        model.handleInput(ENTER);

        model.moveCars();
        model.moveCars();
        model.moveCars();

        assertEquals(model.getRivalCars().get(0).getBody()[4][1].y, 2);
    }

    @Test
    void addSecondCarToRoad() {
        model.handleInput(ENTER);

        for (int i = 0; i < 16; i++) {
            model.moveCars();
            changeRoadLine(model);
        }
        int secondCar;
        if (model.getRivalCars().get(0).isFree()) {
            secondCar = 0;
            assertEquals(model.getRivalCars().get(1).getBody()[0][1].y, CAR_DISTANCE + 1);
        } else {
            secondCar = 1;
            assertEquals(model.getRivalCars().get(0).getBody()[0][1].y, CAR_DISTANCE + 1);
        }
        assertFalse(model.getRivalCars().get(secondCar).isFree());
        assertEquals(model.getRivalCars().get(secondCar).getBody()[4][1].y, 0);
    }

    @Test
    void overtakeRivalCar() {
        model.handleInput(ENTER);

        model.moveCars();
        int carOnRoadIndex = model.getRivalCars().get(0).isFree() ? 1 : 0;
        for (int i = 0; i < GAME_FIELD_HEIGHT + CAR_HEIGHT; i++) {
            model.moveCars();
            changeRoadLine(model);
        }

        assertTrue(model.getRivalCars().get(carOnRoadIndex).isFree());
        assertEquals(model.getRivalCars().get(carOnRoadIndex).getBody()[0][1].y, -5);
        assertEquals(model.getRivalCars().get(carOnRoadIndex).getBody()[4][1].y, -1);

        assertEquals(model.getScore(), 1);
        assertEquals(model.getHighestScore(), 1);
    }

    @Test
    void increaseLevelDifficulty() {
        model.handleInput(ENTER);

        for (int i = 0; i < 90; i++) {
            model.moveCars();
            changeRoadLine(model);
        }

        assertEquals(model.getScore(), 5);
        assertEquals(model.getHighestScore(), 5);
        assertEquals(model.getLevel(), 1);
        assertEquals(model.getSpeed(), START_SPEED - SPEED_UP);
    }

    @Test
    void gameOverWithCollision() {
        model.handleInput(ENTER);
        model.moveCars();

        int carOnRoadIndex = model.getRivalCars().get(0).isFree() ? 1 : 0;
        if (model.getPlayerCar().getRoadLane() < model.getRivalCars().get(carOnRoadIndex).getRoadLane()) {
            for (int i = 0; i < model.getRivalCars().get(carOnRoadIndex).getRoadLane(); i++) {
                model.handleInput(RIGHT);
            }
        } else if (model.getPlayerCar().getRoadLane() > model.getRivalCars().get(carOnRoadIndex).getRoadLane()) {
            for (int i = 0; i < model.getPlayerCar().getRoadLane(); i++) {
                model.handleInput(LEFT);
            }
        }

        for (int i = 0; i < CAR_DISTANCE + CAR_HEIGHT - 1; i++) {
            model.moveCars();
        }

        for (Car car : model.getRivalCars()) {
            assertTrue(car.isFree());
            assertEquals(car.getBody()[0][1].y, -5);
            assertEquals(car.getBody()[4][1].y, -1);
        }
        assertEquals(model.getStatus(), LOOSE);
    }

    @Test
    void gameOverWithWin() {
        model.handleInput(ENTER);

        for (int i = 0; i < 3108; i++) {
            model.moveCars();
            changeRoadLine(model);
        }

        assertEquals(model.getScore(), WIN_SCORE);
        assertEquals(model.getHighestScore(), WIN_SCORE);
        assertEquals(model.getLevel(), 10);
        assertEquals(model.getSpeed(), START_SPEED - SPEED_UP * 10);
        assertEquals(model.getStatus(), WIN);
    }

    private void changeRoadLine(Model model) {
        Car car = model.getPlayerCar();
        for (Car rival : model.getRivalCars()) {
            if (car.getBody()[0][1].x == rival.getBody()[4][1].x &&
                    car.getBody()[0][1].y - 3 == rival.getBody()[4][1].y) {
                switch (car.getRoadLane()) {
                    case 0:
                        model.handleInput(RIGHT);
                        return;
                    case 1:
                        model.handleInput(LEFT);
                        return;
                    case 2:
                        model.handleInput(LEFT);
                }
            }
        }
    }
}
