package DealNoDealGame;

import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;
import java.nio.file.Path;

public class GameLogs {

    private Path logPath;

    public GameLogs(String filename) {
        this.logPath = Path.of(filename);
    }

    public void outputFile(String output) {
        try (BufferedWriter bw = new BufferedWriter(new FileWriter(logPath.toFile(), true))) {
            bw.write(output);
            //visible help for when idk where the file is
            System.out.println("Log successful. Location in " + logPath.toAbsolutePath());
        } catch (IOException e) {
            System.out.println("File could no be written. Please check error message:" + e.getMessage());
        }
    }
}
