import backend.racing.Car;
import backend.racing.CarGenerator;
import org.junit.jupiter.api.Test;

import java.awt.*;

import static org.junit.jupiter.api.Assertions.*;

public class CarGeneratorTest {

    @Test
    void generatePlayerCar() {
        Car car = CarGenerator.generatePlayerCar();
        assertNotNull(car);
        assertEquals(car.getRoadLane(), 1);
        assertNotNull(car.getBody());
        assertEquals(car.getBody().length, 5);
        assertEquals(car.getBody()[0].length, 3);

        for (int i = 0; i < car.getBody().length; i++) {
            for (int j = 0; j < car.getBody()[i].length; j++) {
                if (i % 2 != 0) {
                    assertNotNull(car.getBody()[i][j]);
                } else {
                    if (j % 2 == 0) {
                        assertNull(car.getBody()[i][j]);
                    } else {
                        assertNotNull(car.getBody()[i][j]);
                    }
                }
            }
        }
    }

    @Test
    void generatePlayerCarPosition() {
        Car car = CarGenerator.generatePlayerCar();
        int y = 14;
        for (int i = 0; i < car.getBody().length; i++) {
            if (i % 2 != 0) {
                assertArrayEquals(car.getBody()[i],
                        new Point[]{new Point(3, y), new Point(4, y), new Point(5, y)});

            } else {
                assertArrayEquals(car.getBody()[i],
                        new Point[]{null, new Point(4, y), null});
            }
            y++;
        }
    }

    @Test
    void generateRivalCar() {
        Car car = CarGenerator.generateRivalCar();
        assertNotNull(car);
        assertEquals(car.getRoadLane(), -1);
        assertTrue(car.isFree());
        assertNotNull(car.getBody());
        assertEquals(car.getBody().length, 5);
        assertEquals(car.getBody()[0].length, 3);

        for (int i = 0; i < car.getBody().length; i++) {
            for (int j = 0; j < car.getBody()[i].length; j++) {
                if (i % 2 != 0) {
                    assertNotNull(car.getBody()[i][j]);
                } else {
                    if (j % 2 == 0) {
                        assertNull(car.getBody()[i][j]);
                    } else {
                        assertNotNull(car.getBody()[i][j]);
                    }
                }
            }
        }
    }

    @Test
    void generateRivalCarPosition() {
        Car car = CarGenerator.generateRivalCar();
        int y = -5;
        for (int i = 0; i < car.getBody().length; i++) {
            if (i % 2 != 0) {
                assertArrayEquals(car.getBody()[i],
                        new Point[]{new Point(-1, y), new Point(-1, y), new Point(-1, y)});

            } else {
                assertArrayEquals(car.getBody()[i],
                        new Point[]{null, new Point(-1, y), null});
            }
            y++;
        }
    }
}
