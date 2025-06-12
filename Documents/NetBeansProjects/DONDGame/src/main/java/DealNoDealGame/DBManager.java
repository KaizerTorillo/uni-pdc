/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package DealNoDealGame;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;

/**
 *
 * @author Kaizer T
 */
public class DBManager {
    private static String url = "jdbc:derby:C:\\Users\\Kaizer T\\AppData\\Roaming\\NetBeans\\23\\derby\\DONDGameDB;";
    private static String username = "pdc";
    private static String password = "pdc";
    public static void saveResult(String name, double finalAmount, double bankerOffer, boolean tookDeal)
    {
        String sql = "INSERT INTO results(name, final_amount, banker_offer, took_deal) VALUES (?, ?, ?, ?)";
        try (Connection c = DriverManager.getConnection(url, username, password);
            PreparedStatement p = c.prepareStatement(sql))
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
    
    public void DBConnection()
    {
        Connection connection = null;
        try {
            Class.forName("org.apache.derby.jdbc.EmbeddedDriver");
            connection = DriverManager.getConnection(url, username, password);
            System.out.println("database connected");
        } catch (Exception e) {
            System.out.println("connection error");
        }
    }
}
