package DealNoDealGame;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class CaseMechanics {

    private static int numCases = 20; //maybe add option for user to change. 

    public static int getNumCases() //used in case numCases is changed 
    {
        return numCases;
    }

    private static List<Double> moneyList() {
        return Arrays.asList(0.01, 1.0, 5.0, 10.0, 50.0, 100.0,
                250.0, 500.0, 750.0, 1000.0, 2000.0, 3000.0, 4000.0,
                5000.0, 7500.0, 10000.0, 25000.0, 50000.0, 75000.0,
                100000.0);
    }

    public static Map<Integer, Case> setupCases(String filename) // randomise money amount inside case
    {

        List<Double> money = readMoney(filename);
        Collections.shuffle(money);

        Map<Integer, Case> cases = new HashMap<>();
        for (int i = 0; i < numCases; i++) {
            cases.put(i + 1, new Case(i + 1, money.get(i)));
        }

        return cases;
    }

    private static List<Double> readMoney(String filename) { //read prize pot from txt file
        List<Double> amounts = new ArrayList<>();

        try {
            List<String> row = Files.readAllLines(Paths.get(filename));
            for (String num : row) {
                amounts.add(Double.parseDouble(num.trim()));
            }

            if (amounts.size() != numCases) {
                System.out.println("Input file has incorrect number of values. \nFile must contain exactly " + numCases + " values. \nReverting to default 20-item prize money list.");
                return moneyList();
            }
        } catch (IOException e) {
            System.out.println("Error reading prize file: " + e.getMessage() + ".\nUsing default 20-item prize money values.");
            return moneyList();

        } catch (NumberFormatException n) {
            System.out.println("Input file has attempted to convert a String to a Double. \n" + n.getMessage() + "\nUsing default 20-item prize money values.");
            return moneyList();
        }

        return amounts;
    }
}
