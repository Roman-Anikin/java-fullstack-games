package backend.server.dto;

import backend.racing.Car;
import backend.racing.GameStatus;

import java.util.List;

public record RacingDto(Car playerCar,
                        List<Car> rivalCars,
                        int speed,
                        int score,
                        int highestScore,
                        int level,
                        GameStatus status) {
}
