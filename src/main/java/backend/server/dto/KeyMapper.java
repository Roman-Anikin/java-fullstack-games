package backend.server.dto;

import static backend.racing.GameUtils.*;

public class KeyMapper {

    public static int mapKeys(String key) {
        key = key.substring(8, key.length() - 2);
        return switch (key) {
            case "Enter" -> ENTER;
            case " " -> PAUSE;
            case "ArrowUp" -> FORWARD;
            case "ArrowLeft" -> LEFT;
            case "ArrowRight" -> RIGHT;
            default -> 0;
        };
    }
}
