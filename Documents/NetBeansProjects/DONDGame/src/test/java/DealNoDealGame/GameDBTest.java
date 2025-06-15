package DealNoDealGame;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import org.junit.AfterClass;
import org.junit.BeforeClass;
import org.junit.Test;
import static org.junit.Assert.*;

/**
 *
 * @author Kaizer T
 */
public class GameDBTest {
    private static final String url = "jdbc:derby:gamedb;create=true;";
    private static Connection conn;
    
    
    public GameDBTest() {
    }
    
    @BeforeClass
    public static void setUpClass() throws SQLException {
        conn = DriverManager.getConnection(url);
        GameDB.dbCheck();
    }
    
    @AfterClass
    public static void tearDownClass() throws SQLException {
        if (conn !=null && !conn.isClosed())
        {
            conn.close();
        }
    }
    
   
    @Test//confirm db creation/setup
    public void testDbCheck() {
        System.out.println("dbCheck");
        try {
            GameDB.dbCheck();
            System.out.println("Pass");
        } catch (Exception e)
        {
            fail("dbCheck() FAIL, error with db creation: " + e.getMessage());
        }

    }

    
    @Test//confirm db connetion method works
    public void testDbConnection() {
        System.out.println("dbConnection");
        try {
            GameDB.dbConnection();
            System.out.println("Pass");
        } catch (Exception e) {
            fail("dbConnection() FAIL, could not connect: " +e.getMessage());

        }
        // TODO review the generated test code and remove the default call to fail.
    }


    @Test//check if the results table exists
    public void testHasTables() throws Exception {
        try
        { 
            boolean has = GameDB.hasTables(conn);
            assertTrue("At least one table in the database has been found", has);
            System.out.println("Pass");
        } catch (Exception e)
        {
            fail("saveResult() FAIL, issue with saving result to db:" + e.getMessage());
        }
    }

    
    @Test//insert test record 
    public void testSaveResult() {
        System.out.println("saveResult");
        try
        {
            GameDB.saveResult("Test", 1500.0, 1000.0, true);
            
        } catch (Exception e)
        {
            fail("hasTables() FAIL, exception:" + e.getMessage());
        }
    }

    /**
     * Test of printResultsTable method, of class GameDB.
     */
    @org.junit.Test
    public void testPrintResultsTable() throws Exception {
        try
        {
            GameLogs.printResultsTable();//should print the test results to console
            System.out.println("Pass");
        } catch (Exception e)
        {
            fail("printRestultsTable() FAIL, threw an exception:" +e.getMessage());
        }
    }
    
}
