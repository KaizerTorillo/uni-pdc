
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

/**
 *
 * @author Kaizer T
 */
public class DBManager {
    private static String url = "jdbc:derby:C:\\Users\\Kaizer T\\AppData\\Roaming\\NetBeans\\23\\derby\\DONDGameDB;";
    private static String dbname = "DONDGameDB";
    private static String dburlexist = "jdbc:derby:DONDGameDB";
    private static final String dburl = "jdbc:derby:DONDGameDB;create=true";
    private static String username = "pdc";
    private static String password = "pdc";
    
    public static void checkDB()
    {
        boolean dbExist = new File(dbname).exists();
        try {
            Class.forName("org.apache.derby.jdbc.EmbeddedDriver");
        } catch (ClassNotFoundException ex) {
            Logger.getLogger(DBManager.class.getName()).log(Level.SEVERE, null, ex);
        }
        try(Connection co = DriverManager.getConnection(dburl))
        {
            if(!dbExist)
            {
                System.out.println("Database created");
                createTables(co);
            }
            else
            {
                System.out.println("Database exists");
                if(!hasTables(co))
                {
                    createTables(co);
                }
                else{
                System.out.println("Tables exist");
                }
            }
        }
        catch (SQLException e)
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
            Logger.getLogger(DBManager.class.getName()).log(Level.SEVERE, null, ex);
        }

        String sql = "INSERT INTO results(name, final_amount, banker_offer, took_deal) VALUES (?, ?, ?, ?)";
        try (Connection co = DriverManager.getConnection(dburlexist, username, password);
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
    
    public static void dbConnection()
    {
        Connection connection = null;
        try {
            Class.forName("org.apache.derby.jdbc.EmbeddedDriver");
            connection = DriverManager.getConnection(dburlexist, username, password);
            System.out.println("database connected");
        } catch (Exception e) {
            System.out.println("connection error");
        }
    }
}
