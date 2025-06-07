package DealNoDealGame;

public class Main {

    public static void main(String[] args) {
        DONDGame game = new DONDGame();
        game.printCases();
        game.playerChooseCase();
        boolean takeDeal;

        //after every round (round is hard coded for 4 case openings each)
        while (!game.allCaseOpen()) {
            game.openCase(4);
            //end game message if player chooses their case till the end
            if (game.allCaseOpen()) {
                System.out.println("All cases are open, the game is over! Let's see your case!");
                System.out.println("Your case had... $" + game.showPlayerCase());
                System.out.println("Final banker offer: $" + game.lastOffer);
                if (game.showPlayerCase() > game.lastOffer) {
                    System.out.println("Congratulations! You picked the better case.");
                    takeDeal = false;
                    game.writeLogs(game.showPlayerCase(), takeDeal);
                    game.endGame();
                    break;
                } else {
                    System.out.println("Oh no! Better luck next time!");
                    takeDeal = false;
                    game.writeLogs(game.showPlayerCase(), takeDeal);
                    game.endGame();
                    break;
                }
            }

            if (game.offerDeal()) //check if player wants to take banker offer
            {
                System.out.println("You accepted the deal! You win $" + game.bankOffer());
                System.out.println("Your case had... $" + game.showPlayerCase());
                System.out.println("Final banker offer: $" + game.bankOffer());
                if (game.showPlayerCase() > game.bankOffer()) {
                    System.out.println("Uh oh! Better luck next time!");
                    takeDeal = true;
                    game.writeLogs(game.bankOffer(), takeDeal);
                    game.endGame();
                    break;

                } else {
                    System.out.println("Congratulations! You picked the better deal.");
                    takeDeal = true;
                    game.writeLogs(game.bankOffer(), takeDeal);
                    game.endGame();
                    break;
                }

            } else {
                System.out.println("You declined the deal. Let's continue!");
            }

        }
        game.endGame();
    }

}
