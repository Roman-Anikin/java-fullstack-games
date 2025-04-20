package backend.server.dto;

import backend.racing.Model;
import org.springframework.stereotype.Component;

@Component
public class RacingMapper {

    public RacingDto toDto(Model model) {
        return new RacingDto(model.getPlayerCar(),
                model.getRivalCars(),
                model.getSpeed(),
                model.getScore(),
                model.getHighestScore(),
                model.getLevel(),
                model.getStatus());
    }
}
