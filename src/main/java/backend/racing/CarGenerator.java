package backend.racing;

import java.awt.*;

import static backend.racing.GameUtils.CAR_HEIGHT;
import static backend.racing.GameUtils.CAR_WIDTH;

public class CarGenerator {

    public static Car generatePlayerCar() {
        Point[][] body = new Point[CAR_HEIGHT][CAR_WIDTH];
        generateParts(body);
        setPlayerCoordinates(body);
        return new Car(body, 1);
    }

    public static Car generateRivalCar() {
        Point[][] body = new Point[CAR_HEIGHT][CAR_WIDTH];
        generateParts(body);
        setRivalCoordinates(body);
        return new Car(body, -1);
    }

    public static void setPlayerCoordinates(Point[][] body) {
        int x, y = 14;
        for (int i = 1; i <= body.length; i++) {
            x = i % 2 == 0 ? CAR_WIDTH : CAR_WIDTH + 1;
            for (int j = 0; j < body[i - 1].length; j++) {
                if (body[i - 1][j] != null) {
                    body[i - 1][j].move(x, y);
                    x++;
                }
            }
            y++;
        }
    }

    public static void setRivalCoordinates(Point[][] body) {
        int y = -5;
        for (Point[] points : body) {
            for (Point point : points) {
                if (point == null) {
                    continue;
                }
                point.move(-1, y);
            }
            y++;
        }
    }

    private static void generateParts(Point[][] body) {
        for (int i = 1; i <= body.length; i++) {
            if (i % 2 != 0) {
                body[i - 1][1] = new Point();
            } else {
                for (int j = 0; j < body[i - 1].length; j++) {
                    body[i - 1][j] = new Point();
                }
            }
        }
    }
}
