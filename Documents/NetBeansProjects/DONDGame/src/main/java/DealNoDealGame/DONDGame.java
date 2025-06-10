package DealNoDealGame;

import java.util.*;
import java.nio.file.Path;
import java.nio.file.Paths;
import javax.swing.JOptionPane;
import javax.swing.*;
import java.awt.*;
import java.awt.event.*;

public class DONDGame extends JFrame
{

    private Map<Integer, Case> cases;
    Scanner scan = new Scanner(System.in);
    public double lastOffer = 0.0;
    private Player player;
    private CaseMechanics cm;
    private Banker b;
    private GameLogs log;
    Path currentRelativePath = Paths.get("");
    String currentPath = currentRelativePath.toAbsolutePath().toString();
    String prizeList = "src\\main\\java\\DealNoDealGame\\Prizemoney.txt";
    String hsFile = "src\\main\\java\\DealNoDealGame\\DOND_GameLogs.txt";
    private JButton[] caseButtons = new JButton[26];
    private JTextArea logArea;
    private int openCase = 0;


    

    public DONDGame() 
    { //constructor
        super("Deal or No Deal");
        System.out.println(currentPath);
        cases = CaseMechanics.setupCases(prizeList);
        b = new Banker("The Banker");
        log = new GameLogs(hsFile);
        String playerName = JOptionPane.showInputDialog(
        this,
        "Enter your name:",
        "Welcome to Deal or No Deal",
        JOptionPane.PLAIN_MESSAGE);
        
        if (playerName == null || playerName.trim().isEmpty()) 
        {
            playerName = "Player";
        }
        player = new Player(playerName);
        this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        this.setSize(900, 600);
        this.setLayout(new BorderLayout());
        
         // Center Panel - Case Buttons
        JPanel casePanel = new JPanel(new GridLayout(4, 7, 10, 10));

        for (int i = 0; i < 26; i++) 
        {
            int index = i;
            caseButtons[i] = new JButton("Case " + (i + 1));
            casePanel.add(caseButtons[i]);
            int caseNumber = i + 1;
            caseButtons[i].addActionListener(e -> handleCaseClick(caseNumber - 1));
        }
        // Bottom Panel - Logs
        logArea = new JTextArea(6, 20);
        logArea.setEditable(false);
        JScrollPane scrollPane = new JScrollPane(logArea);

        this.add(casePanel, BorderLayout.CENTER);
        this.add(scrollPane, BorderLayout.SOUTH);
        this.setVisible(true);

        log(player.getName() + ", Choose one case to keep.");
    }

    private void log(String message) 
    {
        logArea.append(message + "\n");
    }

     private void handleCaseClick(int index) 
    {
        Case selected = cases.get(index+1);
        if (player.getChosenCase() == null) 
        {
            player.setChosenCase(selected);
            caseButtons[index].setEnabled(false);
            log("You chose Case #" + selected.getCaseNum() + ". We shall find out what's inside the case at the end!");
        } else if (!selected.getIsOpen() && selected != player.getChosenCase()) 
        {
            selected.open();
            openCase++;
            caseButtons[index].setEnabled(false);
            log("Opened Case " + selected.getCaseNum() + " | Value: $" + selected.getMoney());

            if (openCase == 25) 
            {
                // Only the player's case is left unopened
                showPlayerCaseEnd();
            }
            else if (openCase % 6 == 0 || openCase == 20) 
            {
            offerDeal();
            }
        }
    }
     
    public void printCases() //viusal rep of open and close cases
    {
        for (Case c : cases.values()) 
        {

            if (!c.getIsOpen()) 
            {
                System.out.println(c);
            } else if (c == player.getChosenCase()) 
            {
                System.out.println("Your case");
            } else 
            {
                System.out.println("$" + c.getMoney());
            }
        }
    }

    private void showPlayerCaseEnd() {
    Case kept = player.getChosenCase();
    kept.open();
    player.rejectOfferEnd();  // use case value
    log("All other cases opened.");
    log("Your case contained: $" + kept.getMoney());

    JOptionPane.showMessageDialog(this,
        "No Deal!\n" +
        "You opened all other cases.\n" +
        "Your case contained: $" + kept.getMoney());
        endGame();
    }
    
    public void playerChooseCase() //user picks case and info is stored
    {

        int choice = 0;
        boolean goodInput = false;

        while (!goodInput) 
        {
            System.out.println("Choose a case from 1 to " + CaseMechanics.getNumCases() + " :");
            try 
            {
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



        player.setChosenCase(cases.get(choice));
        player.getChosenCase().open();
        System.out.println("You chose Case # " + player.getChosenCase().getCaseNum() + ". We shall find out what's inside the case at the end!");

        }
    }

    

    public double bankOffer() // 
    {
        return b.bankerOffer(cases, player.getChosenCase());
    }

    public void offerDeal() 
    {
        double offer = bankOffer(); //the average prize amount of all closed cases
        lastOffer = offer; //useful for last round. stores the offer from the last round 
        log("Banker's Offer: $" + offer);

        int response = JOptionPane.showConfirmDialog(this,
            "The Banker offers you: $" + offer + "\nDeal or No Deal?",
            "Banker's Offer", JOptionPane.YES_NO_OPTION);

        if (response == JOptionPane.YES_OPTION) {
            player.acceptOffer(offer);
            log("You took the Banker's offer of $" + offer);
            endGame();
        } else {
            log("You rejected the offer.");
        }
            //checks if user input is y or n. because we use buttons, no need for while-if loop

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

    public void endGame() 
    {
        Case kept = player.getChosenCase();
        double caseValue = kept.getMoney();
        double finalAmount = player.getFinalAmount();
        boolean tookDeal = player.tookDeal();

        log("Your case was worth: $" + caseValue);

        
        if (tookDeal && finalAmount < caseValue)
        {
            JOptionPane.showMessageDialog(this,
            "Game Over!\n" +
            player.getName() + ", you walked away with: $" + finalAmount +
            "\nYour case had: $" + caseValue);
            log("\n You win!");
        }
        else
        {
            JOptionPane.showMessageDialog(this,
            "Game Over!\n" +
            player.getName() + ", you walked away with: $" + finalAmount +
            "\nYour case had: $" + caseValue);
            log("Uh oh! Better luck next time");
        }
        writeLogs(finalAmount, player.tookDeal());

 

        System.exit(0);
    }

    public double showPlayerCase() // reveal player case
    {

        return player.getChosenCase().getMoney();
    }

    public void writeLogs(Double finalAmount, boolean takeDeal) //log results 
    {
        try
        {
            String tookDeal;
        if (takeDeal) {
            tookDeal = "Yes";
        } else {
            tookDeal = "No";
        }
        String result = "\nName: " + player.getName()
                + "\nAmount Won: $" + finalAmount
                + "\nBanker Final Offer: $" + lastOffer
                + "\nDid they take the banker's offer? " + tookDeal;
        log.outputFile(result);

            
        }
        catch (Exception e)
        {
            log("There was an error with saving the game! " + e.getMessage());
        }
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
    public static void main(String[] args) {
        SwingUtilities.invokeLater(DONDGame::new);
    }
}
