package backend.server;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

@Controller
@RequestMapping("/game")
public class GameController {

    @GetMapping
    public String gameSelection() {
        return "start-page";
    }

    @GetMapping("/web")
    public String launchWebGame(@RequestParam("gameType") String gameType, Model model) {
        model.addAttribute("gameType", gameType);
        return "game-page";
    }
}
