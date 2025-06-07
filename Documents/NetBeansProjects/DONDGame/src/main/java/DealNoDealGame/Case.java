package DealNoDealGame;

public class Case {

    private int caseNum;
    private double money;
    private boolean isOpen;

    public Case(int caseNum, double money) {
        this.caseNum = caseNum;
        this.money = money;
        this.isOpen = false;
    }

    public int getCaseNum() {
        return caseNum;
    }

    public double getMoney() {
        return money;
    }

    public boolean getIsOpen() {
        return isOpen;
    }

    public void open() {
        this.isOpen = true;
    }

    @Override
    public String toString() {
        return "Case " + caseNum;
    }
}
