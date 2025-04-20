package backend.racing;

public class GameUtils {

    public static final String FILE_PATH = "src/main/resources/racingScore.txt";

    public static final int GAME_FIELD_WIDTH = 10;
    public static final int GAME_FIELD_HEIGHT = 20;
    public static final int SCORE_FIELD_WIDTH = GAME_FIELD_WIDTH * 2 + 2;
    public static final int SCORE_FIELD_HEIGHT = GAME_FIELD_HEIGHT + 2;
    public static final int DOT_WIDTH = 20;
    public static final int DOT_HEIGHT = 20;

    public static final int CAR_WIDTH = 3;
    public static final int CAR_HEIGHT = 5;
    public static final int NUMBER_OF_RIVALS = 2;
    public static final int CAR_DISTANCE = 10;
    public static final int START_SPEED = 300;
    public static final int BOOST_SPEED = 75;
    public static final int SPEED_UP = 20;
    public static final int WIN_SCORE = 200;

    public static final int LEFT = 1;
    public static final int RIGHT = 2;
    public static final int FORWARD = 3;
    public static final int PAUSE = 4;
    public static final int ENTER = 5;

    public static final char CAR_PART = '*';

}
