package backend.racing;

import java.awt.*;

public class Car {

    private final Point[][] body;
    private boolean isFree;
    private int roadLane;

    public Car(Point[][] body, int roadLine) {
        this.body = body;
        isFree = true;
        this.roadLane = roadLine;
    }

    public Point[][] getBody() {
        return body;
    }

    public boolean isFree() {
        return isFree;
    }

    public void setFree(boolean free) {
        isFree = free;
    }

    public int getRoadLane() {
        return roadLane;
    }

    public void setRoadLane(int roadLane) {
        this.roadLane = roadLane;
    }

}
