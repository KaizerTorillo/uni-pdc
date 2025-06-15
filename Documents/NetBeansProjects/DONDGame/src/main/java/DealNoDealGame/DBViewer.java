
package DealNoDealGame;

import java.io.File;
import java.sql.*;

public class DBViewer {

    public static void printResultsTable() throws ClassNotFoundException {
        String dbUrl = "jdbc:derby:gamedb;";
        File file = new File (dbUrl);
        String path = file.getAbsolutePath();
        

        try (Connection conn = DriverManager.getConnection(path);//connects to the DB. 
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery("SELECT * FROM APP.results")) {//had to specify the schema
            Class.forName("org.apache.derby.jdbc.EmbeddedDriver");
            ResultSetMetaData meta = rs.getMetaData();
            int columnCount = meta.getColumnCount();

            
            for (int i = 1; i <= columnCount; i++) {// print column headers
                System.out.printf("%-20s", meta.getColumnName(i));
            }
            System.out.println("\n" + "-".repeat(columnCount * 20));

            
            while (rs.next()) {
                for (int i = 1; i <= columnCount; i++) {// print rows
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

