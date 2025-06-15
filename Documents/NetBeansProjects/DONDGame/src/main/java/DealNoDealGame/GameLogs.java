package DealNoDealGame;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.nio.file.Path;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;
import java.sql.Statement;

public class GameLogs {
    
    private static String url = "jdbc:derby:gamedb;";
    private static String urln = "jdbc:derby:gamedb;create=true";
    private static String username = "pdc";
    private static String password = "pdc";
    private static String dbname = "gamedb";
    
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
    
    public static void printResultsTable() throws ClassNotFoundException {
        File file = new File (url);
        String path = file.getPath();
        

        try (Connection conn = DriverManager.getConnection(path);
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery("SELECT * FROM APP.results")) {
            Class.forName("org.apache.derby.jdbc.EmbeddedDriver");
            ResultSetMetaData meta = rs.getMetaData();
            int columnCount = meta.getColumnCount();

            // Print column headers
            for (int i = 1; i <= columnCount; i++) {
                System.out.printf("%-20s", meta.getColumnName(i));
            }
            System.out.println("\n" + "-".repeat(columnCount * 20));

            // Print rows
            while (rs.next()) {
                for (int i = 1; i <= columnCount; i++) {
                    Object value = rs.getObject(i);
                    System.out.printf("%-20s", value);
                }
                System.out.println();
            }

        } catch (SQLException e) {
            System.out.println("Error displaying results table: " + e.getMessage());
        }
    }
}
