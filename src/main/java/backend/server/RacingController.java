package backend.server;

import backend.racing.Model;
import backend.server.dto.KeyMapper;
import backend.server.dto.RacingDto;
import backend.server.dto.RacingMapper;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/game/racing")
public class RacingController {

    private final RacingInitializer initializer;
    private final Model model;
    private final RacingMapper mapper;

    public RacingController(RacingInitializer initializer, Model model, RacingMapper mapper) {
        this.initializer = initializer;
        this.model = model;
        this.mapper = mapper;
    }

    @PostMapping
    public void startRacing(@RequestParam("mode") String mode) {
        initializer.startRacing(mode);
    }

    @PostMapping("/action")
    public void handleInput(@RequestBody String key) {
        model.handleInput(KeyMapper.mapKeys(key));
    }

    @PatchMapping("/move")
    public void moveCars() {
        model.moveCars();
    }

    @GetMapping("/state")
    public RacingDto updateState() {
        return mapper.toDto(model);
    }
}
