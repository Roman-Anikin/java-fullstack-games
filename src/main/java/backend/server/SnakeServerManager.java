package backend.server;

import jakarta.annotation.PostConstruct;
import org.springframework.context.ApplicationListener;
import org.springframework.context.event.ContextClosedEvent;
import org.springframework.lang.NonNull;
import org.springframework.stereotype.Component;

import java.nio.file.Path;

@Component
public class SnakeServerManager implements ApplicationListener<ContextClosedEvent> {

    private Process snakeProcess;

    @PostConstruct
    public void snakeServerStart() {
        try {
            Path jarDir = Path.of(getClass()
                            .getProtectionDomain()
                            .getCodeSource()
                            .getLocation()
                            .toURI())
                    .getParent();

            Path exePath = jarDir.resolve("classes/resources/Snake.exe");

            ProcessBuilder processBuilder = new ProcessBuilder(exePath.toAbsolutePath().toString());
            snakeProcess = processBuilder.start();
            Runtime.getRuntime().addShutdownHook(new Thread(this::killProcess));
        } catch (Exception e) {
            System.out.println(e.getMessage());
        }
    }

    @Override
    public void onApplicationEvent(@NonNull ContextClosedEvent event) {
        killProcess();
    }

    private void killProcess() {
        if (snakeProcess != null && snakeProcess.isAlive()) {
            snakeProcess.destroyForcibly();
        }
    }
}
