
package DealNoDealGame;

import java.io.File;
import java.sql.Connection;
import java.sql.DatabaseMetaData;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.sql.ResultSetMetaData;


public class GameDB {
    private static String url = "jdbc:derby:gamedb;";
    private static String urln = "jdbc:derby:gamedb;create=true";
    private static String username = "pdc";
    private static String password = "pdc";
    private static String dbname = "gamedb";
    
        
    public static void dbCheck()
    {
        boolean dbExist = new File(dbname).exists();
        File file = new File(urln);
        File file2 = new File(url);
        String path = file.getAbsolutePath();
        String path2 = file2.getAbsolutePath(); //helps with finding out where the db is located in 
        Connection conn = null;
        try {
            Class.forName("org.apache.derby.jdbc.EmbeddedDriver");
            System.setProperty("derby.language.sequence.preallocator", "1");
        } catch (ClassNotFoundException ex) {
            Logger.getLogger(GameDB.class.getName()).log(Level.SEVERE, null, ex);
        }
        try
        {
            
            if(!dbExist)
            {
                conn = DriverManager.getConnection(urln);
                System.setProperty("derby.language.sequence.preallocator", "1");//increments ID value by 1 instead of 100
                createTables(conn);
                System.out.println("Database created in " + path);
            }
            else
            {
                conn = DriverManager.getConnection(url);
                System.setProperty("derby.language.sequence.preallocator", "1");//increments ID value by 1 instead of 100
                System.out.println("Database exists in " + path2);
                if(!hasTables(conn))
                {
                    createTables(conn);
                }
                else{
                System.out.println("Tables exist");
                }
            }
        } catch (SQLException e)
        {
            System.out.println("Error initialising DB" + e.getMessage()); 
        }

    }
    
    private static void createTables(Connection co) throws SQLException
    {
        try(Statement s = co.createStatement())
        {
            //s.executeUpdate("""
            //                DROP TABLE""");
            //System.out.println("Table Dropped");
            s.executeUpdate("""
                CREATE TABLE results 
                    (id INT PRIMARY KEY GENERATED ALWAYS AS IDENTITY, 
                    name VARCHAR(100), final_amount DOUBLE, banker_offer DOUBLE, 
                    took_deal BOOLEAN)""");
            System.out.println("Table created");
        }
    }
    
    public static void dbConnection()
    {
        Connection conn = null;

        try {
            Class.forName("org.apache.derby.jdbc.EmbeddedDriver");
            conn = DriverManager.getConnection(url, username, password);
            System.setProperty("derby.language.sequence.preallocator", "1");//increments ID value by 1 instead of 100
            System.out.println("Database connected");
        } catch (ClassNotFoundException ex) {
            Logger.getLogger(GameDB.class.getName()).log(Level.SEVERE, null, ex);
        } catch (SQLException ex) {
            Logger.getLogger(GameDB.class.getName()).log(Level.SEVERE, null, ex);
        }
        
    }
    public static boolean hasTables(Connection co) throws SQLException
    {
        DatabaseMetaData m = co.getMetaData();
        ResultSet rs = m.getTables(null,null,null,new String[]{"TABLE"});
        boolean has = rs.next();
        return has;
    }
    
    public static void saveResult(String name, double finalAmount, double bankerOffer, boolean tookDeal)
    {
        try {
            Class.forName("org.apache.derby.jdbc.EmbeddedDriver");
            System.out.println("Embedded driver found");
        } catch (ClassNotFoundException ex) {
            System.out.println("Issue with driver");
            Logger.getLogger(GameDB.class.getName()).log(Level.SEVERE, null, ex);
        }

        String sql = "INSERT INTO APP.results(name, final_amount, banker_offer, took_deal) VALUES (?, ?, ?, ?)";
        try (Connection co = DriverManager.getConnection(url, username, password);
            PreparedStatement p = co.prepareStatement(sql))
        {
            p.setString(1, name);
            p.setDouble(2, finalAmount);
            p.setDouble(3, bankerOffer);
            p.setBoolean(4, tookDeal);
            
            p.executeUpdate();
            System.out.println("Results saved to Database.");
            
        } catch (Exception e)
        {
            System.out.println("There was an error saving results to Database." + e.getMessage());
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
            
    

