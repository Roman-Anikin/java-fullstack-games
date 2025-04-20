package backend.server;

import org.springframework.stereotype.Component;

import java.io.IOException;
import java.nio.file.Paths;
import java.util.Arrays;

@Component
public class RacingInitializer {

    public void startRacing(String mode) {
        String dependencyPath = Paths.get(System.getProperty("user.dir"),
                "target",
                "dependency").toString();
        String jarPath = Paths.get(System.getProperty("user.dir"),
                "target",
                "ArcadeGames-1.0.jar").toString();
        if (mode.equals("tui")) {
            dependencyPath = dependencyPath.concat("\\lanterna-3.1.2.jar");
            buildProcess("javaw",
                    "-cp", jarPath + ";" + dependencyPath,
                    "backend.racing.TuiLauncher");
        } else if (mode.equals("gui")) {
            buildProcess("java",
                    "--module-path", dependencyPath,
                    "--add-modules", "javafx.controls",
                    "-cp", jarPath,
                    "backend.racing.GuiLauncher");
        }
    }

    private void buildProcess(String... command) {
        try {
            ProcessBuilder pb = new ProcessBuilder(command);
            pb.inheritIO();
            Process process = pb.start();
            process.waitFor();
            if (process.isAlive()) {
                process.destroyForcibly();
            }
        } catch (IOException | InterruptedException e) {
            System.out.println(Arrays.toString(e.getStackTrace()));
        }
    }
}
