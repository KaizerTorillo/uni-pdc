package DealNoDealGame;

import java.util.*;
import java.nio.file.Path;
import java.nio.file.Paths;

public class DONDGame {

    private Map<Integer, Case> cases;
    Scanner scan = new Scanner(System.in);
    public double lastOffer = 0.0;
    private Player player;
    private CaseMechanics cm;
    private Banker b;
    private GameLogs log;
    private String playerName = getPlayerName();
    Path currentRelativePath = Paths.get("");
    String currentPath = currentRelativePath.toAbsolutePath().toString();
    String prizeList = "src\\main\\java\\DealNoDealGame\\Prizemoney.txt";
    String hsFile = "src\\main\\java\\DealNoDealGame\\DOND_GameLogs.txt";
    

    public DONDGame() { //constructor
        System.out.println(currentPath);
        cases = CaseMechanics.setupCases(prizeList);
        b = new Banker();
        log = new GameLogs(hsFile);
    }

    public String getPlayerName() { //ask user input for their name
        System.out.println("Please enter your name:");
        String name = scan.nextLine();
        player = new Player(name);
        return name;
    }

    public void printCases() //viusal rep of open and close cases
    {
        for (Case c : cases.values()) {

            if (!c.getIsOpen()) {
                System.out.println(c);
            } else if (c == player.getChosenCase()) {
                System.out.println("Your case");
            } else {
                System.out.println("$" + c.getMoney());
            }
        }
    }

    public void playerChooseCase() //user picks case and info is stored
    {

        int choice = 0;
        boolean goodInput = false;

        while (!goodInput) {
            System.out.println("Choose a case from 1 to " + CaseMechanics.getNumCases() + " :");
            try {
                choice = scan.nextInt();
                scan.nextLine();

                if (choice < 1 || choice > CaseMechanics.getNumCases()) // uif user int input greater than all case num
                {
                    System.out.println("That case does not exist. Please choose another case!");
                    continue;
                }

                goodInput = true;

            } catch (InputMismatchException e) { //if user input not int
                System.out.println("That's not a case! Please enter a number");
                scan.nextLine();
            }

        }

        player.setChosenCase(cases.get(choice));
        player.getChosenCase().open();
        System.out.println("You chose Case # " + player.getChosenCase().getCaseNum() + ". We shall find out what's inside the case at the end!");

    }

    public void openCase(int caseToOpen) //game round case-opening mechanism
    {
        int choiceLoop = 0;
        int max = Math.min(caseToOpen, remainingCasesClosed());
        while (choiceLoop < max) {
            System.out.println("Choose a case to open");

            printCases();

            int choice = scan.nextInt();
            
            if (choice < 1 || choice > CaseMechanics.getNumCases()) {
                System.out.println("That's not a case! Pick another one (ಠ_ಠ)");
                continue;
            }

            Case chosen = cases.get(choice);

            if (chosen == player.getChosenCase()) {
                System.out.println("That's your case! Pick another one ( ͠° ͟ʖ ͡° )");
                continue;
            }

            if (chosen.getIsOpen()) {
                System.out.println("That case is already open, pick another one! (.-.)");
                continue;
            }

            chosen.open();
            System.out.println("You picked case " + chosen.getCaseNum() + "! It had $" + chosen.getMoney());
            choiceLoop++;
        }

    }

    public double bankOffer() // 
    {
        return b.bankerOffer(cases, player.getChosenCase());
    }

    public boolean offerDeal() {
        double offer = bankOffer(); //the average prize amount of all closed cases
        lastOffer = offer; //useful for last round. stores the offer from the last round 
        boolean validYN = false;
        String response = "";
        System.out.println("The Banker offers you: $" + offer);
        while (!validYN) {
            System.out.println("Do you accept the deal? (y/n ONLY)");
            response = scan.next().toLowerCase();
            if (response.equals("y") || response.equals("n")) {
                validYN = true;
            } else {
                System.out.println("Not a valid response. Please enter y or n");
            }
            //checks if user input is y or n. 

        }
        return response.equals("y");
    }

    public int remainingCasesClosed() //checks how many closed cases are left
    {
        int count = 0;
        for (Case c : cases.values()) {
            if (!c.getIsOpen() && c != player.getChosenCase()) {
                count++;
            }
        }
        return count;
    }

    public boolean allCaseOpen() //checks if all cases are open 
    {
        for (Case c : cases.values()) {
            if (!c.getIsOpen()) {
                return false;
            }
        }
        return true;
    }

    public void endGame() //just to close the scanner
    {
        scan.close();
    }

    public double showPlayerCase() // reveal player case
    {

        return player.getChosenCase().getMoney();
    }

    public void writeLogs(Double finalAmount, boolean takeDeal) //log results 
    {
        String tookDeal;
        if (takeDeal) {
            tookDeal = "Yes";
        } else {
            tookDeal = "No";
        }
        String result = "\nName: " + playerName
                + "\nAmount Won: $" + finalAmount
                + "\nBanker Final Offer: $" + lastOffer
                + "\nDid they take the banker's offer? " + tookDeal;

        log.outputFile(result);
    }

    /*
	 * public void yourScore(double won) { Map<String, Double> score = new
	 * HashMap<>(); String named = playerName.toUpperCase().trim(); for(String key:
	 * score.keySet()) { if (key.equals(named)) { double current = score.get(named);
	 * if(won > current) { score.put(named, won); } } else { score.put(named, won);
	 * } }
	 * 
	 * lb.outputFile(score); }
     */
}
