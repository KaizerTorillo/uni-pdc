package DealNoDealGame;

public class Player {
	private String name;
	private Case chosenCase;
        private boolean offerTaken;
        private double finalAmount;
	
	public Player(String name) {
        this.name = name;
    }

    public String getName() {
        return name;
    }

    public void setChosenCase(Case chosenCase) {
        this.chosenCase = chosenCase;
    }
    
    public Case getChosenCase() {
        return chosenCase;
    }

    public double revealCaseAmount() {
        return chosenCase.getMoney();
    }
    
    public void acceptOffer(double offer) {
        this.offerTaken = true;
        this.finalAmount = offer;
    }

    public void rejectOfferEnd() {
        this.offerTaken = false;
        if (chosenCase != null) {
            this.finalAmount = chosenCase.getMoney();
        }
    }
    
    public boolean tookDeal()
    {
        return offerTaken;
    }
    
    public double getFinalAmount()
    {
        return finalAmount;
    }

}
