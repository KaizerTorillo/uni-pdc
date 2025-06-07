package DealNoDealGame;

public class Player {
	private String name;
	private Case chosenCase;
	
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

}
