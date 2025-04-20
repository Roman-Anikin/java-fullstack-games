package backend.server;

import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Mono;

@RestController
@RequestMapping("/game/snake")
public class SnakeProxyController {

    private final WebClient snakeClient;

    public SnakeProxyController() {
        snakeClient = WebClient.builder()
                .baseUrl("http://localhost:8081/game/snake")
                .build();
    }

    @PostMapping
    public Void startSnake(@RequestParam("mode") String mode) {
        return snakeClient.post()
                .uri(uriBuilder ->
                        uriBuilder.queryParam("mode", mode).build())
                .retrieve()
                .bodyToMono(Void.class)
                .block();
    }

    @PostMapping("/action")
    public Mono<Void> handleInput(@RequestBody String key) {
        return snakeClient.post()
                .uri("/action")
                .contentType(MediaType.APPLICATION_JSON)
                .bodyValue(key)
                .retrieve()
                .bodyToMono(Void.class);
    }

    @PatchMapping("/move")
    public Mono<Void> moveSnake() {
        return snakeClient.patch()
                .uri("/move")
                .retrieve()
                .bodyToMono(Void.class);
    }

    @GetMapping("/state")
    public Mono<String> updateState() {
        return snakeClient.get()
                .uri("/state")
                .retrieve()
                .bodyToMono(String.class);
    }
}
